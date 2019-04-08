package com.chris.mechanization.dao;


import com.binance.api.client.domain.market.Candlestick;
import com.chris.mechanization.domain.Account;
import com.chris.mechanization.domain.xueqiuData.ItemStock;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-04-02 10:36
 * @description：
 */

public interface IOperateTableDao {
    int existTable(@Param("tableName")String tableName);

    void dropTable(@Param("tableName") String tableName);

    void trancateTable(@Param("tableName") String tableName);

    void createNewTable_symbol(@Param("tableName")String tableName);

    void createNewTable_coin(@Param("tableName")String tableName);

    float queryInfoForSum(@Param("tableName") String tableName,@Param("orderField")String orderField, @Param("limitStart") int limitStart, @Param("limitEnd") int limitEnd);

    float queryInfoForAvg(@Param("tableName") String tableName,@Param("orderField")String orderField,@Param("field") String field, @Param("limitStart") int limitStart, @Param("limitEnd") int limitEnd);

    int updateInfo(@Param("tableName") String tableName, @Param("feild") String feild, @Param("feildValue") String feildValue, @Param("condition") String condition, @Param("conditionValue") String conditionValue);



    /////////////
    int addXueqiuStock(@Param("tableName") String tableName, @Param("itemStock") ItemStock itemStock);
    int addBinanceStock(@Param("tableName") String tableName, @Param("itemStock") Candlestick itemStock);

    List<ItemStock> queryInfoForLimit_symbol(@Param("tableName") String tableName,@Param("orderField")String orderField, @Param("limitStart") int limitStart, @Param("limitEnd") int limitEnd);
    List<Candlestick> queryInfoForLimit_coin(@Param("tableName") String tableName,@Param("orderField")String orderField, @Param("limitStart") int limitStart, @Param("limitEnd") int limitEnd);
    Account queryInfoFo_account();
}
