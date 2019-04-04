package com.chris.mechanization.web;

import com.chris.mechanization.service.impl.SymbolDataImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：ChrisY
 * @date ：Created in 2019-04-01 11:34
 * @description：
 */


@RestController
@RequestMapping(value = "/Mechanization")

public class MechanizationController {

    @Autowired
    private SymbolDataImpl symbolData;

    @PostMapping("/downloadSymbol")

    public String downloadSymbol(String symbol,String beginTime,String endTime,boolean update) {
        return symbolData.saveSymbolData(symbol,beginTime,endTime,update);
    }
}
