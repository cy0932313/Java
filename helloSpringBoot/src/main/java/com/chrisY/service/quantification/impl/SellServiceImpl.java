package com.chrisY.service.quantification.impl;

import com.chrisY.service.quantification.ISellConditionService;
import com.chrisY.util.ChrisDateUtils;
import org.springframework.stereotype.Service;
import static com.chrisY.web.QuantificationController.printLog;
import java.util.HashMap;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-03-03 19:01
 **/
@Service
public class SellServiceImpl implements ISellConditionService {
    HashMap<String, String> indexHash;
    HashMap<String, String> previousIndexHash;

    @Override
    public boolean sellCondition(String indexValue, HashMap<String, String> indexHash, HashMap<String, String> previousIndexHash) {
        this.indexHash = indexHash;
        this.previousIndexHash = previousIndexHash;

        if (indexValue.equals("cci")) {
            return this.cci();
        } else if(indexValue.equals("ma"))
        {
            return  this.ma();
        }
        return false;
    }

    private boolean cci() {
        double cciData = Double.parseDouble(indexHash.get("cci"));
        double previouscciData = Double.parseDouble(previousIndexHash.get("cci"));
        double previouscciClose = Double.parseDouble(previousIndexHash.get("close"));
        double cciClose = Double.parseDouble(indexHash.get("close"));
        if (cciData > 250) {
            printLog.append("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "这个小时CCI数据：" + cciData+",CCI大于250");
            printLog.append("<br />");
            return true;
        }
        else if (cciData < -100) {

            printLog.append("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "，CCI技术指标符合卖出条件，上一个小时CCI数据：" +previouscciData + "，这个小时CCI数据：" +cciData);
            printLog.append("<br />");
            return true;
        }
        return false;
    }
    private boolean ma()
    {
        double ma20 = Double.parseDouble(indexHash.get("ma20"));
        double close= Double.parseDouble(indexHash.get("close"));
        if(close < ma20)
        {
            printLog.append("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "，收盘价" + close+ "，ma20：" + ma20);
            printLog.append("<br />");

            return true;
        }
        return false;
    }
}
