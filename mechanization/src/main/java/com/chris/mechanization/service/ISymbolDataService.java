package com.chris.mechanization.service;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-04-01 11:40
 * @description：
 */

public interface ISymbolDataService {
    public String saveSymbolData(String symbolName,String period,String beginTime,String endTime,boolean update);
}
