package com.chrisY.service.quantification.impl;

import com.chrisY.service.quantification.IBuyConditionService;
import com.chrisY.util.ChrisDateUtils;
import com.chrisY.web.QuantificationController;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static com.chrisY.web.QuantificationController.printLog;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-03-03 19:01
 **/
@Service
public class BuyServiceImpl implements IBuyConditionService {
    HashMap<String, String> indexHash;
    HashMap<String, String> previousIndexHash;

    @Override
    public boolean buyCondition(String openPrice,String indexValue, HashMap<String, String> indexHash, HashMap<String, String> previousIndexHash) {
        this.indexHash = indexHash;
        this.previousIndexHash = previousIndexHash;

        if (indexValue.equals("cci")) {
            return this.cci();
        }
        else if(indexValue.equals("ma"))
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

        double kdjd = Double.parseDouble(indexHash.get("kdjd"));
        double previouskdjd = Double.parseDouble(previousIndexHash.get("kdjd"));


        if (cciData > -100 && previouscciData < -100 && cciClose > previouscciClose&&!ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "HH").equals("15") ) {
            QuantificationController.test1 = cciClose;
            printLog.append("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "，CCI技术指标符合买入条件，上一个小时CCI数据：" + previouscciData + "，这个小时CCI数据：" + cciData);
            printLog.append("<br />");
            return true;
        }
        if(QuantificationController.test != "" && cciClose < Double.parseDouble( QuantificationController.test))
        {
            QuantificationController.test1 = cciClose;
            QuantificationController.test = "";
            printLog.append("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "，上次涨了5个点，现在买回来。");
            printLog.append("<br />");
            return  true;
        }

        if(cciData < 100 && cciData > -100 && kdjd > 20 && previouskdjd < 20)
        {
//            QuantificationController.test1 = cciClose;
            printLog.append("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "，KDJ技术指标符合买入条件，上一个小时KDJ数据：" + previouskdjd + "，这个小时KDJ数据：" + kdjd);
            printLog.append("<br />");
            return true;
        }


        return false;
    }

    private boolean ma()
    {
        double ma20 = Double.parseDouble(indexHash.get("ma20"));
        double close= Double.parseDouble(indexHash.get("close"));
        if(close > ma20)
        {
            printLog.append("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "，收盘价" + close+ "，ma20：" + ma20);
            printLog.append("</br>");
            return true;
        }
        return false;
    }
}
