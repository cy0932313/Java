package com.chris.quantification.service.impl.monitor;

import com.chris.quantification.service.IMonitorCenter;
import com.chris.quantification.service.impl.EmailServiceImpl;
import com.chris.quantification.service.impl.XueqiuSixtyDataImpl;
import com.chrisY.util.ChrisDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-03-16 22:47
 **/
@Service
public class Price_MonitorCenterImpl implements IMonitorCenter {
    private Map<String, String> symbolMap = new HashMap<String, String>() {
        {
            put("SH600196", "复兴医药");
            put("SH600436", "片子癀");
        }
    };
    private Map<String, String> monitorPirceMap = new HashMap<String, String>() {
        {
            put("复兴医药", ">|29.03");
            put("片子癀", "<|229.03");
        }
    };

    @Autowired
    XueqiuSixtyDataImpl xueqiuSixtyData;
    @Autowired
    EmailServiceImpl emailService;

    @Override
    public void TechnicalIndex() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("period", "60m");
        param.put("begin", "1552011341000");
        if(monitorPirceMap.size() > 0)
        {
            for (String symbol : this.symbolMap.keySet()) {
                param.put("symbol", symbol);
                xueqiuSixtyData.setParameter(param);
                xueqiuSixtyData.getDataSoruce();
                handle(this.symbolMap.get(symbol), xueqiuSixtyData.getHandleDataResult());
            }
        }
    }

    private void handle(String symbolName, ArrayList<HashMap<String, String>> resultDataList) {
        Collections.reverse(resultDataList);
        String temp = this.monitorPirceMap.get(symbolName);
        String[] tempArray = temp.split("\\|");
        double monitorSymbolPrice = Double.parseDouble(tempArray[1]);
        if (tempArray[0].equals(">")) {
            if (Double.parseDouble(resultDataList.get(0).get("close")) >= monitorSymbolPrice) {
                this.emailService.sendMail("[目标价提醒]" + ChrisDateUtils.timeStamp2Date(
                        ChrisDateUtils.timeStamp(), null), symbolName + "已大于目标价:" + monitorSymbolPrice + "\n" + "当前价格为：" + Double.parseDouble(resultDataList.get(0).get("close")) + "\n");
                this.monitorPirceMap.remove(symbolName);
            }
        } else if (tempArray[0].equals("<")) {
            if (Double.parseDouble(resultDataList.get(0).get("close")) <= monitorSymbolPrice) {
                this.emailService.sendMail("[目标价提醒]" + ChrisDateUtils.timeStamp2Date(
                        ChrisDateUtils.timeStamp(), null), symbolName + "已小于目标价:" + monitorSymbolPrice + "\n" + "当前价格为：" + Double.parseDouble(resultDataList.get(0).get("close")) + "\n");
                this.monitorPirceMap.remove(symbolName);
            }
        }
    }

    @Override
    public void testEnvironmental()
    {

    }
}
