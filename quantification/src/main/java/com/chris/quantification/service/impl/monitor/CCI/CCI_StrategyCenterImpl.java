package com.chris.quantification.service.impl.monitor.CCI;

import com.chris.quantification.dao.IOperateTableDao;
import com.chris.quantification.domain.HoldRecord;
import com.chris.quantification.domain.SymbolHold;
import com.chris.quantification.enumType.TipsEnum;
import com.chris.quantification.service.IStrategyCenter;
import com.chris.quantification.service.impl.EmailServiceImpl;
import com.chrisY.util.ChrisDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

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
    public SymbolHold symbolHold;

    @Autowired
    EmailServiceImpl emailService;

    @Autowired
    IOperateTableDao iOperateTableDao;

    @Override
    public boolean buyCondition() {
        if (currentData.get("cci") == null || currentData.get("cci").equals("")) {
            emailService.sendMail("数据异常:" + ChrisDateUtils.timeStamp2Date(
                    ChrisDateUtils.timeStamp(), null), currentData.toString() + "监控结束");
            System.out.println(currentData.toString());
        }

        double cciData = Double.parseDouble(currentData.get("cci"));
        double cciClose = Double.parseDouble(currentData.get("close"));
        double cciOpen = Double.parseDouble(currentData.get("open"));
        double volume = Double.parseDouble(currentData.get("volume"));

        double previouscciData = Double.parseDouble(previousData.get("cci"));
        double previouscciClose = Double.parseDouble(previousData.get("close"));
        double previousvolume = Double.parseDouble(previousData.get("volume"));

        if (cciClose > cciOpen && volume > previousvolume && cciData > -100 && previouscciData < -100 && !ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(currentData.get("timestamp")) / 1000), "HH").equals("15")) {
            return true;
        }
        return false;
    }

    @Override
    public TipsEnum sellCondition() {
        double cciData = Double.parseDouble(currentData.get("cci"));
        double cciClose = Double.parseDouble(currentData.get("close"));
        double dayProfit = 0.05;
        double countProfit = 0.1;

        this.iOperateTableDao.addHoldRecord(new HoldRecord(symbolHold.symbolCode, symbolHold.symbolName
                , currentData.get("open")
                , currentData.get("close")
                , currentData.get("high")
                , currentData.get("low")
                , previousData.get("cci")
                , currentData.get("cci")
                , currentData.get("timestamp")
                , String.valueOf(symbolHold.getId())));

        if (this.symbolHold.isETF) {
            dayProfit = 0.03;
            countProfit = 0.05;
        }

        if (cciData > 250) {
            return TipsEnum.SELL_1;
        } else if (cciData < -100) {
            return TipsEnum.SELL_2;
        } else if (this.currentDayOpenPrice != 0 && (cciClose / this.currentDayOpenPrice) - 1 > dayProfit) {
            return TipsEnum.SELL_3;
        } else if (this.symbolHold != null && (cciClose / this.symbolHold.buyPrice) - 1 > countProfit) {
            return TipsEnum.SELL_4;
        } else {
            return targetProfit();
        }
    }

    private TipsEnum targetProfit() {
        List<HoldRecord> holdRecordList = iOperateTableDao.queryInfoForHoldRecord(symbolHold.getId());
        int size = holdRecordList.size();
        double maxProfit = 0;
        for (int i = 0; i < size; i++) {
            HoldRecord holdRecord = holdRecordList.get(i);
            if ((Float.parseFloat(holdRecord.getClose()) / symbolHold.getBuyPrice() - 1) > maxProfit) {
                maxProfit = Float.parseFloat(holdRecord.getClose()) / symbolHold.getBuyPrice() - 1;
            }
        }

        if (maxProfit > 0.03 && maxProfit < 0.05
                && Double.parseDouble(currentData.get("close")) / symbolHold.getBuyPrice() - 1 < 0.03) {
            return TipsEnum.SELL_5;
        } else if (maxProfit > 0.05 && maxProfit < 0.07
                && Double.parseDouble(currentData.get("close")) / symbolHold.getBuyPrice() - 1 < 0.05) {
            return TipsEnum.SELL_6;
        } else if (maxProfit > 0.07 && maxProfit < 0.1
                && Double.parseDouble(currentData.get("close")) / symbolHold.getBuyPrice() - 1 < 0.07) {
            return TipsEnum.SELL_7;
        } else if (maxProfit > 0.1
                && Double.parseDouble(currentData.get("close")) / symbolHold.getBuyPrice() - 1 < 0.1) {
            return TipsEnum.SELL_8;
        }

        return TipsEnum.PASS;
    }
}
