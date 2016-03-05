package com.topolyai.dbtool;

import android.content.ContentValues;

public class ContentValuesBuilder {

    private ContentValues contentValues = new ContentValues();

    public static ContentValuesBuilder builder() {
        return new ContentValuesBuilder();
    }

    public ContentValuesBuilder put(String key, String value) {
        contentValues.put(key, value);
        return this;
    }

    public ContentValuesBuilder put(String key, Byte value) {
        contentValues.put(key, value);
        return this;
    }

    public ContentValuesBuilder put(String key, Short value) {
        contentValues.put(key, value);
        return this;
    }

    public ContentValuesBuilder put(String key, Integer value) {
        contentValues.put(key, value);
        return this;
    }

    public ContentValuesBuilder put(String key, Long value) {
        contentValues.put(key, value);
        return this;
    }

    public ContentValuesBuilder put(String key, Double value) {
        contentValues.put(key, value);
        return this;
    }

    public ContentValuesBuilder put(String key, Float value) {
        contentValues.put(key, value);
        return this;
    }

    public ContentValuesBuilder put(String key, byte[] value) {
        contentValues.put(key, value);
        return this;
    }

    public ContentValuesBuilder putBoolean(String key, Boolean value) {
        if (value == null) {
            value = false;
        }
        contentValues.put(key, value);
        return this;
    }

    public ContentValuesBuilder putString(String key, String value) {
        contentValues.put(key, value);
        return this;
    }

    public ContentValuesBuilder putLong(String key, Long value) {
        if (value == null) {
            value = 0L;
        }
        contentValues.put(key, value);
        return this;
    }

    public ContentValuesBuilder putInteger(String key, Integer value) {
        if (value == null) {
            value = 0;
        }
        contentValues.put(key, value);
        return this;
    }

    public ContentValuesBuilder putDouble(String key, Double value) {
        if (value == null) {
            value = 0D;
        }
        contentValues.put(key, value);
        return this;
    }

    public ContentValuesBuilder putFloat(String key, Float value) {
        if (value == null) {
            value = 0F;
        }
        contentValues.put(key, value);
        return this;
    }

    public ContentValues build() {
        return contentValues;
    }


}
