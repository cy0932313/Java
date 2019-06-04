package com.chris.mechanization.service.impl.strategy;

import com.chris.mechanization.dao.IOperateTableDao;
import com.chris.mechanization.domain.CandlestickCopy;
import com.chris.mechanization.domain.Transaction;
import com.chris.mechanization.domain.xueqiuData.ItemStock;
import com.chris.mechanization.enumType.MakeMoney;
import com.chris.mechanization.service.IBackTest;
import com.chris.mechanization.utils.ChrisCalculationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-05-29 22:32
 **/
@Service
public class StrategyTQA implements IBackTest {
    @Autowired
    IOperateTableDao iOperateTableDao;


    String symbolCode;
    String symbolName;
    String pVolume;

    Transaction transaction;

    public String strategy(List<ItemStock> symbolList, List<CandlestickCopy> coinList) {
        boolean transactionStatus = true;
        float buyPrice = 0f;
        iOperateTableDao.trancateTable("transaction");

        int size = coinList.size();

        String transactionType = "";
        CandlestickCopy shangItemStock = new CandlestickCopy();
        for (int i = 18; i < size; i++) {
            CandlestickCopy itemStock = coinList.get(i);
            this.setTQATD(coinList.subList(i - 18,i),itemStock);

                if(transactionType.equals("buy-market"))
                {
                    //3个点止损
//                    if((Float.parseFloat(itemStock.getLow()) / buyPrice - 1) * 100 < -3)
//                    {
//                        transaction.setSellPrice(itemStock.getMa5());
//                        transaction.setSellTime(itemStock.getTime());
//                        transaction.setProfit(String.valueOf(-3));
//                        iOperateTableDao.addTransaction(transaction);
//                        transactionType = "";
//                        continue;
//                    }

                        if(Float.parseFloat(itemStock.getLow()) < Float.parseFloat(itemStock.getMa5()))
                        {
                            transaction.setSellPrice(itemStock.getMa5());
                            transaction.setSellTime(itemStock.getTime());
                            transaction.setProfit(String.valueOf((Float.parseFloat(itemStock.getMa5()) / buyPrice - 1)* 100));
                            iOperateTableDao.addTransaction(transaction);
                            transactionType = "";
                            continue;
                        }
//                        //止盈
//                    if((Float.parseFloat(itemStock.getHigh()) / buyPrice - 1) * 100 > 10)
//                    {
//                        transaction.setSellPrice(transaction.getBuyNextPrice());
//                        transaction.setSellTime(itemStock.getTime());
//                        transaction.setProfit(String.valueOf(10));
//                        iOperateTableDao.addTransaction(transaction);
//                        transactionType = "";
//                        continue;
//                    }


                }

                if(transactionType.equals("sell-market"))
                {

                    //3个点止损
//                    if((1 - Float.parseFloat(itemStock.getHigh()) / buyPrice)* 100 < -3)
//                    {
//                        transaction.setSellPrice(itemStock.getMa5());
//                        transaction.setSellTime(itemStock.getTime());
//                        transaction.setProfit(String.valueOf(-3));
//                        iOperateTableDao.addTransaction(transaction);
//                        transactionType = "";
//                        continue;
//                    }

                        if(Float.parseFloat(itemStock.getHigh()) > Float.parseFloat(itemStock.getMa7()))
                        {
                            transaction.setSellPrice(itemStock.getMa7());
                            transaction.setSellTime(itemStock.getTime());
                            transaction.setProfit(String.valueOf((1 - Float.parseFloat(itemStock.getMa7()) / buyPrice)* 100));
                            iOperateTableDao.addTransaction(transaction);
                            transactionType = "";
                            continue;
                        }

                    //止盈
//                    if((1 - Float.parseFloat(itemStock.getLow()) / buyPrice)* 100 > 10)
//                    {
//                        transaction.setSellPrice(transaction.getBuyNextPrice());
//                        transaction.setSellTime(itemStock.getTime());
//                        transaction.setProfit(String.valueOf(10));
//                        iOperateTableDao.addTransaction(transaction);
//                        transactionType = "";
//                        continue;
//                    }
                }

            if(transactionType.equals(""))
            {
                if(Float.parseFloat(itemStock.getHigh()) > Float.parseFloat(itemStock.getMa14()))
                {
                        transaction = new Transaction();
                        transaction.setSymbolCode(symbolCode);
                        transaction.setSymbolName(symbolName);
                        transactionType = "buy-market";
                        transaction.setBuyReason("做多");
                        transaction.setBuyTime(itemStock.getTime());

                            transaction.setBuyPrice(itemStock.getMa14());
                            transaction.setBuyNextPrice(String.valueOf( Float.parseFloat(itemStock.getMa14()) *1.1));
                            buyPrice = Float.parseFloat(itemStock.getMa14());

                }

                if(Float.parseFloat(itemStock.getLow()) < Float.parseFloat(itemStock.getMa10()))
                {
                        transaction = new Transaction();
                        transaction.setSymbolCode(symbolCode);
                        transaction.setSymbolName(symbolName);
                        transaction.setBuyReason("做空");
                        transactionType = "sell-market";
                        transaction.setBuyTime(itemStock.getTime());


                        transaction.setBuyPrice(itemStock.getMa10());
                        transaction.setBuyNextPrice(String.valueOf( Float.parseFloat(itemStock.getMa10()) *0.9));
                        buyPrice = Float.parseFloat(itemStock.getMa10());
                }
            }
            pVolume = itemStock.getVolume();
        }
        ChrisCalculationUtils.outPrint(iOperateTableDao, this.symbolCode, this.symbolName, "5-18区间正常操作");

        return "success";
    }

