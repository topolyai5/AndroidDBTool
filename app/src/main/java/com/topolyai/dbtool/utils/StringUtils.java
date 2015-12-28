package com.topolyai.dbtool.utils;

public class StringUtils {

    public static String UpperCaseAndUnderLineAtCapitals(String string) {
       return string.replaceAll("(.)([A-Z])", "$1_$2").toUpperCase();
    }
}
