package com.chrisY.domain.quantification.Xueqiu;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-03-02 19:58
 **/
public class Data {

    XueqiuData data;
    int error_code;

    public XueqiuData getData() {
        return data;
    }

    public void setData(XueqiuData data) {
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
