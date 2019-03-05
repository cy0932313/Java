package com.chrisY.service.quantification;

import com.chrisY.domain.quantification.Account;

/**
 * @description: 账户管理
 * @author: Chris.Y
 * @create: 2019-03-02 09:31
 **/

public interface IAccountService {
    /**
    * @Description: 初始化一个账户
    * @Param: [accountId  账号ID, money  初始化本金]
    * @return: void
    * @Author: Chris.Y
    * @Date: 2019/3/2
    */
    public Account initAccount(String accountId, Double money,int status);

    public void buy(Account account,Double price,StringBuilder printLog);

    public void sell(Account account,Double price,StringBuilder printLog);


}
