
package com.jacky.compatible.launcher.features.setting;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jacky.launcher.R;
import com.jacky.compatible.launcher.features.app.AppAutoRun;
import com.jacky.compatible.launcher.features.app.AppUninstall;
import com.jacky.compatible.launcher.features.eliminateprocess.EliminateMainActivity;
import com.jacky.compatible.launcher.features.garbageclear.GarbageClear;
import com.jacky.compatible.launcher.features.speedtest.SpeedTestActivity;
import com.jacky.compatible.launcher.main.MainActivity;
import com.jacky.uikit.fragment.BaseFragment;

public class SettingFragment extends BaseFragment implements View.OnClickListener {

    private ImageButton mCleanGarbage;
    private ImageButton mCleanMemory;
    private ImageButton mAppUninstall;
    private ImageButton mNetworkSetting;
    private ImageButton mOtherSetting;
    private ImageButton mNetworkSpeed;
    private ImageButton mSysUpdate;
    private ImageButton mFileManage;
    private ImageButton mAbout;
    private ImageButton mAutoRunManage;

    private MainActivity mParent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParent = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_setting, null);
        initView(view);
        setListener();
        return view;
    }

    private void initView(View view) {
        mAppUninstall = (ImageButton) view.findViewById(R.id.setting_uninstall);
        mNetworkSetting = (ImageButton) view.findViewById(R.id.setting_net);
        mOtherSetting = (ImageButton) view.findViewById(R.id.setting_more);
        mNetworkSpeed = (ImageButton) view.findViewById(R.id.setting_net_speed);
        mSysUpdate = (ImageButton) view.findViewById(R.id.setting_update);
        mFileManage = (ImageButton) view.findViewById(R.id.setting_file);
        mAbout = (ImageButton) view.findViewById(R.id.setting_about);
        mCleanGarbage = (ImageButton) view.findViewById(R.id.setting_clean);
        mCleanMemory = (ImageButton) view.findViewById(R.id.setting_accelerate);
        mAutoRunManage = (ImageButton) view.findViewById(R.id.setting_autorun);

        mAppUninstall.setOnFocusChangeListener(mFocusChangeListener);
        mNetworkSetting.setOnFocusChangeListener(mFocusChangeListener);
        mOtherSetting.setOnFocusChangeListener(mFocusChangeListener);
        mNetworkSpeed.setOnFocusChangeListener(mFocusChangeListener);
        mSysUpdate.setOnFocusChangeListener(mFocusChangeListener);
        mFileManage.setOnFocusChangeListener(mFocusChangeListener);
        mAbout.setOnFocusChangeListener(mFocusChangeListener);
        mCleanGarbage.setOnFocusChangeListener(mFocusChangeListener);
        mCleanMemory.setOnFocusChangeListener(mFocusChangeListener);
        mAutoRunManage.setOnFocusChangeListener(mFocusChangeListener);
    }

    private void setListener() {
        mCleanGarbage.setOnClickListener(this);
        mCleanMemory.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mOtherSetting.setOnClickListener(this);
        mAppUninstall.setOnClickListener(this);
        mNetworkSetting.setOnClickListener(this);
        mFileManage.setOnClickListener(this);
        mNetworkSpeed.setOnClickListener(this);
        mSysUpdate.setOnClickListener(this);
        mAutoRunManage.setOnClickListener(this);
    }

    @Override
    public void requestInitFocus() {
        mAppUninstall.requestFocus();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if (getCurrentView() == mAppUninstall || getCurrentView() == mCleanGarbage
                        || getCurrentView() == mNetworkSetting || getCurrentView() == mOtherSetting) {
                    mParent.requestTabFocus();
                    return true;
                }
                return false;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        Intent jumpIntent;
        switch (view.getId()) {
            case R.id.setting_clean:
                jumpIntent = new Intent(mParent, GarbageClear.class);
                startActivity(jumpIntent);
                break;
            case R.id.setting_accelerate:
                jumpIntent = new Intent(mParent, EliminateMainActivity.class);
                startActivity(jumpIntent);
                break;
            case R.id.setting_about:
                break;
            case R.id.setting_more:
                try {
                    jumpIntent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(jumpIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.setting_file:
                break;
            case R.id.setting_update:
                break;
            case R.id.setting_net:
                jumpIntent = new Intent(mParent, SettingCustom.class);
                startActivity(jumpIntent);
                break;
            case R.id.setting_uninstall:
                jumpIntent = new Intent(mParent, AppUninstall.class);
                startActivity(jumpIntent);
                break;
            case R.id.setting_autorun:
                jumpIntent = new Intent(mParent, AppAutoRun.class);
                startActivity(jumpIntent);
                break;
            case R.id.setting_net_speed:
                jumpIntent = new Intent(mParent, SpeedTestActivity.class);
                startActivity(jumpIntent);
                break;
            default:
                break;
        }
    }
}
