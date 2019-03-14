package com.chris.quantification.run;

import com.chris.quantification.service.impl.CCI.CCI_MonitorCenterImpl;
import com.chrisY.util.ChrisDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-03-14 11:33
 * @description：
 */

@Component
public class MonitorApplication {

    @Autowired
    CCI_MonitorCenterImpl cci_monitorCenter;

    @Scheduled(fixedRate = 30000)
    public void startMonitor_CCI() {
        String currentTimeStamp = ChrisDateUtils.timeStamp();
        String currentTime = ChrisDateUtils.timeStamp2Date(
                currentTimeStamp, null);

        int hour = Integer.parseInt(ChrisDateUtils.timeStamp2Date(
                currentTimeStamp, "HH"));
        int minute = Integer.parseInt(ChrisDateUtils.timeStamp2Date(
                currentTimeStamp, "mm"));
//        int week = ChrisDateUtils.getFullDateWeekTime(currentTime);
//
//        if (week < 6 && (hour > 8 && hour < 15)) {
//            if (minute == 0 || (minute == 53 && hour == 14)) {
                cci_monitorCenter.currentTimeStamp = currentTimeStamp;
                cci_monitorCenter.TechnicalIndex();
//            }
//        }
    }
}
