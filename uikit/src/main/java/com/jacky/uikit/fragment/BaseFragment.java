package com.jacky.uikit.fragment;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;

import com.jacky.uikit.R;


public class BaseFragment extends Fragment {

    private View mCurrentView;

    public View.OnFocusChangeListener mFocusChangeListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                mCurrentView = v;
                enlargeAnim(v);
            } else {
                reduceAnim(v);
            }
        }
    };

    public View getCurrentView() {
        return mCurrentView;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public void requestInitFocus(){

    }

    public View.OnFocusChangeListener getFocusChangeListener() {
        return mFocusChangeListener;
    }

    public void enlargeAnim(View v) {
        Animation a = android.view.animation.AnimationUtils.loadAnimation(v.getContext(), R.anim.uikit_enlarge);
        a.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
        a.setFillAfter(true);
        v.clearAnimation();
        v.setAnimation(a);
        v.bringToFront();
        a.start();
    }

    public void reduceAnim(View v) {
        Animation a = android.view.animation.AnimationUtils.loadAnimation(v.getContext(), R.anim.uikit_reduce);
        a.setFillAfter(true);
        v.clearAnimation();
        v.startAnimation(a);
        a.start();
    }
}
