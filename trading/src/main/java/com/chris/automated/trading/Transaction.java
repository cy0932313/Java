package com.chris.automated.trading;

import com.chris.automated.trading.domian.SymbolInfo;
import com.chris.automated.trading.requestManager.ApiRequest;
import com.chris.automated.trading.requestManager.UrlParamsBuilder;
import com.chris.automated.trading.utils.ChrisDateUtils;
import com.chris.automated.trading.utils.JsonWrapper;
import com.chris.automated.trading.utils.JsonWrapperArray;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-05-22 23:26
 **/
public class Transaction {
    private Account account = new Account();
    private UrlParamsBuilder builder = UrlParamsBuilder.build();
    private BigDecimal balance = new BigDecimal(0);

    private void getUsdt(JsonWrapper accountInfo)
    {
        JsonWrapper data = accountInfo.getJsonObject("data");
        JsonWrapperArray jsonWrapperArray = data.getJsonArray("list");

        jsonWrapperArray.forEach((item) -> {
            if(item.getString("currency").equals("usdt") && item.getString("type").equals("trade"))
            {
                balance = item.getBigDecimal("balance");
            }
        });
    }

    private void getSymbol(JsonWrapper accountInfo)
    {
        JsonWrapper data = accountInfo.getJsonObject("data");
        JsonWrapperArray jsonWrapperArray = data.getJsonArray("list");

        jsonWrapperArray.forEach((item) -> {
            if(item.getString("currency").equals("xrp") && item.getString("type").equals("trade"))
            {
                balance = item.getBigDecimal("balance");
            }
        });
    }

    private void setOrdersPlace(JsonWrapper accountInfo,SymbolInfo transactionSymbol,String type)
    {
        builder.putToPost("account-id",Account.account_id);
        builder.putToPost("symbol",transactionSymbol.getSymbol());
        builder.putToPost("type",type);
        if(type.equals("buy-market"))
        {
            //获取USDT余额
            this.getUsdt(accountInfo);
            //可以购买的数量
            float amount = balance.floatValue() / transactionSymbol.getCurrentPrice().floatValue();
            builder.putToPost("amount",String.format("%.2f",amount-5));
            builder.putToPost("price",String.valueOf(transactionSymbol.getCurrentPrice().floatValue() - 0.0005));
        }
        else
        {
            //获取XRP余额
            this.getSymbol(accountInfo);
            builder.putToPost("amount",String.format("%.2f",balance.floatValue()));
            builder.putToPost("price",String.valueOf(transactionSymbol.getCurrentPrice().floatValue() + 0.0005));
        }

        builder.putToPost("source","margin-api");
        String resultStr =  new ApiRequest().sendReq("/v1/order/orders/place",builder,true);

        System.out.println(resultStr);
    }

    public void calculationTransaction(SymbolInfo transactionSymbol)
    {
        System.out.println(transactionSymbol.getSymbol()+":" + transactionSymbol.getCurrentPrice() +"=="+ ChrisDateUtils.timeStamp2Date(transactionSymbol.getCurrentTime(),null));

//       如果上一次监控的价格小于3日线且当前的价格大于3日线，那么买入做多
        if(transactionSymbol.getLastPrice().floatValue() < transactionSymbol.getThreeAvg() &&  transactionSymbol.getCurrentPrice().floatValue() > transactionSymbol.getThreeAvg())
        {
            System.out.println("买入条件满足");
            this.setOrdersPlace(account.getAccountMoney(),transactionSymbol,"buy-market");
        }

//       如果上一次监控的价格大于3日线且当前的价格小于3日线，那么卖出入反手做空
        if(transactionSymbol.getLastPrice().floatValue() > transactionSymbol.getThreeAvg() &&  transactionSymbol.getCurrentPrice().floatValue() < transactionSymbol.getThreeAvg())
        {
            System.out.println("卖入条件满足");
            this.setOrdersPlace(account.getAccountMoney(),transactionSymbol,"sell-market");
        }
    }
}
