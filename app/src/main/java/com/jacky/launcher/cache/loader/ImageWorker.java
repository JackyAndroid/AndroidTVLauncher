package com.jacky.launcher.cache.loader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.ImageView;

import com.jacky.launcher.R;
import com.jacky.launcher.cache.ImageCache;
import com.jacky.launcher.cache.util.LogUtil;

import java.lang.ref.WeakReference;

/**
 * 图片加载器.<P>
 * 图片的加载过程：1.从自身的图片缓存中取;2.如果取不到,则从网络中获取.<P>
 * 同时,我们可以停止后台的图片获取线程.
 *@Title:
 *@Description:
 */
public abstract class ImageWorker
{
    /** 日志TAG. */
    private static final String TAG = "ImageWorker";
    /** 图片加载效果时间<淡入时间>. */
    private static final int FADE_IN_TIME = 200;
    /** 图片缓存. */
    private ImageCache mImageCache;
    /** 是否将要推出加载<如:应用程序结束的时候,加载线程就应该停止>. */
    private boolean mExitTasksEarly = false;
    /** 上下文Context. */
    protected Context mContext;
    /** 正在加载时候的图片. */
    private SparseArray<Bitmap> loadingImageMap;

    /**
     * 构造方法.
     * @param context context
     */
    protected ImageWorker(Context context)
    {
        mContext = context;
        loadingImageMap = new SparseArray<Bitmap>();
    }

    /**
     * 根据url加载一个图片到ImageView上,未加载成功的时候显示加载图片<loadingImageId>
     *
     * @param url 图片网络地址.
     * @param imageView 图片View
     * @param loadingImageId 加载图片资源ID
     * @Description:
     */
    public void loadImage(String url, ImageView imageView, int loadingImageId)
    {
        //如果图片网络地址为空,直接返回.
        if(TextUtils.isEmpty(url))
        {
            return;
        }
        Bitmap bitmap = null;
        if (mImageCache != null)
        {
            //先从内存缓存中取.
            bitmap = mImageCache.getBitmapFromMemCache(url);
        }
        if (bitmap != null)
        {
            //如果取到了,直接显示.
            imageView.setImageBitmap(bitmap);
        }
        else if (cancelPotentialWork(url, imageView))
        {
            //如果没有取到,取消之前相同的加载线程,取消成功,就启动一个新的加载线程.
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(mContext.getResources(), getLoadingImage(loadingImageId), task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(url);
        }
    }

    /**
     * 根据资源ID获取加载中的图片.
     * @param resId 资源ID.
     * @Description:
     */
    private Bitmap getLoadingImage(int resId)
    {
        //先从所有加载中的图片列表中取,如果取到,直接返回
        Bitmap loadingBitmap = null;
        loadingBitmap = loadingImageMap.get(resId);
        //如果没有取到
        if (loadingBitmap == null)
        {
            //从资源文件中获取,并且放到列表中,供后期提取.
            loadingBitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
            loadingImageMap.put(resId, loadingBitmap);
        }
        return loadingBitmap;
    }


    /**
     * 设置图片缓存.
     * @param imageCache 图片缓存.
     */
    public void setImageCache(ImageCache imageCache)
    {
        mImageCache = imageCache;
    }

    /**
     * 获得图片缓存.
     * @return imageCache
     * @Description:
     */
    public ImageCache getImageCache()
    {
        return mImageCache;
    }

    /**
     * 设置是否停止加载.
     */
    public void setExitTasksEarly(boolean exitTasksEarly)
    {
        mExitTasksEarly = exitTasksEarly;
    }

    /**
     * 根据url得到bitmap图片.
     */
    protected abstract Bitmap processBitmap(String url);

    /**
     * 取消一个ImageView的加载线程.
     * @param imageView 被取消加载线程的ImageView.
     * @Description:
     */
    public static void cancelWork(ImageView imageView)
    {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null)
        {
            bitmapWorkerTask.cancel(true);
            LogUtil.d(TAG, "cancel load : " + bitmapWorkerTask.url);
        }
    }

