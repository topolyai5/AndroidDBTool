package com.topolyai.dbtool.expressions;

import java.util.List;

public class EmptyExpression implements Expression {

    @Override
    public String format() {
        return "";
    }

    @Override
    public void appendArgsValue(List<String> objectList) {
    }
}
