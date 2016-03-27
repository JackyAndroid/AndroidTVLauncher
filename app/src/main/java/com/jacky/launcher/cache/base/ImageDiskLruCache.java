package com.jacky.launcher.cache.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.jacky.launcher.cache.util.CacheConfig;
import com.jacky.launcher.cache.util.CacheUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

/**
 * 硬盘缓存.
 *
 * @Title:
 * @Description:
 * @Since:2014-3-5
 * @Version:
 */
public final class ImageDiskLruCache extends DiskLruCache {
    /**
     * 日志TAG. *
     */
    private final static String TAG = "ImageDiskLruCache";

    /**
     * Hint to the compressor, 0-100. 0 meaning compress for small size,
     * 100 meaning compress for max quality.
     * Some formats, like PNG which is lossless, will ignore the quality setting
     */
    private int mCompressQuality = 100;

    /**
     * 构造函数.
     *
     * @param cacheDir    缓存文件目录
     * @param maxByteSize 最大缓存大小
     */
    protected ImageDiskLruCache(File cacheDir, long maxByteSize) {
        super(cacheDir, maxByteSize);
    }

    /**
     * 打开一个图片硬盘缓存.
     *
     * @Description:
     * @Date 2014-3-6
     */
    public final static ImageDiskLruCache openImageCache(Context context, String cacheName, long maxByteSize) {
        File cacheDir = CacheUtils.getEnabledCacheDir(context, cacheName);
        if (cacheDir.isDirectory() && cacheDir.canWrite() && CacheUtils.getUsableSpace(cacheDir) > maxByteSize) {
            return new ImageDiskLruCache(cacheDir, maxByteSize);
        }
        return null;
    }

    /**
     * 存入图片.
     *
     * @param url    key 关键字
     * @param bitmap bitmap
     * @Description:
     * @Date 2014-3-5
     */
    public final void putImage(String url, Bitmap bitmap) {
        synchronized (mLinkedHashMap) {
            if (mLinkedHashMap.get(url) == null) {
                final String filePath = createFilePath(url);

                if (writeBitmapToFile(bitmap, filePath, url)) {
                    onPutSuccess(url, filePath);
                    flushCache();
                }
            }
        }
    }

    /**
     * 获取图片.
     *
     * @param url key 关键字
     * @return bitmap
     * @Description:
     * @Date 2014-3-5
     */
    public final Bitmap getImage(String url) {
        synchronized (mLinkedHashMap) {
            final String filePath = mLinkedHashMap.get(url);
            if (!TextUtils.isEmpty(filePath)) {
                return BitmapFactory.decodeFile(filePath);
            } else {
                final String existFilePath = createFilePath(url);
                if (new File(existFilePath).exists()) {
                    onPutSuccess(url, existFilePath);
                    return BitmapFactory.decodeFile(existFilePath);
                }
            }
            return null;
        }
    }

    /**
     * 把bitmap写入到缓存文件中.
     *
     * @param bitmap   bitmap
     * @param filePath 缓存文件路径
     * @Description:
     * @Date 2014-3-5
     */
    private boolean writeBitmapToFile(Bitmap bitmap, String filePath, String url) {
        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(filePath), CacheConfig.IO_BUFFER_SIZE);
            return bitmap.compress(getCompressFormat(url), mCompressQuality, outputStream);
        } catch (FileNotFoundException e) {
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
            }
        }
        return false;
    }

    /**
     * 根据文件类型获得CompressFormat.
     *
     * @Description:
     * @Date 2014-3-7
     */
    private CompressFormat getCompressFormat(String url) {
        String lowerUrl = url.toLowerCase(Locale.ENGLISH);
        if (lowerUrl.endsWith(".jpg")) {
            return CompressFormat.JPEG;
        } else if (lowerUrl.endsWith(".png")) {
            return CompressFormat.PNG;
        }
        return CompressFormat.JPEG;
    }
}
