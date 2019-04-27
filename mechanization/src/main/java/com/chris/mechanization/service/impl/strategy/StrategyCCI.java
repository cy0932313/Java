package com.chris.mechanization.service.impl.strategy;

import com.binance.api.client.domain.market.Candlestick;
import com.chris.mechanization.dao.IOperateTableDao;
import com.chris.mechanization.domain.Account;
import com.chris.mechanization.domain.BackTestResult;
import com.chris.mechanization.domain.CandlestickCopy;
import com.chris.mechanization.domain.Transaction;
import com.chris.mechanization.domain.xueqiuData.ItemStock;
import com.chris.mechanization.enumType.MakeMoney;
import com.chris.mechanization.service.IBackTest;
import com.chris.mechanization.utils.ChrisCalculationUtils;
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
    List<CandlestickCopy> coinList;

    String tableName;
    String symbolCode;
    String symbolName;


    Transaction transaction;

    public String strategy() {
        boolean transactionStatus = true;
        float buyPrice = 0;
        iOperateTableDao.trancateTable("transaction");

        if (symbolList != null && symbolList.size() > 0) {
            int size = symbolList.size();

            String nextPrice = "0";
            int isNextDay = 0;
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
                    ++isNextDay;
                    if (transactionStatus && close > open && volume > previousvolume && cci > -100 && previousCci < -100) {

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
                        isNextDay = 0;
                        continue;
                    }

                    if (!transactionStatus) {
                        transaction.setSellPrice(String.valueOf(close));
                        transaction.setSellTime(time);
                        transaction.setProfit(String.valueOf( close / buyPrice - 1));

                            if (cci < -100) {
                            transaction.setSellReason("cci<-100");
                            iOperateTableDao.addTransaction(transaction);
                            transactionStatus = true;
                        }
                            else if(Float.parseFloat(nextPrice) < buyPrice && isNextDay == 1)
                            {
                                transaction.setSellReason("第二天跌了");
                                iOperateTableDao.addTransaction(transaction);
                                transactionStatus = true;
                            }
                       else if (close / buyPrice - 1 > 0.03) {
                            transaction.setSellReason("盈利3个点");
                            iOperateTableDao.addTransaction(transaction);
                            transactionStatus = true;
                        }
                    }

                }

            }
            ChrisCalculationUtils.outPrint(iOperateTableDao,this.symbolCode,this.symbolName,"cci");

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
}
