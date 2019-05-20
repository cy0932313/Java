package com.chris.automated.trading;
import com.chris.automated.trading.requestManager.ApiRequest;
import com.chris.automated.trading.requestManager.UrlParamsBuilder;

public class Account
{
    public void getAccountInfo()
    {
        ApiRequest apiRequest = new ApiRequest();
        UrlParamsBuilder builder = UrlParamsBuilder.build();
        System.out.println(apiRequest.sendReq("/v1/account/accounts",builder,true));
    }

    public void getAccountMoney()
    {
        ApiRequest apiRequest = new ApiRequest();
        UrlParamsBuilder builder = UrlParamsBuilder.build();
        System.out.println(apiRequest.sendReq("/v1/account/accounts/7409207/balance",builder,true));
    }

    public static void main(String[] args) {
        Account account = new Account();
        account.getAccountInfo();
        account.getAccountMoney();
    }
}
