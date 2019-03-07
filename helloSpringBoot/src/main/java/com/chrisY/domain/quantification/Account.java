package com.chrisY.domain.quantification;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-03-02 09:36
 **/
public class Account {

    private String accountName;

    private Double initMoney;
    private Double money;
    private int status;
    private String buyDate;
    private double shareNumber;
    private double costPrice;
    private double frozenShareNumber;

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

    private int sucessNum;
    private int failNum;

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

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
