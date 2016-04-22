
package com.jacky.launcher;

import android.content.Context;

import com.jacky.common.app.BaseApp;

/**
 * Launcher Application
 *
 * @author jacky
 * @version 1.0
 * @since 2016.4.14
 */

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
