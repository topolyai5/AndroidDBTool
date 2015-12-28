package com.topolyai.dbtool.parser;

import android.text.TextUtils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlFileParser {

    public static List<String> statments(InputStream inputStream) throws IOException {
        SqlFileParser parser = new SqlFileParser();
        List<String> ret = new ArrayList<>();

        List<String> list = IOUtils.readLines(inputStream);
        StringBuilder sb = new StringBuilder();
        StatementType type = null;
        boolean terminated = true;
        for (String s : list) {
            if (!TextUtils.isEmpty(s)) {
                if (terminated) {
                    type = parser.type(s);
                    terminated = false;
                }
                sb.append(s.trim());
                if (s.endsWith(type.terminal())) {
                    ret.add(sb.toString());
                    sb.delete(0, sb.length() - 1);
                    terminated = true;
                }
            }
        }
        return ret;
    }

    private StatementType type(String s) {
        Pattern pattern = Pattern.compile("^[^\\s]*\\s");
        Matcher matcher = pattern.matcher(s);
        String statement = "";
        if (matcher.find()) {
            statement = matcher.group();
        }
        if (TextUtils.isEmpty(statement)) {
            return StatementType.COMMON;
        } else {
            return Enum.valueOf(StatementType.class, statement.trim().toUpperCase());
        }
    }
}
