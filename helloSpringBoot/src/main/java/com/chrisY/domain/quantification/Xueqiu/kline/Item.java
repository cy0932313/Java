package com.chrisY.domain.quantification.Xueqiu.kline;

import java.util.List;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-03-02 19:26
 **/
public class Item {

    public List<List<String>> getDataList() {
        return dataList;
    }

    public void setDataList(List<List<String>> dataList) {
        this.dataList = dataList;
    }

    List<List<String>> dataList;
}
