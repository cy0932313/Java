package com.chris.quantification.service.impl.monitor.CCI;

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
    public double currentDayOpenPrice;

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

        if (cciData > 250) {
            return true;
        }
        else if (cciData < -100) {
            return true;
        }
        else if(this.currentDayOpenPrice != 0 &&(cciClose/this.currentDayOpenPrice) - 1> 0.05)
        {
            return true;
        }
        return false;
    }
}
