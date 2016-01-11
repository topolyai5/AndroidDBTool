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
        Cursor cursor = null;
        try {
            SQLiteDatabase db = DatabaseProvider.readableDatabase();
            cursor = db.rawQuery(format("SELECT * FROM %s", tableName), null);
            return extractAll(cursor);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<T> findAll(int page, int count) {
        Cursor cursor = null;
        try {
            cursor = DatabaseProvider
                    .readableDatabase()
                    .rawQuery(format("SELECT * FROM %s LIMIT %s OFFSET %s", tableName, count, page), null);
            return extractAll(cursor);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    protected List<T> extractAll(Cursor cursor) {
        List<T> ret = new ArrayList<>();
        Extractor<T> extractor = getExtractor();
        while (cursor.moveToNext()) {
            ret.add(extractor.extract(cursor));
        }
        return ret;
    }

    public T findById(long id) {
        SQLiteDatabase db = DatabaseProvider.readableDatabase();
        Cursor cursor = db.rawQuery(format("SELECT * FROM %s WHERE id = %s", tableName, id), null);
        Extractor<T> extractor = getExtractor();
        boolean b = cursor.moveToFirst();
        return (b) ? extractor.extract(cursor) : null;
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

    public int update(ContentValues contentValues, String whereClause, String[] args) {
        SQLiteDatabase db = null;
        try {
            db = DatabaseProvider.instance().getWritableDatabase();
            db.beginTransaction();
            int update = db.update(tableName, contentValues, whereClause, args);
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

    public int update(T entity) {

        Long id = entity.getId();
        String[] args;
        String whereClause = null;
        if (id != null) {
            args = new String[1];
            args[0] = entity.getId().toString();
            whereClause = "id = ?";
        } else {
            args = new String[0];
        }

        return update(createContentValues(entity), whereClause, args);
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
            long id = db.insert(tableName, null, createContentValues(obj));
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
                long id = db.insert(tableName, null, createContentValues(t));
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

    protected T findByFields(FieldKeyValuePair... fields) {
        SQLiteDatabase db = DatabaseProvider.readableDatabase();
        String where = "";
        String[] args = new String[fields.length];
        if (fields.length > 0) {
            StringBuilder sb = new StringBuilder("WHERE");
            for (int i = 0; i< fields.length; i++) {
                FieldKeyValuePair field = fields[i];
                if (i > 0) {
                    sb.append("AND");
                }
                sb.append(" ").append(field.getKey()).append(" = ?");
                args[i] = field.getValue().toString();
            }
            where = sb.toString();
        }
        Cursor cursor = db.rawQuery(format("SELECT * FROM %s %s", tableName, where), args);
        Extractor<T> extractor = getExtractor();
        boolean b = cursor.moveToFirst();
        return (b) ? extractor.extract(cursor) : null;
    }

    protected List<T> findAllByFields(FieldKeyValuePair... fields) {
        SQLiteDatabase db = DatabaseProvider.readableDatabase();
        String where = "";
        String[] args = new String[fields.length];
        if (fields.length > 0) {
            StringBuilder sb = new StringBuilder("WHERE");
            for (int i = 0; i< fields.length; i++) {
                FieldKeyValuePair field = fields[i];
                if (i > 0) {
                    sb.append("AND");
                }
                sb.append(" ").append(field.getKey()).append(" = ?");
                args[i] = field.getValue().toString();
            }
            where = sb.toString();
        }
        Cursor cursor = db.rawQuery(format("SELECT * FROM %s %s", tableName, where), args);
        return extractAll(cursor);
    }
}
