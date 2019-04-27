package com.chris.mechanization.web;

import com.chris.mechanization.dao.IOperateTableDao;
import com.chris.mechanization.domain.SymbolMonitor;
import com.chris.mechanization.enumType.MakeMoney;
import com.chris.mechanization.service.impl.CoinDataImpl;
import com.chris.mechanization.service.impl.SymbolDataImpl;
import com.chris.mechanization.service.impl.strategy.StrategyCCI;
import com.chris.mechanization.service.impl.strategy.StrategyMA;
import com.chris.mechanization.service.impl.strategy.StrategySH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
    private StrategyMA strategyMA;
    @Autowired
    private StrategySH strategySH;
    @Autowired
    private IOperateTableDao iOperateTableDao;

    @PostMapping("/downloadSymbol")

    public String downloadSymbol(String symbol,String period,String beginTime,String endTime,boolean update) {
        //        1230739200000
//        1555344000000
//        symbolData.saveSymbolData("SH600196", period,"1230739200000","1555344000000",true);
        List<SymbolMonitor> symbolMonitorList = iOperateTableDao.queryInfoForMonitorSymbol();
        int size = symbolMonitorList.size();
        for(int i = 0;i < size;i++)
        {
            SymbolMonitor symbolMonitor = symbolMonitorList.get(i);
            System.out.println(symbolData.saveSymbolData(symbolMonitor.getSymbolCode(), period,"1230739200000","1556294400000",true));
//            symbolData.saveSymbolData(symbol, period,beginTime,endTime,update)
        }

        return "success";
    }

    @PostMapping("/downloadCoin")
    public String coin(String symbol,String period,String beginTime,String endTime,boolean update) {
//        1230739200000
//        1555344000000
        String[] test = new String[]{"bchabcusdt","btcusdt","etcusdt","ethusdt","iostusdt","ltcusdt","trxusdt","xrpusdt","eosusdt"};
//
//        for(int i = 0;i < test.length;i++)
//        {
//            coinData.saveSymbolData(symbol,period,beginTime,endTime,update);
//        }
//        return "success";
        return coinData.saveSymbolData(symbol,period,beginTime,endTime,update);
    }

    @PostMapping("/backTestCCI_Symbol")
    public String backTestCCI() {
//        List<SymbolMonitor> symbolMonitorList = iOperateTableDao.queryInfoForMonitorSymbol();
//        MakeMoney makeMoney = MakeMoney.SYMBOL;
        List<SymbolMonitor> symbolMonitorList = this.getCoinList();
        MakeMoney makeMoney = MakeMoney.COIN;
        int size = symbolMonitorList.size();
        for(int i = 0;i < size;i++)
        {
            SymbolMonitor symbolMonitor = symbolMonitorList.get(i);
            strategyMA.setData(symbolMonitor.symbolCode,symbolMonitor.symbolName,"1d", makeMoney);
//            strategySH.setData(symbolMonitor.symbolCode,symbolMonitor.symbolName,"1day", makeMoney);
            System.out.println(symbolMonitor.symbolName+"完成");
        }
        return "";
    }

    private List<SymbolMonitor> getCoinList()
    {
        List<SymbolMonitor> symbolMonitorList= new ArrayList<>();
        String[] listStr = new String[]{"bchabcusdt","btcusdt","etcusdt","ethusdt","iostusdt","ltcusdt","trxusdt","xrpusdt","eosusdt","bnbusdt"};

//        String[] listStr = new String[]{"btcusdt"};
        for(int i = 0;i < listStr.length;i++)
        {
            SymbolMonitor symbolMonitor = new SymbolMonitor();
            symbolMonitor.setSymbolCode(listStr[i]);
            symbolMonitor.setSymbolName(listStr[i]);
            symbolMonitorList.add(symbolMonitor);
        }
        return  symbolMonitorList;
    }

}
