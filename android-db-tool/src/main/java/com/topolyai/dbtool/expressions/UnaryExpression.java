package com.topolyai.dbtool.expressions;

import java.util.List;

public class UnaryExpression implements Expression {

    private Object value;

    public UnaryExpression(Object value) {
        this.value = value;
    }

    @Override
    public String format() {
        return "?";
    }

    @Override
    public void appendArgsValue(List<String> objectList) {
        if (value == null) {
            value = "null";
        }
        objectList.add(value.toString());
    }
}
