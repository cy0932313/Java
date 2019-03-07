package com.chrisY.service.quantification.impl;

import com.chrisY.domain.quantification.Account;
import com.chrisY.service.quantification.IAccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-03-02 09:40
 **/
@Service
public class AccountServiceImpl implements IAccountService {

    @Override
    public Account initAccount(String accountId, Double money, int status) {
        Account account = new Account();
        account.setInitMoney(money);
        account.setAccountName(accountId);
        account.setMoney(money);
        account.setStatus(status);
        return account;
    }

    @Override
    public void buy(Account account, Double price,StringBuilder printLog) {
        account.setCostPrice(account.getMoney());
        //账户余额可以买入多少股
        double shareNumber = Math.floor(Math.floor(account.getMoney() / price) / 100) * 100;
        account.setShareNumber(shareNumber);
        double priceCount = price * shareNumber;//股价花费金额

        //W2.5
        double servicePrice = (priceCount / 10000) * 2.5;
        if (servicePrice < 5) {
            servicePrice = 5;
        }

        account.setStatus(0);
        account.setMoney(account.getMoney() - priceCount - servicePrice);
        printLog.append("买入成功：共花费：" + (priceCount + servicePrice) + "，其中股票花费" + priceCount + ",手续费:" + servicePrice + "，账户余额:" + account.getMoney());
        printLog.append("<br />");
        System.out.println("买入成功：共花费：" + (priceCount + servicePrice) + "，其中股票花费" + priceCount + ",手续费:" + servicePrice + "，账户余额:" + account.getMoney());
    }

    @Override
    public void sell(Account account, Double price,StringBuilder printLog) {

        double spend = price * account.getShareNumber();

        //W2.5
        double servicePrice = (spend / 10000) * 2.5;
        if (servicePrice < 5) {
            servicePrice = 5;
        }
        //印花税
        double stampDuty = spend * 0.001;

        spend = spend - servicePrice - stampDuty;

        account.setStatus(1);
        account.setMoney(account.getMoney() + spend);

        double profitLoss =  account.getMoney() / account.getCostPrice() - 1;
        if(profitLoss > 0)
        {
            account.setSucessNum(account.getSucessNum() + 1);
        }
        else
        {
            account.setFailNum(account.getFailNum() + 1);
        }
        printLog.append("卖出入成功：共花费：" + spend + ",其中手续费:" + (servicePrice + stampDuty) + ",买入成本" + account.getCostPrice() + "，卖出后账户余额:" + account.getMoney() + "，本次盈亏:"+ (account.getMoney() - account.getCostPrice()) +"</br><font color=\"color:#333333\">本次盈亏率:" +    new BigDecimal(profitLoss * 100).setScale(2, RoundingMode.UP) + "%</font>");
        printLog.append("<br />");
        System.out.println("卖出入成功：共花费：" + spend + ",其中手续费:" + (servicePrice + stampDuty) + ",买入成本" + account.getCostPrice() + "，卖出后账户余额:" + account.getMoney() + "，本次盈亏:"+ (account.getMoney() - account.getCostPrice()) +"<span style=\"color:#333\">本次盈亏率:" +    new BigDecimal(profitLoss * 100).setScale(2, RoundingMode.UP) + "%</span>");
    }
}
