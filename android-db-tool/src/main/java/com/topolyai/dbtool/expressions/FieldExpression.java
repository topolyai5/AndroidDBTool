package com.topolyai.dbtool.expressions;

import java.util.List;

public class FieldExpression implements Expression {

    private String field;

    public FieldExpression(String field) {
        this.field = field;
    }

    @Override
    public String format() {
        return field;
    }

    @Override
    public void appendArgsValue(List<String> objectList) {

    }
}
