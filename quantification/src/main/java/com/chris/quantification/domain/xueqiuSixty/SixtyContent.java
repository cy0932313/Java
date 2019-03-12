package com.chris.quantification.domain.xueqiuSixty;

import java.util.ArrayList;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-03-12 17:35
 * @description：
 */

public class SixtyContent {
    String symbol;
    ArrayList<String> column;
    ArrayList<ArrayList<String>> item;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public ArrayList<String> getColumn() {
        return column;
    }

    public void setColumn(ArrayList<String> column) {
        this.column = column;
    }

    public ArrayList<ArrayList<String>> getItem() {
        return item;
    }

    public void setItem(ArrayList<ArrayList<String>> item) {
        this.item = item;
    }

}
