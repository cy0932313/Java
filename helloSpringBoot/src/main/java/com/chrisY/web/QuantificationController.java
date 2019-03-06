package com.chrisY.web;

import com.chrisY.service.quantification.IQuantificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/60")
    public String get60mk() {
        return quantification.initQuantification("60m");
    }

    @RequestMapping(value = "/30")
    public String get30mk() {
        return quantification.initQuantification("30m");
    }

    @RequestMapping(value = "/day")
    public String getDay() {
        return quantification.initQuantification("1day");
    }

}
