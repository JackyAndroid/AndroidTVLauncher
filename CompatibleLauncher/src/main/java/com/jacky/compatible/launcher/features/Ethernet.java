package com.jacky.compatible.launcher.features;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;

import com.jacky.launcher.R;


/**
 * 有线网络连接
 * @author jacky
 * @version 1.0
 * @since 2016.4.4
 */

public class Ethernet extends Activity {

    private TextView tip;

    private final BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent
                    .getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_IS_FAILOVER, false);
            NetworkInfo currentNetworkInfo = (NetworkInfo) intent
                    .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent
                    .getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            if (currentNetworkInfo.isConnected()
                    && currentNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                tip.setText("已连接无线网络，若连接有线请插好网线");
            } else if (currentNetworkInfo.isConnected()
                    && currentNetworkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                tip.setText("已连接有线网络");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_ethernet);
        initViews();
    }

    private void initViews() {
        tip = (TextView) findViewById(R.id.ethernet_tv);
        registerReceiver(mConnReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mConnReceiver);
    }

}