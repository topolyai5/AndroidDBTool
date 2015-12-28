package com.topolyai.dbtool.extractor;

import android.database.Cursor;

public interface Extractor<T> {
    T extract(Cursor cursor);
}
