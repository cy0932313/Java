package com.chrisY.service.quantification.impl;

import com.chrisY.service.quantification.ISellConditionService;
import com.chrisY.util.ChrisDateUtils;
import com.chrisY.web.QuantificationController;
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
String openPrice;
    @Override
    public boolean sellCondition(String openPrice,String indexValue, HashMap<String, String> indexHash, HashMap<String, String> previousIndexHash) {
        this.indexHash = indexHash;
        this.previousIndexHash = previousIndexHash;
this.openPrice = openPrice;
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
        double cciOpen = Double.parseDouble(indexHash.get("open"));

        double kdjk = Double.parseDouble(indexHash.get("kdjk"));
        double kdjd = Double.parseDouble(indexHash.get("kdjd"));
        double kdjj = Double.parseDouble(indexHash.get("kdjj"));
        int once = 0;
        if(kdjk > 80)
        {
            ++once;
        }
         if(kdjd > 80)
        {
            ++once;
        }
         if(kdjj > 80)
        {
            ++once;
        }

        if (cciData < -100) {
            printLog.append("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "，CCI技术指标符合卖出条件，上一个小时CCI数据：" +previouscciData + "，这个小时CCI数据：" +cciData);
            printLog.append("<br />");
            return true;
        }
//        else
//        if(this.openPrice != null &&(cciClose/Double.parseDouble(this.openPrice)) - 1> 0.05)
//        {
//            QuantificationController.test = this.openPrice;
//            printLog.append("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "当天开盘价为:"+this.openPrice+"当前时间收盘价："+cciClose);
//            printLog.append("<br />");
//            return true;
//        }
        else  if(QuantificationController.test1 != 0 && (cciClose/QuantificationController.test1) - 1> 0.01)
        {
            QuantificationController.test1 = 0;
            printLog.append("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "买入价为:"+QuantificationController.test1+"当前时间收盘价："+cciClose);
            printLog.append("<br />");
            return true;
        }
        else if( QuantificationController.test1 != 0 && (cciClose/QuantificationController.test1) - 1 < 0)
        {
            printLog.append("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "买入价为:"+QuantificationController.test1+"当前时间收盘价："+cciClose);
            printLog.append("<br />");
            return true;
        }
//        else if (cciData > 250) {
//            printLog.append("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "这个小时CCI数据：" + cciData+",CCI大于250");
//            printLog.append("<br />");
//            return true;
//        }
//        else if(kdjd >= 80 && cciData <100 && cciData > -100)
//        {
//            printLog.append("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "KDJ卖出:"+once);
//            printLog.append("<br />");
//            return true;
//        }
//        else if(this.openPrice != null && 1 - (cciClose/Double.parseDouble(this.openPrice)) > 0.05)
//        {
//            QuantificationController.test = this.openPrice;
//            printLog.append("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "当天开盘价为:"+this.openPrice+"当前时间收盘价："+cciClose);
//            printLog.append("<br />");
//            return true;
//        }
//        else if(cciData > 200 && previouscciData > 200 && previouscciData > cciData && cciClose > previouscciClose)
//        {
//            printLog.append("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "，CCI背离");
//            printLog.append("<br />");
//            return true;
//        }

        if(QuantificationController.test1 > 0)
        {
            printLog.append("利益"+cciClose/QuantificationController.test1);
            printLog.append("<br />");
        }

//        if(QuantificationController.test1_1 == 0)
//        {
//            QuantificationController.test1_1 = cciClose/QuantificationController.test1;
//        }
//        else
//        {
//            double tempTest1_1 = cciClose/QuantificationController.test1;
//            if(tempTest1_1 > QuantificationController.test1_1)
//            {
//                QuantificationController.test1_1 = tempTest1_1;
//                printLog.append("最高利益"+tempTest1_1);
//                printLog.append("<br />");
//        }
//            else if(QuantificationController.test1_1 - tempTest1_1 >= 0.02)
//            {
//                printLog.append("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "利润回测卖出,买入价" + QuantificationController.test1 +"卖出价"+cciClose );
//                printLog.append("<br />");
//                return  true;
//            }
//        }


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
