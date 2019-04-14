package com.chris.quantification.dao;

import com.chris.quantification.domain.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-04-13 22:26
 **/

public interface IOperateTableDao {
    List<SymbolMonitor> queryInfoForMonitorSymbol();
    List<SymbolHold> queryInfoForHoldSymbol();
    int addMonitorRecord( @Param("monitorRecord") MonitorRecord monitorRecord);
    int addHoldRecord( @Param("holdRecord") HoldRecord holdRecord);
    List<Tips> queryInfoForTips();
    List<HoldRecord> queryInfoForHoldRecord( @Param("groupID") int groupID);
}
