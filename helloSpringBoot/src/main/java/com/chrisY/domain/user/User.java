package com.chrisY.domain.user;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-01-25 16:25
 * @description：人员信息类
 */

public class User {
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private String userName;
    private int age;

}
