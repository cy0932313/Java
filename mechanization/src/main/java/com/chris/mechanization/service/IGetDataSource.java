package com.chris.mechanization.service;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-04-01 10:23
 * @description：
 */

public interface IGetDataSource {
    public void setParameter(HashMap<String,String> hashParameter);

    public void getDataSoruce();

    public ArrayList getHandleDataResult();
}
