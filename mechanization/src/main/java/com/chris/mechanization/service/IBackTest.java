package com.chris.mechanization.service;

import com.chris.mechanization.enumType.MakeMoney;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-04-08 17:13
 * @description：
 */

public interface IBackTest {
    public void setData(String symbolCode,String symbolName, String period, MakeMoney makeMoney);
    public String strategy();
}
