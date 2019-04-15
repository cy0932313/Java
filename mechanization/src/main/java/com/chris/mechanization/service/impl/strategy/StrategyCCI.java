package com.chris.mechanization.service.impl.strategy;

import com.binance.api.client.domain.market.Candlestick;
import com.chris.mechanization.dao.IOperateTableDao;
import com.chris.mechanization.domain.Account;
import com.chris.mechanization.domain.Transaction;
import com.chris.mechanization.domain.xueqiuData.ItemStock;
import com.chris.mechanization.enumType.MakeMoney;
import com.chris.mechanization.service.IBackTest;
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
    @Override
    public String strategy()
     {
         boolean transactionStatus = true;
         float buyPrice = 0;
         iOperateTableDao.trancateTable("transaction");

         if(symbolList != null && symbolList.size() > 0)
         {
             int size = symbolList.size();
             for(int i = 0;i < size;i++)
             {
                 if(i > 1)
                 {
                     ItemStock previousItemStock = symbolList.get(i - 1);
                     ItemStock itemStock = symbolList.get(i);
                     if(itemStock.getCci().equals("") || previousItemStock.getCci().equals(""))
                     {
                         continue;
                     }
                     float close = Float.parseFloat(itemStock.getClose());
                     float open = Float.parseFloat(itemStock.getOpen());
                     float volume = Float.parseFloat(itemStock.getVolume());
                     float previousvolume = Float.parseFloat(previousItemStock.getVolume());
                     float cci = Float.parseFloat(itemStock.getCci());
                     float previousCci = Float.parseFloat(previousItemStock.getCci());
                     String time = itemStock.getTime();
                     if(transactionStatus && close > open && volume > previousvolume && cci > -100 && previousCci < -100)
                     {
                         Transaction transaction = new Transaction(itemStock.getClose(),"BUY",time,"CCI数据大于-100");
                         iOperateTableDao.addTransaction(transaction);
                         buyPrice = close;
                         transactionStatus = false;
                     }

                     if(!transactionStatus)
                     {
                         if(close/buyPrice - 1 > 0.01)
                         {
                             Transaction transaction = new Transaction(itemStock.getClose(),"SELL",time,"赚钱");
                             iOperateTableDao.addTransaction(transaction);
                             transactionStatus = true;
                         }
                         else if(cci < -100)
                         {
                             Transaction transaction = new Transaction(itemStock.getClose(),"SELL",time,"亏钱");
                             iOperateTableDao.addTransaction(transaction);
                             transactionStatus = true;
                         }
                     }

                 }

             }
         }
         else if(coinList != null && coinList.size() > 0)
         {
             int size = coinList.size();
         }

         return "";
     }

     @Override
     public void setData(String symbol, String period, MakeMoney makeMoney)
     {
        if(makeMoney == MakeMoney.SYMBOL)
        {
            tableName = "symbol_"+period+"_" + symbol;
            this.symbolList = iOperateTableDao.queryInfoForLimit_symbol(tableName, "timestamp", 0, 100000000);
        }
        else if(makeMoney == MakeMoney.COIN)
        {
            tableName = "coin_" + period + "_" + symbol;
            this.coinList = iOperateTableDao.queryInfoForLimit_coin(tableName,"closeTime", 0, 100000000);
        }
     }

     private void outPrint()
     {

     }
}
