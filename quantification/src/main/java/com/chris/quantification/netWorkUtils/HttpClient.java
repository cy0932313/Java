package com.chris.quantification.netWorkUtils;

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
    private String url;
    private String cookie;


    public String HttpClient(String url)
    {
        this.url = url;
        return  sendGetRequest();
    }

    public  String HttpClient(String url,String cookie)
    {
        this.url = url;
        this.cookie = cookie;
        return  sendGetRequest();
    }

    private String sendGetRequest()
    {
        HttpHeaders requestHeaders = new HttpHeaders();
        if(this.cookie != null)
        {
//            requestHeaders.add("Cookie", "xq_a_token=839343b0d4e4de6c50a055f7927e214b8afa75f6");
            requestHeaders.add("Cookie",cookie);
        }

        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responses = restTemplate.exchange(url,
                HttpMethod.GET, requestEntity,  String.class);
        return responses.getBody();
    }


}
