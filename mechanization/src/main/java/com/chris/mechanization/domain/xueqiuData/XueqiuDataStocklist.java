package com.chris.mechanization.domain.xueqiuData;

import java.util.ArrayList;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-03-06 20:59
 **/
public class XueqiuDataStocklist {
    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ArrayList<ItemStock> getChartlist() {
        return chartlist;
    }

    public void setChartlist(ArrayList<ItemStock> chartlist) {
        this.chartlist = chartlist;
    }

    Stock stock;
    String success ;
    ArrayList<ItemStock> chartlist;
}
