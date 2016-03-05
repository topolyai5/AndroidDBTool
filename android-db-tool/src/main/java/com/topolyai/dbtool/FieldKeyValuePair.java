package com.topolyai.dbtool;

import lombok.Getter;

@Getter
public class FieldKeyValuePair {

    private String key;
    private Object value;

    public FieldKeyValuePair(String key, Object value) {
        this.key = key;
        this.value = value;
    }

}
