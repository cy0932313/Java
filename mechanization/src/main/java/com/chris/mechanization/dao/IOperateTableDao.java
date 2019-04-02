package com.chris.mechanization.dao;


import org.apache.ibatis.annotations.Param;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-04-02 10:36
 * @description：
 */

public interface IOperateTableDao {
    int existTable(@Param("tableName")String tableName);

    void dropTable(@Param("tableName") String tableName);

    void trancateTable(@Param("tableName") String tableName);

    void createNewTable(@Param("tableName")String tableName);
}
