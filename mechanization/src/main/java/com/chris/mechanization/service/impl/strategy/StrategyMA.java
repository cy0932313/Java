package com.chris.mechanization.service.impl.strategy;

import com.binance.api.client.domain.market.Candlestick;
import com.chris.mechanization.dao.IOperateTableDao;
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
 * @description:
 * @author: Chris.Y
 * @create: 2019-04-20 16:42
 **/
@Service
public class StrategyMA implements IBackTest {
    @Autowired
    IOperateTableDao iOperateTableDao;


    List<Candlestick> coinList;

    String symbolCode;
    String symbolName;


    Transaction transaction;

    public String strategy(List<ItemStock> symbolList, String maValue) {
        boolean transactionStatus = true;
        float buyPrice = 0;
        iOperateTableDao.trancateTable("transaction");

        if (symbolList != null && symbolList.size() > 0) {
            int size = symbolList.size();

            String nextPrice = "0";
            int isNextDay = 0;
            for (int i = 0; i < size; i++) {
                ItemStock itemStock = symbolList.get(i);

                float close = Float.parseFloat(itemStock.getClose());
                float open = Float.parseFloat(itemStock.getOpen());
                float tempMA = 0;
                if (maValue.equals("ma5")) {
                    tempMA = Float.parseFloat(itemStock.getMa5());
                } else if (maValue.equals("ma10")) {
                    tempMA = Float.parseFloat(itemStock.getMa10());
                } else if (maValue.equals("ma20")) {
                    tempMA = Float.parseFloat(itemStock.getMa20());
                } else if (maValue.equals("ma30")) {
                    tempMA = Float.parseFloat(itemStock.getMa30());
                } else if (maValue.equals("ma60")) {
                    if (itemStock.getMa60().equals("")) {
                        continue;
                    }
                    tempMA = Float.parseFloat(itemStock.getMa60());
                }

                float ma20   = Float.parseFloat(itemStock.getMa20());
                float ma10   = Float.parseFloat(itemStock.getMa10());

                String time = itemStock.getTime();
                ++isNextDay;
                if (transactionStatus && close > ma20) {

                    if (i + 1 < size) {
                        nextPrice = symbolList.get(i + 1).getClose();
                    }

                    transaction = new Transaction();
                    transaction.setSymbolCode(symbolCode);
                    transaction.setSymbolName(symbolName);
                    transaction.setBuyTime(time);
                    transaction.setBuyPrice(String.valueOf(close));
                    transaction.setBuyNextPrice(nextPrice);
                    transaction.setBuyReason("close>" + tempMA);
                    buyPrice = close;
                    transactionStatus = false;
                    isNextDay = 0;
                    continue;
                }

                if (!transactionStatus) {
                    transaction.setSellPrice(String.valueOf(close));
                    transaction.setSellTime(time);
                    transaction.setProfit(String.format("%.2f", close / buyPrice - 1));

                    if (close < ma10) {

                        transaction.setSellReason("close<" + tempMA);
                        iOperateTableDao.addTransaction(transaction);
                        transactionStatus = true;
                    }
                }

            }
            this.outPrint(maValue);
        } else if (coinList != null && coinList.size() > 0) {
            int size = coinList.size();
        }

        return "";
    }

    @Override
    public void setData(String symbolCode, String symbolName, String period, MakeMoney makeMoney) {
        this.symbolCode = symbolCode;
        this.symbolName = symbolName;
        String tableName = "";
        String[] str = new String[]{"ma5", "ma10", "ma20", "ma30", "ma60"};
        if (makeMoney == MakeMoney.SYMBOL) {
            tableName = "symbol_" + period + "_" + symbolCode;
            if (iOperateTableDao.existTable(tableName) == 1) {
//                for (int i = 0; i < str.length; i++) {
                    this.strategy(iOperateTableDao.queryInfoForLimit_symbol(tableName, "timestamp", 0, 100000000), "ma20");
//                }
            }
        } else if (makeMoney == MakeMoney.COIN) {
            tableName = "coin_" + period + "_" + symbolCode;
            this.coinList = iOperateTableDao.queryInfoForLimit_coin(tableName, "closeTime", 0, 100000000);
        }
    }

    private void outPrint(String maValue) {
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
            holdDay = holdDay + ChrisDateUtils.differentDaysByMillisecond(transaction.getBuyTime(), transaction.getSellTime(), "yyyy-MM-dd");
            if (countProfit > maxProfit) {
                maxProfit = countProfit;
                minxProfit = 0;
            } else {
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
        Transaction lastTransaction = transactionList.get(size - 1);
        immobility = String.format("%.2f", (Float.valueOf(lastTransaction.getSellPrice()) / Float.valueOf(fistTransaction.getBuyPrice())) * 100);

        t = String.format("%.2f", Float.valueOf(holdDay * 100 / ChrisDateUtils.differentDaysByMillisecond
                (fistTransaction.getBuyTime(), lastTransaction.getSellTime(), "yyyy-MM-dd")));

        BackTestResult backTestResult = new BackTestResult(symbolCode, symbolName, maValue,
                String.format("%.2f", countProfit * 100) + "%", String.valueOf(transactionNum), String.valueOf(holdDay), String.format("%.2f", md * 100) + "%",
                String.valueOf(lossNum), winProfit, t + "%", immobility + "%");
        iOperateTableDao.addBackTestResult(backTestResult);
    }
}
