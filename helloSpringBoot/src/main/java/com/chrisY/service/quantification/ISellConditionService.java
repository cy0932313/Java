package com.chrisY.service.quantification;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-03-03 19:00
 **/

public interface ISellConditionService {
    //cci卖出入条件
    public boolean cci(Double cciData,Double previouscciData);
    //macd卖出入条件
    public boolean macd(Double macdData,Double previousmacdData);

}
