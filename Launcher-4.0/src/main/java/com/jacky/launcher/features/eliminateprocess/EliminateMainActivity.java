package com.jacky.launcher.features.eliminateprocess;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jacky.launcher.R;
import com.jacky.launcher.model.TaskInfo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.List;

@SuppressLint("InflateParams")
public class EliminateMainActivity extends Activity {

    protected static final int LOAD_FINISH = 0;
    public static final int CLEAR_FINISH = 1;
    public static final int NEEDENT_CLEAR = 2;
    public static final int PERCENT_CHANGE = 3;
    private List<RunningAppProcessInfo> appProcessInfo;
    private ActivityManager activityManager;
    private List<TaskInfo> userTaskInfo;
    private ImageView roundImg;
    private Button startKill;
    private TextView increaseSpeed;
    private TextView allPercent;
    private String percentnum;
    private TextView releaseMemory;
    private String clearMemory;
    private LinearLayout clearEndlayout;
    private RelativeLayout clearingLayout;
    private static float memorySurPlus;
    private static float totalMemory;
    private MemoryInfo info;
    private int allpercent;
    private Boolean ISRound = true;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_FINISH:
                    break;
                case CLEAR_FINISH:
                    ISRound = false;
                    Animation animation = null;
                    roundImg.setAnimation(animation);
                    clearingLayout.setVisibility(View.GONE);
                    clearEndlayout.setVisibility(View.VISIBLE);
                    increaseSpeed.setText(percentnum + "%");
                    releaseMemory.setText(clearMemory + "MB");
                    startKill.setText("清理完成");
                    break;
                case NEEDENT_CLEAR:
                    percentnum = String.valueOf(0);
                    clearMemory = String.valueOf(0);
                    Toast.makeText(EliminateMainActivity.this, "当前不需要清理", Toast.LENGTH_LONG).show();
                    break;
                case PERCENT_CHANGE:
                    allPercent.setText(allpercent + "%");
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
        setContentView(R.layout.eliminateactivity_main);
        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        // 获取activityManager对象
        Init();
        InitData();
    }

    public void Init() {
        GetSurplusMemory();
        roundImg = (ImageView) findViewById(R.id.eliminate_roundimg);
        startKill = (Button) findViewById(R.id.start_killtask);
        releaseMemory = (TextView) findViewById(R.id.relase_memory);
        increaseSpeed = (TextView) findViewById(R.id.increase_speed);
        allPercent = (TextView) findViewById(R.id.all_percent);
        clearEndlayout = (LinearLayout) findViewById(R.id.clear_endlayout);
        clearingLayout = (RelativeLayout) findViewById(R.id.clearing_layout);
        Animation animation = AnimationUtils.loadAnimation(EliminateMainActivity.this, R.anim.eliminatedialog_anmiation);
        totalMemory = GetTotalMemory();
        roundImg.setAnimation(animation);
        startKill.setClickable(false);
        startKill.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    // 加载所有获取到的应用的信息
    public void InitData() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (ISRound) {
                    allpercent = (int) ((float) (memorySurPlus / totalMemory) * 100);
                    handler.sendEmptyMessage(PERCENT_CHANGE);
                }
            }
        }).start();
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                getRunningApp();
                TaskInfoProvider taskInfoProvider = new TaskInfoProvider(
                        EliminateMainActivity.this);
                userTaskInfo = taskInfoProvider.GetAllTask(appProcessInfo);
                KillTask();
            }
        }).start();
    }

    // 得到当前运行的进程数目
    public List<RunningAppProcessInfo> getRunningApp() {
        appProcessInfo = activityManager.getRunningAppProcesses();
        return appProcessInfo;

    }

    // 得到当前剩余的内存
    public long GetSurplusMemory() {
        info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);
        long memorySize = info.availMem;
        memorySurPlus = (float) memorySize / 1024 / 1024;
        return memorySize;
    }

    public float GetTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initialMemory = 0;
        try {
            FileInputStream fileInputStream = new FileInputStream(str1);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader, 8192);
            str2 = bufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            initialMemory = Integer.valueOf(arrayOfString[1]);
            fileInputStream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return (float) (initialMemory / 1024);
    }

    private void KillTask() {
        for (TaskInfo info : userTaskInfo) {
            if (!info.getIsSystemProcess()) {
                activityManager.killBackgroundProcesses(info.getPackageName());
            }
        }
        MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);
        float memorySize = (float) info.availMem / 1024 / 1024;
        float size = memorySize - memorySurPlus;
        if (size > 0) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            clearMemory = decimalFormat.format(size);
            percentnum = decimalFormat.format((size / totalMemory) * 100);
        } else {
            Message message = new Message();
            message.what = NEEDENT_CLEAR;
            handler.sendMessage(message);
        }
        Message message = new Message();
        message.what = CLEAR_FINISH;
        handler.sendMessage(message);
    }
}
