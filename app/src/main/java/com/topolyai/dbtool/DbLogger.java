package com.topolyai.dbtool;

import com.topolyai.dbtool.config.DbConfig;
import com.topolyai.dbtool.config.LogLevel;
import com.topolyai.vlogger.Logger;

public class DbLogger {

    private static final Logger LOGGER = Logger.get(DbLogger.class);

    private static boolean log;
    private static LogLevel logLevel;

    public static void init(boolean log, LogLevel logLevel) {
        DbLogger.log = log;
        DbLogger.logLevel = logLevel;
    }
    public static void init(boolean log) {
        DbLogger.log = log;
    }


    public static void log(String msg, Object... args) {
        if (log) {
            LOGGER.i(msg, args);
        }
    }
}
