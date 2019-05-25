package com.chris.quantification.service.impl.monitor.CCI;

import com.chris.quantification.dao.IOperateTableDao;
import com.chris.quantification.domain.*;
import com.chris.quantification.enumType.TipsEnum;
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

    private HashMap<String,List> tempSavesymbol = new HashMap<>();
    public boolean isHistory;
    public boolean isRecord;
    private String recordCurrTime = "";

    @Override
    public void TechnicalIndex() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("period", "60m");
        param.put("begin", "1555516800000");

        this.emailContent.delete(0, this.emailContent.length());
        this.holdSymbolContent.delete(0, this.holdSymbolContent.length());

        symbolMonitorList = iOperateTableDao.queryInfoForMonitorSymbol();

        int symbolMonitorSize = symbolMonitorList.size();

        outer:for (int i = 0; i < symbolMonitorSize; i++) {
            SymbolMonitor item = symbolMonitorList.get(i);

            if(!this.isRecord && !this.recordCurrTime.equals(""))
            {
                List list = this.tempSavesymbol.get(this.recordCurrTime);
                if(list != null)
                {
                    for(int j = 0;j < list.size();j++)
                    {
                        if(list.get(j).equals(item.symbolCode))
                        {
                            continue outer;
                        }
                    }
                }
            }

            param.put("symbol", item.getSymbolCode());
            xueqiuSixtyData.setParameter(param);
            xueqiuSixtyData.getDataSoruce();
            handle(item, xueqiuSixtyData.getHandleDataResult());
        }

        if (!this.isRecord && this.emailContent.length() > 0) {
            emailService.sendMail("[监控结果]" + ChrisDateUtils.timeStamp2Date(
                    ChrisDateUtils.timeStamp(), null), this.holdSymbolContent.toString() + this.emailContent.toString());
        } else if(this.isRecord) {
            List<Tips> tips = this.iOperateTableDao.queryInfoForTips();
            if (tips.size() > 0) {
                this.emailContent.append("行情分析:共监控" + this.symbolMonitorList.size() + "只股票\n");
                this.emailContent.append(this.getTime(tips.get(0).getTime(), null) + "：\n超买" + tips.get(0).getOverbought() + "只，正常" + tips.get(0).getNormal() + "只，超卖" + tips.get(0).getOversold() + "只\n");
                if (tips.size() > 1) {
                    this.emailContent.append(this.getTime(tips.get(1).getTime(), null) + "：\n超买" + tips.get(1).getOverbought() + "只，正常" + tips.get(1).getNormal() + "只，超卖" + tips.get(1).getOversold() + "只\n");
                }
                if (tips.size() > 2) {
                    this.emailContent.append(this.getTime(tips.get(2).getTime(), null) + "：\n超买" + tips.get(2).getOverbought() + "只，正常" + tips.get(2).getNormal() + "只，超卖" + tips.get(2).getOversold() + "只\n");
                }
            }

            if (this.isHistory) {
                emailService.sendMail("[昨日复盘]" + ChrisDateUtils.timeStamp2Date(
                        ChrisDateUtils.timeStamp(), null), "对于15点提示的买入股票需要看早盘的走势在做决定\n" + this.emailContent.toString());
            } else {
                emailService.sendMail("[监控结果]" + ChrisDateUtils.timeStamp2Date(
                        ChrisDateUtils.timeStamp(), null), this.holdSymbolContent.toString() + this.emailContent.toString());
            }
        }
    }

    private void handle(SymbolMonitor symbolMonitor, ArrayList<HashMap<String, String>> resultDataList) {
        if (resultDataList.size() > 2) {
            Collections.reverse(resultDataList);

            cci_strategyCenter.currentData = resultDataList.get(0);
            cci_strategyCenter.previousData = resultDataList.get(1);
            this.recordCurrTime = cci_strategyCenter.currentData.get("timestamp");
            saveCCILog(symbolMonitor);

            SymbolHold symbolHold = this.isHold(symbolMonitor.getSymbolCode());
            if (symbolHold != null && this.isRecord) {
                cci_strategyCenter.currentDayOpenPrice = this.getCurrentOpenPirce(resultDataList);
                cci_strategyCenter.symbolHold = symbolHold;

                System.out.println("监控持有股票：<" + symbolHold.getSymbolName() + ">" + "\n" + "买入价：" + symbolHold.buyPrice + "\n" + "开盘价为：" + cci_strategyCenter.currentDayOpenPrice + "\n" +
                        "当前价为：" + cci_strategyCenter.currentData.get("close") + "\n"
                        + "持股天数：" + ChrisDateUtils.differentDaysByMillisecond(symbolHold.buyTime, this.getTime(cci_strategyCenter.currentData.get("timestamp"), "yyyy-MM-dd"), "yyyy-MM-dd") + "\n" +
                        "盈亏:" + String.format("%.2f", (Float.parseFloat(cci_strategyCenter.currentData.get("close")) / symbolHold.buyPrice - 1) * 100)
                        + "%\n\n"
                );
                this.holdSymbolContent.append("监控持有股票：<" + symbolHold.getSymbolName() + ">"
                        + "\n" + "买入价：" + symbolHold.buyPrice + "\n"
                        + "开盘价为：" + cci_strategyCenter.currentDayOpenPrice + "\n"
                        + "当前价为：" + cci_strategyCenter.currentData.get("close") + "\n"
                        + "持股天数：" + ChrisDateUtils.differentDaysByMillisecond(symbolHold.buyTime, this.getTime(cci_strategyCenter.currentData.get("timestamp"), "yyyy-MM-dd"), "yyyy-MM-dd") + "\n"
                        + "盈亏:" + String.format("%.2f", (Float.parseFloat(cci_strategyCenter.currentData.get("close")) / symbolHold.buyPrice - 1) * 100)
                        + "%\n\n");

                if (ChrisDateUtils.compare_date(symbolHold.getBuyTime(), this.getTime(cci_strategyCenter.currentData.get("timestamp"), "yyyy-MM-dd"), "yyyy-MM-dd") != -1) {
                    this.holdSymbolContent.append("当天买入，继续持有" + "\n\n");
                    return;
                }

                TipsEnum tipsEnum = cci_strategyCenter.sellCondition();
                if (tipsEnum != TipsEnum.PASS) {
                    String tempTips = "卖出卖出卖出!!!" + "\n"
                            + "卖出时间：" + this.getTime(cci_strategyCenter.currentData.get("timestamp"), null) + "\n"
                            + "卖出参考价：" + cci_strategyCenter.currentData.get("close") + "\n";
                    if (tipsEnum == TipsEnum.SELL_1) {
                        tempTips = tempTips + "卖出原因：CCI数据大于250" + "\n\n";
                    } else if (tipsEnum == TipsEnum.SELL_2) {
                        tempTips = tempTips + "卖出原因：CCI数据小于-100" + "\n\n";
                    } else if (tipsEnum == TipsEnum.SELL_3) {
                        tempTips = tempTips + "卖出原因：当天涨幅超过%5（ETF为%3）" + "\n\n";
                    } else if (tipsEnum == TipsEnum.SELL_4) {
                        tempTips = tempTips + "卖出原因：盈利超过10%(ETF为%5)" + "\n\n";
                    } else if (tipsEnum == TipsEnum.SELL_5) {
                        tempTips = tempTips + "卖出原因：利润超过%3之后又回踩了%3" + "\n\n";
                    } else if (tipsEnum == TipsEnum.SELL_6) {
                        tempTips = tempTips + "卖出原因：利润超过%5之后又回踩了%5" + "\n\n";
                    } else if (tipsEnum == TipsEnum.SELL_7) {
                        tempTips = tempTips + "卖出原因：利润超过%7之后又回踩了%7" + "\n\n";
                    } else if (tipsEnum == TipsEnum.SELL_8) {
                        tempTips = tempTips + "卖出原因：利润超过%10之后又回踩了%10" + "\n\n";
                    }

                    this.holdSymbolContent.append(tempTips);
                } else {
                    this.holdSymbolContent.append("继续持有" + "\n\n");
                }
            } else {
                if (cci_strategyCenter.buyCondition()) {

                    if(!this.isRecord)
                    {
                        List addSavesymbolList = this.tempSavesymbol.get(cci_strategyCenter.currentData.get("timestamp"));
                        if(addSavesymbolList == null)
                        {
                            addSavesymbolList = new ArrayList();
                        }

                        addSavesymbolList.add(symbolMonitor.getSymbolCode());
                        this.tempSavesymbol.put(cci_strategyCenter.currentData.get("timestamp"),addSavesymbolList);
                    }


                    this.emailContent.append(symbolMonitor.getSymbolName() + ",买入买入买入!!!" + "\n");

                    this.emailContent.append("通过指标监控到\nCCI数据\n上个小时：" + String.format("%.2f", Float.parseFloat(cci_strategyCenter.previousData.get("cci"))) + "\n这个小时：" + String.format("%.2f", Float.parseFloat(cci_strategyCenter.currentData.get("cci")))
                            + "\n" +
                            "买入时间：" + this.getTime(cci_strategyCenter.currentData.get("timestamp"), null)
                            + "\n" + "买入参考价：" + cci_strategyCenter.currentData.get("close") + "\n\n");
                }
            }
        }
    }

    private void saveCCILog(SymbolMonitor symbolMonitor) {
        if(this.isRecord)
        {
            MonitorRecord monitorRecord = new MonitorRecord(symbolMonitor.getSymbolCode(), symbolMonitor.getSymbolName(), cci_strategyCenter.currentData.get("cci"), cci_strategyCenter.currentData.get("timestamp"));
            iOperateTableDao.addMonitorRecord(monitorRecord);
        }
    }

    private SymbolHold isHold(String symbolCode) {
        if(symbolHoldList == null)
        {
            symbolHoldList = iOperateTableDao.queryInfoForHoldSymbol();
        }
        for (int i = 0; i < symbolHoldList.size(); i++) {
            SymbolHold item = symbolHoldList.get(i);
            if (item.getSymbolCode().equals(symbolCode)) {
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
