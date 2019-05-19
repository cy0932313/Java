package com.chris.mechanization.service.impl;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.chris.mechanization.dao.IOperateTableDao;
import com.chris.mechanization.enumType.MakeMoney;
import com.chris.mechanization.service.ISymbolDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-04-06 16:19
 **/
@Service
public class CoinDataImpl implements ISymbolDataService {
    @Autowired
    IOperateTableDao iOperateTableDao;

    public String saveSymbolData(String symbolName, String period, String beginTime, String endTime, boolean update) {
        String tableName = "coin_" + period + "_" + symbolName;
        ArrayList arrayList = new ArrayList();
        int isExist = iOperateTableDao.existTable(tableName);
        if (isExist == 1 && !update) {
            this.getTechnicalIndex(tableName);
        } else {
            if (isExist == 1) {
                iOperateTableDao.trancateTable(tableName);
            } else {
                iOperateTableDao.createNewTable_coin(tableName);
            }
            BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("mdAZ2oLE1vfchDc7zvFrlJm9zPMQeDIAj40zDmKVKFoG7ZIbcF1Tp7YUaOk6fnsE", "8B66Yfd92hWTFHu72gIF745gRf9QASy3dH8fDzqaIDaLsVh1yPdyw7sCTr2WZKLj");
            BinanceApiRestClient client = factory.newRestClient();
            this.makeUpData(tableName, symbolName, period, beginTime, endTime, client);
            this.getTechnicalIndex(tableName);
        }
        return "成功更新数据" + arrayList.size() + "条";
    }

    private void makeUpData(String tableName, String symbolName, String period, String beginTime, String endTime, BinanceApiRestClient client) {

        CandlestickInterval candlestickInterval = CandlestickInterval.DAILY;
        if (period.equals("1h")) {
            candlestickInterval = CandlestickInterval.HOURLY;
        }
        else if(period.equals("2h"))
        {

            candlestickInterval = CandlestickInterval.TWO_HOURLY;
        }
        else if(period.equals("4h"))
        {

            candlestickInterval = CandlestickInterval.FOUR_HOURLY;
        }
        else if(period.equals("6h"))
        {

            candlestickInterval = CandlestickInterval.SIX_HOURLY;
        }
        else if(period.equals("8h"))
        {

            candlestickInterval = CandlestickInterval.EIGHT_HOURLY;
        }
        else if(period.equals("12h"))
        {

            candlestickInterval = CandlestickInterval.TWELVE_HOURLY;
        }
        else if(period.equals("1d"))
        {

            candlestickInterval = CandlestickInterval.DAILY;
        }
        else if(period.equals("3d"))
        {

            candlestickInterval = CandlestickInterval.THREE_DAILY;
        }
        List<Candlestick> list = client.getCandlestickBars(symbolName, candlestickInterval, 1000, Long.parseLong(beginTime), client.getServerTime());

        int size = list.size();
        long lastTime = 0L;
        for (int i = 0; i < size; i++) {
            Candlestick candlestick = list.get(i);
            iOperateTableDao.addBinanceStock(tableName, candlestick);
            if (i == size - 1) {
                lastTime = candlestick.getCloseTime();
            }
        }
        if (lastTime < Long.parseLong(endTime)) {
            this.makeUpData(tableName, symbolName, period, String.valueOf(lastTime), endTime, client);
        }
    }

    private void getTechnicalIndex(String tableName) {
        TechnicalIndexImpl technicalIndex = new TechnicalIndexImpl(this.iOperateTableDao);

//        technicalIndex.cci(tableName, 14, MakeMoney.COIN);
//        int[] ma = {5,7,10,14,20,30,60,22,55,25,27};
        int[] ma = {25,27};
        for(int i = 0;i < ma.length;i++)
        {
            System.out.println("我还在动");
            technicalIndex.ma(tableName, ma[i], "ma"+ma[i],MakeMoney.COIN);
        }
    }
}
