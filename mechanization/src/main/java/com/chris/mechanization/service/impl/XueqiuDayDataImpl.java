package com.chris.mechanization.service.impl;

import com.chris.mechanization.domain.xueqiuData.ItemStock;
import com.chris.mechanization.domain.xueqiuData.XueqiuDataStocklist;
import com.chris.mechanization.service.IGetDataSource;
import com.chris.mechanization.utils.HttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-04-01 10:43
 * @description：
 */

@Service
public class XueqiuDayDataImpl implements IGetDataSource {

    private String symbol, period, begin, end, result;

    @Override
    public void setParameter(HashMap<String, String> hashParameter) {
        this.symbol = hashParameter.get("symbol");
        this.period = hashParameter.get("period");
        this.begin = hashParameter.get("begin");
        this.end = hashParameter.get("end");
    }

    @Override
    public void getDataSoruce() {
        String cookie = "xq_a_token=c90503da9578c457eaef9b92e49c94a54932e81d";
        String baseUrl = "https://xueqiu.com/stock/forchartk/stocklist.json?"
                + "symbol=" + symbol
                + "&begin=" + begin
                + "&end=" + end
                + "&period=" + period
                + "&type=after"
                +"&_="+String.valueOf(System.currentTimeMillis());

        HttpClient httpClient = new HttpClient(baseUrl, cookie, "GET");
        this.result = httpClient.sendRequest();
    }

    @Override
    public ArrayList getHandleDataResult() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<ItemStock> resultList = new ArrayList<>();

        XueqiuDataStocklist xueqiuDataStocklist = new XueqiuDataStocklist();

        try {
            xueqiuDataStocklist = mapper.readValue(this.result, XueqiuDataStocklist.class);

            for (int i = 0; i < xueqiuDataStocklist.getChartlist().size(); i++) {

                ItemStock indexStock = xueqiuDataStocklist.getChartlist().get(i);
                resultList.add(indexStock);

            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }

        return resultList;
    }
}
