package com.chris.quantification.domain;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-03-25 09:58
 * @description：
 */

public class SymbolHold {
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

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public boolean isETF() {
        return isETF;
    }

    public void setETF(boolean ETF) {
        isETF = ETF;
    }
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String symbolCode;
    public String symbolName;
    public String buyTime;
    public double buyPrice;
    public boolean isETF;


}
