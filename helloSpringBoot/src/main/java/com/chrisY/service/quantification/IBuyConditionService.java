package com.chrisY.service.quantification;

/**
 * @description:买入条件
 * @author: Chris.Y
 * @create: 2019-03-03 19:00
 **/


public interface IBuyConditionService {

    //cci指标买入条件
    public boolean cci(Double cciData,Double previouscciData);

    //macd买入条件
    public boolean macd(Double macdData,Double previousmacdData);

}
