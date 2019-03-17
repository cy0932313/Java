package com.chris.quantification.service.impl.monitor.CCI;

import com.chris.quantification.service.IMonitorCenter;
import com.chris.quantification.service.impl.EmailServiceImpl;
import com.chris.quantification.service.impl.XueqiuSixtyDataImpl;
import com.chrisY.util.ChrisDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-03-12 18:19
 * @description：
 */

@Service
public class CCI_MonitorCenterImpl implements IMonitorCenter {

    private Map<String, String> symbolMap = new HashMap<String, String>() {
        {
            put("SH600196", "复兴医药");
            put("SH600436", "片子癀");
            put("SZ000963", "华东医药");
            put("SH600276", "恒瑞医药");
            put("SZ300015", "爱尔眼科");
            put("SH600518", "康美药业");
            put("SZ000538", "云南白药");

            put("SZ159915", "创业板");
            put("SH510300", "沪深300");
            put("SZ000651", "格力电器");
            put("SH601318", "中国平安");
            put("SH601398", "工商银行");
            put("SZ000858", "五粮液");
            put("SH600030", "中信证券");
            put("SZ000333", "美的集团");

            put("SZ300730", "科创信息");
            put("SZ300059", "东方财富");
            put("SH601890", "亚星锚链");
            put("SZ000725", "京东方A");
            put("SZ300468", "四方精创");
            put("SH600776", "东方通信");
        }
    };

    @Autowired
    XueqiuSixtyDataImpl xueqiuSixtyData;
    @Autowired
    CCI_StrategyCenterImpl cci_strategyCenter;
    @Autowired
    EmailServiceImpl emailService;

    private boolean trigger = false;

    private StringBuilder emailContent = new StringBuilder();

    private Map<String, String> buySymbolMap = new HashMap<String, String>() {
        {
            put("复兴医药", "2019-03-11");
            put("创业板", "2019-03-15");
            put("中信证券", "2019-03-15");
        }
    };

    @Override
    public void TechnicalIndex() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("period", "60m");
        param.put("begin", "1552011341000");
        this.emailContent.delete(0, this.emailContent.length());

        for (String symbol : this.symbolMap.keySet()) {
            param.put("symbol", symbol);
            xueqiuSixtyData.setParameter(param);
            xueqiuSixtyData.getDataSoruce();
            handle(this.symbolMap.get(symbol), xueqiuSixtyData.getHandleDataResult());
        }

        if (!this.trigger) {
            emailService.sendMail("安心上班", ChrisDateUtils.timeStamp2Date(
                    ChrisDateUtils.timeStamp(), null) + "监控结束");
        } else {
            emailService.sendMail("[交易提醒]" + ChrisDateUtils.timeStamp2Date(
                    ChrisDateUtils.timeStamp(), null), this.emailContent.toString());
            this.trigger = false;
        }
    }

    private void handle(String symbolName, ArrayList<HashMap<String, String>> resultDataList) {
        if (resultDataList.size() > 2) {
            Collections.reverse(resultDataList);

            cci_strategyCenter.currentData = resultDataList.get(0);
            cci_strategyCenter.previousData = resultDataList.get(1);

            System.out.println("监控内容：" + symbolName + "上个小时CCI数据" + cci_strategyCenter.previousData.get("cci"));
            System.out.println("监控内容：" + symbolName + "这个小时CCI数据" + cci_strategyCenter.currentData.get("cci"));

            if (this.isHold(symbolName, cci_strategyCenter.currentData.get("timestamp"))) {
                cci_strategyCenter.currentDayOpenPrice = this.getCurrentOpenPirce(resultDataList);
                if (cci_strategyCenter.sellCondition()) {
                    this.emailContent.append(symbolName + ",卖出卖出卖出!!!" + "\n");
                    this.emailContent.append("通过指标监控到\nCCI数据\n上个小时：" + cci_strategyCenter.previousData.get("cci") + "\n这个小时：" + cci_strategyCenter.currentData.get("cci")
                            + "\n" +
                            "卖入时间：" + this.getTime(cci_strategyCenter.currentData.get("timestamp"), null)
                            + "\n" + "卖入参考价：" + cci_strategyCenter.currentData.get("close") + "\n\n");

                    this.buySymbolMap.remove(symbolName);
                    this.trigger = true;
                }
            }

            if (cci_strategyCenter.buyCondition()) {
                this.emailContent.append(symbolName + ",买入买入买入!!!" + "\n");
                this.emailContent.append("通过指标监控到\nCCI数据\n上个小时：" + cci_strategyCenter.previousData.get("cci") + "\n这个小时：" + cci_strategyCenter.currentData.get("cci")
                        + "\n" +
                        "买入时间：" + this.getTime(cci_strategyCenter.currentData.get("timestamp"), null)
                        + "\n" + "买入参考价：" + cci_strategyCenter.currentData.get("close") + "\n\n");

                this.trigger = true;
            }
        }
    }

    private boolean isHold(String symbolName, String buyDate) {
        for (String symbol : this.buySymbolMap.keySet()) {
            if (symbol.equals(symbolName) && this.buySymbolMap.get(symbol).equals(this.getTime(buyDate, "yyyy-MM-dd"))) {
                return true;
            }
        }
        return false;
    }

    private double getCurrentOpenPirce(ArrayList<HashMap<String, String>> resultDataList) {
        int currentHour = Integer.parseInt(this.getTime(resultDataList.get(0).get("timestamp"), "HH"));
        int index = 0;
        if (currentHour < 12) {
            index = currentHour - 10;
        } else {
            index = currentHour - 12;
        }

        return Double.parseDouble(resultDataList.get(index).get("open"));
    }

    private String getTime(String timestamp, String formate) {
        if (formate == null) {
            formate = "yyyy-MM-dd HH:mm:ss";
        }

        return ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(timestamp) / 1000), formate);
    }

}