    /**
     * 取消指定url的图片加载线程.
     * @param url 图片网络地址
     * @param imageView 图片View
     * @return 如果有相同的线程在进行,返回false,否则返回 true.
     */
    public static boolean cancelPotentialWork(String url, ImageView imageView)
    {
        //从ImageView中获取加载线程.
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null)
        {
            final String taskUrl = bitmapWorkerTask.url;
            if (TextUtils.isEmpty(taskUrl) || !taskUrl.equals(url))
            {
                //如果加载的url为空,获得加载的url不等指定的url,则取消当前的加载线程.
                bitmapWorkerTask.cancel(true);
                LogUtil.d(TAG, "cancel load : " + taskUrl);
            }
            else
            {
                // 如果加载的url等于指定的url,则说明不需要重新加载,返回false.
                return false;
            }
        }
        return true;
    }

    /**
     * 从ImageView获得加载线程.
     * @param imageView 图片View.
     */
    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView)
    {
        //如果IamgeView不为空.
        if (imageView != null)
        {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable)
            {
                //获得ImageView的drawable,如果是AsyncDrawable的实例,返回加载线程.
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;

                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    /**
     * 图片加载线程.
     */
    private class BitmapWorkerTask extends AsyncTask<String, String, Bitmap>
    {
        /** 图片网络地址. */
        private String url;
        /** 弱引用显示图片的ImageView. */
        private final WeakReference<ImageView> imageViewReference;

        /**
         * 构造方法
         * @param imageView 显示图片的ImageView
         */
        public BitmapWorkerTask(ImageView imageView)
        {
            //进行弱引用
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params)
        {
            url = params[0];
            Bitmap bitmap = null;

            // 1.如果图片缓存不为null.
            // 2.如果当前线程没有被取消.
            // 3.如果被弱引用的ImageView没有被销毁.
            // 4.如果加载程序无须退出.
            if (mImageCache != null && !isCancelled() && getAttachedImageView() != null && !mExitTasksEarly)
            {
                //从硬盘缓存中去.
                bitmap = mImageCache.getBitmapFromDiskCache(url);
            }

            // 1.如果从硬盘缓存中获取的图片为Null.
            // 2.如果当前线程没有被取消.
            // 3.如果被弱引用的ImageView没有被销毁.
            // 4.如果加载程序无须退出.
            if (bitmap == null && !isCancelled() && getAttachedImageView() != null && !mExitTasksEarly)
            {
                //后台获取图片<网络获取>
                bitmap = processBitmap(params[0]);
            }

            // 1.如果加载过后的图片不为null.
            // 2.如果图片缓存不为null.
            if (bitmap != null && mImageCache != null)
            {
                //添加到缓存中.
                mImageCache.addBitmapToCache(url, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result)
        {
            // 1.如果当前线程<被取消>.
            // 2.如果加载程序<被命令退出>.
            if (isCancelled() || mExitTasksEarly)
            {
                result = null;
            }
            final ImageView imageView = getAttachedImageView();
            if (result != null && imageView != null)
            {
                //如果弱引用的ImageView还在,并且加载的图片不为空,则显示图片.
                setImageBitmap(imageView, result);
            }
        };

        /**
         * 获得弱引用的ImageView
         */
        private ImageView getAttachedImageView()
        {
            final ImageView imageView = imageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
            //获得的ImageView中的加载线程 为 当前线程
            if (this == bitmapWorkerTask)
            {
                //则返回这个IamgeView.
                return imageView;
            }
            return null;
        }
    }

    /**
     * 弱引用加载线程的BitmapDrawable
     *@Title:
     */
    private class AsyncDrawable extends BitmapDrawable
    {
        /** 弱引用加载线程. */
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        /**
         * 构造方法.
         * @param res 系统资源
         * @param bitmap 加载过程中的图片.
         * @param bitmapWorkerTask 图片加载线程.
         */
        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask)
        {
            super(res, bitmap);
            //弱引用加载线程.
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        /**
         * 返回弱引用的图片加载线程.
         */
        public BitmapWorkerTask getBitmapWorkerTask()
        {
            return bitmapWorkerTaskReference.get();
        }
    }

    /**
     * 将图片设置到ImageView中.
     */
    private void setImageBitmap(ImageView imageView, Bitmap bitmap)
    {
        //当图片加载好的时候,设置一个跳转样式,从一个<透明图片>跳至<加载好的图片>,用时<FADE_IN_TIME><200ms>
        final Drawable[] layers = new Drawable[] {
                new ColorDrawable(R.color.translucent),
                new BitmapDrawable(mContext.getResources(), bitmap) };

        final TransitionDrawable transDrawable = new TransitionDrawable(layers);

        imageView.setImageDrawable(transDrawable);
        //开始展示.
        transDrawable.startTransition(FADE_IN_TIME);
    }
}
