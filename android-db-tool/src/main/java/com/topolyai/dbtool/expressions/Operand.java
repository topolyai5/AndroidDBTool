package com.topolyai.dbtool.expressions;

import lombok.Getter;

public enum Operand {

    AND("AND"), OR("OR"), GT(">"), GTE(">="), LT("<"), LTE("<="), EQ("="), IS("is");

    @Getter
    private String value;

    Operand(String value) {
        this.value = value;
    }
}
