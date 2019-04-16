package com.chris.mechanization.domain;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-04-08 17:59
 * @description：
 */

public class Transaction {
    public String getSymbolCode() {
        return symbolCode;
    }

    public void setSymbolCode(String symbolCode) {
        this.symbolCode = symbolCode;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getBuyNextPrice() {
        return buyNextPrice;
    }

    public void setBuyNextPrice(String buyNextPrice) {
        this.buyNextPrice = buyNextPrice;
    }

    public String getBuyReason() {
        return buyReason;
    }

    public void setBuyReason(String buyReason) {
        this.buyReason = buyReason;
    }

    public String getSellTime() {
        return sellTime;
    }

    public void setSellTime(String sellTime) {
        this.sellTime = sellTime;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getSellReason() {
        return sellReason;
    }

    public void setSellReason(String sellReason) {
        this.sellReason = sellReason;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    private String symbolCode;
    private String symbolName;
    private String buyTime;
    private String buyPrice;
    private String buyNextPrice;
    private String buyReason;
    private String sellTime;
    private String sellPrice;
    private String sellReason;
    private String profit;

    
}
