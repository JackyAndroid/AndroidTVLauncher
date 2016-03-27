package com.jacky.launcher.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.jacky.launcher.db.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * apk更新下载
 */
public class UpdateManager {
	private static final String TAG = "UpdateManager";
	private Context mContext;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private String packageVersion;
	private String packageDownloadPath;
	private String packageMD5;
	private JSONArray appList;
	private boolean d = true;// debug flag
	private SharedPreferencesUtil sp;
	private FileCache fileCache;

	public UpdateManager(Context context) {
		this.mContext = context;
		fileCache = new FileCache(mContext);
		sp = SharedPreferencesUtil.getInstance(mContext);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				break;
			case DOWN_OVER:
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 外部接口让主Activity调用 请求服务器并检查服务器apk版本信息
	 */
	public void checkUpdateInfo() {

    }

	public String getCacheDir() {
		return fileCache.getCacheDir();
	}

	/**
	 * 下载apk
	 * 
	 */
	private void downloadApk() throws JSONException {

    }

	/**
	 * 下载zip
	 */
	private void downloadZip() {}

}
