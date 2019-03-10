package com.chrisY.service.quantification;

import java.util.HashMap;

/**
 * @description:买入条件
 * @author: Chris.Y
 * @create: 2019-03-03 19:00
 **/


public interface IBuyConditionService {

    public boolean buyCondition(String indexValue,HashMap<String, String> indexHash, HashMap<String, String> previousIndexHash);

}
