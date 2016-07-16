package com.jacky.catlauncher.ui;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;

import com.jacky.catlauncher.R;
import com.jacky.catlauncher.presenter.CardPresenter;

public class MainActivity extends Activity {

    private static final int NUM_ROWS = 4;

    protected BrowseFragment mBrowseFragment;
    private ArrayObjectAdapter rowsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBrowseFragment = (BrowseFragment) getFragmentManager().findFragmentById(R.id.browse_fragment);

        mBrowseFragment.setHeadersState(BrowseFragment.HEADERS_DISABLED);
        mBrowseFragment.setTitle(getString(R.string.app_name));

        buildRowsAdapter();
    }

    protected void updateBackground(Drawable drawable) {
        BackgroundManager.getInstance(this).setDrawable(drawable);
    }

    private void buildRowsAdapter() {
        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        addPhotoRow();
        addVideoRow();
        addFunctionRow();

        mBrowseFragment.setAdapter(rowsAdapter);
        mBrowseFragment.setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {

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
        int cardCount = 10;
        String headerName = getResources().getString(R.string.app_header_video_name);
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
