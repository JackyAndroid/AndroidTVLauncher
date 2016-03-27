package com.jacky.launcher.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DBManager.DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void update(String table_name, int id, String text) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = "_id" + " = ?";
        String[] whereValue = {Integer.toString(id)};
        ContentValues cv = new ContentValues();
        cv.put("url_data", text);
        db.update(table_name, cv, where, whereValue);
    }

}