package com.jacky.compatible.launcher.features.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.jacky.launcher.R;
import com.jacky.compatible.launcher.adapter.AppAdapter;
import com.jacky.compatible.launcher.main.MainActivity;
import com.jacky.compatible.launcher.model.AppBean;
import com.jacky.uikit.fragment.BaseFragment;

import java.util.List;

public class AppFragment extends BaseFragment {

    private MainActivity mParent;
    private List<AppBean> mAppList;
    private Receiver receiver;
    private GridView mGridView;
    private AppAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParent = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_app, null);
        init(view);
        return view;
    }

    public void init(View view) {
        mGridView = (GridView) view.findViewById(R.id.app_grid);

        AppDataManage getAppInstance = new AppDataManage(mParent);
        mAppList = getAppInstance.getLaunchAppList();

        mAdapter = new AppAdapter(this, getContext(), mAppList);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    public void requestInitFocus() {
        if (mAdapter.getCount() > 0) {
            mGridView.getChildAt(0).requestFocus();
        }
    }

    private boolean isFocusOnTopLine() {
        int count = mAdapter.getCount();
        if (mAdapter.getCount() > 5) {
            count = 5;
        }
        for (int i = 0; i < count; i++) {
            View view = mGridView.getChildAt(i);
            if (view.hasFocus()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if (isFocusOnTopLine()) {
                    mParent.requestTabFocus();
                    return true;
                }
                return false;
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        receiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addDataScheme("package");
        mParent.registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            mParent.unregisterReceiver(receiver);
        }
    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {

            }
            if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {

            }
        }
    }
}


