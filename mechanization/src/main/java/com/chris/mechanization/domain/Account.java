package com.chris.mechanization.domain;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-04-08 17:45
 * @description：
 */

public class Account {
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String userId;
    private String status;
}
