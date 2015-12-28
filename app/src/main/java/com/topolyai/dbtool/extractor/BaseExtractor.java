package com.topolyai.dbtool.extractor;

import android.database.Cursor;

public abstract class BaseExtractor<T> implements Extractor<T> {
    protected Long getLong(Cursor cursor, String name) {
        return cursor.getLong(cursor.getColumnIndex(name));
    }

    protected Double getDouble(Cursor cursor, String name) {
        return cursor.getDouble(cursor.getColumnIndex(name));
    }

    protected String getString(Cursor cursor, String name) {
        return cursor.getString(cursor.getColumnIndex(name));
    }

    protected Integer getInt(Cursor cursor, String name) {
        return cursor.getInt(cursor.getColumnIndex(name));
    }

    protected Boolean getBoolean(Cursor cursor, String name) {
        return cursor.getInt(cursor.getColumnIndex(name)) == 1;
    }
}
