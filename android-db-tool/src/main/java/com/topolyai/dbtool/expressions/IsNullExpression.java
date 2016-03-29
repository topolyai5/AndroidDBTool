package com.topolyai.dbtool.expressions;

import java.util.List;

public class IsNullExpression implements Expression {

    private String field;

    public IsNullExpression(String field) {
        this.field = field;
    }

    @Override
    public String format() {
        return String.format("%s is null", field);
    }

    @Override
    public void appendArgsValue(List<String> objectList) {

    }
}
