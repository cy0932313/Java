package com.chris.quantification.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-03-12 18:20
 * @description：
 */

public class HttpClient {
    private String url, cookie, reqType;


    public HttpClient(String url, String reqType) {
        this.url = url;
        this.reqType = reqType;
    }

    public HttpClient(String url, String cookie, String reqType) {
        this.url = url;
        this.cookie = cookie;
        this.reqType = reqType;
    }

    public String sendRequest() {
        HttpHeaders requestHeaders = new HttpHeaders();
        if (this.cookie != null) {
            requestHeaders.add("Cookie", cookie);
        }

        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responses = null;
        if (this.reqType.equals("GET")) {
            responses = restTemplate.exchange(url,
                    HttpMethod.GET, requestEntity, String.class);
        }
        return responses.getBody();
    }
}
