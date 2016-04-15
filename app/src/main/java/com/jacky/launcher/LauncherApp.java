
package com.jacky.launcher;

import android.content.Context;

import com.jacky.common.app.BaseApp;

public class LauncherApp extends BaseApp {

    public static boolean netFlag = false;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
