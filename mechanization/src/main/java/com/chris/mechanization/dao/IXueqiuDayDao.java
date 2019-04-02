package com.chris.mechanization.dao;

import com.chris.mechanization.domain.xueqiuData.ItemStock;
import org.apache.ibatis.annotations.Param;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-04-01 11:08
 * @description：
 */

public interface IXueqiuDayDao {
    int addXueqiuStock(@Param("tableName")String tableName, @Param("itemStock")ItemStock itemStock);
}
