package com.chris.automated.trading;

import com.chris.automated.trading.requestManager.ApiRequest;
import com.chris.automated.trading.requestManager.UrlParamsBuilder;
import com.chris.automated.trading.utils.ChrisDateUtils;
import com.chris.automated.trading.utils.JsonWrapper;
import com.chris.automated.trading.utils.JsonWrapperArray;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-05-20 22:19
 **/
public class Average
{
    private static HashMap<String,ArrayList> avgMap = new HashMap();
    private UrlParamsBuilder builder = UrlParamsBuilder.build();

    private String getAvgUrl(String symbol,String period,int size)
    {
        return "/market/history/kline?period=" + period + "&size=" + size + "&symbol=" + symbol;
    }

    public float getCustomAvg(String symbol,int size,float currentPrice)
    {
        String key = symbol+"_"+size+"_"+ ChrisDateUtils.timeStamp2Date(ChrisDateUtils.timeStamp(),"yyyyMMdd");
        ArrayList avgArray = avgMap.get(key);
        float resultAvg = 0.0f;

        if(avgArray == null || avgArray.size() != size - 1)
        {
            String reqStr = new ApiRequest().sendReq(this.getAvgUrl(symbol,"60min",(size - 1) * 24),builder,false);

            JsonWrapper jsonWrapper = JsonWrapper.parseFromString(reqStr);
            JsonWrapperArray jsonWrapperArray = jsonWrapper.getJsonArray("data");

            ArrayList tempItemArray = new ArrayList();
            jsonWrapperArray.forEach((item) -> {
                if(ChrisDateUtils.timeStamp2Date(item.getString("id"),"HH").equals("08"))
                {
                    tempItemArray.add(item);
                }
            });
            avgMap.put(key,tempItemArray);
            if(tempItemArray.size() != size - 1)
            {
                return 0;
            }

            resultAvg = (this.handleAvgVal(tempItemArray) + currentPrice)/size;
            return resultAvg;
        }
        else
        {
            resultAvg = (this.handleAvgVal(avgArray) + currentPrice)/size;
            return resultAvg;
        }
    }

    private float handleAvgVal(ArrayList avgList)
    {
        int size = avgList.size();
        float sumOpen = 0.0f;
        for(int i = 0;i < size;i++)
        {
            JsonWrapper objItem = (JsonWrapper)avgList.get(i);

            sumOpen = sumOpen + Float.parseFloat(objItem.getString("open"));
        }
        return sumOpen;
    }

    public static void main(String[] args) {
        Average average = new Average();

        average.getCustomAvg("xrpusdt",3,0.3905f);

        average.getCustomAvg("xrpusdt",7,0.3905f);

        average.getCustomAvg("xrpusdt",22,0.3905f);
    }
}
