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

    @Scheduled(fixedRate = 60000)
    public void startMonitor_CCI() {
        String currentTimeStamp = ChrisDateUtils.timeStamp();
        String currentTime = ChrisDateUtils.timeStamp2Date(
                currentTimeStamp, null);

        int hour = Integer.parseInt(ChrisDateUtils.timeStamp2Date(
                currentTimeStamp, "HH"));
        int minute = Integer.parseInt(ChrisDateUtils.timeStamp2Date(
                currentTimeStamp, "mm"));
        int week = ChrisDateUtils.getFullDateWeekTime(currentTime);

        if (week < 6 && hour > 9 && hour < 15) {
            if ((hour == 9 && minute == 30) ||
                    (hour == 10 && minute == 29) ||
                    (hour == 11 && minute == 28) ||
                    (hour == 13 && minute == 59) ||
                    (hour == 14 && minute == 53)) {
                System.out.println("监控时间：" + currentTime);
                cci_monitorCenter.TechnicalIndex();
            }
        }
    }
}
