package com.topolyai.dbtool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.topolyai.vlogger.Logger;


public class DatabaseProvider extends SQLiteOpenHelper {

    private static final Logger LOGGER = Logger.get(DatabaseProvider.class);

    private static DatabaseProvider instance;

    private DatabaseProvider(Context c, String name, int version) {
        super(c, name, null, version);
    }

    public static void create(Context c, String name, int version) {
        if (instance == null) {
            synchronized (LOGGER) {
                if (instance == null) {
                    instance = new DatabaseProvider(c, name, version);
                }
            }
        }
    }

    public static DatabaseProvider instance() {
        return instance;
    }

    public static SQLiteDatabase writableDatabase() {
        return instance.getWritableDatabase();
    }

    public static SQLiteDatabase readableDatabase() {
        return instance.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        throw new IllegalArgumentException("Should be init first! Use DBManager");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new IllegalArgumentException("Should be init first!");
    }



}
