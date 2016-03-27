package com.jacky.launcher.activitys;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.jacky.launcher.R;
import com.jacky.launcher.application.ClientApplication;


public class WoDouGameBaseFragment extends Fragment {

    /**
     * 当前的ImageView
     * 添加边框时
     */
    ImageView currentImage;
    private Context context = ClientApplication.getContext();

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

    protected void showLongToast(String pMsg) {
        Toast.makeText(context, pMsg, Toast.LENGTH_LONG).show();
    }

    protected void showShortToast(String pMsg) {
        Toast.makeText(context, pMsg, Toast.LENGTH_SHORT).show();
    }
}
