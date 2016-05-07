
package com.jacky.launcher.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jacky.launcher.R;

import java.util.Timer;

public class TitleView extends RelativeLayout {

    private RelativeLayout layout;
    private View view;
    private Context context;
    private Typeface typeface;
    private ImageView imgWeather;
    private TextView tvTime, tvDate;
    private Timer timer;
    private ImageView imgNetWorkState;

    private Handler timeHandle = new Handler();

    private Runnable timeRun = new Runnable() {

        public void run() {
            setTvTimeText(TimeUtil.getTime());
            setTvDateDate(TimeUtil.getDate());
            timeHandle.postDelayed(this, 1000);
        }

    };

    private BroadcastReceiver wifiChange = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo.getBSSID() != null) {
                // wifi信号强度
                int signalLevel = WifiManager.calculateSignalLevel(
                        wifiInfo.getRssi(), 4);
                if (signalLevel == 0) {
                    imgNetWorkState.setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.wifi_1));

                } else if (signalLevel == 1) {
                    imgNetWorkState.setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.wifi_2));

                } else if (signalLevel == 2) {
                    imgNetWorkState.setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.wifi_3));

                } else if (signalLevel == 3) {
                    imgNetWorkState.setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.network_state_on));
                }
            }
        }
    };

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
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

                imgNetWorkState.setImageDrawable(context.getResources()
                        .getDrawable(R.drawable.network_state_on));

            } else if (currentNetworkInfo.isConnected()
                    && currentNetworkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {

                imgNetWorkState.setImageDrawable(context.getResources()
                        .getDrawable(R.drawable.networkstate_ethernet));

            } else if (!currentNetworkInfo.isConnected()) {
                imgNetWorkState.setImageDrawable(context.getResources()
                        .getDrawable(R.drawable.networkstate_off));
            }
        }
    };

    public TitleView(Context context) {
        super(context);
        this.context = context;
        initTitleView();
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initTitleView();
    }

    public void initTitleView() {
        view = LayoutInflater.from(context).inflate(R.layout.titleview, this, true);
        layout = (RelativeLayout) view.findViewById(R.id.home_title);
        tvTime = (TextView) view.findViewById(R.id.title_bar_hour);
        tvDate = (TextView) view.findViewById(R.id.title_bar_date);
        imgNetWorkState = (ImageView) view.findViewById(R.id.title_bar_network_state);
        typeface = Typeface.createFromAsset(context.getAssets(),
                "font/helvetica_neueltpro_thex.otf");
        tvTime.setTypeface(typeface);
        tvDate.setTypeface(typeface);
        timeHandle.post(timeRun);
        imgNetWorkState = (ImageView) this.findViewById(R.id.title_bar_network_state);
        context.registerReceiver(mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        context.registerReceiver(wifiChange, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
    }

    public void setTvTimeText(String text) {
        tvTime.setText(text);
    }

    public void setTvDateDate(String text) {
        tvDate.setText(text);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        context.unregisterReceiver(mConnReceiver);
        context.unregisterReceiver(wifiChange);
    }
}
