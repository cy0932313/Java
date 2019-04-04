package com.chris.mechanization.dao;

import com.chris.mechanization.domain.xueqiuData.ItemStock;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-04-01 11:08
 * @description：
 */

public interface IXueqiuDayDao {
    int addXueqiuStock(@Param("tableName") String tableName, @Param("itemStock") ItemStock itemStock);

    List<ItemStock> queryInfoForLimit(@Param("tableName") String tableName, @Param("limitStart") String limitStart, @Param("limitEnd") String limitEnd);

    float queryInfoForSum(@Param("tableName") String tableName, @Param("limitStart") String limitStart, @Param("limitEnd") String limitEnd);

    int updateInfo(@Param("tableName") String tableName, @Param("feild") String feild, @Param("feildValue") String feildValue, @Param("timestamp") String timestamp);
}
