package com.util.common;

import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-2-16
 * Time: 11:02:45
 * Copyright Dsl.Worgsoft.
 */
public class LogWriter {

//    static Mongo logDB = MongoUtils.getMongo(MongoDbIndex.log_db);

    public static void writeError(Class c, String log) {
        Logger logger = Logger.getLogger(c);
        logger.error(log);
    }

    public static void writeInfo(LoggerType logger, String log) {

        Logger.getLogger(logger.getCode()).info(log);
    }

    public static void writeLog(LoggerType logger, LogType level, LogMessage message, String content) {

        Log log = new Log();
        log.setType(logger.getCode());
        log.setCode(message.getCode());
        log.setLevel(level.getCode());
        log.setName(message.getName());
        log.setContent(content);
        log.setCreateDate(DateTimeFormatter.formatDate(new Date()));

//        logDB.insert(logger.getCode(), log);


    }

    public static void exception(LoggerType logger, Exception e) {
        exception(logger, LogMessage.UNEXPECTED_ERROR, e);
    }

    public static void exception(LoggerType logger, LogMessage detail, Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.close();
        pw.flush();

        error(logger, detail, sw.toString());

    }

    public static void debug(LoggerType logger, LogMessage detail, String data) {
        writeLog(logger, LogType.Debug, detail, data);
    }

    public static void info(LoggerType logger, LogMessage detail, String data) {
        writeLog(logger, LogType.Info, detail, data);
    }

    public static void warn(LoggerType logger, LogMessage detail, String data) {
        writeLog(logger, LogType.Warning, detail, data);
    }

    public static void error(LoggerType logger, LogMessage detail, String data) {
        writeLog(logger, LogType.Error, detail, data);
    }

    public static void main(String[] args) {
        try {
            int i = 10 / 0;
        } catch (Exception e) {
            LogWriter.exception(LoggerType.WMS, e);
        }
    }


}

