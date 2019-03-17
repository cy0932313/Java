package com.chris.quantification.utils;

import com.chris.quantification.QuantificationApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: Chris.Y
 * @create: 2019-03-17 21:29
 **/
public class LogUtils {  /**
 * 将信息打印到自定义日志（my_info.log）中
 * @param message 需要被打印的信息
 */
public static void printLog(String message){
    Logger logger  =  LoggerFactory.getLogger(QuantificationApplication.class);

    StringBuffer logOut = new StringBuffer();
    logOut.append("\n");
    logOut.append(message);
    logOut.append("\n");

    logger.info(logOut.toString());
}

    /**
     * 将信息打印到自定义日志（system_error.log）中
     * @param e 异常信息
     */
    public static void printLog(Exception e, Class<?> clazz){
        Logger logger  =  LoggerFactory.getLogger(clazz);

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
