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
import com.jacky.launcher.model.NetworkSpeedInfo;
import com.jacky.launcher.utils.ReadFileUtil;

import java.util.ArrayList;
import java.util.List;

public class SpeedTestActivity extends Activity implements OnClickListener {
    private static final String URL = "http://gdown.baidu.com/data/wisegame/6546ec811c58770b/labixiaoxindamaoxian_8.apk";
    private static final int PROGRESSCHANGE = 0;
    private static final int SPEEDUPDATE = 1;
    private static final int SPEED_FINISH = 2;
    private Button didNotStart;//未开始
    private Button inStart;//已开始
    private Button startAgain;//再次开始
    private LinearLayout didNotStartLayout;
    private LinearLayout inStartLayout;
    private LinearLayout startAgainLayout;
    private long currenSpeed;//当前速度
    private long averageSpeed;//平均速度
    private long speedTaital;
    private byte[] fileData;
    private NetworkSpeedInfo networkSpeedInfo;
    private final List<Long> list = new ArrayList<>();
    private ProgressBar speedProgressBar;
    private TextView speed;
    private TextView percent;
    private TextView movieType;
    private int progress;
    private Thread thread;
    private Boolean threadCanRun = true;
    private Boolean progressThreadCanRun = true;
    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRESSCHANGE:
                    progress = NetworkSpeedInfo.progress;
                    percent.setText(progress + "%");
                    if (progress < 100) {
                        speedProgressBar.setProgress(progress);
                    } else {
                        inStart.performClick();
                        progressThreadCanRun = false;
                        progress = 0;
                        speedProgressBar.setProgress(progress);
                    }
                    break;
                case SPEEDUPDATE:
                    currenSpeed = NetworkSpeedInfo.Speed;
                    list.add(currenSpeed);
                    for (long speed : list) {
                        speedTaital += speed;
                    }
                    averageSpeed = speedTaital / list.size();
                    speed.setText(averageSpeed + "kb/s");
                    if (averageSpeed <= 200) {
                        movieType.setText("普清电影");
                    } else if (averageSpeed <= 400) {
                        movieType.setText("高清电影");
                    } else if (averageSpeed > 400) {
                        movieType.setText("超清电影");
                    }
                    speedTaital = 0;
                    break;
                case SPEED_FINISH:
                    speed.setText(averageSpeed + "kb/s");
                    if (averageSpeed <= 200) {
                        movieType.setText("普清电影");
                    } else if (averageSpeed <= 400) {
                        movieType.setText("高清电影");
                    } else if (averageSpeed > 400) {
                        movieType.setText("超清电影");
                    }
                    progressThreadCanRun = false;
                    threadCanRun = false;
                    NetworkSpeedInfo.FILECANREAD = false;
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_speed_main);
        Init();
    }

    public void Init() {
        networkSpeedInfo = new NetworkSpeedInfo();
        didNotStart = (Button) findViewById(R.id.speedtest_btn_start);
        inStart = (Button) findViewById(R.id.speedtset_btn_stoptest);
        startAgain = (Button) findViewById(R.id.speedtest_btn_startagain);
        didNotStart.setOnClickListener(this);
        inStart.setOnClickListener(this);
        startAgain.setOnClickListener(this);
        didNotStartLayout = (LinearLayout) findViewById(R.id.speedtset_didinotlayout);
        inStartLayout = (LinearLayout) findViewById(R.id.speedtest_instartlayout);
        startAgainLayout = (LinearLayout) findViewById(R.id.speedtest_startagainlayout);
        speedProgressBar = (ProgressBar) findViewById(R.id.speedtest_progressBar);
        speed = (TextView) findViewById(R.id.speedtest_speed);
        movieType = (TextView) findViewById(R.id.speed_movietype);
        percent = (TextView) findViewById(R.id.speed_test_percent);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.speedtest_btn_start:
                didNotStartLayout.setVisibility(View.GONE);
                inStartLayout.setVisibility(View.VISIBLE);
                startAgainLayout.setVisibility(View.GONE);
                inStart.requestFocus();
                inStart.requestFocusFromTouch();
                progressThreadCanRun = true;
                threadCanRun = true;
                NetworkSpeedInfo.FILECANREAD = true;
                new Thread() {

                    @Override
                    public void run() {
                        fileData = ReadFileUtil.ReadFileFromURL(URL,
                                networkSpeedInfo);
                    }
                }.start();
                thread = new Thread() {

                    @Override
                    public void run() {
                        while (threadCanRun) {
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
                        while (progressThreadCanRun) {
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
                startAgainLayout.setVisibility(View.VISIBLE);
                inStartLayout.setVisibility(View.GONE);
                didNotStartLayout.setVisibility(View.GONE);
                startAgain.requestFocus();
                startAgain.requestFocusFromTouch();
                NetworkSpeedInfo.progress = 0;
                NetworkSpeedInfo.FinishBytes = 0;
                handler.sendEmptyMessage(SPEED_FINISH);
                break;
            case R.id.speedtest_btn_startagain:
                didNotStartLayout.setVisibility(View.VISIBLE);
                startAgainLayout.setVisibility(View.GONE);
                inStartLayout.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
}
