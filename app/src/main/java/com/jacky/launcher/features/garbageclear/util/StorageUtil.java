package com.jacky.launcher.features.garbageclear.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class StorageUtil {
    private final static long ERROR = -1;

    public Boolean extermalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static long getUseMemorySize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(path.getPath());
        long blocksize = statFs.getBlockSize();
        long totalblock = statFs.getBlockCount();
        long availableBlocks = statFs.getAvailableBlocks();
        return (totalblock * blocksize) - (blocksize * availableBlocks);
    }
}
