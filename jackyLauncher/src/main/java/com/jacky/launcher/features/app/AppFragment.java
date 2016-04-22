package com.jacky.launcher.features.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.jacky.launcher.R;
import com.jacky.uikit.fragment.BaseFragment;
import com.jacky.launcher.adapter.DataPagerAdapter;
import com.jacky.launcher.model.AppBean;
import com.jacky.launcher.views.Rotate3dAnimation;

import java.util.ArrayList;
import java.util.List;

public class AppFragment extends BaseFragment {

    private Context mContext;
    private List<AppBean> mAppList = null;
    private int mPagerCount = -1;//一共的页数
    private List<AllApp> mPagerListAllApp = new ArrayList<>();
    private ViewPager mViewPager = null;
    private static final String TAG = "AppFragment";
    private static final boolean d = false;
    private TextView pointer = null;
    private Rotate3dAnimation rotation;
    private Receiver receiver;
    private DataPagerAdapter<AllApp> adapter;

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int position) {
            pointer.startAnimation(rotation);
            pointer.setText((position + 1) + "/" + mPagerCount);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_app, null);
        mViewPager = (ViewPager) view.findViewById(R.id.app_view_pager);
        pointer = (TextView) view.findViewById(R.id.app_pointer);

        initAnimation();
        pointer.startAnimation(rotation);

        initAllApp();
        mViewPager.setOnPageChangeListener(pageChangeListener);
        return view;
    }

    /**
     * 3D旋转动画
     */
    private void initAnimation() {
        rotation = new Rotate3dAnimation(0, 360, 25,
                25, 0.0f, false);
        rotation.setDuration(700);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator(2.0f));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (pointer != null) {
                pointer.startAnimation(rotation);
            }
        } else {
        }
    }

    /**
     * 初始化app数据和布局
     */
    public void initAllApp() {

        GetAppList getAppInstance = new GetAppList(mContext);
        mAppList = getAppInstance.getLaunchAppList();
        if (mPagerListAllApp != null && mPagerListAllApp.size() > 0) {
            mPagerListAllApp.clear();
        }
        if (mAppList.size() % 15 == 0) {
            mPagerCount = mAppList.size() / 15;
        } else {
            mPagerCount = mAppList.size() / 15 + 1;
        }

        for (int i = 0; i < mPagerCount; i++) {
            AllApp mAllayout = new AllApp(mContext);
            mAllayout.setAppList(mAppList, i, mPagerCount);
            mAllayout.managerAppInit();
            mPagerListAllApp.add(mAllayout);
        }

        adapter = new DataPagerAdapter<>(mContext, mPagerListAllApp);
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        receiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addDataScheme("package");
        mContext.registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //安装广播
            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
                initAllApp();
            }
            //卸载广播
            if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
                initAllApp();
            }
        }
    }
}


