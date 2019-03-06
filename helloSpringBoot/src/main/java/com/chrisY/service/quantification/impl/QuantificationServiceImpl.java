package com.chrisY.service.quantification.impl;

import com.chrisY.domain.quantification.Account;
import com.chrisY.domain.quantification.Xueqiu.kline.Data;
import com.chrisY.domain.quantification.Xueqiu.kline.XueqiuDataKline;
import com.chrisY.domain.quantification.Xueqiu.stocklist.ItemStock;
import com.chrisY.domain.quantification.Xueqiu.stocklist.XueqiuDataStocklist;
import com.chrisY.service.quantification.IAccountService;
import com.chrisY.service.quantification.IBuyConditionService;
import com.chrisY.service.quantification.IQuantificationService;
import com.chrisY.service.quantification.ISellConditionService;
import com.chrisY.service.quantification.http.HttpClient;
import com.chrisY.util.ChrisDateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    //格力 SZ000651  //复兴 SH600196
    //京东方A SZ000725   //恒瑞 SH600276
    //四方 SZ300468  //创业板 SH159915
    private String symbol = "SH600196";
    private String begin = "1314201600000";
    private String end = "1314374400000";
    private String period;

    private Data xueqiuDataSource;
    private XueqiuDataStocklist xueqiuDataStocklist;
    private Account account;

    @Autowired
    HttpClient httpClient;
    @Autowired
    IAccountService iAccountService;
    @Autowired
    IBuyConditionService iBuyConditionService;
    @Autowired
    ISellConditionService iSellConditionService;

    //输出
    StringBuilder printLog = new StringBuilder();

    public String sendRequest(String period) {
        String baseUrl;
        this.period = period;
        if (period.equals("1day"))
        {
            baseUrl = "https://xueqiu.com/stock/forchartk/stocklist.json?";
            String type = "after";
            baseUrl = baseUrl + "symbol=" + symbol + "&begin=" + begin + "&end=" + end + "&period=" + period + "&type=" + type;
        } else
        {
            baseUrl = "https://stock.xueqiu.com/v5/stock/chart/kline.json?";
            String count = "500000";
            String indicator = "kline,ma,macd,kdj,boll,rsi,wr,bias,cci,psy";
            baseUrl = baseUrl + "symbol=" + symbol + "&begin=" + begin + "&period=" + period + "&count=" + count + "&indicator=" + indicator;
        }
        return baseUrl;
    }

    @Override
    public String initQuantification(String period) {
        String dataSource = httpClient.client(sendRequest(period));

        //账号准备就绪
        this.account = iAccountService.initAccount("chris", 50000.00, 1);
        printLog.delete(0, printLog.length());

        //数据处理
        this.xueqiuDataSource = new Data();
        this.xueqiuDataStocklist = new XueqiuDataStocklist();
        ObjectMapper mapper = new ObjectMapper();
        double stratPrice = 0.00;
        double endPrice = 0.00;
        try {
            if (this.period.equals("1day")) {
                this.xueqiuDataStocklist = mapper.readValue(dataSource, XueqiuDataStocklist.class);
                this.stockList();
            } else {
                this.xueqiuDataSource = mapper.readValue(dataSource, Data.class);
                stratPrice = Double.parseDouble(this.xueqiuDataSource.getData().getItem().get(0).get(5));
                endPrice = Double.parseDouble(this.xueqiuDataSource.getData().getItem().get(this.xueqiuDataSource.getData().getItem().size() - 1).get(5));
                this.kLine();
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }

        this.calculation(stratPrice, endPrice);

        return printLog.toString();
    }

    public void stockList() {
        for (int i = 0; i < this.xueqiuDataStocklist.getChartlist().size(); i++) {
            //数据转换
            ItemStock indexStock, previousStock = null;
            if (i > 0) {
                previousStock = this.xueqiuDataStocklist.getChartlist().get(i - 1);
            }
            indexStock = this.xueqiuDataStocklist.getChartlist().get(i);

            //T+1
//            if (ChrisDateUtils.timeStamp2Date(this.account.getBuyDate(), "yyyy-MM-dd").equals(ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd"))) {
//                printLog.append("T+1限制，当天无法买入或者卖出,时间：" + ChrisDateUtils.timeStamp2Date(this.account.getBuyDate(), "yyyy-MM-dd"));
//                printLog.append("<br />");
//                System.out.println("T+1限制，当天无法买入或者卖出,时间：" + ChrisDateUtils.timeStamp2Date(this.account.getBuyDate(), "yyyy-MM-dd"));
//                continue;
//            }

//            this.handleData(indexStock, previousStock);
        }
    }

    public void kLine() {
        XueqiuDataKline data = this.xueqiuDataSource.getData();

        for (int i = 0; i < data.getItem().size(); i++) {
            //数据转换
            HashMap<String, String> indexHash, previousIndexHash = null;
            if (i > 0) {
                previousIndexHash = this.itemToIndex(data.getColumn(), data.getItem().get(i - 1));
            }
            indexHash = this.itemToIndex(data.getColumn(), data.getItem().get(i));

            //T+1
            if (ChrisDateUtils.timeStamp2Date(this.account.getBuyDate(), "yyyy-MM-dd").equals(ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd"))) {
                printLog.append("T+1限制，当天无法买入或者卖出,时间：" + ChrisDateUtils.timeStamp2Date(this.account.getBuyDate(), "yyyy-MM-dd"));
                printLog.append("<br />");
                System.out.println("T+1限制，当天无法买入或者卖出,时间：" + ChrisDateUtils.timeStamp2Date(this.account.getBuyDate(), "yyyy-MM-dd"));
                continue;
            }

            this.handleData(indexHash, previousIndexHash);
        }

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
//                result = this.iBuyConditionService.macd(Double.parseDouble(indexHash.get("macd")), Double.parseDouble(previousIndexHash.get("macd")));
            }
            if (result) {
                printLog.append("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "，CCI技术指标符合买入条件，上一个小时CCI数据：" + previousIndexHash.get("cci") + "，这个小时CCI数据：" + indexHash.get("cci"));
                printLog.append("<br />");
                System.out.println("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "，CCI技术指标符合买入条件，上一个小时CCI数据：" + previousIndexHash.get("cci") + "，这个小时CCI数据：" + indexHash.get("cci"));
                this.handleAccount(Double.parseDouble(indexHash.get("close")), "buy", String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000));
            }
        } else {
            if (indexHash != null && previousIndexHash != null) {
                result = this.iSellConditionService.cci(Double.parseDouble(indexHash.get("cci")), Double.parseDouble(previousIndexHash.get("cci")));
//                result = this.iSellConditionService.macd(Double.parseDouble(indexHash.get("macd")), Double.parseDouble(previousIndexHash.get("macd")));
            }
            if (result) {
                printLog.append("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "，CCI技术指标符合卖出条件，上一个小时CCI数据：" + previousIndexHash.get("cci") + "，这个小时CCI数据：" + indexHash.get("cci"));
                printLog.append("<br />");
                System.out.println("触发交易：时间点" + ChrisDateUtils.timeStamp2Date(String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000), "yyyy-MM-dd HH:mm:ss") + "，CCI技术指标符合卖出条件，上一个小时CCI数据：" + previousIndexHash.get("cci") + "，这个小时CCI数据：" + indexHash.get("cci"));
                this.handleAccount(Double.parseDouble(indexHash.get("close")), "sell", String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000));
            }
        }
    }

    public void handleAccount(Double price, String type, String timestamp) {
        if (type == "buy") {
            this.account.setBuyDate(timestamp);
            this.iAccountService.buy(this.account, price, printLog);
        } else {
            this.iAccountService.sell(this.account, price, printLog);
        }
    }

    private HashMap itemToIndex(ArrayList column, ArrayList item) {
        HashMap<String, String> indexMap = new HashMap<>();
        for (int i = 0; i < column.size(); i++) {
            if (item.get(i) == null) {
                indexMap.put(column.get(i).toString(), "");
            } else {
                indexMap.put(column.get(i).toString(), item.get(i).toString());
            }
        }

        return indexMap;
    }

    public void calculation(double stratPrice, double endPrice) {
        if (this.account.getStatus() == 1) {
            printLog.append("本策略结果为：" + new BigDecimal((this.account.getMoney() / this.account.getInitMoney() - 1) * 100).setScale(2, RoundingMode.UP) + "%");
        } else {
            printLog.append("本策略结果为：" + new BigDecimal(((this.account.getMoney() + this.account.getShareNumber() * endPrice) / this.account.getInitMoney() - 1) * 100).setScale(2, RoundingMode.UP) + "%");
        }
        printLog.append("<br />");
        printLog.append("本次回测区间涨幅为" + new BigDecimal((endPrice / stratPrice - 1) * 100).setScale(2, RoundingMode.UP) + "%");
        printLog.append("<br />");
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
