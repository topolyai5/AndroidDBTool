package com.topolyai.dbtool.parser;

public enum StatementType {
    CREATE(");"), INSERT(";"), ALTER(";"), COMMON(";"), UPDATE(";");

    private String terminal;

    StatementType(String terminal) {
        this.terminal = terminal;
    }

    public String terminal() {
        return terminal;
    }
}
