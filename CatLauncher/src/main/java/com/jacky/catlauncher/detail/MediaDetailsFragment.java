package com.jacky.catlauncher.detail;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewLogoPresenter;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.util.DisplayMetrics;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

/**
 * @author jacky
 * @version v1.0
 * @since 16/8/28
 */
public class MediaDetailsFragment extends DetailsFragment {

    private ArrayObjectAdapter mRowsAdapter;
    private MediaModel mMediaModel;
    private Context mContext;

    private BackgroundManager mBackgroundManager;
    private DisplayMetrics mMetrics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        mMediaModel = getActivity().getIntent().getParcelableExtra(MediaDetailsActivity.MEDIA);

        prepareBackgroundManager();
        buildDetails();
    }

    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void buildDetails() {
        ClassPresenterSelector selector = new ClassPresenterSelector();
        FullWidthDetailsOverviewRowPresenter rowPresenter = new FullWidthDetailsOverviewRowPresenter(
                new MediaDetailsDescriptionPresenter(),
                new DetailsOverviewLogoPresenter());

        selector.addClassPresenter(DetailsOverviewRow.class, rowPresenter);
        selector.addClassPresenter(ListRow.class, new ListRowPresenter());
        mRowsAdapter = new ArrayObjectAdapter(selector);

        final DetailsOverviewRow detailsOverview = new DetailsOverviewRow(mMediaModel);

        Glide.with(mContext)
                .load(mMediaModel.getImageUrl())
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {

                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        detailsOverview.setImageBitmap(mContext, resource);
                        return true;
                    }
                })
                .into(-1, -1);

        updateBackground(mMediaModel.getImageUrl());
//        detailsOverview.addAction(new Action(2, "Rent $2.99"));
        mRowsAdapter.add(detailsOverview);

        setAdapter(mRowsAdapter);
    }

    private void updateBackground(String uri) {
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(new SimpleTarget<Bitmap>(mMetrics.widthPixels, mMetrics.heightPixels) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mBackgroundManager.setBitmap(resource);
                    }
                });
    }

    @Override
    public void onStop() {
        mBackgroundManager.release();
        super.onStop();
    }
}