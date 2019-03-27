package com.chris.quantification.domain;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-03-25 09:58
 * @description：
 */

public class SymbolHold {
    public String symbolId;
    public String symbolName;
    public String symbolBuyTime;
    public double symbolBuyPrice;
    public boolean isETF;

    public SymbolHold(String symbolId, String symbolName, String symbolBuyTime, double symbolBuyPrice, boolean isETF) {
        this.symbolId = symbolId;
        this.symbolName = symbolName;
        this.symbolBuyTime = symbolBuyTime;
        this.symbolBuyPrice = symbolBuyPrice;
        this.isETF = isETF;
    }

    public String getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(String symbolId) {
        this.symbolId = symbolId;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public String getSymbolBuyTime() {
        return symbolBuyTime;
    }

    public void setSymbolBuyTime(String symbolBuyTime) {
        this.symbolBuyTime = symbolBuyTime;
    }

    public double getSymbolBuyPrice() {
        return symbolBuyPrice;
    }

    public void setSymbolBuyPrice(double symbolBuyPrice) {
        this.symbolBuyPrice = symbolBuyPrice;
    }

    public boolean isETF() {
        return isETF;
    }

    public void setETF(boolean ETF) {
        isETF = ETF;
    }
}
