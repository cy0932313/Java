package com.chris.automated.trading;

import com.chris.automated.trading.requestManager.ApiRequest;
import com.chris.automated.trading.requestManager.UrlParamsBuilder;
import com.chris.automated.trading.utils.ChrisDateUtils;
import com.chris.automated.trading.utils.JsonWrapper;
import com.chris.automated.trading.utils.JsonWrapperArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-05-20 22:19
 **/
public class Average
{
    private HashMap<String,ArrayList> threeAvgMap = new HashMap();
    private HashMap<String,ArrayList>  SevenAvgMap = new HashMap();
    private HashMap<String,ArrayList>  TwentyTwoAvgMap = new HashMap();
    private UrlParamsBuilder builder = UrlParamsBuilder.build();

    private String getAvgUrl(String symbol,String period,int size)
    {
        return "/market/history/kline?period=" + period + "&size=" + size + "&symbol=" + symbol;
    }

    public float getThreeAvg(String symbol)
    {
        String key = symbol+ ChrisDateUtils.timeStamp2Date(ChrisDateUtils.timeStamp(),"yyyymmdd");
        ArrayList threeAvgList = threeAvgMap.get("key");
        if(threeAvgList == null)
        {
            String reqStr = new ApiRequest().sendReq(this.getAvgUrl(symbol,"1day",3),builder,false);

            JsonWrapper jsonWrapper = JsonWrapper.parseFromString(reqStr);
            JsonWrapperArray jsonWrapperArray = jsonWrapper.getJsonArray("data");
            jsonWrapperArray.forEach((item) -> {
                System.out.println(item.getString("close"));
            });
        }
        return 0.0f;
    }
    public float getSevenAvg(String symbol)
    {
        return 0.0f;
    }
    public float getTwentyTwoAvg(String symbol)
    {
        return 0.0f;
    }

    public static void main(String[] args) {
        Average average = new Average();
        average.getThreeAvg("xrpusdt");
    }
}
