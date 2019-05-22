package com.chris.automated.trading.domian;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-05-22 22:40
 **/
public class SymbolInfo {
    private String symbol;
    private BigDecimal currentPrice;
    private String currentTime;
    private BigDecimal lastPrice;
    private String lastTime;
    private float threeAvg;
    private float sevenAvg;
    private float twentyTwoAvg;
    private int pricePrecision;
    private int amountPrecision;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    public float getThreeAvg() {
        return threeAvg;
    }

    public void setThreeAvg(float threeAvg) {
        this.threeAvg = threeAvg;
    }

    public float getSevenAvg() {
        return sevenAvg;
    }

    public void setSevenAvg(float sevenAvg) {
        this.sevenAvg = sevenAvg;
    }

    public float getTwentyTwoAvg() {
        return twentyTwoAvg;
    }

    public void setTwentyTwoAvg(float twentyTwoAvg) {
        this.twentyTwoAvg = twentyTwoAvg;
    }

    public int getPricePrecision() {
        return pricePrecision;
    }

    public void setPricePrecision(int pricePrecision) {
        this.pricePrecision = pricePrecision;
    }

    public int getAmountPrecision() {
        return amountPrecision;
    }

    public void setAmountPrecision(int amountPrecision) {
        this.amountPrecision = amountPrecision;
    }
}
