package net.sunzc.housecomputer.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;


import net.sunzc.housecomputer.MyLog;
import net.sunzc.housecomputer.db.inject.InjectUtils;

import java.util.ArrayList;
import java.util.List;

public class DBDao {

    private static final String TAG = "DBDao";
    private static DBDao dbdao;
    private final Pool helper;

    public DBDao(Context context) {
        helper = new Pool(context);
    }

    public static void init(Context context) {
        dbdao = new DBDao(context);
    }

    public static DBDao getInstance() {
        return dbdao;
    }

    public void insert(Object message) {
        synchronized (helper) {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                db.insert(InjectUtils.getTableName(message.getClass()), null, InjectUtils.toContentValues(message));// 执行插入操作
            } catch (Exception e) {
                e.printStackTrace();
                MyLog.e(TAG, "插入数据异常", e);
            } finally {
                helper.close();
            }
        }
    }

    public void remove(Class message, String where, String[] args) {
        synchronized (helper) {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                db.delete(InjectUtils.getTableName(message), where, args);// 执行插入操作
            } catch (Exception e) {
                MyLog.e(TAG, "删除数据异常", e);
            } finally {
                helper.close();
            }
        }
    }

    public void clearTable(Class message) {
        synchronized (helper) {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                String tableName = InjectUtils.getTableName(message);
                int row = db.delete(tableName, null, null);
                MyLog.d(TAG, "清除" + tableName + "表中所有数据,影响行数:" + row);
            } catch (Exception e) {
                MyLog.e(TAG, "清除表数据异常", e);
            } finally {
                helper.close();
            }
        }
    }

    public <M> List<M> query(Class<M> mClass, String where, String[] args) {
        StringBuilder querySql = new StringBuilder("select * from ")
                .append(InjectUtils.getTableName(mClass))
                .append(" ");
        if (!TextUtils.isEmpty(where)) {
            querySql.append("where").append(" ").append(where);
        }
        SQLiteDatabase dbs = helper.getWritableDatabase();
        List<M> beans = new ArrayList<>();
        try {
            MyLog.d(TAG, "查询语句：" + querySql.toString());
            Cursor cursor = dbs.rawQuery(querySql.toString(), args);
            if (cursor.moveToFirst()) {
                do {
                    M m = InjectUtils.generateDataBean(cursor, mClass);
                    beans.add(m);
                } while (cursor.moveToNext());
            }
            cursor.close();
            MyLog.d(TAG, "查询结果条数：" + beans.size());
        } catch (Exception e) {
            MyLog.e(TAG, e.getMessage(), e);
        } finally {
            helper.close();
        }
        return beans;
    }

    public <T> void replace(T object) {
        synchronized (helper) {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                long row = db.replace(InjectUtils.getTableName(object.getClass()), null, InjectUtils.toContentValues(object));// 执行插入操作
                MyLog.d(TAG, "replace操作成功:" + object + "影响行数:" + row);
            } catch (Exception e) {
                MyLog.e(TAG, "replace操作异常", e);
            } finally {
                helper.close();
            }
        }
    }

    private static class Pool {
        private int num;
        private SQLiteDatabase database;
        private DBOpenHelper helper;

        Pool(Context context) {
            this.helper = new DBOpenHelper(context);
        }

        SQLiteDatabase getWritableDatabase() {
            if (num <= 0 || database == null || !database.isOpen()) {
                database = helper.getReadableDatabase();
                num = 0;
            } else
                num++;
            return database;
        }

        void close() {
            if (num <= 0 && database != null && database.isOpen()) {
                database.close();
                num = 0;
            } else
                num--;
        }
    }
}
