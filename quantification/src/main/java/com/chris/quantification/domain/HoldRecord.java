package com.chris.quantification.domain;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-04-14 16:39
 **/
public class HoldRecord {
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

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }
    public HoldRecord(){};
    public HoldRecord(String symbolCode, String symbolName, String open, String close, String high, String low, String previous_cci, String cci, String time, String groupID) {
        this.symbolCode = symbolCode;
        this.symbolName = symbolName;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.previous_cci = previous_cci;
        this.cci = cci;
        this.time = time;
        this.groupID = groupID;
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

    public String getPrevious_cci() {
        return previous_cci;
    }

    public void setPrevious_cci(String previous_cci) {
        this.previous_cci = previous_cci;
    }

    public String getCci() {
        return cci;
    }

    public void setCci(String cci) {
        this.cci = cci;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
    public String symbolCode;
    public String symbolName;
    public String open;
    public String close;
    public String high;
    public String low;
    public String previous_cci;
    public String cci;
    public String time;
    public String groupID;
}
