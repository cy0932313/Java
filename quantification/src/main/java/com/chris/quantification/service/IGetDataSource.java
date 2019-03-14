package com.chris.quantification.service;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-03-12 18:10
 * @description：
 */

public interface IGetDataSource {
    public void setParameter(HashMap<String,String> hashParameter);

    public void getDataSoruce();

    public ArrayList<HashMap<String,String>> getHandleDataResult();
}
