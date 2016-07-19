package com.jacky.catlauncher.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.DisplayMetrics;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jacky.catlauncher.R;
import com.jacky.catlauncher.presenter.CardPresenter;

public class MainActivity extends Activity {

    protected BrowseFragment mBrowseFragment;
    private ArrayObjectAdapter rowsAdapter;
    private BackgroundManager mBackgroundManager;
    private DisplayMetrics mMetrics;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mBrowseFragment = (BrowseFragment) getFragmentManager().findFragmentById(R.id.browse_fragment);

        mBrowseFragment.setHeadersState(BrowseFragment.HEADERS_DISABLED);
        mBrowseFragment.setTitle(getString(R.string.app_name));

        prepareBackgroundManager();
        buildRowsAdapter();
    }

    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(this);
        mBackgroundManager.attach(this.getWindow());
        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void buildRowsAdapter() {
        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        addPhotoRow();
        addVideoRow();
        addAppRow();
        addFunctionRow();

        mBrowseFragment.setAdapter(rowsAdapter);
        mBrowseFragment.setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {

            }
        });
        mBrowseFragment.setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
            @Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                String url = (String) item;
                int width = mMetrics.widthPixels;
                int height = mMetrics.heightPixels;
                Glide.with(mContext)
                        .load(url)
                        .asBitmap()
                        .centerCrop()
                        .into(new SimpleTarget<Bitmap>(width, height) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                                    glideAnimation) {
                                mBackgroundManager.setBitmap(resource);
                            }
                        });
            }
        });
    }

    private void addPhotoRow() {
        String urls[] = {
                "http://tupian.enterdesk.com/2012/0528/gha/9/120523112107-15.jpg",
                "http://imgstore.cdn.sogou.com/app/a/100540002/541762.jpg",
                "http://image.tianjimedia.com/uploadImages/2012/291/Q5V0024X6GQM.jpg",
                "http://www.bz55.com/uploads/allimg/140729/138-140H9144A7.jpg",
                "http://imgstore.cdn.sogou.com/app/a/100540002/717240.jpg",
                "http://www.bz55.com/uploads/allimg/141005/138-141005115Q4.jpg",
                "http://e.hiphotos.baidu.com/zhidao/pic/item/5ab5c9ea15ce36d3418e754838f33a87e850b1c4.jpg",
                "http://d.3987.com/liuyan_140827/005.jpg",
                "http://www.bz55.com/uploads/allimg/150402/139-150402152530.jpg",
        };
        int photoCardCount = 9;
        String headerName = getResources().getString(R.string.app_header_photo_name);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
        for (int i = 0; i < photoCardCount; i++) {
            listRowAdapter.add(urls[i]);
        }
        HeaderItem header = new HeaderItem(0, headerName);
        rowsAdapter.add(new ListRow(header, listRowAdapter));
    }

    private void addVideoRow() {
        String urls[] = {
                "http://pic1.win4000.com/wallpaper/4/50a4a3c70e0b6.jpg",
                "http://pic.nipic.com/2008-07-01/20087111641251_2.jpg",
                "http://pic6.nipic.com/20100402/4452376_013049940716_2.jpg",
                "http://pic.nipic.com/2007-12-15/2007121514361888_2.jpg",
                "http://img14.poco.cn/mypoco/myphoto/20130219/19/66523397201302191911218443817764509_001.jpg",
                "http://pic31.nipic.com/20130710/3606040_162007539156_2.jpg",
                "http://pic22.nipic.com/20120620/1931052_155503912000_2.jpg",
                "http://img.zcool.cn/community/0335a8c554c70c700000158fcecac3c.jpg",
                "http://image5.tuku.cn/wallpaper/Movie%20Wallpapers/4016_2560x1600.jpg",
        };
        int cardCount = 9;
        String headerName = getResources().getString(R.string.app_header_video_name);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
        for (int i = 0; i < cardCount; i++) {
            listRowAdapter.add(urls[i]);
        }
        HeaderItem header = new HeaderItem(0, headerName);
        rowsAdapter.add(new ListRow(header, listRowAdapter));
    }

    private void addAppRow() {
        int cardCount = 10;
        String headerName = getResources().getString(R.string.app_header_app_name);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
        for (int i = 0; i < cardCount; i++) {
            listRowAdapter.add("");
        }
        HeaderItem header = new HeaderItem(0, headerName);
        rowsAdapter.add(new ListRow(header, listRowAdapter));
    }

    private void addFunctionRow() {
        int cardCount = 10;
        String headerName = getResources().getString(R.string.app_header_function_name);
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
        for (int i = 0; i < cardCount; i++) {
            listRowAdapter.add("");
        }
        HeaderItem header = new HeaderItem(0, headerName);
        rowsAdapter.add(new ListRow(header, listRowAdapter));
    }
}
