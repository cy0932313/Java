package com.chris.automated.trading;
import com.chris.automated.trading.requestManager.ApiRequest;
import com.chris.automated.trading.requestManager.UrlParamsBuilder;
import com.chris.automated.trading.utils.JsonWrapper;
import com.chris.automated.trading.utils.JsonWrapperArray;

public class Account
{
    public static String  account_id  = "";

    private void getAccountInfo()
    {
        ApiRequest apiRequest = new ApiRequest();
        UrlParamsBuilder builder = UrlParamsBuilder.build();
        String resultStr = apiRequest.sendReq("/v1/account/accounts",builder,true);
        JsonWrapper jsonWrapper = JsonWrapper.parseFromString(resultStr);
        JsonWrapperArray jsonWrapperArray = jsonWrapper.getJsonArray("data");

        jsonWrapperArray.forEach((item) -> {
            if(item.getString("type").equals("margin") && item.getString("subtype").equals("xrpusdt"))
            {
                account_id = item.getString("id");
            }
        });
    }

    public JsonWrapper getAccountMoney()
    {
        if(account_id == "")
        {
            this.getAccountInfo();
        }

        ApiRequest apiRequest = new ApiRequest();
        UrlParamsBuilder builder = UrlParamsBuilder.build();
        String resultStr = apiRequest.sendReq("/v1/account/accounts/"+ account_id +"/balance",builder,true);
        JsonWrapper jsonWrapper = JsonWrapper.parseFromString(resultStr);
        return jsonWrapper;
    }

    public static void main(String[] args) {
        Account account = new Account();
        account.getAccountInfo();
        account.getAccountMoney();
    }
}
