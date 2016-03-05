package com.topolyai.dbtool;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.topolyai.dbtool.expressions.EmptyExpression;
import com.topolyai.dbtool.expressions.Expression;
import com.topolyai.dbtool.expressions.UnaryExpression;
import com.topolyai.dbtool.extractor.Extractor;
import com.topolyai.dbtool.utils.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;

public abstract class AbstractCrudRepositoryBase<T extends Identifiable> {

    protected String tableName;

    public AbstractCrudRepositoryBase(Class<T> clss) {
        tableName = StringUtils.upperCaseAndUnderLineAtCapitals(clss.getSimpleName());
    }

    protected abstract Extractor<T> getExtractor();

    public List<T> findAll() {
        return findAll(new String[]{"*"});
    }

    public List<T> findAll(String[] projections) {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = DatabaseProvider.readableDatabase();
            cursor = db.rawQuery(format("SELECT %s FROM %s", getProjs(projections), tableName), null);
            return extractAll(cursor, projections);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<T> findAll(int page, int count) {
        return findAll(new String[]{"*"}, page, count);
    }

    public List<T> findAll(String[] projections, int page, int count) {
        return runRawQueryForListAndExtract(format("SELECT %s FROM %s LIMIT %s OFFSET %s", getProjs(projections), tableName, count, page), null);
        /*Cursor cursor = null;
        try {
            cursor = DatabaseProvider
                    .readableDatabase()
                    .rawQuery(format("SELECT %s FROM %s LIMIT %s OFFSET %s", getProjs(projections), tableName, count, page), null);
            return extractAll(cursor, projections);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }*/

    }

    public T findById(long id) {
        return findById(new String[]{"*"}, id);
    }

    public T findById(String[] projections, long id) {
        return runRawQueryForSingleAndExtract(format("SELECT %s FROM %s WHERE id = %s", getProjs(projections), tableName, id), null);
        /*Cursor cursor = null;
        try {
            SQLiteDatabase db = DatabaseProvider.readableDatabase();
            cursor = db.rawQuery(format("SELECT %s FROM %s WHERE id = %s", getProjs(projections), tableName, id), null);
            Extractor<T> extractor = getExtractor();
            boolean b = cursor.moveToFirst();
            return (b) ? extractor.extract(cursor) : null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }*/
    }

    /*protected void executeBatch(List<String> sqls) {
        SQLiteDatabase db = DatabaseProvider.instance().getWritableDatabase();
        db.beginTransaction();
        for (String string : sqls) {
            DbLogger.log("Executed: {}", string);
            db.execSQL(string);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }*/

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
        ContentValuesBuilder builder = ContentValuesBuilder.builder();
        addContentValues(builder, entity);
        return update(builder.build(), whereClause, args);
    }

    public int delete(long id) {
        return delete(new FieldKeyValuePair(Identifiable.ID, id));
    }

    public int delete(FieldKeyValuePair field) {
        return delete(new UnaryExpression(field));
    }

