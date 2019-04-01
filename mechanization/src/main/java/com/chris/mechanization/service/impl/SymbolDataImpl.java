package com.chris.mechanization.service.impl;

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
    XueqiuDayDataImpl xueqiuDayData;
    @Override
    public boolean saveSymbolData(String SymbolName)
    {
        HashMap<String,String> paramHash = new HashMap<>();
        paramHash.put("symbol",SymbolName);
        paramHash.put("period","1day");
        paramHash.put("begin","1325413098000");
        paramHash.put("end",String.valueOf(System.currentTimeMillis()));
        xueqiuDayData.setParameter(paramHash);
        xueqiuDayData.getDataSoruce();
        ArrayList arrayList =  xueqiuDayData.getHandleDataResult();
        return true;
    }
}
