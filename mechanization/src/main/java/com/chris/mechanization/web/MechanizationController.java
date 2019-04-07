package com.chris.mechanization.web;

import com.chris.mechanization.service.impl.CoinDataImpl;
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
    @Autowired
    private CoinDataImpl coinData;

    @PostMapping("/downloadSymbol")

    public String downloadSymbol(String symbol,String period,String beginTime,String endTime,boolean update) {
        return symbolData.saveSymbolData(symbol, period,beginTime,endTime,update);
    }

    @PostMapping("/downloadCoin")
    public String coin(String symbol,String period,String beginTime,String endTime,boolean update) {
//        1514736000000
        return coinData.saveSymbolData(symbol,period,beginTime,endTime,update);
    }

}
