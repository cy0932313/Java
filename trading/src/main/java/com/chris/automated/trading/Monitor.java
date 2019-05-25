package com.chris.automated.trading;

import com.chris.automated.trading.domian.SymbolInfo;
import com.chris.automated.trading.requestManager.ApiRequest;
import com.chris.automated.trading.requestManager.UrlParamsBuilder;
import com.chris.automated.trading.utils.JsonWrapper;
import com.chris.automated.trading.utils.JsonWrapperArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-05-22 21:59
 **/

@Component
public class Monitor {
    @Autowired
    Transaction transaction;

    private static final String[] leverArray = new String[]
            {"xrp"};
//                    "btc","eth","xrp","ltc","bch","eos","etc"};
//    ,"ada",
//                    "omg","zec","dash","trx","atom","iost",
//                    "ont","btt","neo","zil","btm","qtum",
//                    "dta","elf","ruff","hc","ela","bsv"};

    private UrlParamsBuilder builder = UrlParamsBuilder.build();
    private ArrayList monitorSymbolsArray = new ArrayList();
    private HashMap<String, SymbolInfo> hashMapSymbol = new HashMap();
    private Average average = new Average();
    private String getSymbolsUrl()
    {
        return "/v1/common/symbols";
    }

    private String getCurrentMarket(String symbol)
    {
        return "/market/detail/merged?symbol="+ symbol;
    }

    @Scheduled(fixedRate = 10000)
    public void startMonitor_symbols()
    {
        if(this.monitorSymbolsArray.size() == 0)
        {
            this.getMonitorSymbol();
        }

        int size = this.monitorSymbolsArray.size();
        for(int i = 0;i < size;i++)
        {
            JsonWrapper objItem = (JsonWrapper)this.monitorSymbolsArray.get(i);
            String reqStr = new ApiRequest().sendReq(this.getCurrentMarket(objItem.getString("symbol")),builder,false);
            this.handleSymbol(reqStr,objItem);
        }
    }

    private void handleSymbol(String resultStr,JsonWrapper objItem)
    {
        String key = objItem.getString("symbol");
        JsonWrapper jsonWrapper = JsonWrapper.parseFromString(resultStr);
        JsonWrapper jsonWrappertick = jsonWrapper.getJsonObject("tick");
        String timestamp = jsonWrapper.getString("ts");
        SymbolInfo symbolInfo = this.hashMapSymbol.get(key);
        if(symbolInfo == null)
        {
            symbolInfo = new SymbolInfo();
            symbolInfo.setSymbol(key);
            symbolInfo.setLastPrice(new BigDecimal(0));
            symbolInfo.setLastTime("");
            symbolInfo.setPricePrecision(objItem.getInteger("price-precision"));
            symbolInfo.setAmountPrecision(objItem.getInteger("amount-precision"));
        }
        else
        {
            symbolInfo.setLastPrice(symbolInfo.getCurrentPrice());
            symbolInfo.setLastTime(symbolInfo.getCurrentTime());
        }

        BigDecimal closePrice = jsonWrappertick.getBigDecimal("close");
        symbolInfo.setCurrentPrice(closePrice);
        symbolInfo.setCurrentTime(timestamp);
        symbolInfo.setThreeAvg(average.getCustomAvg(symbolInfo.getSymbol(),3,closePrice.floatValue()));
        symbolInfo.setSevenAvg(average.getCustomAvg(symbolInfo.getSymbol(),7,closePrice.floatValue()));
        symbolInfo.setTwentyTwoAvg(average.getCustomAvg(symbolInfo.getSymbol(),22,closePrice.floatValue()));
        this.hashMapSymbol.put(key,symbolInfo);
        if(symbolInfo.getLastTime().length() > 0)
        {
            transaction.calculationTransaction(symbolInfo);
        }
    }

    private void getMonitorSymbol()
    {
        String reqStr = new ApiRequest().sendReq(this.getSymbolsUrl(),builder,false);
        JsonWrapper jsonWrapper = JsonWrapper.parseFromString(reqStr);
        JsonWrapperArray jsonWrapperArray = jsonWrapper.getJsonArray("data");

        jsonWrapperArray.forEach((item) -> {
            for(int i = 0;i < leverArray.length;i++)
            {
                //只玩USDT对应的交易对（币种）
                if(leverArray[i].equals(item.getString("base-currency")) && "usdt".equals(item.getString("quote-currency")))
                {
                    monitorSymbolsArray.add(item);
                }
            }
        });
    }
}
