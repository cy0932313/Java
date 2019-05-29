package com.chris.mechanization.service.impl;

import com.chris.mechanization.dao.IOperateTableDao;
import com.chris.mechanization.domain.xueqiuData.ItemStock;
import com.chris.mechanization.enumType.MakeMoney;
import com.chris.mechanization.service.ISymbolDataService;
import com.chris.mechanization.utils.ChrisDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-04-01 11:41
 * @description：
 */

@Service
public class SymbolDataImpl implements ISymbolDataService {
    @Autowired
    private XueqiuDayDataImpl xueqiuDayData;

    @Autowired
    IOperateTableDao iOperateTableDao;

    @Override
    public String saveSymbolData(String symbolName,String period, String beginTime, String endTime, boolean update) {
        String tableName = "symbol_"+period+"_" + symbolName;
        ArrayList arrayList = new ArrayList();
        int isExist = iOperateTableDao.existTable(tableName);
        if (isExist == 1 && !update) {
            this.getTechnicalIndex(tableName);
        } else {
            if (isExist == 1) {
//                iOperateTableDao.trancateTable(tableName);
                return "已存在";
            } else {
                iOperateTableDao.createNewTable_symbol(tableName);
            }
            HashMap<String, String> paramHash = new HashMap<>();
            paramHash.put("symbol", symbolName);
            paramHash.put("period", period);
            paramHash.put("begin", beginTime);
            paramHash.put("end", endTime);
            xueqiuDayData.setParameter(paramHash);
            xueqiuDayData.getDataSoruce();
            arrayList = xueqiuDayData.getHandleDataResult();
            for (int i = 0; i < arrayList.size(); i++) {
                ItemStock itemStock = (ItemStock) arrayList.get(i);
                itemStock.setTime(ChrisDateUtils.timeStamp2Date(itemStock.getTimestamp(), "yyyy-MM-dd"));
                iOperateTableDao.addXueqiuStock(tableName, itemStock);
            }

//            this.getTechnicalIndex(tableName);
        }

        return "成功更新数据" + arrayList.size() + "条";
    }

    private void getTechnicalIndex(String tableName) {
        TechnicalIndexImpl technicalIndex = new TechnicalIndexImpl(this.iOperateTableDao);
        technicalIndex.ma(tableName,60,"ma60", MakeMoney.SYMBOL);
        technicalIndex.ma(tableName,22,"ma22", MakeMoney.SYMBOL);
        technicalIndex.ma(tableName,55,"ma55", MakeMoney.SYMBOL);
        technicalIndex.cci(tableName,14,MakeMoney.SYMBOL);
    }
}