    public void setTQATD(List<CandlestickCopy> tempItemArray,CandlestickCopy itemStock)
    {
        List maxArrayList = tempItemArray.subList(0,18);
        float max22OpenPrice = 0.0f;
        float min22OpenPrice = 0.0f;

        for(int i = 0;i < maxArrayList.size();i++)
        {
            CandlestickCopy objItem = (CandlestickCopy)maxArrayList.get(i);

            float tempHighPrice= Float.parseFloat(objItem.getHigh());
            float tempLowPrice= Float.parseFloat(objItem.getLow());

            if(tempHighPrice > max22OpenPrice)
            {
                max22OpenPrice = tempHighPrice;
            }

            if(min22OpenPrice == 0)
            {
                min22OpenPrice =  tempLowPrice;
            }
            if(tempLowPrice < min22OpenPrice)
            {
                min22OpenPrice = tempLowPrice;
            }

        }

        List minArrayList = tempItemArray.subList(13,18);
        float min10OpenPrice = 0.0f;
        float max10OpenPrice = 0.0f;
        for(int i = 0;i < minArrayList.size();i++)
        {
            CandlestickCopy objItem = (CandlestickCopy)minArrayList.get(i);

            float tempHighPrice= Float.parseFloat(objItem.getHigh());
            float tempLowPrice= Float.parseFloat(objItem.getLow());

            if(min10OpenPrice == 0)
            {
                min10OpenPrice = tempLowPrice;
            }

            if(tempLowPrice < min10OpenPrice)
            {
                min10OpenPrice = tempLowPrice;
            }

            if(tempHighPrice > max10OpenPrice)
            {
                max10OpenPrice = tempHighPrice;
            }
        }
        itemStock.setMa10(String.valueOf(min22OpenPrice));

        itemStock.setMa14(String.valueOf(max22OpenPrice));

        itemStock.setMa5(String.valueOf(min10OpenPrice));

        itemStock.setMa7(String.valueOf(max10OpenPrice));
    }
    @Override
    public void setData(String symbolCode, String symbolName, String period, MakeMoney makeMoney) {
        this.symbolCode = symbolCode;
        this.symbolName = symbolName;
        String tableName = "";
        if (makeMoney == MakeMoney.COIN) {
            tableName = "coin_" + period + "_" + symbolCode.toUpperCase();
            if (iOperateTableDao.existTable(tableName) == 1) {
                this.strategy(null, iOperateTableDao.queryInfoForLimit_coin(tableName, "closeTime", 0, 100000000));
            }
        }
    }
}