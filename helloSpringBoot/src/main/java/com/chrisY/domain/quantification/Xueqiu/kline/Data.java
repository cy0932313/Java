package com.chrisY.domain.quantification.Xueqiu.kline;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-03-02 19:58
 **/
public class Data {

    XueqiuDataKline data;
    int error_code;

    public XueqiuDataKline getData() {
        return data;
    }

    public void setData(XueqiuDataKline data) {
        this.data = data;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    String error_description;
}
