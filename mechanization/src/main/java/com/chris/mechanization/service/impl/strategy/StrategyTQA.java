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

import java.util.ArrayList;
import java.util.Collections;
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


    Transaction transaction;

    public String strategy(List<ItemStock> symbolList, List<CandlestickCopy> coinList) {
        boolean transactionStatus = true;
        float buyPrice = 0f;
        iOperateTableDao.trancateTable("transaction");

        int size = coinList.size();

        String transactionType = "";

        for (int i = 16; i < size; i++) {
            CandlestickCopy itemStock = coinList.get(i);

            this.setTQATD(coinList.subList(i - 16,i),itemStock);

                if(transactionType.equals("buy-market"))
                {
                    if(Float.parseFloat(itemStock.getLow()) < Float.parseFloat(itemStock.getMa5()))
                    {
                        transaction.setSellPrice(itemStock.getMa5());
                        transaction.setSellTime(itemStock.getTime());
                        transaction.setProfit(String.valueOf((Float.parseFloat(itemStock.getMa5()) / buyPrice - 1)* 100));
                        iOperateTableDao.addTransaction(transaction);
                        transactionType = "";
                    }
                }

                if(transactionType.equals("sell-market"))
                {
                    if(Float.parseFloat(itemStock.getHigh()) > Float.parseFloat(itemStock.getMa7()))
                    {
                        transaction.setSellPrice(itemStock.getMa7());
                        transaction.setSellTime(itemStock.getTime());
                        transaction.setProfit(String.valueOf((1 - Float.parseFloat(itemStock.getMa7()) / buyPrice)* 100));
                        iOperateTableDao.addTransaction(transaction);
                        transactionType = "";
                    }
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
                    buyPrice = Float.parseFloat(itemStock.getMa10());
                }
            }
        }
        ChrisCalculationUtils.outPrint(iOperateTableDao, this.symbolCode, this.symbolName, "TQA");

        return "success";
    }

    public void setTQATD(List<CandlestickCopy> tempItemArray,CandlestickCopy itemStock)
    {
        List maxArrayList = tempItemArray.subList(0,16);
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

        List minArrayList = tempItemArray.subList(8,16);
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
            tableName = "coin_" + period + "_" + symbolCode;
            if (iOperateTableDao.existTable(tableName) == 1) {
                this.strategy(null, iOperateTableDao.queryInfoForLimit_coin(tableName, "closeTime", 0, 100000000));
            }
        }
    }
}