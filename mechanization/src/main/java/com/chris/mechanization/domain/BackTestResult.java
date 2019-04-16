package com.chris.mechanization.domain;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-04-16 17:43
 * @description：
 */

public class BackTestResult {
    private String symbolCode;

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

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getCountProfit() {
        return countProfit;
    }

    public void setCountProfit(String countProfit) {
        this.countProfit = countProfit;
    }

    public String getTransactionNum() {
        return transactionNum;
    }

    public void setTransactionNum(String transactionNum) {
        this.transactionNum = transactionNum;
    }

    public String getHoldDay() {
        return holdDay;
    }

    public void setHoldDay(String holdDay) {
        this.holdDay = holdDay;
    }

    public String getMd() {
        return md;
    }

    public void setMd(String md) {
        this.md = md;
    }

    public String getLossNum() {
        return lossNum;
    }

    public void setLossNum(String lossNum) {
        this.lossNum = lossNum;
    }

    public String getWinProfit() {
        return winProfit;
    }

    public void setWinProfit(String winProfit) {
        this.winProfit = winProfit;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public BackTestResult(String symbolCode) {
        this.symbolCode = symbolCode;
    }

    public String getImmobility() {
        return immobility;
    }

    public void setImmobility(String immobility) {
        this.immobility = immobility;
    }

    private String symbolName;
    private String plan;

    public BackTestResult(String symbolCode, String symbolName, String plan, String countProfit, String transactionNum, String holdDay, String md, String lossNum, String winProfit, String t, String immobility) {
        this.symbolCode = symbolCode;
        this.symbolName = symbolName;
        this.plan = plan;
        this.countProfit = countProfit;
        this.transactionNum = transactionNum;
        this.holdDay = holdDay;
        this.md = md;
        this.lossNum = lossNum;
        this.winProfit = winProfit;
        this.t = t;
        this.immobility = immobility;
    }

    private String countProfit;
    private String transactionNum;
    private String holdDay;
    private String md;
    private String lossNum;
    private String winProfit;
    private String t;
    private String immobility;
}
