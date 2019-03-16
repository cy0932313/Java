package com.chrisY.domain.quantification;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-03-02 09:36
 **/
public class Account {
    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    private String test;
    private String accountName;

    private Double initMoney;
    private Double money;

    private boolean accountStatus;
    private String buyDate;
    private double shareNumber;
    private double frozenShareNumber;

    private int sucessNum;
    private int failNum;

    private boolean shunt;

    private double shuntAvg;

    //买入股票总共的花费
    private double buyMoney;

    public double getBuyMoney() {
        return buyMoney;
    }

    public void setBuyMoney(double buyMoney) {
        this.buyMoney = buyMoney;
    }


    public double getShuntAvg() {
        return shuntAvg;
    }

    public void setShuntAvg(double shuntAvg) {
        this.shuntAvg = shuntAvg;
    }

    public boolean getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(boolean accountStatus) {
        this.accountStatus = accountStatus;
    }

    public boolean getShunt() {
        return shunt;
    }

    public void setShunt(boolean shunt) {
        this.shunt = shunt;
    }


    public int getSucessNum() {
        return sucessNum;
    }

    public void setSucessNum(int sucessNum) {
        this.sucessNum = sucessNum;
    }

    public int getFailNum() {
        return failNum;
    }

    public void setFailNum(int failNum) {
        this.failNum = failNum;
    }


    public Double getInitMoney() {
        return initMoney;
    }

    public void setInitMoney(Double initMoney) {
        this.initMoney = initMoney;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }


    public double getFrozenShareNumber() {
        return frozenShareNumber;
    }

    public void setFrozenShareNumber(double frozenShareNumber) {
        this.frozenShareNumber = frozenShareNumber;
    }

    public double getShareNumber() {
        return shareNumber;
    }

    public void setShareNumber(double shareNumber) {
        this.shareNumber = shareNumber;
    }
}
