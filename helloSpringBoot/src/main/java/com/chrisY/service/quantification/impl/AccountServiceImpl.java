package com.chrisY.service.quantification.impl;

import com.chrisY.domain.quantification.Account;
import com.chrisY.service.quantification.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    IEmailService emailService;
    @Override
    public Account initAccount(String accountId, Double money, boolean accountStatus) {
        Account account = new Account();
        account.setInitMoney(money);
        account.setAccountName(accountId);
        account.setMoney(money);
        account.setAccountStatus(accountStatus);
        account.setShunt(false);
        return account;
    }

    @Override
    public Account initAccount(String accountId, Double money, boolean accountStatus, boolean shunt, double shuntAvg) {
        Account account = new Account();
        account.setInitMoney(money);
        account.setAccountName(accountId);
        account.setMoney(money);
        account.setAccountStatus(accountStatus);
        account.setShunt(shunt);
        account.setShuntAvg(shuntAvg);
        return account;
    }


    @Override
    public void buy(Account account, Double price, StringBuilder printLog) {
        //账户余额可以买入多少股
        double shareNumber = 0;
        if (!account.getShunt()) {
            shareNumber = Math.floor(Math.floor(account.getMoney() / price) / 100) * 100;
        } else {
            shareNumber = Math.floor(Math.floor(account.getInitMoney() * account.getShuntAvg() / price) / 100) * 100;
        }

        double priceCount = price * shareNumber;//股价花费金额


        //W2.5
        double servicePrice = (priceCount / 10000) * 2.5;
        if (servicePrice < 5) {
            servicePrice = 5;
        }

        if (account.getMoney() - priceCount - servicePrice < 0) {
            return;
        }

        account.setShareNumber(account.getShareNumber() + shareNumber);
        account.setAccountStatus(true);
        account.setMoney(account.getMoney() - priceCount - servicePrice);
        account.setBuyMoney(priceCount + servicePrice + account.getBuyMoney());
        printLog.append("买入成功：共花费：" + (priceCount + servicePrice) + "，其中股票花费" + priceCount + ",手续费:" + servicePrice + "，账户余额:" + account.getMoney() + "，目前持股" + account.getShareNumber());
        printLog.append("<br />");
        emailService.sendMail("SH600196","买卖信号");
    }

    @Override
    public void sell(Account account, Double price, StringBuilder printLog) {

        double spend = price * account.getShareNumber();

        //W2.5
        double servicePrice = (spend / 10000) * 2.5;
        if (servicePrice < 5) {
            servicePrice = 5;
        }
        //印花税
        double stampDuty = spend * 0.001;

        spend = spend - servicePrice - stampDuty;

        account.setAccountStatus(false);
        account.setMoney(account.getMoney() + spend);

        double profitLoss = spend / account.getBuyMoney() - 1;
        if (profitLoss > 0) {
            account.setSucessNum(account.getSucessNum() + 1);
        } else {
            account.setFailNum(account.getFailNum() + 1);
        }

        printLog.append("卖出入成功：共花费：" + spend + ",其中手续费:" + (servicePrice + stampDuty) + "，账户余额:" + account.getMoney() + "</br><font color=\"color:red\">本次盈亏:" + (int) (spend - account.getBuyMoney()) + ",本次盈亏率:" + new BigDecimal(profitLoss * 100).setScale(2, RoundingMode.UP) + "%,总盈亏："+  new BigDecimal((account.getMoney() / account.getInitMoney() - 1) * 100).setScale(2, RoundingMode.UP) +"%</font>");
        printLog.append("<br />");

        account.setShareNumber(0);
        account.setBuyMoney(0);
        emailService.sendMail("SH600196","买卖信号");
    }
}
