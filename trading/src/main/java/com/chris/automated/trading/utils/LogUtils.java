package com.chris.automated.trading.utils;

import com.chris.automated.trading.TradingApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-03-17 21:29
 **/
public class LogUtils {
    /**
     * @param message 需要被打印的信息
     */
    public static void printLog(String message) {
        Logger logger = LoggerFactory.getLogger(TradingApplication.class);

        StringBuffer logOut = new StringBuffer();
        logOut.append("\n");
        logOut.append(message);
        logOut.append("\n");

        logger.info(logOut.toString());
    }

    /**
     * @param e 异常信息
     */
    public static void printLog(Exception e, Class<?> clazz) {
        Logger logger = LoggerFactory.getLogger(clazz);

        StringBuffer logOut = new StringBuffer();
        logOut.append("\n");
        logOut.append(e.toString());
        logOut.append("\n");

        StackTraceElement[] errorList = e.getStackTrace();
        for (StackTraceElement stackTraceElement : errorList) {
            logOut.append(stackTraceElement.toString());
            logOut.append("\n");
        }

        logOut.append("\n");
        logOut.append("\n");

        logger.error(logOut.toString());
    }
}
