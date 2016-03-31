package com.jacky.launcher.application;

import android.app.Application;
import android.content.Context;

public class ClientApplication extends Application {

    /**
     * 请求协议
     */
    public static final String HTTP = "http";
    public static final boolean d = true;
    public static  boolean netFlag = false;
    private static Context context;

    /**
     * 调试模式
     */
    public static boolean debug =false;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
    }

   public static Context getContext() {
        return context;
    }
}
