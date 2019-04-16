package com.chris.mechanization.service.impl.strategy;

import com.binance.api.client.domain.market.Candlestick;
import com.chris.mechanization.dao.IOperateTableDao;
import com.chris.mechanization.domain.Account;
import com.chris.mechanization.domain.BackTestResult;
import com.chris.mechanization.domain.Transaction;
import com.chris.mechanization.domain.xueqiuData.ItemStock;
import com.chris.mechanization.enumType.MakeMoney;
import com.chris.mechanization.service.IBackTest;
import com.chris.mechanization.utils.ChrisDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-04-08 17:17
 * @description：
 */
@Service
public class StrategyCCI implements IBackTest {
    @Autowired
    IOperateTableDao iOperateTableDao;

    List<ItemStock> symbolList;
    List<Candlestick> coinList;

    String tableName;
    String symbolCode;
    String symbolName;


    Transaction transaction;

    @Override
    public String strategy() {
        boolean transactionStatus = true;
        float buyPrice = 0;
        iOperateTableDao.trancateTable("transaction");

        if (symbolList != null && symbolList.size() > 0) {
            int size = symbolList.size();
            for (int i = 0; i < size; i++) {
                if (i > 1) {
                    ItemStock previousItemStock = symbolList.get(i - 1);
                    ItemStock itemStock = symbolList.get(i);
                    if (itemStock.getCci().equals("") || previousItemStock.getCci().equals("")) {
                        continue;
                    }
                    float close = Float.parseFloat(itemStock.getClose());
                    float open = Float.parseFloat(itemStock.getOpen());
                    float volume = Float.parseFloat(itemStock.getVolume());
                    float previousvolume = Float.parseFloat(previousItemStock.getVolume());
                    float cci = Float.parseFloat(itemStock.getCci());
                    float previousCci = Float.parseFloat(previousItemStock.getCci());
                    String time = itemStock.getTime();
                    if (transactionStatus && close > open && volume > previousvolume && cci > -100 && previousCci < -100) {
                        String nextPrice = "0";
                        if (i + 1 < size) {
                            nextPrice = symbolList.get(i + 1).getClose();
                        }

                        transaction = new Transaction();
                        transaction.setSymbolCode(symbolCode);
                        transaction.setSymbolName(symbolName);
                        transaction.setBuyTime(time);
                        transaction.setBuyPrice(String.valueOf(close));
                        transaction.setBuyNextPrice(nextPrice);
//                         cci>-100且上一天cci<-100且今天的量大于昨天的量且收盘价大于开盘价
                        transaction.setBuyReason("cci>-100");
                        buyPrice = close;
                        transactionStatus = false;
                    }

                    if (!transactionStatus) {
                        transaction.setSellPrice(String.valueOf(close));
                        transaction.setSellTime(time);
                        transaction.setProfit(String.format("%.2f", close / buyPrice - 1));
                        if (close / buyPrice - 1 > 0.01) {
                            transaction.setSellReason("盈利1个点");
                            iOperateTableDao.addTransaction(transaction);
                            transactionStatus = true;
                        } else if (cci < -100) {
                            transaction.setSellReason("cci<-100");
                            iOperateTableDao.addTransaction(transaction);
                            transactionStatus = true;
                        }
                    }

                }

            }
            this.outPrint();
        } else if (coinList != null && coinList.size() > 0) {
            int size = coinList.size();
        }

        return "";
    }

    @Override
    public void setData(String symbolCode, String symbolName, String period, MakeMoney makeMoney) {
        this.symbolCode = symbolCode;
        this.symbolName = symbolName;
        if (makeMoney == MakeMoney.SYMBOL) {
            tableName = "symbol_" + period + "_" + symbolCode;
            this.symbolList = iOperateTableDao.queryInfoForLimit_symbol(tableName, "timestamp", 0, 100000000);
        } else if (makeMoney == MakeMoney.COIN) {
            tableName = "coin_" + period + "_" + symbolCode;
            this.coinList = iOperateTableDao.queryInfoForLimit_coin(tableName, "closeTime", 0, 100000000);
        }
    }

    private void outPrint() {
        List<Transaction> transactionList = iOperateTableDao.queryInfoForTransaction();
        int size = transactionList.size();
        //总收益
        float countProfit = 0;
        //交易次数
        int transactionNum = 0;
        //持仓天数
        int holdDay = 0;
        //最大回调
        float md = 0;
        //最大连续亏损次数
        int lossNum = 0;
        int tempLossNum = 0;
        //胜率
        String winProfit = "0";
        int tempTrue = 0;
        //持仓与区间时间的百分比
        String t;
        //区间涨幅
        String immobility;

        float maxProfit = 0;
        float minxProfit = 0;


        for (int i = 0; i < size; i++) {
            Transaction transaction = transactionList.get(i);

            countProfit = Float.parseFloat(transaction.getProfit()) + countProfit;
            transactionNum = size;
            holdDay = ChrisDateUtils.differentDaysByMillisecond(transaction.getBuyTime(), transaction.getSellTime(), "yyyy-MM-dd");
            if (countProfit > maxProfit) {
                maxProfit = countProfit;
                minxProfit = 0;
            }

            if (countProfit < minxProfit) {
                minxProfit = countProfit;

                if (maxProfit - minxProfit > md) {
                    md = maxProfit - minxProfit;
                }
            }

            if (Float.parseFloat(transaction.getProfit()) < 0) {
                ++tempLossNum;
            } else {
                tempLossNum = 0;
                ++tempTrue;
            }
            if (tempLossNum > lossNum) {
                lossNum = tempLossNum;
            }

            winProfit = String.format("%.2f", (float) tempTrue / size * 100) + "%";
        }
        Transaction fistTransaction = transactionList.get(0);
        Transaction lastTransaction = transactionList.get(size-1);
        immobility = String.format("%.2f", Float.valueOf(fistTransaction.getBuyPrice()) / Float.valueOf(fistTransaction.getSellPrice()));
        t = String.format("%.2f", Float.valueOf(holdDay / ChrisDateUtils.differentDaysByMillisecond
                (fistTransaction.getBuyTime(), lastTransaction.getSellTime(), "yyyy-MM-dd")));

        BackTestResult backTestResult = new BackTestResult(symbolCode, symbolName, "A",
                String.format("%.2f", countProfit) + "%", String.valueOf(transactionNum), String.valueOf(holdDay), String.format("%.2f", md) + "%",
                String.valueOf(lossNum), winProfit, t, immobility);
        iOperateTableDao.addBackTestResult(backTestResult);
    }
}
