package com.chrisY.service.quantification;

/**
 * @description:控制逻辑
 * @author: Chris.Y
 * @create: 2019-03-02 09:46
 **/

public interface IQuantificationService {
    public String initQuantification(String symbol,String period,long begin,long end,boolean logContent,boolean summary);

}
