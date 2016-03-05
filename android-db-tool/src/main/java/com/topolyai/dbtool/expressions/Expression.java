package com.topolyai.dbtool.expressions;

import java.util.List;

public interface Expression {
    String format();
    void appendArgsValue(List<String> objectList);
}
