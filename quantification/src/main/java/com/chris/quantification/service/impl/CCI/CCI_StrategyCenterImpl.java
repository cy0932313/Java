package com.chris.quantification.service.impl.CCI;

import com.chris.quantification.service.IStrategyCenter;
import com.chrisY.util.ChrisDateUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-03-14 14:44
 * @description：
 */

@Service
public class CCI_StrategyCenterImpl implements IStrategyCenter {
    public HashMap<String, String> currentData;
    public HashMap<String, String> previousData;

    @Override
    public boolean buyCondition() {
        double cciData = Double.parseDouble(currentData.get("cci"));
        double cciClose = Double.parseDouble(currentData.get("close"));
        double previouscciData = Double.parseDouble(previousData.get("cci"));
        double previouscciClose = Double.parseDouble(previousData.get("close"));

        if (cciData > -100 && previouscciData < -100 && cciClose > previouscciClose&&!ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(currentData.get("timestamp")) / 1000), "HH").equals("15") ) {
            return true;
        }
        return false;
    }

    @Override
    public boolean sellCondition() {
        double cciData = Double.parseDouble(currentData.get("cci"));
        double cciClose = Double.parseDouble(currentData.get("close"));
        double cciOpen = Double.parseDouble(currentData.get("open"));
        double previouscciData = Double.parseDouble(previousData.get("cci"));
        double previouscciClose = Double.parseDouble(previousData.get("close"));


        if (cciData > 250) {
            return true;
        }
        else if (cciData < -100) {
            return true;
        }
//        else if((cciClose/cciOpen) - 1 < -0.03)
//        {
//            printLog.append("121212触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "，CCI技术指标符合卖出条件，上一个小时CCI数据：" +previouscciData + "，这个小时CCI数据：" +cciData);
//            printLog.append("<br />");
//            return true;
//        }
//        else if(previouscciData - cciData >= 100)
//        {
//            printLog.append("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "，CCI技术指标符合卖出条件，上一个小时CCI数据：" +previouscciData + "，这个小时CCI数据：" +cciData);
//            printLog.append("<br />");
//                return true;
//        }
        return false;
    }
}
