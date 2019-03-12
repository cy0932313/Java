package com.chris.quantification.service.impl;

import com.chris.quantification.service.IHttpClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-03-12 17:46
 * @description：
 */

@Service
public class HttpClientImpl implements IHttpClient {
    @Override
    public String client(String url)
    {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "xq_a_token=839343b0d4e4de6c50a055f7927e214b8afa75f6");
        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responses = restTemplate.exchange(url,
                HttpMethod.GET, requestEntity,  String.class);
        return responses.getBody();
    }
}
