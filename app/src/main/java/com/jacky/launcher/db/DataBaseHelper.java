package com.jacky.launcher.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;
	String sql = "create table if not exists selectedApps"
			+ "( position integer primary key,packageName string ,appName String,appIcon Blob)";

	// String sql = "create table if not exists TestUsers"+
	// "(id int primary key,name varchar,sex varchar)";
	/**
	 * 在SQLiteOpenHelper的子类当中，必须有该构造函数
	 * 
	 * @param context
	 *            上下文对象
	 * @param name
	 *            数据库名称
	 * @param factory
	 * @param version
	 *            当前数据库的版本，值必须是整数并且是递增的状态
	 */
	public DataBaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		// 必须通过super调用父类当中的构造函数
		super(context, name, factory, version);
	}

	public DataBaseHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	public DataBaseHelper(Context context, String name) {
		this(context, name, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
