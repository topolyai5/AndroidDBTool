package com.topolyai.dbtool.expressions;

import com.topolyai.dbtool.FieldKeyValuePair;

import java.util.List;

public class UnaryExpression implements Expression {

    private FieldKeyValuePair fieldKeyValuePair;

    public UnaryExpression(FieldKeyValuePair fieldKeyValuePair) {
        this.fieldKeyValuePair = fieldKeyValuePair;
    }

    @Override
    public String format() {
        return fieldKeyValuePair.getKey() + " = ?";
    }

    @Override
    public void appendArgsValue(List<String> objectList) {
        objectList.add(fieldKeyValuePair.getValue().toString());
    }
}
