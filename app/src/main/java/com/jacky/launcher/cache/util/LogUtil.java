package com.jacky.launcher.cache.util;

import android.util.Log;

import com.jacky.launcher.BuildConfig;


/**
 * log工具.
 *@Title:
 *@Description:
 */
public class LogUtil
{
    /**  日志log. */
    private static final String TAG = "Cache_Log";
    /** 是否打印log. */
    private static final boolean DEBUG = BuildConfig.DEBUG;


    //======================================= log.e =========================================//
    public static void e(String tag, String err)
    {
        if (DEBUG)
        {
            Log.e(tag, err);
        }
    }
    public static void e(String tag, String debug, Throwable e)
    {
        if (DEBUG)
        {
            Log.e(tag, debug, e);
        }
    }
    public static void e(String msg)
    {
        if (DEBUG)
        {
            Log.e(TAG, msg);
        }
    }

    //======================================= log.d =========================================//
    public static void d(String tag, String debug)
    {
        if (DEBUG && debug != null)
        {
            Log.d(tag, debug);
        }
    }
    public static void d(String tag, String debug, Throwable e)
    {
        if (DEBUG)
        {
            Log.d(tag, debug, e);
        }
    }
    public static void d(String msg)
    {
        if (DEBUG)
        {
            Log.d(TAG, msg);
        }
    }

    //======================================= log.i =========================================//
    public static void i(String tag, String info)
    {
        if (DEBUG)
        {
            Log.i(tag, info);
        }
    }
    public static void i(String tag, String debug, Throwable e)
    {
        if (DEBUG)
        {
            Log.i(tag, debug, e);
        }
    }
    public static void i(String msg)
    {
        if (DEBUG)
        {
            Log.i(TAG, msg);
        }
    }

    //======================================= log.w =========================================//
    public static void w(String tag, String info)
    {
        if (DEBUG)
        {
            Log.w(tag, info);
        }
    }
    public static void w(String tag, String debug, Throwable e)
    {
        if (DEBUG)
        {
            Log.w(tag, debug, e);
        }
    }
    public static void w(String msg)
    {
        if (DEBUG)
        {
            Log.w(TAG, msg);
        }
    }

    // ===================================== log.exception =======================================//
    public static void logException(Throwable e)
    {
        if (DEBUG)
        {
            e.printStackTrace();
        }
    }
}
