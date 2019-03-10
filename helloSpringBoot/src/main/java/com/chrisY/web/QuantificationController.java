package com.chrisY.web;

import com.chrisY.service.quantification.IQuantificationService;
import com.sun.xml.internal.fastinfoset.util.StringArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-02-28 14:47
 * @description：量化工具
 */

@RestController
@RequestMapping(value = "/quan")
public class QuantificationController {
    @Autowired
    IQuantificationService quantification;

    public static StringBuilder printLog = new StringBuilder();

    public static final Map<String, String> symbolMap_ = new HashMap<String, String>() {
        {
//            put("SH600196","复兴医药");
//            put("SH600436","片子癀");
//            put("SZ000963","华东医药");
//            put("SH600276","恒瑞医药");
//            put("SZ300015","爱尔眼科");
//            put("SH600518","康美药业");
//            put("SZ000538","云南白药");


            put("SZ159915", "创业板");
            put("SH510300", "沪深300");
            put("SZ000651", "格力电器");
            put("SH601318", "中国平安");
            put("SH601398", "工商银行");
            put("SZ000858", "五粮液");
            put("SH600030", "中信证券");
            put("SZ000333", "美的集团");

//            put("SZ300730","科创信息");
//            put("SZ300059","东方财富");
//            put("SH601890","亚星锚链");
//            put("SZ000725","京东方A");
//            put("SZ300468","四方精创");
//            put("SH600776","东方通信");
        }
    };

    @RequestMapping(value = "/60")
    public String get60mk() {
        StringBuilder str = new StringBuilder();

        for (String symbol : symbolMap_.keySet()) {
            str.append(quantification.initQuantification(symbol, "60m", false));
        }

        return str.toString();
    }

//    @RequestMapping(value = "/30")
//    public String get30mk() {
//        return quantification.initQuantification("30m");
//    }
//
    @RequestMapping(value = "/day")
    public String getDay() {
        StringBuilder str = new StringBuilder();

        for (String symbol : symbolMap_.keySet()) {
            str.append(quantification.initQuantification(symbol, "1day", false));
        }

        return str.toString();
    }

}
