package com.chris.mechanization.service.impl.strategy;

import com.binance.api.client.domain.market.Candlestick;
import com.chris.mechanization.dao.IOperateTableDao;
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
 * @description:
 * @author: Chris.Y
 * @create: 2019-04-20 16:42
 **/
@Service
public class StrategyMA implements IBackTest {
    @Autowired
    IOperateTableDao iOperateTableDao;


    String symbolCode;
    String symbolName;


    Transaction transaction;

    public String strategy(List<ItemStock> symbolList, List<CandlestickCopy> coinList, String maValue) {
        boolean transactionStatus = true;
        float buyPrice = 0f;
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

                String time = itemStock.getTime();
                ++isNextDay;
                if (transactionStatus && close > tempMA) {

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
                    transaction.setProfit(String.valueOf(close / buyPrice - 1));

                    if (close < tempMA) {

                        transaction.setSellReason("close<" + tempMA);
                        iOperateTableDao.addTransaction(transaction);
                        transactionStatus = true;
                    }
                }

            }
            ChrisCalculationUtils.outPrint(iOperateTableDao, this.symbolCode, this.symbolName, maValue);
        } else if (coinList != null && coinList.size() > 0) {
            int size = coinList.size();

            String nextPrice = "0";
            int isNextDay = 0;
            for (int i = 0; i < size; i++) {
                CandlestickCopy itemStock = coinList.get(i);

//                float close = Float.parseFloat(itemStock.getClose());
                float open = Float.parseFloat(itemStock.getOpen());
                float tempMA = 0;
                if (maValue.equals("ma5")) {
                    if (itemStock.getMa5() == null) {
                        continue;
                    }
                    tempMA = Float.parseFloat(itemStock.getMa5());
                }
                if (maValue.equals("ma7")) {
                    if (itemStock.getMa7() == null) {
                        continue;
                    }
                    tempMA = Float.parseFloat(itemStock.getMa7());
                } else if (maValue.equals("ma10")) {
                    if (itemStock.getMa10() == null) {
                        continue;
                    }
                    tempMA = Float.parseFloat(itemStock.getMa10());
                }
                if (maValue.equals("ma14")) {
                    if (itemStock.getMa14() == null) {
                        continue;
                    }
                    tempMA = Float.parseFloat(itemStock.getMa14());
                } else if (maValue.equals("ma20")) {
                    if (itemStock.getMa20() == null) {
                        continue;
                    }
                    tempMA = Float.parseFloat(itemStock.getMa20());
                }
                if (maValue.equals("ma22")) {
                    if (itemStock.getMa22() == null) {
                        continue;
                    }
                    tempMA = Float.parseFloat(itemStock.getMa22());
                } else if (maValue.equals("ma30")) {
                    if (itemStock.getMa30() == null) {
                        continue;
                    }
                    tempMA = Float.parseFloat(itemStock.getMa30());
                } else if (maValue.equals("ma55")) {
                    if (itemStock.getMa55() == null) {
                        continue;
                    }
                    tempMA = Float.parseFloat(itemStock.getMa55());
                } else if (maValue.equals("ma60")) {
                    if (itemStock.getMa60() == null) {
                        continue;
                    }
                    tempMA = Float.parseFloat(itemStock.getMa60());
                }else if (maValue.equals("ma25")) {
                    if (itemStock.getMa25() == null) {
                        continue;
                    }
                    tempMA = Float.parseFloat(itemStock.getMa25());
                }
                else if (maValue.equals("ma27")) {
                    if (itemStock.getMa27() == null) {
                        continue;
                    }
                    tempMA = Float.parseFloat(itemStock.getMa27());
                }


                String time = itemStock.getTime() + "";
                ++isNextDay;
                if (transactionStatus && open > tempMA) {

                    if (i + 1 < size) {
                        nextPrice = coinList.get(i + 1).getClose();
                    }

                    transaction = new Transaction();
                    transaction.setSymbolCode(symbolCode);
                    transaction.setSymbolName(symbolName);
                    transaction.setBuyTime(time);
                    transaction.setBuyPrice(String.valueOf(open));
                    transaction.setBuyNextPrice(nextPrice);
                    transaction.setBuyReason("close>" + tempMA);
                    buyPrice = open;
                    transactionStatus = false;
                    isNextDay = 0;
                    continue;
                }

                if (!transactionStatus && open < tempMA) {
                    transaction.setSellPrice(String.valueOf(open));
                    transaction.setSellTime(time);
                    transaction.setProfit(String.valueOf(open / buyPrice - 1));
                    transaction.setSellReason("close<" + tempMA);
                    iOperateTableDao.addTransaction(transaction);
                    transactionStatus = true;
                }

            }
            ChrisCalculationUtils.outPrint(iOperateTableDao, this.symbolCode, this.symbolName, maValue);
        }

        return "";
    }

    @Override
    public void setData(String symbolCode, String symbolName, String period, MakeMoney makeMoney) {
        this.symbolCode = symbolCode;
        this.symbolName = symbolName;
        String tableName = "";
//        String[] str = new String[]{"ma5", "ma10", "ma20", "ma30", "ma60"};

        String[] str = new String[]{"ma5", "ma7", "ma10", "ma14", "ma20", "ma22", "ma30", "ma55", "ma60", "ma25", "ma27"};
//        String[] str = new String[]{"ma20"};

        if (makeMoney == MakeMoney.SYMBOL) {
            tableName = "symbol_" + period + "_" + symbolCode;
            if (iOperateTableDao.existTable(tableName) == 1) {
                for (int i = 0; i < str.length; i++) {
                    this.strategy(iOperateTableDao.queryInfoForLimit_symbol(tableName, "timestamp", 0, 100000000), null, str[i]);
                }
            }
        } else if (makeMoney == MakeMoney.COIN) {
            tableName = "coin_" + period + "_" + symbolCode;
            if (iOperateTableDao.existTable(tableName) == 1) {
                for (int i = 0; i < str.length; i++) {
                    this.strategy(null, iOperateTableDao.queryInfoForLimit_coin(tableName, "closeTime", 0, 100000000), str[i]);
                }
            }
        }
    }
}
