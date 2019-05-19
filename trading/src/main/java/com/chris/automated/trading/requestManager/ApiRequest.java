package com.chris.automated.trading.requestManager;

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

    public String sendReq(String address,UrlParamsBuilder builder)
    {
        String resultUrl = this.createRequestWithSignature(address,builder);

        Config config = new Config();
        if (builder.hasPostParam()) {
            config.setDefaultHeader("Content-Type", "application/json");
            UnirestInstance unirestInstance = new UnirestInstance(config);
        } else {
            config.setDefaultHeader("Content-Type", "application/x-www-form-urlencoded");
            UnirestInstance unirestInstance = new UnirestInstance(config);
            HttpResponse<String> response = unirestInstance.get(resultUrl).asString();
            return response.getBody();
        }
        return "";
    }
}
