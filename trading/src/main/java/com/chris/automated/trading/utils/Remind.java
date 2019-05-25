package com.chris.automated.trading.utils;

import com.chris.automated.trading.domian.SymbolInfo;
import com.chris.automated.trading.domian.TransactionInfo;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-05-25 11:02
 **/
public class Remind
{
    public static void remindLogForSymbolInfo(SymbolInfo symbolInfo)
    {
        String monitorLog =  "\n"+"当前监控信息"+"\n" + symbolInfo.getSymbol()+"_"+ ChrisDateUtils.timeStamp2Date(symbolInfo.getCurrentTime(),null)+"\n" +"当前价：" +
                symbolInfo.getCurrentPrice()+"\n" + "上一个价格："+
                symbolInfo.getLastPrice() +"==" +
                ChrisDateUtils.timeStamp2Date(symbolInfo.getLastTime(),null) +"\n" + "3日均线：" +
                symbolInfo.getThreeAvg()+"\n";

        LogUtils.printLog(monitorLog);
    }

    public static void remindLogForTransactionInfo(TransactionInfo transactionInfo, String symbol)
    {
        String transactionInfoLog = "\n"+"当前订单为"+transactionInfo.getBusinessType()+"\n"+
                symbol +"_"+ ChrisDateUtils.timeStamp2Date(transactionInfo.getBuyingTime(),null)+"\n"
                +"买入价(基准线)：" + transactionInfo.getBuyingPrice()+"\n"
                + "数量："+ transactionInfo.getBuyingAmount() +"\n"
                + "当前盈利：" + transactionInfo.getCurrentProfit()+"\n"
                + "最高盈利：" + transactionInfo.getMaxProfit()+"\n"
                + "模式：" + transactionInfo.getReferenceModel()+"\n";

        LogUtils.printLog(transactionInfoLog);
    }

    public static String remindMailForTransactionInfo(TransactionInfo transactionInfo, String symbol)
    {
        Remind.remindLogForTransactionInfo(transactionInfo,symbol);

        String remindContent = "买入成功"+ "\n" +"当前订单为"+transactionInfo.getBusinessType()+"\n"+
                symbol +"_"+ ChrisDateUtils.timeStamp2Date(transactionInfo.getBuyingTime(),null)+"\n"
                +"买入价(基准线)：" + transactionInfo.getBuyingPrice()+"\n"
                + "数量："+ transactionInfo.getBuyingAmount() +"\n"
                + "模式：" + transactionInfo.getReferenceModel()+"\n";

        return remindContent;
    }
}
