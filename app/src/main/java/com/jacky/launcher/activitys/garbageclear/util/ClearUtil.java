package com.jacky.launcher.activitys.garbageclear.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import java.io.File;
import java.io.FileInputStream;

public class ClearUtil {
    public static boolean TakeIsInstallApk(String abPath, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(abPath, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static long getFileSize(File f) {
        long size = 0;
        try {
            FileInputStream inputStream = new FileInputStream(f);
            size = inputStream.available();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return size / 1024;
    }
}
