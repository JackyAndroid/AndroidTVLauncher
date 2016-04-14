
package com.jacky.launcher;

import android.app.Application;
import android.content.Context;

public class LauncherApp extends Application {

    /**
     * 请求协议
     */
    public static boolean netFlag = false;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
