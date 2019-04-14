package com.chris.quantification.service;

import com.chris.quantification.enumType.TipsEnum;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-03-14 14:40
 * @description：
 */

public interface IStrategyCenter {
    public boolean buyCondition();
    public TipsEnum sellCondition();
}
