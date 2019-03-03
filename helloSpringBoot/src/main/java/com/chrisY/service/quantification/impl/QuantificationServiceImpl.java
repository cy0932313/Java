package com.chrisY.service.quantification.impl;

import com.chrisY.domain.quantification.Account;
import com.chrisY.domain.quantification.Xueqiu.Data;
import com.chrisY.domain.quantification.Xueqiu.XueqiuData;
import com.chrisY.service.quantification.IAccountService;
import com.chrisY.service.quantification.IBuyConditionService;
import com.chrisY.service.quantification.IQuantificationService;
import com.chrisY.service.quantification.ISellConditionService;
import com.chrisY.util.ChrisDateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-03-02 09:48
 **/
@Service
public class QuantificationServiceImpl implements IQuantificationService {
    private Data xueqiuDataSource;
    private Account account;
    @Autowired
    IAccountService iAccountService;
    @Autowired
    IBuyConditionService iBuyConditionService;
    @Autowired
    ISellConditionService iSellConditionService;

    @Override
    public String initQuantification(String dataSource) {
        //数据处理
        this.xueqiuDataSource = new Data();
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.xueqiuDataSource = mapper.readValue(dataSource, Data.class);
        } catch (Exception ex) {
            ex.getStackTrace();
        }

        //账号准备就绪
        this.account = iAccountService.initAccount("chris", 50000.00, 1);
        return this.run();
    }

    public String run() {
        XueqiuData data = this.xueqiuDataSource.getData();
        System.out.println(data.getItem().size());
        for (int i = 0; i < data.getItem().size(); i++) {
            //数据转换
            HashMap<String, String> indexHash, previousIndexHash = null;
            if (i > 0) {
                previousIndexHash = this.itemToIndex(data.getColumn(), data.getItem().get(i - 1));
            }
            indexHash = this.itemToIndex(data.getColumn(), data.getItem().get(i));
//            System.out.println(timestampToDateStr(String.valueOf (Long.parseLong(indexHash.get("timestamp"))/1000)) + ":" + indexHash.get("cci"));

            //T+1
            if (ChrisDateUtils.timeStamp2Date(this.account.getBuyDate(), "yyyy-MM-dd").equals(ChrisDateUtils.timeStamp2Date(String.valueOf (Long.parseLong(indexHash.get("timestamp"))/1000), "yyyy-MM-dd"))) {
                System.out.println("T+1限制，当天无法买入或者卖出,时间：" + ChrisDateUtils.timeStamp2Date(this.account.getBuyDate(), "yyyy-MM-dd"));
                continue;
            }

            this.handleData(indexHash, previousIndexHash);
        }
        return "Success";
    }

    public void handleData(HashMap<String, String> indexHash, HashMap<String, String> previousIndexHash) {
        //获取账户当前状态
        int status = this.account.getStatus();

        //持股状态中只需要判断卖出条件，则空仓状态中只需要判断买入条件
        boolean result = false;
        if (status == 1) {
            //CCI
            if (indexHash != null && previousIndexHash != null) {
                result = this.iBuyConditionService.cci(Double.parseDouble(indexHash.get("cci")), Double.parseDouble(previousIndexHash.get("cci")));
            }
            if (result) {
                System.out.println("触发交易：时间点"+ChrisDateUtils.timeStamp2Date(String.valueOf (Long.parseLong(indexHash.get("timestamp"))/1000),"yyyy-MM-dd HH")+"，CCI技术指标符合买入条件，上一个小时CCI数据：" + previousIndexHash.get("cci") + "，这个小时CCI数据：" + indexHash.get("cci"));
                this.handleAccount(Double.parseDouble(indexHash.get("close")), "buy", String.valueOf (Long.parseLong(indexHash.get("timestamp"))/1000));
            }
        } else {
            if (indexHash != null && previousIndexHash != null) {
                result = this.iSellConditionService.cci(Double.parseDouble(indexHash.get("cci")), Double.parseDouble(previousIndexHash.get("cci")));
            }
            if (result) {
                System.out.println("触发交易：时间点"+ChrisDateUtils.timeStamp2Date(String.valueOf (Long.parseLong(indexHash.get("timestamp"))/1000),"yyyy-MM-dd HH")+"，CCI技术指标符合买入条件，上一个小时CCI数据：" + previousIndexHash.get("cci") + "，这个小时CCI数据：" + indexHash.get("cci"));
                this.handleAccount(Double.parseDouble(indexHash.get("close")), "sell", String.valueOf (Long.parseLong(indexHash.get("timestamp"))/1000));
            }
        }
    }

    public void handleAccount(Double price, String type, String timestamp) {
        if (type == "buy") {
            this.account.setBuyDate(timestamp);
            this.iAccountService.buy(this.account, price);
        } else {
            this.iAccountService.sell(this.account, price);
        }
    }

    private HashMap itemToIndex(ArrayList column, ArrayList item) {
        HashMap<String, String> indexMap = new HashMap<>();
        for (int i = 0; i < column.size(); i++) {
            indexMap.put(column.get(i).toString(), item.get(i).toString());
        }

        return indexMap;
    }

    public static String timestampToDateStr(String timestamp) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(timestamp);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
