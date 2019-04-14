package com.chris.quantification.domain;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-04-13 22:31
 **/
public class SymbolMonitor {
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

    public String symbolCode;
    public String symbolName;
}
