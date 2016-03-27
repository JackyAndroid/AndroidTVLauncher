package com.jacky.launcher.cache.base;

import android.content.Context;

import com.jacky.launcher.cache.util.CacheConfig;
import com.jacky.launcher.cache.util.CacheUtils;
import com.jacky.launcher.cache.util.LogUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 硬盘缓存.
 *
 * @Title:
 * @Description:
 * @Since:2014-3-5
 * @Version:
 */
public class DiskLruCache {
    /**
     * 日志TAG.
     */
    private static final String TAG = "DiskLruCache";
    /**
     * 缓存文件命名开始符.
     */
    private static final String CACHE_FILENAME_PREFIX = "cache_";
    /**
     * 一次最大移除数.
     */
    private static final int MAX_REMOVALS = 4;
    /**
     * 缓存文件路径.
     */
    private final File mCacheDir;
    /**
     * 当前缓存文件个数.
     */
    private int cacheNumSize = 0;
    /**
     * 当前缓存文件字节大小.
     */
    private int cacheByteSize = 0;
    /**
     * 缓存最多文件个数    <默认:512个>
     */
    private static final int maxCacheNumSize = 512;
    /**
     * 缓存最大字节数     <默认:10MB>
     */
    private long maxCacheByteSize = 1024 * 1024 * 10;
    /**
     * map<key, 缓存文件路径>.
     */
    protected final Map<String, String> mLinkedHashMap =
            Collections.synchronizedMap(new LinkedHashMap<String, String>(32, 0.75f, true));

    /**
     * 缓存文件获取过滤器.
     * 文件名以 {@link #CACHE_FILENAME_PREFIX} 开头
     */
    private static final FilenameFilter CAHE_FILE_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String filename) {
            //以我们定义的<缓存文件命名开始符>开始的文件名,才认为是缓存文件.
            return filename.startsWith(CACHE_FILENAME_PREFIX);
        }
    };

    /**
     * 打开一个硬盘缓存.
     *
     * @Description:
     * @Date 2014-3-5
     */
    public final static DiskLruCache openCache(Context context, String cacheName, long maxByteSize) {
        File cacheDir = CacheUtils.getEnabledCacheDir(context, cacheName);
        if (cacheDir.isDirectory() && cacheDir.canWrite() && CacheUtils.getUsableSpace(cacheDir) > maxByteSize) {
            return new DiskLruCache(cacheDir, maxByteSize);
        }
        return null;
    }

    /**
     * 构造方法.
     *
     * @param cacheDir    缓存目录.
     * @param maxByteSize 最大缓存字节数.
     */
    protected DiskLruCache(File cacheDir, long maxByteSize) {
        mCacheDir = cacheDir;
        maxCacheByteSize = maxByteSize;
    }

    /**
     * 把一个字节数组以文件存放在缓存中.
     *
     * @param key         关键字
     * @param inputStream 字节流
     * @return 缓存文件路径.
     * @Description:
     * @Date 2014-3-5
     */
    public final String put(String key, InputStream inputStream) {
        BufferedInputStream bufferedInputStream = null;
        OutputStream bufferOps = null;
        try {
            bufferedInputStream = new BufferedInputStream(inputStream);
            String filePath = createFilePath(key);
            bufferOps = new BufferedOutputStream(new FileOutputStream(filePath));

            byte[] b = new byte[CacheConfig.IO_BUFFER_SIZE];
            int count;
            while ((count = bufferedInputStream.read(b)) > 0) {
                bufferOps.write(b, 0, count);
            }
            bufferOps.flush();
            LogUtil.d(TAG, "put success : " + key);
            onPutSuccess(key, filePath);
            flushCache();
            return filePath;
        } catch (IOException e) {
            LogUtil.d(TAG, "store failed to store: " + key, e);
        } finally {
            try {
                if (bufferOps != null) {
                    bufferOps.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                LogUtil.d(TAG, "close stream error : " + e.getMessage());
            }
        }
        return "";
    }

    /**
     * 把一个字节数组以文件存放在缓存中.
     *
     * @param key  关键字
     * @param data 字节数组
     * @return 缓存文件路径.
     */
    public final String put(String key, byte[] data) {
        if (data != null) {
            OutputStream bufferOps = null;
            try {
                String filePath = createFilePath(key);
                bufferOps = new BufferedOutputStream(new FileOutputStream(filePath));
                bufferOps.write(data, 0, data.length);
                bufferOps.flush();
                LogUtil.d(TAG, "put success : " + key);
                onPutSuccess(key, filePath);
                flushCache();
                return filePath;
            } catch (IOException e) {
                LogUtil.d(TAG, "put fail : " + key, e);
            } finally {
                try {
                    if (bufferOps != null) {
                        bufferOps.close();
                    }
                } catch (IOException e) {
                    LogUtil.d(TAG, "close outputStream error : " + e.getMessage());
                }
            }
        }
        return "";
    }

    /**
     * 放入一个文件.
     *
     * @param key      关键字
     * @param filePath 文件路径
     */
    protected final void onPutSuccess(String key, String filePath) {
        mLinkedHashMap.put(key, filePath);
        cacheNumSize = mLinkedHashMap.size();
        cacheByteSize += new File(filePath).length();
    }


    /**
     * 缓存大小溢出处理.
     *
     * @Description:
     * @Date 2014-3-5
     */
    protected final void flushCache() {
        Entry<String, String> eldestEntry;
        File eldestFile;
        long eldestFileSize;
        int count = 0;
        //超过最大缓存文件个数   or  超过最大空间大小    移除不常用的文件,并且一次最多只能移除4个.
        while (count < MAX_REMOVALS && (cacheNumSize > maxCacheNumSize || cacheByteSize > maxCacheByteSize)) {
            eldestEntry = mLinkedHashMap.entrySet().iterator().next();
            eldestFile = new File(eldestEntry.getValue());
            eldestFileSize = eldestFile.length();
            mLinkedHashMap.remove(eldestEntry.getKey());
            eldestFile.delete();
            cacheNumSize = mLinkedHashMap.size();
            cacheByteSize -= eldestFileSize;
            count++;
            LogUtil.d(TAG, "flushCache - Removed :" + eldestFile.getAbsolutePath() + ", " + eldestFileSize);
        }
    }

    /**
     * 根据key返回缓存文件.
     *
     * @param key key
     * @Description:
     * @Date 2014-3-6
     */
    public final File get(String key) {
        if (containsKey(key)) {
            final File file = new File(createFilePath(key));
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

    /**
     * 缓存中是否有key的数据保存
     *
     * @param key 关键字
     * @Description:
     * @Date 2014-3-5
     */
    public final boolean containsKey(String key) {
        //是否在map中.
        if (mLinkedHashMap.containsKey(key)) {
            return true;
        }
        //如果不在map中,看是或否在文件夹中.
        final String existingFile = createFilePath(key);
        if (new File(existingFile).exists()) {
            //如果存在,放入map后期直接提取.
            onPutSuccess(key, existingFile);
            return true;
        }
        return false;
    }

    /**
     * 清除缓存.
     *
     * @Description:
     * @Date 2014-3-5
     */
    public final void clearCache() {
        final File[] files = mCacheDir.listFiles(CAHE_FILE_FILTER);
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
    }

    /**
     * 根据key获得缓存文件路径.
     *
     * @param key 关键字
     * @Description:
     * @Date 2014-3-5
     */
    public final String createFilePath(String key) {
        return new StringBuffer().append(mCacheDir.getAbsolutePath())
                .append(File.separator).append(CACHE_FILENAME_PREFIX)
                .append(key.hashCode()).toString();
    }
}
