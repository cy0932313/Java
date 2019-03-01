package com.chrisY.web;

import com.chrisY.service.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-02-28 14:47
 * @description：量化工具
 */

@RestController
@RequestMapping(value = "/quan")
public class QuantificationController {
    String begin = "1546330682509";
    String symbol = "SH600196";
    String baseUrl = "https://stock.xueqiu.com/v5/stock/chart/kline.json?";
    String count = "2";
    //"kline,ma,macd,kdj,boll,rsi,wr,bias,cci,psy"
    String indicator = "cci";


    @Autowired
    HttpClient httpClient;

    @RequestMapping(value = "/60")
    public String get60mk() {
        return this.sendRequest("60m");
    }

    @RequestMapping(value = "/30")
    public String get30mk() {
        return this.sendRequest("30m");
    }

    public String sendRequest(String period)
    {
        String url = baseUrl + "symbol=" + symbol + "&begin=" + begin + "&period=" + period  + "&count=" + count + "&indicator=" + indicator;
        return httpClient.client(url);
    }
}
