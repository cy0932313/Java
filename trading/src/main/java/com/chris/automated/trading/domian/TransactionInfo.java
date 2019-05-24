package com.chris.automated.trading.domian;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-05-24 20:38
 **/
public class TransactionInfo
{
    private BigDecimal buyingPrice;
    private BigDecimal buyingAmount;
    private String buyingTime;
    private float maxProfit;
    private float currentProfit;
    /**
     * referenceModel=3  以3日线参照买卖  referenceModel=7 以7日线参照买卖   referenceModel=22 以22日线参照买卖
     * referenceModel=custom  以利润点作为参照线
     */

    private String referenceModel;
    /**
     * 当referenceModel=custom customPrice有值
     */
    private float customPrice;

    /**
     * 当前订单的模式（buy-market 为做多  sell-market 为做空）
     */
    private String BusinessType;

    public BigDecimal getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(BigDecimal buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public BigDecimal getBuyingAmount() {
        return buyingAmount;
    }

    public void setBuyingAmount(BigDecimal buyingAmount) {
        this.buyingAmount = buyingAmount;
    }

    public String getBuyingTime() {
        return buyingTime;
    }

    public void setBuyingTime(String buyingTime) {
        this.buyingTime = buyingTime;
    }

    public float getMaxProfit() {
        return maxProfit;
    }

    public void setMaxProfit(float maxProfit) {
        this.maxProfit = maxProfit;
    }

    public float getCurrentProfit() {
        return currentProfit;
    }

    public void setCurrentProfit(float currentProfit) {
        this.currentProfit = currentProfit;
    }

    public String getReferenceModel() {
        return referenceModel;
    }

    public void setReferenceModel(String referenceModel) {
        this.referenceModel = referenceModel;
    }

    public float getCustomPrice() {
        return customPrice;
    }

    public void setCustomPrice(float customPrice) {
        this.customPrice = customPrice;
    }

    public String getBusinessType() {
        return BusinessType;
    }

    public void setBusinessType(String businessType) {
        BusinessType = businessType;
    }
}
