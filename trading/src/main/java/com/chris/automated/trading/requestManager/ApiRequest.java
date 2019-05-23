package com.chris.automated.trading.requestManager;

import com.alibaba.fastjson.JSON;
import unirest.Config;
import unirest.HttpResponse;
import unirest.UnirestInstance;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-05-19 23:10
 **/
public class ApiRequest {
    private static final String url = "https://api.huobi.pro";
    private static UnirestInstance unirestInstanceGet;
    private static UnirestInstance unirestInstancePost;

    public ApiRequest()
    {
        Config configPost = new Config();
        configPost.setDefaultHeader("Content-Type", "application/json");
        unirestInstancePost = new UnirestInstance(configPost);

        Config configGet = new Config();
        configGet.setDefaultHeader("Content-Type", "application/x-www-form-urlencoded");
        unirestInstanceGet = new UnirestInstance(configGet);
    }
    private String createRequestWithSignature(String address,UrlParamsBuilder builder) {
        if (builder == null) {
            return "[Invoking] Builder is null when create request with Signature";
        }
        String requestUrl = url + address;
        if (builder.hasPostParam()) {
            new ApiSignature().createSignature( "POST", address, builder);
        } else {
            new ApiSignature().createSignature("GET", address, builder);
        }
        requestUrl += builder.buildUrl();
        return requestUrl;
    }

    public String sendReq(String address,UrlParamsBuilder builder,boolean isSignature)
    {
        String resultUrl;
        if(isSignature)
        {
            resultUrl = this.createRequestWithSignature(address,builder);
        }
        else
        {
            resultUrl = url + address;
        }
        HttpResponse<String> response;
        if (builder.hasPostParam()) {
            response = unirestInstancePost.post(resultUrl).body(JSON.toJSONString(builder.postBodyMap.map)).asString();
        } else {
            response = unirestInstanceGet.get(resultUrl).asString();
        }

        return response.getBody();
    }
}
