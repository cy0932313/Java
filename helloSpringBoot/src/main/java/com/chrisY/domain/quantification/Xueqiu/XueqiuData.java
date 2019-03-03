package com.chrisY.domain.quantification.Xueqiu;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 从雪球获取过来的数据
 * @author: Chris.Y
 * @create: 2019-03-02 12:37
 **/
public class XueqiuData {


    String symbol;

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

    ArrayList<String> column;
    ArrayList<ArrayList<String>> item;
}
