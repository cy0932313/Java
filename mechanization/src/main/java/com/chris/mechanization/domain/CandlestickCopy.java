package com.chris.mechanization.domain;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-04-27 11:38
 **/
public class CandlestickCopy {
    private String ma5 ;
    private String ma7 ;

    public String getMa5() {
        return ma5;
    }

    public void setMa5(String ma5) {
        this.ma5 = ma5;
    }

    private String ma10 ;

    public String getMa7() {
        return ma7;
    }

    public void setMa7(String ma7) {
        this.ma7 = ma7;
    }

    public String getMa10() {
        return ma10;
    }

    public void setMa10(String ma10) {
        this.ma10 = ma10;
    }

    public String getMa14() {
        return ma14;
    }

    public void setMa14(String ma14) {
        this.ma14 = ma14;
    }

    public String getMa20() {
        return ma20;
    }

    public void setMa20(String ma20) {
        this.ma20 = ma20;
    }

    public String getMa30() {
        return ma30;
    }

    public void setMa30(String ma30) {
        this.ma30 = ma30;
    }

    public String getMa60() {
        return ma60;
    }

    public void setMa60(String ma60) {
        this.ma60 = ma60;
    }

    public String getCci() {
        return cci;
    }

    public void setCci(String cci) {
        this.cci = cci;
    }

    public String getMa22() {
        return ma22;
    }

    public void setMa22(String ma22) {
        this.ma22 = ma22;
    }

    public String getMa55() {
        return ma55;
    }

    public void setMa55(String ma55) {
        this.ma55 = ma55;
    }

    public Long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Long openTime) {
        this.openTime = openTime;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Long closeTime) {
        this.closeTime = closeTime;
    }

    public String getQuoteAssetVolume() {
        return quoteAssetVolume;
    }

    public void setQuoteAssetVolume(String quoteAssetVolume) {
        this.quoteAssetVolume = quoteAssetVolume;
    }

    public Long getNumberOfTrades() {
        return numberOfTrades;
    }

    public void setNumberOfTrades(Long numberOfTrades) {
        this.numberOfTrades = numberOfTrades;
    }

    public String getTakerBuyBaseAssetVolume() {
        return takerBuyBaseAssetVolume;
    }

    public void setTakerBuyBaseAssetVolume(String takerBuyBaseAssetVolume) {
        this.takerBuyBaseAssetVolume = takerBuyBaseAssetVolume;
    }

    public String getTakerBuyQuoteAssetVolume() {
        return takerBuyQuoteAssetVolume;
    }

    public void setTakerBuyQuoteAssetVolume(String takerBuyQuoteAssetVolume) {
        this.takerBuyQuoteAssetVolume = takerBuyQuoteAssetVolume;
    }

    private String ma14 ;
    private String ma20 ;
    private String ma30 ;
    private String ma60 ;
    private String cci ;
    private String ma22 ;
    private String ma55 ;
    private Long openTime;

    private String open;

    private String high;

    private String low;

    private String close;

    private String volume;

    private Long closeTime;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;
    private String quoteAssetVolume;

    private Long numberOfTrades;

    private String takerBuyBaseAssetVolume;

    private String takerBuyQuoteAssetVolume;
}
