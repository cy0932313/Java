package com.chris.mechanization.service.impl;

import com.binance.api.client.domain.market.Candlestick;
import com.chris.mechanization.dao.IOperateTableDao;
import com.chris.mechanization.domain.xueqiuData.ItemStock;
import com.chris.mechanization.enumType.MakeMoney;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-04-07 21:49
 **/
@Service
public class TechnicalIndexImpl {

    IOperateTableDao iOperateTableDao;

    DecimalFormat fnum2 = new DecimalFormat("##0.00");
    DecimalFormat fnum1 = new DecimalFormat("##0.0");

    TechnicalIndexImpl(IOperateTableDao iOperateTableDao) {
        this.iOperateTableDao = iOperateTableDao;
    }

    public void ma(String tableName, int ma, String updateField, MakeMoney makeMoney) {
        if (makeMoney == MakeMoney.SYMBOL) {
            List<ItemStock> list = iOperateTableDao.queryInfoForLimit_symbol(tableName, "timestamp", ma - 1, 100000000);

            int size = list.size();
            for (int i = 0; i < size; i++) {
                float avg = iOperateTableDao.queryInfoForAvg(tableName, "timestamp", "t.close", 0 + i, ma);
//                avg = avg + 0.01f;
                ItemStock itemStock = list.get(i);
                iOperateTableDao.updateInfo(tableName, updateField, fnum2.format(avg),"timestamp", itemStock.getTimestamp());
            }
        } else if (makeMoney == MakeMoney.COIN) {
            List<Candlestick> list = iOperateTableDao.queryInfoForLimit_coin(tableName,"closeTime", ma - 1, 100000000);

            int size = list.size();
            for (int i = 0; i < size; i++) {
                float avg = iOperateTableDao.queryInfoForAvg(tableName, "closeTime", "t.close", 0 + i, ma);
                Candlestick candlestick = list.get(i);
                iOperateTableDao.updateInfo(tableName, updateField, fnum2.format(avg),"closeTime", String.valueOf(candlestick.getCloseTime()));
            }
        }
    }

    public void cci(String tableName, int cciParam, MakeMoney makeMoney) {
        if (makeMoney == MakeMoney.SYMBOL) {
            List<ItemStock> cciList = iOperateTableDao.queryInfoForLimit_symbol(tableName, "timestamp", cciParam - 1, 100000000);

            int cciSize = cciList.size();
            for (int i = 0; i < cciSize; i++) {
                ItemStock itemStock = cciList.get(i);
                float TYP = (Float.parseFloat(itemStock.getHigh()) + Float.parseFloat(itemStock.getLow()) + Float.parseFloat(itemStock.getClose())) / 3;
                float MA = iOperateTableDao.queryInfoForAvg(tableName, "timestamp", "t.close+t.high+t.low", 0 + i, cciParam) / 3;
                float MD = this.AVEDEV_symbol(iOperateTableDao.queryInfoForLimit_symbol(tableName, "timestamp", 0 + i, cciParam), MA);
                double CCI = (TYP - MA) / (0.015 * MD);
                if (CCI < -100 || CCI > 100) {
                    iOperateTableDao.updateInfo(tableName, "cci", fnum1.format(CCI), "timestamp", itemStock.getTimestamp());
                } else {
                    iOperateTableDao.updateInfo(tableName, "cci", fnum2.format(CCI), "timestamp", itemStock.getTimestamp());
                }
            }
        } else if (makeMoney == MakeMoney.COIN) {
            List<Candlestick> cciList = iOperateTableDao.queryInfoForLimit_coin(tableName,"closeTime", cciParam - 1, 100000000);

            int cciSize = cciList.size();
            for (int i = 0; i < cciSize; i++) {
                Candlestick candlestick = cciList.get(i);
                float TYP = (Float.parseFloat(candlestick.getHigh()) + Float.parseFloat(candlestick.getLow()) + Float.parseFloat(candlestick.getClose())) / 3;
                float MA = iOperateTableDao.queryInfoForAvg(tableName, "closeTime", "t.close+t.high+t.low", 0 + i, cciParam) / 3;
                float MD = this.AVEDEV_coin(iOperateTableDao.queryInfoForLimit_coin(tableName,"closeTime", 0 + i, cciParam), MA);
                double CCI = (TYP - MA) / (0.015 * MD);
                if (CCI < -100 || CCI > 100) {
                    iOperateTableDao.updateInfo(tableName, "cci", fnum1.format(CCI),"closeTime", String.valueOf(candlestick.getCloseTime()));
                } else {
                    iOperateTableDao.updateInfo(tableName, "cci", fnum2.format(CCI),"closeTime", String.valueOf(candlestick.getCloseTime()));
                }
            }
        }
    }

    private float AVEDEV_symbol(List<ItemStock> list, float MA) {
        int size = list.size();
        float AVEDEV = 0;
        for (int i = 0; i < size; i++) {
            ItemStock itemStock = list.get(i);
            float TYP = (Float.parseFloat(itemStock.getHigh()) + Float.parseFloat(itemStock.getLow()) + Float.parseFloat(itemStock.getClose())) / 3;
            AVEDEV = Math.abs(TYP - MA) + AVEDEV;
        }
        float avgList = 1 / (float) size;
        return avgList * AVEDEV;
    }

    private float AVEDEV_coin(List<Candlestick> list, float MA) {
        int size = list.size();
        float AVEDEV = 0;
        for (int i = 0; i < size; i++) {
            Candlestick candlestick = list.get(i);
            float TYP = (Float.parseFloat(candlestick.getHigh()) + Float.parseFloat(candlestick.getLow()) + Float.parseFloat(candlestick.getClose())) / 3;
            AVEDEV = Math.abs(TYP - MA) + AVEDEV;
        }
        float avgList = 1 / (float) size;
        return avgList * AVEDEV;
    }
}
