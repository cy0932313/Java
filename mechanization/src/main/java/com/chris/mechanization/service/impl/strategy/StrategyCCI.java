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


         if(symbolList != null && symbolList.size() > 0)
         {
             int size = symbolList.size();
             for(int i = 0;i < size;i++)
             {
                 Transaction transaction = new Transaction();
                 ItemStock itemStock = symbolList.get(i);
                 float cci = Float.parseFloat(itemStock.getCci());
                 if(cci > -100 && transactionStatus)
                 {
                     transaction.setCoinName(tableName);
                     transaction.setPrice(itemStock.getClose());
                     transaction.setTransactionTime(itemStock.getTime());
                     transaction.setTransactionType("BUY");
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
            tableName = "symbol_"+period+"_" + symbol;;
            this.symbolList = iOperateTableDao.queryInfoForLimit_symbol(tableName, "timestamp", 0, 100000000);
        }
        else if(makeMoney == MakeMoney.COIN)
        {
            tableName = "coin_" + period + "_" + symbol;
            this.coinList = iOperateTableDao.queryInfoForLimit_coin(tableName,"closeTime", 0, 100000000);
        }
     }
}
