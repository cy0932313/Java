package com.chris.quantification.domain;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-04-14 10:18
 **/
public class Tips {
    int oversold;

    public int getOversold() {
        return oversold;
    }

    public void setOversold(int oversold) {
        this.oversold = oversold;
    }

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public int getOverbought() {
        return overbought;
    }

    public void setOverbought(int overbought) {
        this.overbought = overbought;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    int normal;
    int overbought;
    String time;
}
