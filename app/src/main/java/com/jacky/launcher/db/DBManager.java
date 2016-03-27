package com.jacky.launcher.db;

import android.content.Context;
import android.os.Environment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class DBManager {
	private final int BUFFER_SIZE = 400000;
	public static final String PACKAGE_NAME = "com.cqsmiletv";
	public static final String DB_NAME = "myapp.db";
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME + "/databases";

	private Context context;
	
	private static final String TAG ="DBManager";
	private final boolean d = true;

	public DBManager(Context context) {
		this.context = context;
	}

	/** copy the database under raw */
	public void copyDatabase() {

    }

	private void readDB(FileOutputStream fos, byte[] buffer, int db_id)
			throws IOException {
		int count;
		InputStream is;
		is = this.context.getResources().openRawResource(db_id);
		while ((count = is.read(buffer)) > 0) {
			fos.write(buffer, 0, count);
		}
		is.close();
	}
}
