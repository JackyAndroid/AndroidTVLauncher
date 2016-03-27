package com.jacky.launcher.activitys.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jacky.launcher.R;
import com.jacky.launcher.activitys.Bluetooth;
import com.jacky.launcher.activitys.Ethernet;
import com.jacky.launcher.activitys.speedtest.SpeedTestActivity;
import com.jacky.launcher.activitys.wifi.WifiActivity;


/**
 *@author Droid
 */
public class SettingCustom extends Activity implements View.OnClickListener {

    private static final String TAG = "UPDATE";
    private static final boolean d = false;

    private TextView wifi;
    private TextView ethernet;
    private TextView bluetooth;
    private TextView detection;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_setting_custom);
        context = this;
        initViews();
        setListener();
    }

    private void initViews() {

        wifi = (TextView) findViewById(R.id.setting_custom_wifi);
        ethernet = (TextView) findViewById(R.id.setting_custom_ethernet);
        bluetooth = (TextView) findViewById(R.id.setting_custom_bluetooth);
        detection = (TextView) findViewById(R.id.setting_custom_net_detection);

    }

    private void setListener() {

        wifi.setOnClickListener(this);
        ethernet.setOnClickListener(this);
        bluetooth.setOnClickListener(this);
        detection.setOnClickListener(this);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "============onRestart========");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "=====onPause===========");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "=========onResume=======");
    }

    @Override
    public void onClick(View v) {

        Intent i = new Intent();

        switch (v.getId()){
            case R.id.setting_custom_wifi:
            	i.setClass(context, WifiActivity.class);
            	startActivity(i);
                break;
            case R.id.setting_custom_ethernet:
                i.setClass(context,Ethernet.class);
                startActivity(i);
                break;
            case R.id.setting_custom_bluetooth:
                i.setClass(context,Bluetooth.class);
                startActivity(i);
                break;
            case R.id.setting_custom_net_detection:
            	i.setClass(context, SpeedTestActivity.class);
            	startActivity(i);
                break;
        }
    }
}