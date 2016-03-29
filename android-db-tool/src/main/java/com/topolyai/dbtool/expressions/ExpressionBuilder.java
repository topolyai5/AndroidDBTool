package com.topolyai.dbtool.expressions;

public class ExpressionBuilder {

    public static Expression and(Expression left, Expression right) {
        return new BinaryExpression(left, right, Operand.AND);
    }

    public static Expression or(Expression left, Expression right) {
        return new BinaryExpression(left, right, Operand.OR);
    }

    public static Expression un(Object value) {
        return new UnaryExpression(value);
    }

    public static Expression field(String field) {
        return new FieldExpression(field);
    }

    public static Expression isNull(String field) {
        return new IsNullExpression(field);
    }

    public static Expression gt(String field, Object value) {
        return new BinaryExpression(field(field), un(value), Operand.GT);
    }

    public static Expression gte(String field, Object value) {
        return new BinaryExpression(field(field), un(value), Operand.GTE);
    }

    public static Expression lt(String field, Object value) {
        return new BinaryExpression(field(field), un(value), Operand.LT);
    }

    public static Expression lte(String field, Object value) {
        return new BinaryExpression(field(field), un(value), Operand.LTE);
    }

    public static Expression eq(String field, Object value) {
        return new BinaryExpression(field(field), un(value), Operand.EQ);
    }


}
