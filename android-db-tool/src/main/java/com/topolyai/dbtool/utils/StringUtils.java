package com.topolyai.dbtool.utils;

public class StringUtils {

    public static String upperCaseAndUnderLineAtCapitals(String string) {
       return string.replaceAll("(.)([A-Z])", "$1_$2").toUpperCase();
    }
}
