package com.topolyai.dbtool;

import android.content.ContentValues;
import android.database.Cursor;

public interface Entity extends Identifiable {

    ContentValues contentValues();
}
