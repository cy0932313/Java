package com.chrisY.service.quantification.impl;

import com.chrisY.domain.quantification.Account;
import com.chrisY.domain.quantification.Xueqiu.kline.Data;
import com.chrisY.domain.quantification.Xueqiu.kline.XueqiuDataKline;
import com.chrisY.domain.quantification.Xueqiu.stocklist.ItemStock;
import com.chrisY.domain.quantification.Xueqiu.stocklist.XueqiuDataStocklist;
import com.chrisY.service.quantification.*;
import com.chrisY.service.quantification.http.HttpClient;
import com.chrisY.util.ChrisDateUtils;
import com.chrisY.web.QuantificationController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.chrisY.web.QuantificationController.printLog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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
    private String begin = "1314201600000";
    private String end = "1314374400000";

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
    @Autowired
    IEmailService iEmailService;


    public String sendRequest(String symbol, String period) {
        String baseUrl;
        if (period.equals("1day")) {
            baseUrl = "https://xueqiu.com/stock/forchartk/stocklist.json?";
            String type = "after";
            baseUrl = baseUrl + "symbol=" + symbol + "&begin=" + begin + "&end=" + end + "&period=" + period + "&type=" + type;
        } else {
            baseUrl = "https://stock.xueqiu.com/v5/stock/chart/kline.json?";
            String count = "500000";
            String indicator = "kline,ma,macd,kdj,boll,rsi,wr,bias,cci,psy";
            baseUrl = baseUrl + "symbol=" + symbol + "&begin=" + begin + "&period=" + period + "&count=" + count + "&indicator=" + indicator;
        }
        return baseUrl;
    }

    @Override
    public String initQuantification(String symbol, String period, boolean logContent) {
        String dataSource = httpClient.client(sendRequest(symbol, period));

        //账号准备就绪
//        this.account = iAccountService.initAccount("chris", 50000.00, false,true,0.33);
        this.account = iAccountService.initAccount("chris", 50000.00, false);
        printLog.delete(0, printLog.length());

        //数据处理
        this.xueqiuDataSource = new Data();
        this.xueqiuDataStocklist = new XueqiuDataStocklist();
        ObjectMapper mapper = new ObjectMapper();
        double stratPrice = 0.00;
        double endPrice = 0.00;
        try {
            if (period.equals("1day")) {
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

        StringBuilder resultStr = this.calculation(symbol, stratPrice, endPrice);
        if (logContent) {
            printLog.append(resultStr);
        } else {
            return resultStr.toString();
        }
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

            this.handleData(indexHash, previousIndexHash);
        }
    }

    public void handleData(HashMap<String, String> indexHash, HashMap<String, String> previousIndexHash) {
        //获取账户当前状态
        boolean status = this.account.getAccountStatus();
        boolean buyResult = false;
        boolean sellResult = false;

        if (indexHash != null && previousIndexHash != null) {
            sellResult = this.iSellConditionService.sellCondition("cci",indexHash, previousIndexHash);
            buyResult = this.iBuyConditionService.buyCondition("cci",indexHash, previousIndexHash);
        }

        if (buyResult) {
            this.handleAccount(indexHash, previousIndexHash, "buy");
        }

        if (sellResult) {
            this.handleAccount(indexHash, previousIndexHash, "sell");
        }
    }

    public void handleAccount(HashMap<String, String> indexHash, HashMap<String, String> previousIndexHash, String type) {
        double price = Double.parseDouble(indexHash.get("close"));
        String timestamp = String.valueOf(Long.parseLong(indexHash.get("timestamp")) / 1000);
        if (type == "buy") {
            if (this.account.getMoney() < price * 100) {
                return;
            }
            this.account.setBuyDate(timestamp);
            this.iAccountService.buy(this.account, price, printLog);
        } else {
            //T+1
            if (ChrisDateUtils.timeStamp2Date(this.account.getBuyDate(), "yyyy-MM-dd").equals(ChrisDateUtils.timeStamp2Date(timestamp, "yyyy-MM-dd"))) {
                printLog.append("T+1限制，当天无法卖出入,时间：" + ChrisDateUtils.timeStamp2Date(this.account.getBuyDate(), "yyyy-MM-dd"));
                printLog.append("<br />");
                return;
            }
            if (this.account.getShareNumber() == 0) {
                return;
            }
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

    public StringBuilder calculation(String symbol, double stratPrice, double endPrice) {
        StringBuilder resultString = new StringBuilder();

        String symbolName = QuantificationController.symbolMap_.get(symbol);
        resultString.append("股票名称：<font color=\"blue\">" + symbolName);
        resultString.append("</font></br>");

        double a = 0;
        if (!this.account.getAccountStatus()) {
            a =  (this.account.getMoney() / this.account.getInitMoney() - 1) * 100;
            resultString.append("本策略结果为：<font color=\"red\">" + new BigDecimal(a).setScale(2, RoundingMode.UP) + "%</font>，盈亏总额：" + (this.account.getMoney() - this.account.getInitMoney()));
        } else {
            a = ((this.account.getMoney() + this.account.getShareNumber() * endPrice) / this.account.getInitMoney() - 1) * 100;
            resultString.append("本策略结果为：<font color=\"red\">" + new BigDecimal(a).setScale(2, RoundingMode.UP) + "%</font>，盈亏总额：" + ((this.account.getMoney() + this.account.getShareNumber() * endPrice) - this.account.getInitMoney()));
        }
        resultString.append("<br />");
        DecimalFormat df = new DecimalFormat("0.00");
        resultString.append("成功率"+ Float.parseFloat(df.format((float)this.account.getSucessNum() / (this.account.getSucessNum() + this.account.getFailNum()))) * 100 +"%" );
        resultString.append("<br />");
        double b = (endPrice / stratPrice - 1) * 100;
        resultString.append("本次回测区间涨幅为<font color=\"green\">" + new BigDecimal(b).setScale(2, RoundingMode.UP) + "%</font>");
        resultString.append("<br />");
        if(a > b)
        {
            resultString.append("策略赢了拿着不动<font size=\"30px\" color=\"red\">" +new BigDecimal(a -b).setScale(2, RoundingMode.UP)+"%</font>");
        }
        else
        {
            resultString.append("策略输了拿着不动<font size=\"30px\" color=\"green\">" +new BigDecimal(a -b).setScale(2, RoundingMode.UP)+"%</font>");
        }

        resultString.append("<br />");
        resultString.append("<br />");

        return resultString;
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
