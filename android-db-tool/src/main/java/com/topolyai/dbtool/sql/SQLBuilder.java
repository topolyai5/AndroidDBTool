package com.topolyai.dbtool.sql;

import android.text.TextUtils;

import static java.lang.String.format;

public class SQLBuilder {

    private String function;
    private String[] projections;
    private String table;

    public static SQLBuilder selectWithAll(String table) {
        return select(table, "*");
    }

    public static SQLBuilder select(String table, String... projections) {
        SQLBuilder builder = new SQLBuilder();
        builder.function = "SELECT";
        builder.projections = projections;
        builder.table = table;
        return builder;
    }




    public String build() {

        StringBuilder stringBuilder = new StringBuilder(function);

        return stringBuilder.toString();
    }
}
