package com.chrisY.service.quantification.impl;

import com.chrisY.service.quantification.IBuyConditionService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-03-03 19:01
 **/
@Service
public class BuyServiceImpl implements IBuyConditionService {
    @Override
    public boolean cci(Double cciData,Double previouscciData)
    {
        if(cciData > -100 && previouscciData < -100)
        {
            return true;
        }
        return false;
    }
}
