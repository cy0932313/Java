package com.chrisY.service.quantification;

import java.util.HashMap;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-03-03 19:00
 **/

public interface ISellConditionService {

    public boolean sellCondition(String openPrice,String indexValue,HashMap<String, String> indexHash, HashMap<String, String> previousIndexHash);


}
