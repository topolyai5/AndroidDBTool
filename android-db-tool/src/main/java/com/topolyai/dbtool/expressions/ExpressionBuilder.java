package com.topolyai.dbtool.expressions;

import com.topolyai.dbtool.FieldKeyValuePair;

public class ExpressionBuilder {


    public static Expression and(FieldKeyValuePair left, FieldKeyValuePair right) {
        return new BinaryExpression(new UnaryExpression(left), new UnaryExpression(right), Operand.AND);
    }

    public static Expression and(Expression left, Expression right) {
        return new BinaryExpression(left, right, Operand.AND);
    }

    public static Expression or(FieldKeyValuePair left, FieldKeyValuePair right) {
        return new BinaryExpression(new UnaryExpression(left), new UnaryExpression(right), Operand.OR);
    }

    public static Expression or(Expression left, Expression right) {
        return new BinaryExpression(left, right, Operand.OR);
    }

}
