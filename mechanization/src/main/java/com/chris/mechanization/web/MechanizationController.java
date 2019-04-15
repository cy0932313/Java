package com.chris.mechanization.web;

import com.chris.mechanization.dao.IOperateTableDao;
import com.chris.mechanization.domain.SymbolMonitor;
import com.chris.mechanization.enumType.MakeMoney;
import com.chris.mechanization.service.impl.CoinDataImpl;
import com.chris.mechanization.service.impl.SymbolDataImpl;
import com.chris.mechanization.service.impl.strategy.StrategyCCI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @Autowired
    private StrategyCCI strategyCCI;

    @Autowired
    private IOperateTableDao iOperateTableDao;

    @PostMapping("/downloadSymbol")

    public String downloadSymbol(String symbol,String period,String beginTime,String endTime,boolean update) {
        //        1230739200000
//        1555344000000

        List<SymbolMonitor> symbolMonitorList = iOperateTableDao.queryInfoForMonitorSymbol();
        int size = symbolMonitorList.size();
        for(int i = 0;i < size;i++)
        {
            SymbolMonitor symbolMonitor = symbolMonitorList.get(i);
            System.out.println(symbolData.saveSymbolData(symbolMonitor.getSymbolCode(), period,"1230739200000","1555344000000",true));
//            symbolData.saveSymbolData(symbol, period,beginTime,endTime,update)
        }

        return "success";
    }

    @PostMapping("/downloadCoin")
    public String coin(String symbol,String period,String beginTime,String endTime,boolean update) {
//        1230739200000
//        1555344000000
        return coinData.saveSymbolData(symbol,period,beginTime,endTime,update);
    }

    @PostMapping("/backTestCCI_Symbol")
    public String backTestCCI() {
        strategyCCI.setData("SH510050","1day", MakeMoney.SYMBOL);
        strategyCCI.strategy();
        return "";
    }

}
