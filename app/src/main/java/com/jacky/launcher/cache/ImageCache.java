package com.jacky.launcher.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.jacky.launcher.cache.base.DiskLruCache;
import com.jacky.launcher.cache.base.ImageDiskLruCache;
import com.jacky.launcher.cache.util.CacheConfig;
import com.jacky.launcher.cache.util.CacheUtils;


/**
 *图片缓存.
 *@Title:
 *@Description:
 *@Since:2014-3-6
 *@Version:
 */
public class ImageCache
{
    /** 日志TAG. **/
    private static final String TAG = "ImageCache";

    /** 图片硬盘缓存. */
    private ImageDiskLruCache mImageDiskCache;

    /** 图片内存缓存. */
    private LruCache<String, Bitmap> mMemoryCache;

    /** 图片缓存实例. */
    private static ImageCache mInstance;

    /**
     * 获得一个图片缓存实例.
     * @param context context
     */
    public static ImageCache getInstance(Context context)
    {
        if(mInstance == null)
        {
            mInstance = new ImageCache(context);
        }
        return mInstance;
    }

    /**
     * 构造方法.
     * @param context context
     */
    private ImageCache(Context context)
    {
        init(context);
    }

    /**
     * 初始化<硬盘缓存>和<内存缓存>
     * @param context context
     * @Description:
     */
    private void init(Context context)
    {
        //设置硬盘缓存
        mImageDiskCache =
                ImageDiskLruCache.openImageCache(context, CacheConfig.Image.DISK_CACHE_NAME, CacheConfig.Image.DISK_CACHE_MAX_SIZE);

        // 设置内存缓存.
        final int imageMemorySize = CacheUtils.getMemorySize(context, CacheConfig.Image.MEMORY_SHRINK_FACTOR);
        mMemoryCache = new LruCache<String, Bitmap>(imageMemorySize)
        {
            @Override
            protected int sizeOf(String key, Bitmap bitmap)
            {
                return CacheUtils.getBitmapSize(bitmap);
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue)
            {
            }
        };
    }

    /**
     * 添加图片到缓存
     * @param key key
     * @param bitmap 图片.
     */
    public void addBitmapToCache(String key, Bitmap bitmap)
    {
        //如果key或者bitmap有一个为null,直接返回.
        if (key == null || bitmap == null)
        {
            return;
        }
        // 添加到内存缓存.
        if (mMemoryCache != null && mMemoryCache.get(key) == null)
        {
            mMemoryCache.put(key, bitmap);
        }
        // 添加到硬盘缓存.
        if (mImageDiskCache != null && !mImageDiskCache.containsKey(key))
        {
            mImageDiskCache.putImage(key, bitmap);
        }
    }

    /**
     * 是否存在.
     * @Date 2014-3-6
     */
    public boolean exists(String key)
    {
        if (mImageDiskCache != null && mImageDiskCache.containsKey(key))
        {
            return true;
        }
        return false;
    }

    /**
     * 从内存缓存中去图片.
     * @Date 2014-3-6
     */
    public Bitmap getBitmapFromMemCache(String key)
    {
        if (mMemoryCache != null)
        {
            final Bitmap bitmap = mMemoryCache.get(key);
            if (bitmap != null)
            {
                return bitmap;
            }
        }
        return null;
    }

    /**
     * 从硬盘缓存中取图片.
     * @Date 2014-3-6
     */
    public Bitmap getBitmapFromDiskCache(String key)
    {
        if (mImageDiskCache != null)
        {
            return mImageDiskCache.getImage(key);
        }
        return null;
    }

    /**
     * 清除内存缓存.
     * @Date 2014-3-6
     */
    public void clearMemoryCache()
    {
        mMemoryCache.evictAll();
    }

    /**
     * 得到硬盘缓存.
     * @Date 2014-3-6
     */
    public DiskLruCache getDiskCache()
    {
        return mImageDiskCache;
    }


    private void test() {

    }


}
