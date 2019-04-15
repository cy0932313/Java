package com.chris.quantification.service.impl.monitor.CCI;

import com.chris.quantification.dao.IOperateTableDao;
import com.chris.quantification.domain.*;
import com.chris.quantification.enumType.TipsEnum;
import com.chris.quantification.service.IMonitorCenter;
import com.chris.quantification.service.impl.EmailServiceImpl;
import com.chris.quantification.service.impl.XueqiuSixtyDataImpl;
import com.chris.quantification.utils.LogUtils;
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
    @Autowired
    XueqiuSixtyDataImpl xueqiuSixtyData;
    @Autowired
    CCI_StrategyCenterImpl cci_strategyCenter;
    @Autowired
    EmailServiceImpl emailService;
    @Autowired
    IOperateTableDao iOperateTableDao;

    private StringBuilder emailContent = new StringBuilder();
    private StringBuilder holdSymbolContent = new StringBuilder();

    private List<SymbolMonitor> symbolMonitorList;
    private List<SymbolHold> symbolHoldList;

    @Override
    public void TechnicalIndex() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("period", "60m");
        param.put("begin", "1554689741000");

        this.emailContent.delete(0, this.emailContent.length());

        symbolMonitorList = iOperateTableDao.queryInfoForMonitorSymbol();
        symbolHoldList = iOperateTableDao.queryInfoForHoldSymbol();

        int symbolMonitorSize = symbolMonitorList.size();
        for (int i = 0; i < symbolMonitorSize; i++) {
            SymbolMonitor item = symbolMonitorList.get(i);
            param.put("symbol", item.getSymbolCode());
            xueqiuSixtyData.setParameter(param);
            xueqiuSixtyData.getDataSoruce();
            handle(item, xueqiuSixtyData.getHandleDataResult());
        }

        List<Tips> tips = this.iOperateTableDao.queryInfoForTips();
        if (tips.size() > 0) {
            this.emailContent.append("行情分析:共监控" + this.symbolMonitorList.size() + "只股票\n");
            this.emailContent.append("这个小时超买" + tips.get(0).getOverbought() + "只，正常" + tips.get(0).getNormal() + "只，超卖" + tips.get(0).getOversold() + "只\n");
            if (tips.size() > 1) {
                this.emailContent.append("上个小时超买" + tips.get(1).getOverbought() + "只，正常" + tips.get(1).getNormal() + "只，超卖" + tips.get(1).getOversold() + "只\n");
            }
            if (tips.size() > 2) {
                this.emailContent.append("上上个小时超买" + tips.get(2).getOverbought() + "只，正常" + tips.get(2).getNormal() + "只，超卖" + tips.get(2).getOversold() + "只\n");

                if (tips.get(0).getOverbought() > tips.get(1).getOverbought() && tips.get(1).getOverbought() > tips.get(2).getOverbought()) {
                    this.emailContent.append("当前行情处于持续上升阶段,建议持股待涨" + "\n");
                } else if (tips.get(0).getOversold() < tips.get(1).getOversold() && tips.get(1).getOversold() < tips.get(2).getOversold()) {
                    this.emailContent.append("当前行情处于反弹阶段,建议轻仓参与" + "\n");
                } else if (tips.get(0).getOversold() > tips.get(1).getOversold() && tips.get(1).getOversold() > tips.get(2).getOversold()) {
                    this.emailContent.append("当前行情处于下降阶段,建议持币观望" + "\n");
                } else {
                    int reference = this.symbolMonitorList.size() / 3;
                    if (tips.get(0).getOverbought() > reference) {
                        this.emailContent.append("当前行情处于超买阶段" + "\n");
                    } else if (tips.get(0).getNormal() > reference) {
                        this.emailContent.append("当前行情处于正常波动阶段" + "\n");
                    } else if (tips.get(0).getOversold() > reference) {
                        this.emailContent.append("当前行情处于超卖阶段" + "\n");
                    }
                }
            }
        }

        emailService.sendMail("[监控结果]" + ChrisDateUtils.timeStamp2Date(
                    ChrisDateUtils.timeStamp(), null),this.holdSymbolContent.toString() + this.emailContent.toString());
    }

    private void handle(SymbolMonitor symbolMonitor, ArrayList<HashMap<String, String>> resultDataList) {
        if (resultDataList.size() > 2) {
            Collections.reverse(resultDataList);

            cci_strategyCenter.currentData = resultDataList.get(0);
            cci_strategyCenter.previousData = resultDataList.get(1);
            saveCCILog(symbolMonitor);

            SymbolHold symbolHold = this.isHold(symbolMonitor.getSymbolCode(), cci_strategyCenter.currentData.get("timestamp"));
            if (symbolHold != null) {
                cci_strategyCenter.currentDayOpenPrice = this.getCurrentOpenPirce(resultDataList);
                cci_strategyCenter.symbolHold = symbolHold;
                System.out.println("监控持有股票：<" + symbolHold.getSymbolName() + ">"+ "\n"+"买入价：" + symbolHold.buyPrice+ "\n" + "开盘价为：" + cci_strategyCenter.currentDayOpenPrice + "\n"+
                        "当前价为：" + cci_strategyCenter.currentData.get("close")
                        + "\n"+ "盈亏:" + String.format("%.2f", (Float.parseFloat(cci_strategyCenter.currentData.get("close")) / symbolHold.buyPrice - 1) * 100)
                        + "%\n\n"
                );
                this.holdSymbolContent.append("监控持有股票：<" + symbolHold.getSymbolName() + ">"+ "\n"+"买入价：" + symbolHold.buyPrice+ "\n" + "开盘价为：" + cci_strategyCenter.currentDayOpenPrice + "\n"+
                        "当前价为：" + cci_strategyCenter.currentData.get("close")
                        + "\n"+ "盈亏:" + String.format("%.2f", (Float.parseFloat(cci_strategyCenter.currentData.get("close")) / symbolHold.buyPrice - 1) * 100)
                        + "%\n\n");

                TipsEnum tipsEnum = cci_strategyCenter.sellCondition();
                if (tipsEnum != TipsEnum.PASS) {
                    String tempTips = symbolHold.getSymbolName()
                            + ",卖出卖出卖出!!!" + "\n"
                            + "卖出时间：" + this.getTime(cci_strategyCenter.currentData.get("timestamp"), null) + "\n"
                            + "卖出参考价：" + cci_strategyCenter.currentData.get("close") + "\n";
                    if (tipsEnum == TipsEnum.SELL_1) {
                        tempTips = tempTips + "卖出条件：CCI数据大于250" + "\n\n";
                    } else if (tipsEnum == TipsEnum.SELL_2) {
                        tempTips = tempTips + "卖出条件：CCI数据小于-100" + "\n\n";
                    } else if (tipsEnum == TipsEnum.SELL_3) {
                        tempTips = tempTips + "卖出条件：当天涨幅超过%5（ETF为%3）" + "\n\n";
                    } else if (tipsEnum == TipsEnum.SELL_4) {
                        tempTips = tempTips + "卖出条件：盈利超过10%(ETF为%5)" + "\n\n";
                    } else if (tipsEnum == TipsEnum.SELL_5) {
                        tempTips = tempTips + "卖出条件：利润超过%3之后又回踩了%3" + "\n\n";
                    } else if (tipsEnum == TipsEnum.SELL_6) {
                        tempTips = tempTips + "卖出条件：利润超过%5之后又回踩了%5" + "\n\n";
                    } else if (tipsEnum == TipsEnum.SELL_7) {
                        tempTips = tempTips + "卖出条件：利润超过%7之后又回踩了%7" + "\n\n";
                    } else if (tipsEnum == TipsEnum.SELL_8) {
                        tempTips = tempTips + "卖出条件：利润超过%10之后又回踩了%10" + "\n\n";
                    }

                    this.holdSymbolContent.append(tempTips);
                }
                else
                {
                    this.holdSymbolContent.append("继续持有"+ "\n\n");
                }
            } else {
                if (cci_strategyCenter.buyCondition()) {
                    this.emailContent.append(symbolMonitor.getSymbolName() + ",买入买入买入!!!" + "\n");
                    this.emailContent.append("通过指标监控到\nCCI数据\n上个小时：" + cci_strategyCenter.previousData.get("cci") + "\n这个小时：" + cci_strategyCenter.currentData.get("cci")
                            + "\n" +
                            "买入时间：" + this.getTime(cci_strategyCenter.currentData.get("timestamp"), null)
                            + "\n" + "买入参考价：" + cci_strategyCenter.currentData.get("close") + "\n\n");
                }
            }
        }
    }

    private void saveCCILog(SymbolMonitor symbolMonitor) {
        MonitorRecord monitorRecord = new MonitorRecord(symbolMonitor.getSymbolCode(), symbolMonitor.getSymbolName(), cci_strategyCenter.currentData.get("cci"), cci_strategyCenter.currentData.get("timestamp"));
        iOperateTableDao.addMonitorRecord(monitorRecord);
    }

    private SymbolHold isHold(String symbolCode, String symbolDate) {
        for (int i = 0; i < symbolHoldList.size(); i++) {
            SymbolHold item = symbolHoldList.get(i);
            if (item.getSymbolCode().equals(symbolCode) && ChrisDateUtils.compare_date(item.getBuyTime(), this.getTime(symbolDate, "yyyy-MM-dd"), "yyyy-MM-dd") == -1) {
                return item;
            }
        }

        return null;
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
