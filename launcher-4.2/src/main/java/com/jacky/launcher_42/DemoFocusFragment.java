package com.jacky.launcher_42;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sgottard.sofa.BrowseFragment;
import com.sgottard.sofa.ContentFragment;
import com.sgottard.sofademo.R;

public class DemoFocusFragment extends Fragment implements ContentFragment {

    private View rootContainer;

    private int marginTop;
    private int marginLeft;
    private View.OnFocusChangeListener focusChangeListener;
    private ImageView top;
    private ImageView left;
    private ImageView right;
    private ImageView bottom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootContainer = inflater.inflate(R.layout.fragment_demo_focus_layout, container, false);

        focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    animateScaleUp(v);
                } else {
                    animateScaleDown(v);
                }
            }
        };

        top = (ImageView) rootContainer.findViewById(R.id.top);
        top.setOnFocusChangeListener(focusChangeListener);
        Glide.with(this)
                .load("http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%20Guitar%20Search/card.jpg")
                .centerCrop()
                .into(top);

        left = (ImageView) rootContainer.findViewById(R.id.left);
        left.setOnFocusChangeListener(focusChangeListener);
        Glide.with(this)
                .load("http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Gone%20Google/Pt%20England%20School%20has%20Gone%20Google/card.jpg")
                .centerCrop()
                .into(left);

        right = (ImageView) rootContainer.findViewById(R.id.right);
        right.setOnFocusChangeListener(focusChangeListener);
        Glide.with(this)
                .load("http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%20Rushmore/card.jpg")
                .centerCrop()
                .into(right);

        bottom = (ImageView) rootContainer.findViewById(R.id.bottom);
        bottom.setOnFocusChangeListener(focusChangeListener);
        Glide.with(this)
                .load("http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%20Route%2066/card.jpg")
                .centerCrop()
                .into(bottom);

        return rootContainer;
    }

    @Override
    public void onStart() {
        super.onStart();

        BrowseFragment container = (BrowseFragment) getParentFragment();
        container.toggleTitle(false);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) rootContainer.getLayoutParams();
//        params.topMargin = marginTop;
        params.leftMargin = marginLeft;
        rootContainer.setLayoutParams(params);
    }

    private synchronized void animateScaleUp(View v) {
        float currentScaleX = v.getScaleX();
        float currentScaleY = v.getScaleY();

        float targetScale = 1.4f;

        PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat(View.SCALE_X, currentScaleX, targetScale);
        PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat(View.SCALE_Y, currentScaleY, targetScale);

        ValueAnimator scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(v, scaleXHolder, scaleYHolder);
        scaleAnimator.start();
    }

    private synchronized void animateScaleDown(View v) {
        float currentScaleX = v.getScaleX();
        float currentScaleY = v.getScaleY();

        float targetScale = 1f;

        PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat(View.SCALE_X, currentScaleX, targetScale);
        PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat(View.SCALE_Y, currentScaleY, targetScale);

        ValueAnimator scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(v, scaleXHolder, scaleYHolder);
        scaleAnimator.start();
    }

    @Override
    public boolean isScrolling() {
        return false;
    }

    @Override
    public View getFocusRootView() {
        return top;
    }

    @Override
    public void setExtraMargin(int marginTop, int marginLeft) {
        this.marginTop = marginTop;
        this.marginLeft = marginLeft;
    }
}
