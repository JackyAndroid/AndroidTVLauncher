
package com.jacky.launcher.features;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jacky.launcher.R;
import com.jacky.launcher.main.MainActivity;
import com.jacky.uikit.fragment.BaseFragment;

public class LocalServiceFragment extends BaseFragment implements View.OnClickListener {

    private MainActivity mParent;

    private ImageButton tour;
    private ImageButton tv;
    private ImageButton ad1;
    private ImageButton cate;
    private ImageButton weather;
    private ImageButton ad2;
    private ImageButton news;
    private ImageButton appStore;
    private ImageButton video;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParent = (MainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_local_service, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

        tv = (ImageButton) view.findViewById(R.id.local_tv);
        tour = (ImageButton) view.findViewById(R.id.local_tour);
        ad1 = (ImageButton) view.findViewById(R.id.local_ad1);
        ad2 = (ImageButton) view.findViewById(R.id.local_ad2);
        cate = (ImageButton) view.findViewById(R.id.local_cate);
        weather = (ImageButton) view.findViewById(R.id.local_weather);
        news = (ImageButton) view.findViewById(R.id.local_news);
        appStore = (ImageButton) view.findViewById(R.id.local_app_store);
        video = (ImageButton) view.findViewById(R.id.local_video);

        tv.setOnFocusChangeListener(mFocusChangeListener);
        tour.setOnFocusChangeListener(mFocusChangeListener);
        ad1.setOnFocusChangeListener(mFocusChangeListener);
        ad2.setOnFocusChangeListener(mFocusChangeListener);
        cate.setOnFocusChangeListener(mFocusChangeListener);
        weather.setOnFocusChangeListener(mFocusChangeListener);
        news.setOnFocusChangeListener(mFocusChangeListener);
        appStore.setOnFocusChangeListener(mFocusChangeListener);
        video.setOnFocusChangeListener(mFocusChangeListener);

        setFocusOrder();
    }

    private void setFocusOrder() {
        tv.setNextFocusDownId(R.id.local_tour);
        tv.setNextFocusRightId(R.id.local_ad1);
        tour.setNextFocusRightId(R.id.local_ad1);
        ad1.setNextFocusRightId(R.id.local_cate);
        cate.setNextFocusRightId(R.id.local_weather);
        cate.setNextFocusDownId(R.id.local_ad2);
        weather.setNextFocusDownId(R.id.local_news);
        ad2.setNextFocusRightId(R.id.local_news);
        ad2.setNextFocusDownId(R.id.local_app_store);
        news.setNextFocusDownId(R.id.local_video);
        appStore.setNextFocusRightId(R.id.local_video);
    }

    @Override
    public void requestInitFocus() {
        tv.requestFocus();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if (getCurrentView() == tv || getCurrentView() == ad1
                        || getCurrentView() == cate || getCurrentView() == weather) {
                    mParent.requestTabFocus();
                    return true;
                }
                return false;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.local_tv:
                break;
            case R.id.local_ad1:
                break;
            case R.id.local_ad2:
                break;
            case R.id.local_weather:
                break;
            case R.id.local_app_store:
                break;
            case R.id.local_cate:
                break;
            case R.id.local_news:
                break;
            case R.id.local_tour:
                break;
            case R.id.local_video:
                break;
            default:
                break;
        }
    }
}
