package com.chris.automated.trading;

import com.chris.automated.trading.domian.SymbolInfo;
import com.chris.automated.trading.domian.TransactionInfo;
import com.chris.automated.trading.requestManager.ApiRequest;
import com.chris.automated.trading.requestManager.UrlParamsBuilder;
import com.chris.automated.trading.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-05-22 23:26
 **/
@Component
public class Transaction {
    private Account account = new Account();
    private UrlParamsBuilder builder = UrlParamsBuilder.build();
    private BigDecimal balance = new BigDecimal(0);
    private static HashMap<String, TransactionInfo> transactionMap = new HashMap();

    @Autowired
    EmailUtils emailService;

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

    private void setOrdersPlace(JsonWrapper accountInfo,SymbolInfo transactionSymbol,String type,String referenceModel)
    {
        builder.putToPost("account-id",Account.account_id);
        builder.putToPost("symbol",transactionSymbol.getSymbol());
        builder.putToPost("type",type);
        if(type.equals("buy-market"))
        {
            //获取USDT余额
            this.getUsdt(accountInfo);
            builder.putToPost("amount",String.format("%.2f",balance.floatValue()));
        }
        else
        {
            //获取XRP余额
            this.getSymbol(accountInfo);
            builder.putToPost("amount",String.format("%.2f",balance.floatValue()));
        }

        builder.putToPost("source","margin-api");
        String resultStr =  new ApiRequest().sendReq("/v1/order/orders/place",builder,true);

        JsonWrapper jsonWrapper = JsonWrapper.parseFromString(resultStr);

        if(jsonWrapper.getString("status").equals("ok"))
        {
            TransactionInfo transactionInfo = new TransactionInfo();
            transactionInfo.setBuyingPrice(transactionSymbol.getCurrentPrice());
            transactionInfo.setBuyingAmount(balance);
            transactionInfo.setBuyingTime(transactionSymbol.getCurrentTime());
            transactionInfo.setMaxProfit(0);
            transactionInfo.setCurrentProfit(0);
            transactionInfo.setReferenceModel(referenceModel);
            if(referenceModel.equals("custom"))
            {
                transactionInfo.setCustomPrice(transactionSymbol.getCurrentPrice().floatValue());
            }
            else
            {
                transactionInfo.setCustomPrice(0);
            }
            transactionInfo.setBusinessType(type);
            transactionMap.put(transactionSymbol.getSymbol(),transactionInfo);

            this.emailService.sendMail("您有一条新的订单",Remind.remindMailForTransactionInfo(transactionInfo,transactionSymbol.getSymbol()));
        }
        else
        {
          this.emailService.sendMail("您有一条新的订单","下单失败,"+resultStr);
        }
    }

    //是否需要交易
    private boolean calculationProfit(TransactionInfo transactionInfo,SymbolInfo transactionSymbol)
    {
        float currentPrice = transactionSymbol.getCurrentPrice().floatValue();
        float buyPrice = transactionInfo.getBuyingPrice().floatValue();
        float maxProfit  = transactionInfo.getMaxProfit();
        float currentProfit;
        if(transactionInfo.getBusinessType().equals("buy-market"))
        {
            currentProfit = (currentPrice/buyPrice - 1) * 100;
        }
        else
        {
            currentProfit = (1 - currentPrice/buyPrice) * 100;
        }

        transactionInfo.setCurrentProfit(currentProfit);

        if(currentProfit > maxProfit)
        {
            transactionInfo.setMaxProfit(currentProfit);
            transactionMap.put(transactionSymbol.getSymbol(),transactionInfo);
        }
        else
        {
            int tempProfit = (int)(maxProfit/3);
            //如果收益回测到3X以内
            if(currentProfit < 3*tempProfit && tempProfit > 0)
            {
                if(transactionInfo.getBusinessType().equals("buy-market"))
                {
                    //那么反手做空，并且以做空价位基准
                    this.setOrdersPlace(account.getAccountMoney(),transactionSymbol,"sell-market","custom");
                }
                else
                {
                    //那么反手做多，并且以做多价位基准
                    this.setOrdersPlace(account.getAccountMoney(),transactionSymbol,"buy-market","custom");
                }
                return true;
            }
        }
        return false;
    }

