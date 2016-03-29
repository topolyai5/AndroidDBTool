package com.topolyai.dbtool.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WhereBuilder {

    private StringBuilder where = new StringBuilder();
    private List<String> args = new ArrayList<>();

    public static WhereBuilder create() {
        return new WhereBuilder();
    }

    public WhereBuilder and() {
        where.append(" and ");
        return this;
    }

    public WhereBuilder or() {
        where.append(" or ");
        return this;
    }

    public WhereBuilder obr() {
        where.append(" (");
        return this;
    }

    public WhereBuilder cbr() {
        where.append(")");
        return this;
    }

    public WhereBuilder eq(String field, Object value) {
        internal(field, value, Operand.EQ);
        return this;
    }

    public WhereBuilder lt(String field, Object value) {
        internal(field, value, Operand.LT);
        return this;
    }

    public WhereBuilder lte(String field, Object value) {
        internal(field, value, Operand.LTE);
        return this;
    }

    public WhereBuilder gt(String field, Object value) {
        internal(field, value, Operand.GT);
        return this;
    }

    public WhereBuilder gte(String field, Object value) {
        internal(field, value, Operand.GTE);
        return this;
    }

    public WhereBuilder eqFields(String fieldLeft, String fieldRight) {
        where.append(fieldLeft).append(Operand.EQ.getValue()).append(fieldRight);
        return this;
    }

    public WhereBuilder isNull(String field) {
        where.append(field).append("is null");
        return this;
    }

    public String expression() {
        return where.toString();
    }

    public String[] args() {
        return args.toArray(new String[args.size()]);
    }

    private void internal(String field, Object value, Operand operand) {
        where.append(field).append(operand.getValue()).append("?");
        args.add(String.valueOf(value));
    }

}
