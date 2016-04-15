package com.jacky.uikit.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jacky.uikit.R;


public class BaseFragment extends Fragment {

    /**
     * 提供选中放大的效果
     */
    public View.OnFocusChangeListener mFocusChangeListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            int focus = 0;
            if (hasFocus) {
                focus = R.anim.enlarge;
            } else {
                focus = R.anim.decrease;
            }
            //如果有焦点就放大，没有焦点就缩小
            Animation mAnimation = AnimationUtils.loadAnimation(
                    getActivity().getApplication(), focus);
            mAnimation.setBackgroundColor(Color.TRANSPARENT);
            mAnimation.setFillAfter(hasFocus);
            v.startAnimation(mAnimation);
            mAnimation.start();
            v.bringToFront();
        }
    };
}