    public void calculationTransaction(SymbolInfo transactionSymbol)
    {
        Remind.remindLogForSymbolInfo(transactionSymbol);

        /**
         * TEST
         */
//        transactionSymbol.setCurrentPrice(new BigDecimal(0.39));
//        transactionSymbol.setLastPrice(new BigDecimal(0.4));
//        transactionSymbol.setThreeAvg(0.3f);
//
//        if(transactionMap.size() == 0)
//        {
//            TransactionInfo transactionInfoTest = new TransactionInfo();
//            transactionInfoTest.setBuyingPrice(new BigDecimal("0.4"));
//            transactionInfoTest.setBuyingAmount(balance);
//            transactionInfoTest.setBuyingTime(transactionSymbol.getCurrentTime());
//            transactionInfoTest.setMaxProfit(0);
//            transactionInfoTest.setCurrentProfit(0);
//            transactionInfoTest.setReferenceModel("custom");
//            transactionInfoTest.setCustomPrice(0.4f);
//            transactionInfoTest.setBusinessType("sell-market");
//            transactionMap.put(transactionSymbol.getSymbol(),transactionInfoTest);
//            Remind.remindMailForTransactionInfo(transactionInfoTest,transactionSymbol.getSymbol());
//        }
//        else
//        {
//            transactionSymbol.setCurrentPrice(new BigDecimal(0.3));
//            transactionSymbol.setLastPrice(new BigDecimal(0.4));
//        }
//
        /**
         * END TEST
         */

        //如果有单子在手上需要计算收益
        TransactionInfo transactionInfo = transactionMap.get(transactionSymbol.getSymbol());

        if(transactionInfo != null)
        {
            Remind.remindLogForTransactionInfo(transactionInfo,transactionSymbol.getSymbol());

            if(this.calculationProfit(transactionInfo,transactionSymbol))
            {
                //如果收益有回测交易单，那么设置完自定义基准线后，下次在循环进行买卖判断
                return;
            }
        }
        else
        {
//            //有订单的时候
//            TransactionInfo tempTransactionInfo = new TransactionInfo();
//            tempTransactionInfo.setBuyingPrice(new BigDecimal("0.3854"));
//            tempTransactionInfo.setBuyingAmount(new BigDecimal("100.954"));
//            tempTransactionInfo.setBuyingTime("");
//            tempTransactionInfo.setMaxProfit(0);
//            tempTransactionInfo.setCurrentProfit(0);
//            tempTransactionInfo.setCustomPrice(new BigDecimal("0.3854").floatValue());
//            tempTransactionInfo.setBusinessType("buy-market");
//            transactionMap.put(transactionSymbol.getSymbol(),tempTransactionInfo);
//
//            //没有订单的时候
//            if(transactionSymbol.getCurrentPrice().floatValue() > transactionSymbol.getThreeAvg())
//            {
//                this.setOrdersPlace(account.getAccountMoney(),transactionSymbol,"buy-market","3");
//                return;
//            }
//
//            if(transactionSymbol.getCurrentPrice().floatValue() < transactionSymbol.getThreeAvg())
//            {
//                this.setOrdersPlace(account.getAccountMoney(),transactionSymbol,"sell-market","3");
//                return;
//            }
        }

        //当没有单子时直接进行3日线模式判断买入
        if(transactionInfo == null || transactionInfo.getReferenceModel().equals("3"))
        {
            //如果上一次监控的价格小于3日线且当前的价格大于3日线，那么买入反手做多
            if(transactionSymbol.getLastPrice().floatValue() < transactionSymbol.getThreeAvg() &&  transactionSymbol.getCurrentPrice().floatValue() > transactionSymbol.getThreeAvg())
            {
                this.setOrdersPlace(account.getAccountMoney(),transactionSymbol,"buy-market","3");
            }

            // 如果上一次监控的价格大于3日线且当前的价格小于3日线，那么卖出入反手做空
            if(transactionSymbol.getLastPrice().floatValue() > transactionSymbol.getThreeAvg() &&  transactionSymbol.getCurrentPrice().floatValue() < transactionSymbol.getThreeAvg())
            {
                this.setOrdersPlace(account.getAccountMoney(),transactionSymbol,"sell-market","3");
            }
        }
        else
        {
            //如果我在做空
            if(transactionInfo.getBusinessType().equals("sell-market"))
            {
                //如果上一次监控的价格小于自定义基准线且当前的价格大于自定义基准线，那么买入反手做多
                if(transactionSymbol.getLastPrice().floatValue() <= transactionInfo.getCustomPrice() &&  transactionSymbol.getCurrentPrice().floatValue() > transactionInfo.getCustomPrice()) {
                    this.setOrdersPlace(account.getAccountMoney(),transactionSymbol,"buy-market","custom");
                }
                else
                {
                    //如果做空的时候当前价格在3日线上，那么当快接近3日线的时候（0.5个点），反手做多，并调整为3日线基准
                    if(transactionSymbol.getCurrentPrice().floatValue() > transactionSymbol.getThreeAvg())
                    {
                        if(transactionSymbol.getCurrentPrice().floatValue() < transactionSymbol.getThreeAvg() * 1.01 && transactionSymbol.getCurrentPrice().floatValue() > transactionSymbol.getThreeAvg())
                        {
                            this.setOrdersPlace(account.getAccountMoney(),transactionSymbol,"buy-market","3");
                        }
                    }
                }
            }
            //如果我在做多
            else
            {
                //如果上一次监控的价格大于自定义基准线且当前的价格小于自定义基准线，那么卖出入反手做空
                if(transactionSymbol.getLastPrice().floatValue() >= transactionInfo.getCustomPrice() &&  transactionSymbol.getCurrentPrice().floatValue() < transactionInfo.getCustomPrice())
                {
                    this.setOrdersPlace(account.getAccountMoney(),transactionSymbol,"sell-market","custom");
                }
                else
                {
                    //如果做多的时候当前价格在3日线下，那么当快接近3日线的时候（0.5个点），反手做空，并调整为3日线基准
                    if(transactionSymbol.getCurrentPrice().floatValue() < transactionSymbol.getThreeAvg())
                    {
                        if(transactionSymbol.getCurrentPrice().floatValue() > transactionSymbol.getThreeAvg() * 0.99 && transactionSymbol.getCurrentPrice().floatValue() < transactionSymbol.getThreeAvg())
                        {
                            this.setOrdersPlace(account.getAccountMoney(),transactionSymbol,"sell-market","3");
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        int tempProfit = (int)(1.2/3);

        System.out.println(tempProfit);

    }
}
