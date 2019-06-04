package com.chris.mechanization.utils;

import com.chris.mechanization.dao.IOperateTableDao;
import com.chris.mechanization.domain.BackTestResult;
import com.chris.mechanization.domain.Transaction;

import java.util.List;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-04-27 22:13
 **/
public class ChrisCalculationUtils {
    public static void outPrint(IOperateTableDao iOperateTableDao,String symbolCode,String symbolName,String plan)
    {
        List<Transaction> transactionList = iOperateTableDao.queryInfoForTransaction();
        int size = transactionList.size();
        if(size == 0)
        {
            return;
        }
        //总收益
        float countProfit = 0;
        //按1W来算可以赚多少钱
        float initMoney = 10000;
        //交易次数
        int transactionNum = 0;
        //持仓天数
        int holdDay = 0;
        //最大回调
        float md = 0;
        //最大连续亏损次数
        int lossNum = 0;
        int tempLossNum = 0;
        //胜率
        String winProfit = "0";
        int tempTrue = 0;
        //持仓与区间时间的百分比
        String t;
        //区间涨幅
        String immobility;


        float maxProfit = 0;
        float minxProfit = 0;


        for (int i = 0; i < size; i++) {
            Transaction transaction = transactionList.get(i);

            countProfit = Float.parseFloat(transaction.getProfit()) + countProfit;
            transactionNum = size;
            holdDay = holdDay + ChrisDateUtils.differentDaysByMillisecond(transaction.getBuyTime(), transaction.getSellTime(), "yyyy-MM-dd");
            if (countProfit > maxProfit) {
                maxProfit = countProfit;
                minxProfit = 0;
            } else {
                minxProfit = countProfit;
                if (maxProfit - minxProfit > md) {
                    md = maxProfit - minxProfit;
                }
            }

            if (Float.parseFloat(transaction.getProfit()) < 0) {
                ++tempLossNum;
            } else {
                tempLossNum = 0;
                ++tempTrue;
            }
            if (tempLossNum > lossNum) {
                lossNum = tempLossNum;
            }

            initMoney = initMoney + ((Float.parseFloat(transaction.getProfit())/100 *initMoney));
            System.out.println("当前本金"+initMoney+"==当前收益"+Float.parseFloat(transaction.getProfit())/100);
            winProfit = String.format("%.2f", (float) tempTrue / size * 100) + "%";
        }
        Transaction fistTransaction = transactionList.get(0);
        Transaction lastTransaction = transactionList.get(size - 1);
        immobility = String.format("%.2f", (Float.valueOf(lastTransaction.getSellPrice()) / Float.valueOf(fistTransaction.getBuyPrice()) - 1) * 100);

        t = String.format("%.2f", Float.valueOf(holdDay * 100 / ChrisDateUtils.differentDaysByMillisecond
                (fistTransaction.getBuyTime(), lastTransaction.getSellTime(), "yyyy-MM-dd")));

        BackTestResult backTestResult = new BackTestResult(symbolCode, symbolName, plan,
                String.format("%.2f", countProfit) + "%", String.valueOf(transactionNum), String.valueOf(holdDay), String.format("%.2f", md) + "%",
                String.valueOf(lossNum), winProfit, t + "%", immobility + "%");
        iOperateTableDao.addBackTestResult(backTestResult);
        System.out.println(symbolCode+"总盈利人民币："+initMoney);
    }
}
