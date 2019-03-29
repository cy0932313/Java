package com.chris.quantification.service.impl;

import com.chris.quantification.domain.xueqiuSixty.DataSixty;
import com.chris.quantification.domain.xueqiuSixty.SixtyContent;
import com.chris.quantification.utils.HttpClient;
import com.chris.quantification.service.IGetDataSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-03-12 18:12
 * @description：
 */
@Service
public class XueqiuSixtyDataImpl implements IGetDataSource {

    private String symbol, period, begin,result;

    @Override
    public void setParameter(HashMap<String, String> hashParameter) {
        this.symbol = hashParameter.get("symbol");
        this.period = hashParameter.get("period");
        this.begin = hashParameter.get("begin");
    }

    @Override
    public void getDataSoruce() {
        String count = "500000";
        String indicator = "kline,ma,macd,kdj,boll,rsi,wr,bias,cci,psy";
        String cookie = "xq_a_token=c90503da9578c457eaef9b92e49c94a54932e81d";
        String baseUrl = "https://stock.xueqiu.com/v5/stock/chart/kline.json?"
                + "symbol=" + symbol
                + "&begin=" + begin
                + "&period=" + period
                + "&count=" + count
                + "&indicator=" + indicator;

        HttpClient httpClient = new HttpClient(baseUrl, cookie, "GET");
        this.result = httpClient.sendRequest();
    }

    @Override
    public ArrayList<HashMap<String,String>> getHandleDataResult()
    {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<HashMap<String,String>> resultList = new ArrayList<>();
        try {
            DataSixty dataSixty = mapper.readValue(this.result, DataSixty.class);
            SixtyContent sixtyContent = dataSixty.getData();
            for (int i = 0; i < sixtyContent.getItem().size(); i++) {
                resultList.add(this.itemToIndex(sixtyContent.getColumn(), sixtyContent.getItem().get(i)));
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }

        return resultList;
    }

    private HashMap itemToIndex(ArrayList column, ArrayList item) {
        HashMap<String, String> indexMap = new HashMap<>();
        for (int i = 0; i < column.size(); i++) {
            if (item.get(i) == null) {
                indexMap.put(column.get(i).toString(), "");
            } else {
                indexMap.put(column.get(i).toString(), item.get(i).toString());
            }
        }

        return indexMap;
    }
}
