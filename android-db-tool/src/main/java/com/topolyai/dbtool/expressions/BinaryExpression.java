package com.topolyai.dbtool.expressions;

import java.util.List;

public class BinaryExpression implements Expression {

    private Expression left;
    private Expression right;
    private Operand operand;

    public BinaryExpression(Expression left, Expression right, Operand operand) {
        this.left = left;
        this.right = right;
        this.operand = operand;
    }

    @Override
    public String format() {
        return String.format("(%s %s %s)", left.format(), operand.getValue(), right.format());
    }

    @Override
    public void appendArgsValue(List<String> objectList) {
        left.appendArgsValue(objectList);
        right.appendArgsValue(objectList);
    }
}
