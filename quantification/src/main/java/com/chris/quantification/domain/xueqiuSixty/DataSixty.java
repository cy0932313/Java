package com.chris.quantification.domain.xueqiuSixty;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-03-12 17:33
 * @description：
 */

public class DataSixty {
    SixtyContent sixtyContent;
    int error_code;

    public SixtyContent getData() {
        return sixtyContent;
    }

    public void setData(SixtyContent data) {
        this.sixtyContent = data;
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
