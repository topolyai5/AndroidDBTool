package com.topolyai.dbtool.expressions;

import lombok.Getter;

public enum Operand {

    AND("AND"), OR("OR");

    @Getter
    private String value;

    Operand(String value) {
        this.value = value;
    }
}