    public int delete(Expression expression) {
        SQLiteDatabase db = DatabaseProvider.writableDatabase();
        try {
            db.beginTransaction();
            List<String> args = new LinkedList<>();
            expression.appendArgsValue(args);
            int deleted = db.delete(tableName, expression.format(), args.toArray(new String[args.size()]));
            db.setTransactionSuccessful();
            return deleted;
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public T insert(T entity) {
        SQLiteDatabase db = DatabaseProvider.writableDatabase();
        try {
            db.beginTransaction();
            ContentValuesBuilder builder = ContentValuesBuilder.builder();
            addContentValues(builder, entity);
            long id = db.insert(tableName, null, builder.build());
            entity.setId(id);
            db.setTransactionSuccessful();
            return entity;
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
            for (T entity : all) {
                ContentValuesBuilder builder = ContentValuesBuilder.builder();
                addContentValues(builder, entity);
                long id = db.insert(tableName, null, builder.build());
                entity.setId(id);
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
            cursor = executeQuery(new String[]{"count(*)"}, new EmptyExpression());;
            cursor.moveToFirst();
            return cursor.getInt(0);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    protected T findByFields(FieldKeyValuePair field) {
        return findByFields(null, new UnaryExpression(field));
    }

    protected T findByFields(Expression expression) {
        return findByFields(null, expression);
    }

    protected T findByFields(String[] projections, Expression expression) {
        Cursor cursor = null;
        try {
            cursor = executeQuery(projections, expression);
            Extractor<T> extractor = getExtractor();
            return (cursor.moveToFirst()) ? extractor.extract(cursor) : null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private String groupBy() {
        return null;
    }

    private String having() {
        return null;
    }

    private String orderBy() {
        return null;
    }

    protected List<T> findAllByFields(FieldKeyValuePair field) {
        return findAllByFields(null, new UnaryExpression(field));
    }

    protected List<T> findAllByFields(Expression expression) {
        return findAllByFields(null, expression);
    }

    protected List<T> findAllByFields(String[] projections, Expression expression) {
        Cursor cursor = null;
        try {
            cursor = executeQuery(projections, expression);
            return extractAll(cursor, projections);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private Cursor executeQuery(String[] projections, Expression expression) {
        SQLiteDatabase db = DatabaseProvider.readableDatabase();
        List<String> objectList = new LinkedList<>();
        expression.appendArgsValue(objectList);
        return db.query(tableName, projections, expression.format(), objectList.toArray(new String[objectList.size()]), groupBy(), having(), orderBy());
    }


    public boolean exists(FieldKeyValuePair field) {
        return exists(new UnaryExpression(field));
    }

    public boolean exists(Expression expression) {
        Cursor cursor = null;
        try {
            cursor = executeQuery(new String[]{"count(*)"}, expression);
            if (cursor.moveToFirst()) {
                return cursor.getInt(0) > 0;
            } else {
                return false;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<T> in(String field, Object... args) {
        return in(new String[]{"*"}, field, args);
    }

    public List<T> in(String[] projections, String field, Object... args) {
        return runRawQueryForListAndExtract(format("SELECT %s FROM %s WHERE %s in (%s)", getProjs(projections), tableName, field, TextUtils.join(",", args)), null);
        /*Cursor cursor = null;
        try {
            cursor = DatabaseProvider.readableDatabase().rawQuery(format("SELECT %s FROM %s WHERE %s in (%s)", getProjs(projections), tableName, field, TextUtils.join(",", args)), null);
            return extractAll(cursor, projections);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }*/
    }

    /*private String buildWhere(String[] args, boolean appendWhere, FieldKeyValuePair... fields) {
        String where = "";
        if (fields.length > 0) {
            StringBuilder sb = new StringBuilder();
            if (appendWhere) {
                sb.append("WHERE");
            }
            for (int i = 0; i < fields.length; i++) {
                FieldKeyValuePair field = fields[i];
                if (i > 0) {
                    sb.append(" AND");
                }
                sb.append(" ").append(field.getKey()).append(" = ?");
                args[i] = field.getValue().toString();
            }
            where = sb.toString();
        }
        return where;
    }*/

    public int truncate() {
        SQLiteDatabase db = DatabaseProvider.writableDatabase();
        try {
            db.beginTransaction();
            int deleted = db.delete(tableName, null, null);
            db.setTransactionSuccessful();
            return deleted;
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    protected T runRawQueryForSingleAndExtract(String sql, String[] args) {
        Cursor cursor = null;
        try {
            cursor = DatabaseProvider
                    .readableDatabase()
                    .rawQuery(sql, args);
            boolean b = cursor.moveToFirst();
            return (b) ? getExtractor().extract(cursor) : null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    protected List<T> runRawQueryForListAndExtract(String sql, String[] args) {
        Cursor cursor = null;
        try {
            cursor = DatabaseProvider
                    .readableDatabase()
                    .rawQuery(sql, args);
            return extractAll(cursor, new String[]{"*"});
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    protected List<T> extractAll(Cursor cursor, String[] projections) {

        List<T> ret = new ArrayList<>();
        Extractor<T> extractor = getExtractor();
        while (cursor.moveToNext()) {
            ret.add(extractor.extract(cursor));
        }
        return ret;
    }


    private String getProjs(String[] projections) {
        return TextUtils.join(",", projections);
    }

//    public abstract ContentValues createContentValues(T entity);
    public abstract void addContentValues(ContentValuesBuilder builder, T entity);
}
