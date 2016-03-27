package com.jacky.launcher.application;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

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
        initImageLoader(getApplicationContext());
    }

   public static Context getContext() {
        return context;
    }

    /**
     * init UIL ImageLoader
     */
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
