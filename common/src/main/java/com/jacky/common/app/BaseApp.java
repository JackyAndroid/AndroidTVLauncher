package com.jacky.common.app;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Intent;
import android.os.Build;

import com.jacky.common.logger.Logger;


/**
 * App抽象类
 *
 * @author jacky
 * @version v1.0
 * @since 2016.4.11
 */
public abstract class BaseApp extends Application {

    private static BaseApp sApp;

    private static boolean mResume;
    private static boolean mPendingKill;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    /**
     * 获得Application对象
     *
     * @return
     */
    public static BaseApp getApplication() {
        return sApp;
    }

    /**
     * 销毁
     */
    protected void onDestroy() {

    }

    /**
     * 隐藏软件
     */
    public static void hideApp() {
        //模拟HOME键
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
        intent.addCategory(Intent.CATEGORY_HOME);
        sApp.startActivity(intent);
    }

    public static void resumeApp() {
        mResume = true;
        Logger.d("app", "resume...");
    }

    public static boolean isPendingKillApp() {
        return mPendingKill;
    }
}
