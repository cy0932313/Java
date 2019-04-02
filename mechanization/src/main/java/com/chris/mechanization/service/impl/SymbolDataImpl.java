package com.chris.mechanization.service.impl;

import com.chris.mechanization.dao.IOperateTableDao;
import com.chris.mechanization.dao.IXueqiuDayDao;
import com.chris.mechanization.domain.xueqiuData.ItemStock;
import com.chris.mechanization.service.ISymbolDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

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
    @Autowired
    IXueqiuDayDao iXueqiuDayDao;

    @Override
    public String saveSymbolData(String symbolName) {
        String tableName = "symbol_" + symbolName;
        this.operateTable(tableName);

        HashMap<String, String> paramHash = new HashMap<>();
        paramHash.put("symbol", symbolName);
        paramHash.put("period", "1day");
        paramHash.put("begin", "1325413098000");
        paramHash.put("end", String.valueOf(System.currentTimeMillis()));
        xueqiuDayData.setParameter(paramHash);
        xueqiuDayData.getDataSoruce();
        ArrayList arrayList = xueqiuDayData.getHandleDataResult();
        for (int i = 0; i < arrayList.size(); i++) {
            iXueqiuDayDao.addXueqiuStock(tableName, (ItemStock) arrayList.get(i));
        }
        return "成功更新数据" + arrayList.size() + "条";
    }

    private void operateTable(String tableName) {
        if (iOperateTableDao.existTable(tableName) == 1) {
            iOperateTableDao.trancateTable(tableName);
        } else {
            iOperateTableDao.createNewTable(tableName);
        }
    }
}
