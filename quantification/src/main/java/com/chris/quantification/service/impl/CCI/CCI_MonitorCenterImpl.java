package com.chris.quantification.service.impl.CCI;

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
//
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

    public String currentTimeStamp;

    //当前任务启动状态，需要设置当前是否持股或则空仓
    private boolean currentStatus = true;
    private String currentSymbol = "复兴医药";


    @Override
    public void TechnicalIndex() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("period", "60m");
        param.put("begin", "1552011341000");

        for (String symbol : this.symbolMap.keySet()) {
            param.put("symbol", symbol);
            xueqiuSixtyData.setParameter(param);
            xueqiuSixtyData.getDataSoruce();
            handle(this.symbolMap.get(symbol), xueqiuSixtyData.getHandleDataResult());
        }
    }

    private void handle(String symbolName, ArrayList<HashMap<String, String>> resultDataList) {
        if (resultDataList.size() > 2) {
            Collections.reverse(resultDataList);

            int hour = Integer.parseInt(ChrisDateUtils.timeStamp2Date(
                    this.currentTimeStamp, "HH"));
            int minute = Integer.parseInt(ChrisDateUtils.timeStamp2Date(
                    this.currentTimeStamp, "mm"));

            if (minute == 53 && hour == 14) {
                cci_strategyCenter.currentData = resultDataList.get(0);
                cci_strategyCenter.previousData = resultDataList.get(1);
            } else {
                cci_strategyCenter.currentData = resultDataList.get(1);
                cci_strategyCenter.previousData = resultDataList.get(2);
            }

            if (this.currentStatus && this.currentSymbol.equals(symbolName)) {
                if (cci_strategyCenter.sellCondition()) {
                    this.currentStatus = false;
                    String content = "通过指标监控到\nCCI数据\n上个小时：" + cci_strategyCenter.previousData.get("cci") + "\n这个小时：" + cci_strategyCenter.currentData.get("cci")
                            + "\n" +
                            "卖入时间：" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(cci_strategyCenter.currentData.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss")
                            + "\n" + "卖入参考价：" + cci_strategyCenter.currentData.get("close");
                    emailService.sendMail(symbolName + ",卖出卖出卖出!!!", content);
                }
            } else {
                if (cci_strategyCenter.buyCondition()) {
                    this.currentStatus = true;
                    String content = "通过指标监控到\nCCI数据\n上个小时：" + cci_strategyCenter.previousData.get("cci") + "\n这个小时：" + cci_strategyCenter.currentData.get("cci")
                            + "\n" +
                            "买入时间：" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(cci_strategyCenter.currentData.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss")
                            + "\n" + "买入参考价：" + cci_strategyCenter.currentData.get("close");
                    emailService.sendMail(symbolName + ",买入买入买入!!!", content);
                }
            }


        }
    }
}
