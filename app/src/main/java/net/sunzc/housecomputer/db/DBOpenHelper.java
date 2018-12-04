package net.sunzc.housecomputer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.sunzc.housecomputer.db.inject.InjectUtils;
import net.sunzc.housecomputer.entity.HouseType;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 5;
    private static final String DB_NAME = "house.db";

    DBOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(InjectUtils.createTable(HouseType.class));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}