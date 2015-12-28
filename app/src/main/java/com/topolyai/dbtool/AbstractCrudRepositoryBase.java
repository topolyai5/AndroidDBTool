package com.topolyai.dbtool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.topolyai.dbtool.extractor.Extractor;
import com.topolyai.dbtool.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public abstract class AbstractCrudRepositoryBase<T extends Identifiable> {

    protected String tableName;

    public AbstractCrudRepositoryBase(Class<T> clss) {
        this.tableName = StringUtils.UpperCaseAndUnderLineAtCapitals(clss.getSimpleName());
    }

    protected abstract Extractor<T> getExtractor();

    public List<T> findAll() {
        SQLiteDatabase db = DatabaseProvider.instance().getReadableDatabase();
        Cursor cursor = db.rawQuery(format("SELECT * FROM %s", tableName), null);
        List<T> ret = new ArrayList<>();
        Extractor<T> extractor = getExtractor();
        while (cursor.moveToNext()) {
            ret.add(extractor.extract(cursor));
        }
        return ret;
    }

    public List<T> findById(long id) {
        SQLiteDatabase db = DatabaseProvider.readableDatabase();
        Cursor cursor = db.rawQuery(format("SELECT * FROM %s WHERE id = %s", tableName, id), null);
        List<T> ret = new ArrayList<>();
        Extractor<T> extractor = getExtractor();
        while (cursor.moveToNext()) {
            ret.add(extractor.extract(cursor));
        }
        return ret;
    }

    protected void executeBatch(List<String> sqls) {
        SQLiteDatabase db = DatabaseProvider.instance().getWritableDatabase();
        db.beginTransaction();
        for (String string : sqls) {
            DbLogger.log("Executed: {}", string);
            db.execSQL(string);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public int update(ContentValues contentValues) {
        SQLiteDatabase db = null;
        try {
            db = DatabaseProvider.instance().getWritableDatabase();
            db.beginTransaction();
            int update = db.update(tableName, contentValues, null, null);
            db.setTransactionSuccessful();
            return update;
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public abstract ContentValues createContentValues(T entity);
    public abstract ContentValues createContentValuesWithoutNullValues(T entity);

    public int update(T entity) {
        return update(createContentValues(entity));
    }

    public int delete(long id) {
        SQLiteDatabase db = DatabaseProvider.writableDatabase();
        try {
            db.beginTransaction();
            int deleted = db.delete(tableName, "id = ?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
            return deleted;
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public T insert(T obj) {
        SQLiteDatabase db = DatabaseProvider.writableDatabase();
        try {
            db.beginTransaction();
            long id = db.insert(tableName, null, createContentValuesWithoutNullValues(obj));
            obj.setId(id);
            db.setTransactionSuccessful();
            return obj;
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public List<T> insertAll(List<T> all) {
        SQLiteDatabase db = DatabaseProvider.writableDatabase();
        try {
            db.beginTransaction();
            for (T t : all) {
                long id = db.insert(tableName, null, createContentValuesWithoutNullValues(t));
                t.setId(id);
            }
            db.setTransactionSuccessful();
            return all;
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public int count() {
        Cursor cursor = null;
        try {
            cursor = DatabaseProvider.readableDatabase().rawQuery("SELECT count(*) FROM " + tableName, null);
            cursor.moveToFirst();
            return cursor.getInt(0);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

}
