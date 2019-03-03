package com.chrisY.domain.quantification;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-03-02 09:36
 **/
public class Account {
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

    private String accountName;
    private Double money;
    private int status;

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    private String buyDate;
    private double shareNumber;
    private double costPrice;

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

    private double frozenShareNumber;

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
