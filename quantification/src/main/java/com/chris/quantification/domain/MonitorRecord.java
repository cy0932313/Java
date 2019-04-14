package com.chris.quantification.domain;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-04-14 08:51
 **/
public class MonitorRecord {

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

    public MonitorRecord(String symbolCode, String symbolName, String cci, String time) {
        this.symbolCode = symbolCode;
        this.symbolName = symbolName;
        this.cci = cci;
        this.time = time;
    }

    public String symbolCode;
    public String symbolName;
    public String cci;
    public String time;
}
