package com.jacky.launcher.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import com.jacky.launcher.db.SharedPreferencesUtil;


public class MainService extends Service {
	private static final String TAG = "MainService";
	private MyBinder mBinder = new MyBinder();
	private boolean d = true;// debug
	private SharedPreferencesUtil sp;
	private GetPicsRunnable run;
	private HandlerThread thread;
	private Handler handler;

	@Override
	public void onCreate() {
		super.onCreate();
        //更新界面图片，一个小时一次
		sp = SharedPreferencesUtil.getInstance(getApplicationContext());
		run = new GetPicsRunnable();
		thread = new HandlerThread("com.droid");
		thread.start();
		handler = new Handler(thread.getLooper());
		handler.postDelayed(run, 1000 * 60 * 60);
		Log.d(TAG, "service ................onCreate");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "service ................onDestroy");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public class MyBinder extends Binder {

		public void startDownload() {
			Log.d("TAG", "startDownload() executed");
			// 执行具体的下载任务
		}
	}

	/**
	 * 获取图片数据的线程 todo: get pics 色块管理
	 */
	private class GetPicsRunnable implements Runnable {

		@Override
		public void run() {
            //更新图片的一些操作。。。
            saveStringJsonPicData2sp("");
			Intent intent = new Intent();
			intent.setAction("com.droid.updateUI");
			sendBroadcast(intent);
			handler.postDelayed(run, 1000 * 60 * 60);
		}
	}

	/**
	 * 将返回的json数据保存到sp中
	 * @param responseJson
	 */
	private void saveStringJsonPicData2sp(String responseJson) {
		sp.putString("url_str_json_data", responseJson);
	}
}
