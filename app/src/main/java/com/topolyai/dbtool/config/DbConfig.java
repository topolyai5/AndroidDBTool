package com.topolyai.dbtool.config;

public class DbConfig {

    private boolean log;
    private LogLevel logLevel;

    public DbConfig(boolean log, LogLevel logLevel) {
        this.log = log;
        this.logLevel = logLevel;
    }

    public boolean isLog() {
        return log;
    }

    public void setLog(boolean log) {
        this.log = log;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public static DbConfig create() {
        return new DbConfig(true, LogLevel.INFO);
    }

    public static DbConfig create(boolean log, LogLevel logLevel) {
        return new DbConfig(log, logLevel);
    }
}
