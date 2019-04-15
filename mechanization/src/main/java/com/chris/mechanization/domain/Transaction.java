package com.chris.mechanization.domain;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-04-08 17:59
 * @description：
 */

public class Transaction {
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    String price;

    public Transaction(String price, String transactionType, String transactionTime, String reason) {
        this.price = price;
        this.transactionType = transactionType;
        this.transactionTime = transactionTime;
        this.reason = reason;
    }

    String transactionType;
    String transactionTime;
    String reason;
}
