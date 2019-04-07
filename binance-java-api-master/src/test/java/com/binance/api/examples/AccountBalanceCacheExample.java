package com.binance.api.examples;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.AssetBalance;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.binance.api.client.domain.event.UserDataUpdateEvent.UserDataUpdateEventType.ACCOUNT_UPDATE;

/**
 * Illustrates how to use the user data event stream to create a local cache for the balance of an account.
 */
public class AccountBalanceCacheExample {

  private final BinanceApiClientFactory clientFactory;

  /**
   * Key is the symbol, and the value is the balance of that symbol on the account.
   */
  private Map<String, AssetBalance> accountBalanceCache;

  /**
   * Listen key used to interact with the user data streaming API.
   */
  private final String listenKey;

  public AccountBalanceCacheExample(String apiKey, String secret) {
    this.clientFactory = BinanceApiClientFactory.newInstance(apiKey, secret);
    this.listenKey = initializeAssetBalanceCacheAndStreamSession();
    startAccountBalanceEventStreaming(listenKey);
  }

  /**
   * Initializes the asset balance cache by using the REST API and starts a new user data streaming session.
   *
   * @return a listenKey that can be used with the user data streaming API.
   */
  private String initializeAssetBalanceCacheAndStreamSession() {
    BinanceApiRestClient client = clientFactory.newRestClient();
    Account account = client.getAccount();

    this.accountBalanceCache = new TreeMap<>();
    for (AssetBalance assetBalance : account.getBalances()) {
      accountBalanceCache.put(assetBalance.getAsset(), assetBalance);
    }

    return client.startUserDataStream();
  }

  /**
   * Begins streaming of agg trades events.
   */
  private void startAccountBalanceEventStreaming(String listenKey) {
    BinanceApiWebSocketClient client = clientFactory.newWebSocketClient();

    client.onUserDataUpdateEvent(listenKey, response -> {
      if (response.getEventType() == ACCOUNT_UPDATE) {
        // Override cached asset balances
        for (AssetBalance assetBalance : response.getAccountUpdateEvent().getBalances()) {
          accountBalanceCache.put(assetBalance.getAsset(), assetBalance);
        }
        System.out.println(accountBalanceCache);
      }
    });
  }

  /**
   * @return an account balance cache, containing the balance for every asset in this account.
   */
  public Map<String, AssetBalance> getAccountBalanceCache() {
    return accountBalanceCache;
  }

  public static void main(String[] args) {
//    new AccountBalanceCacheExample("mdAZ2oLE1vfchDc7zvFrlJm9zPMQeDIAj40zDmKVKFoG7ZIbcF1Tp7YUaOk6fnsE", "8B66Yfd92hWTFHu72gIF745gRf9QASy3dH8fDzqaIDaLsVh1yPdyw7sCTr2WZKLj");
    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("mdAZ2oLE1vfchDc7zvFrlJm9zPMQeDIAj40zDmKVKFoG7ZIbcF1Tp7YUaOk6fnsE", "8B66Yfd92hWTFHu72gIF745gRf9QASy3dH8fDzqaIDaLsVh1yPdyw7sCTr2WZKLj");
    BinanceApiRestClient client = factory.newRestClient();
    long serverTime = client.getServerTime();
    List<Candlestick> list = client.getCandlestickBars("BTCUSDT", CandlestickInterval.HOURLY,1000,1514736000000L,serverTime);
//    getCandlestickBars(String symbol, CandlestickInterval interval, Integer limit, Long startTime, Long endTime);
    int size = list.size();
  for(int i = 0;i < size;i++)
  {

    Candlestick candlestick = list.get(i);
    System.out.println(candlestick.getCloseTime() +":"+ candlestick.getClose());
  }

//    client.ping();
//    long serverTime = client.getServerTime();
//
//    System.out.println(client.getTrades("ETHBTC",1000));
//    getCandlestickBars(String symbol, CandlestickInterval interval);
  }
}
