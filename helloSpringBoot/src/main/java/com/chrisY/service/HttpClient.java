package com.chrisY.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-02-28 14:54
 * @description：请求
 */

@Service
public class HttpClient {
    public String client(String url) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "xq_a_token=839343b0d4e4de6c50a055f7927e214b8afa75f6");
        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String>  responses = restTemplate.exchange(url,
                HttpMethod.GET, requestEntity,  String.class);
        return responses.getBody();
    }
}
