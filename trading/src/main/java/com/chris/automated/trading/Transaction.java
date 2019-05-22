package com.chris.automated.trading;

import com.chris.automated.trading.domian.SymbolInfo;
import com.chris.automated.trading.utils.ChrisDateUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-05-22 23:26
 **/
public class Transaction {
    public void calculationTransaction(SymbolInfo transactionSymbol)
    {
        System.out.println(transactionSymbol.getSymbol()+":" + transactionSymbol.getCurrentPrice() +"=="+ ChrisDateUtils.timeStamp2Date(transactionSymbol.getCurrentTime(),null));
    }
}
