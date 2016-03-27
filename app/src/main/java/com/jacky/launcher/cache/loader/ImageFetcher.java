package com.jacky.launcher.cache.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.jacky.launcher.cache.base.DiskLruCache;
import com.jacky.launcher.cache.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片撷取器.
 *@Title:
 *@Description:
 *@Version:
 */
public class ImageFetcher extends ImageWorker
{
    /** 日志TAG. */
    private static final String TAG = "ImageFetcher";

    /**
     * 构造方法.
     * @param context 上下文
     */
    public ImageFetcher(Context context)
    {
        super(context);
    }

    @Override
    protected Bitmap processBitmap(String url)
    {
        //从网络下载图片.
        final File file = downloadBitmap(url);
        if (file != null)
        {
            // 如果下载成功,转换成Bitmap返回.
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }
        return null;
    }

    /**
     * 从网络下载一个图片.
     * @param netUrl 图片网络地址.
     * @Description:
     */
    private File downloadBitmap(String netUrl)
    {
        DiskLruCache diskCache = getImageCache().getDiskCache();
        LogUtil.d(TAG, "load : " + netUrl);
        HttpURLConnection urlConnection = null;
        try
        {
            final URL url = new URL(netUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            //放到图片的硬盘缓存中.
            String filePath = diskCache.put(netUrl, urlConnection.getInputStream());
            if(!TextUtils.isEmpty(filePath))
            {
                return new File(filePath);
            }
        }
        catch (IOException e)
        {
            LogUtil.e(TAG, "load error :" + netUrl, e);
        }
        finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}
