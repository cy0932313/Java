package com.chris.mechanization.service.impl;

import com.chris.mechanization.dao.IOperateTableDao;
import com.chris.mechanization.dao.IXueqiuDayDao;
import com.chris.mechanization.domain.xueqiuData.ItemStock;
import com.chris.mechanization.service.ISymbolDataService;
import com.chris.mechanization.utils.ChrisDateUtils;
import org.hibernate.validator.constraints.EAN;
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
    @Autowired
    IXueqiuDayDao iXueqiuDayDao;

    @Override
    public String saveSymbolData(String symbolName, String beginTime, String endTime, boolean update) {
        String tableName = "symbol_" + symbolName;

        int isExist = iOperateTableDao.existTable(tableName);
        if (isExist == 1 && !update) {
            return "开始策略吧~";
        } else {
            if (isExist == 1) {
                iOperateTableDao.trancateTable(tableName);
            } else {
                iOperateTableDao.createNewTable(tableName);
            }
            HashMap<String, String> paramHash = new HashMap<>();
            paramHash.put("symbol", symbolName);
            paramHash.put("period", "1day");
            paramHash.put("begin", beginTime);
            paramHash.put("end", endTime);
            xueqiuDayData.setParameter(paramHash);
            xueqiuDayData.getDataSoruce();
            ArrayList arrayList = xueqiuDayData.getHandleDataResult();
            for (int i = 0; i < arrayList.size(); i++) {
                ItemStock itemStock = (ItemStock)arrayList.get(i);
                itemStock.setTime(ChrisDateUtils.timeStamp2Date(itemStock.getTimestamp(),"yyyy-MM-dd") );
                iXueqiuDayDao.addXueqiuStock(tableName,itemStock);
            }

            this.getTechnicalIndex(tableName);

            return "成功更新数据" + arrayList.size() + "条";
        }
    }

    private void getTechnicalIndex(String tableName) {
        //获取M60
        List<ItemStock> m60List = iXueqiuDayDao.queryInfoForLimit(tableName, "59", "100000000000");
        float sum = iXueqiuDayDao.queryInfoForSum(tableName, "0", "59");
        float sum60 = 0;
        DecimalFormat fnum = new DecimalFormat("##0.00");
        int size = m60List.size();
        for (int i = 0; i < size; i++) {
            ItemStock itemStock = m60List.get(i);
            sum60 = (sum + Float.parseFloat(itemStock.getClose())) / 60;
            System.out.println(fnum.format(sum60));
            iXueqiuDayDao.updateInfo(tableName, "ma60", fnum.format(sum60), itemStock.getTimestamp());
        }

    }
}
