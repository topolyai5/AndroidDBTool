package com.topolyai.dbtool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.topolyai.dbtool.parser.SqlFileParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DbManager extends SQLiteOpenHelper {

    private Context context;
    private List<SQLVersion> updateSQLs;

    private DbManager(Context context, String name, int version) {
        super(context, name, null, version);
        this.context = context;
        DbLogger.init(true);
        init(context, name, version);
    }

    public static void create(Context context, String name, int version, List<SQLVersion> updateSQLs) {
        new DbManager(context, name, version, updateSQLs);
    }

    public static void create(Context context, String name, int version) {
        new DbManager(context, name, version);
    }


    private DbManager(Context context, String name, int version, List<SQLVersion> updateSQLs) {
        super(context, name, null, version);
        this.context = context;
        DbLogger.init(true);
        this.updateSQLs = updateSQLs;
        init(context, name, version);
    }

    private void init(Context context, String name, int version) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
        } finally {
            if (db != null) {
                db.close();
            }
            DatabaseProvider.create(context, name, version);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DbLogger.log("onCreate");
        for (SQLVersion updateSQL : updateSQLs) {
            loadFileAndExecuteScripts(db, updateSQL.getRawId());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DbLogger.log("onUpgrade: new: {}, old: {}", newVersion, oldVersion);
        for (SQLVersion updateSQL : updateSQLs) {
            if (oldVersion < updateSQL.getVersion()) {
                loadFileAndExecuteScripts(db, updateSQL.getRawId());
            }
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DbLogger.log("onDowngrade: new: {}, old: {}", newVersion, oldVersion);
    }

    private void loadFileAndExecuteScripts(SQLiteDatabase db, int fileResId) {
        InputStream open = null;
        try {
            open = context.getResources().openRawResource(fileResId);
            List<String> statements = SqlFileParser.statments(open);
            for (String statment : statements) {
                db.execSQL(statment);
                DbLogger.log("Success: {}", statment);
            }
        } catch (IOException e) {
            DbLogger.log("Sql [{}] execution is failed! Reason: {}", e.getMessage());
        } finally {
            if (open != null) {
                try {
                    open.close();
                } catch (IOException e) {
                    DbLogger.log("Input stream could not be closed. Reason: {}", e.getMessage());
                }
            }
        }
    }
}
