package com.jacky.launcher.features.speedtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jacky.launcher.R;

import java.util.ArrayList;
import java.util.List;

public class SpeedTestActivity extends Activity implements OnClickListener {
    private Button DidNotStart;//未开始
    private Button InStart;//已开始
    private Button StartAgain;//再次开始
    private LinearLayout DidNotStartLayout;
    private LinearLayout InStartLayout;
    private LinearLayout StartAgainLayout;
    private long CurrenSpeed = 0;//当前速度
    private long AverageSpeed = 0;//平均速度
    private long SpeedTaital = 0;
    private byte[] FileData = null;
    private NetworkSpeedInfo networkSpeedInfo = null;
    private String URL = "http://gdown.baidu.com/data/wisegame/6546ec811c58770b/labixiaoxindamaoxian_8.apk";
    private List<Long> list = new ArrayList<Long>();
    private final int PROGRESSCHANGE = 0;
    private final int SPEEDUPDATE = 1;
    private final int SPEED_FINISH = 2;
    private ProgressBar SpeedProgressBar;
    private TextView Speed;
    private TextView percent;
    private TextView Movie_TYPE;
    private int progress;
    private Thread thread;
    private Boolean THREADCANRUN = true;
    private Boolean PROGRESSTHREADCANRUN = true;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRESSCHANGE:
                    progress = NetworkSpeedInfo.progress;
                    percent.setText(progress + "%");
                    if (progress < 100) {
                        SpeedProgressBar.setProgress(progress);
                    } else {
                        InStart.performClick();
                        PROGRESSTHREADCANRUN = false;
                        progress = 0;
                        SpeedProgressBar.setProgress(progress);
                    }
                    break;
                case SPEEDUPDATE:
                    CurrenSpeed = NetworkSpeedInfo.Speed;
                    list.add(CurrenSpeed);
                    for (long speed : list) {
                        SpeedTaital += speed;
                    }
                    AverageSpeed = SpeedTaital / list.size();
                    Speed.setText(AverageSpeed + "kb/s");
                    if (AverageSpeed <= 200) {
                        Movie_TYPE.setText("普清电影");
                    } else if (AverageSpeed <= 400) {
                        Movie_TYPE.setText("高清电影");
                    } else if (AverageSpeed > 400) {
                        Movie_TYPE.setText("超清电影");
                    }
                    SpeedTaital = 0;
                    break;
                case SPEED_FINISH:
                    Speed.setText(AverageSpeed + "kb/s");
                    if (AverageSpeed <= 200) {
                        Movie_TYPE.setText("普清电影");
                    } else if (AverageSpeed <= 400) {
                        Movie_TYPE.setText("高清电影");
                    } else if (AverageSpeed > 400) {
                        Movie_TYPE.setText("超清电影");
                    }
                    PROGRESSTHREADCANRUN = false;
                    THREADCANRUN = false;
                    NetworkSpeedInfo.FILECANREAD = false;
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.speedactivity_main);
        Init();
    }

    public void Init() {
        networkSpeedInfo = new NetworkSpeedInfo();
        DidNotStart = (Button) findViewById(R.id.speedtest_btn_start);
        InStart = (Button) findViewById(R.id.speedtset_btn_stoptest);
        StartAgain = (Button) findViewById(R.id.speedtest_btn_startagain);
        DidNotStart.setOnClickListener(this);
        InStart.setOnClickListener(this);
        StartAgain.setOnClickListener(this);
        DidNotStartLayout = (LinearLayout) findViewById(R.id.speedtset_didinotlayout);
        InStartLayout = (LinearLayout) findViewById(R.id.speedtest_instartlayout);
        StartAgainLayout = (LinearLayout) findViewById(R.id.speedtest_startagainlayout);
        SpeedProgressBar = (ProgressBar) findViewById(R.id.speedtest_progressBar);
        Speed = (TextView) findViewById(R.id.speedtest_speed);
        Movie_TYPE = (TextView) findViewById(R.id.speed_movietype);
        percent = (TextView) findViewById(R.id.speed_test_percent);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.speedtest_btn_start:
                DidNotStartLayout.setVisibility(View.GONE);
                InStartLayout.setVisibility(View.VISIBLE);
                StartAgainLayout.setVisibility(View.GONE);
                InStart.requestFocus();
                InStart.requestFocusFromTouch();
                PROGRESSTHREADCANRUN = true;
                THREADCANRUN = true;
                NetworkSpeedInfo.FILECANREAD = true;
                new Thread() {

                    @Override
                    public void run() {
                            FileData = ReadFileUtil.ReadFileFromURL(URL,
                                    networkSpeedInfo);
                    }
                }.start();
                thread = new Thread() {

                    @Override
                    public void run() {
                        while (THREADCANRUN) {
                            try {
                                sleep(50);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(SPEEDUPDATE);
                            if (NetworkSpeedInfo.FinishBytes >= NetworkSpeedInfo.totalBytes) {
                                handler.sendEmptyMessage(SPEED_FINISH);
                                NetworkSpeedInfo.FinishBytes = 0;
                            }
                        }
                    }
                };
                thread.start();

                new Thread() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        while (PROGRESSTHREADCANRUN) {
                            try {
                                sleep(500);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(PROGRESSCHANGE);
                        }
                    }
                }.start();
                break;
            case R.id.speedtset_btn_stoptest:
                StartAgainLayout.setVisibility(View.VISIBLE);
                InStartLayout.setVisibility(View.GONE);
                DidNotStartLayout.setVisibility(View.GONE);
                StartAgain.requestFocus();
                StartAgain.requestFocusFromTouch();
                NetworkSpeedInfo.progress = 0;
                NetworkSpeedInfo.FinishBytes = 0;
                handler.sendEmptyMessage(SPEED_FINISH);
                break;
            case R.id.speedtest_btn_startagain:
                DidNotStartLayout.setVisibility(View.VISIBLE);
                StartAgainLayout.setVisibility(View.GONE);
                InStartLayout.setVisibility(View.GONE);
                break;
        }
    }
}
