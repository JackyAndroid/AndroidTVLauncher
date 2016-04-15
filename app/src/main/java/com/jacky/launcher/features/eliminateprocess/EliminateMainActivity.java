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
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.List;

@SuppressLint("InflateParams")
public class EliminateMainActivity extends Activity {

    protected static final int LOAD_FINISH = 0;
    public final int CLEAR_FINISH = 1;
    public final int NEEDENT_CLEAR = 2;
    public final int PERCENT_CHANGE = 3;
    private List<RunningAppProcessInfo> appProcessInfo;
    private ActivityManager activityManager;
    private List<TaskInfo> UserTaskInfo;
    private ImageView Round_img;
    private Button Start_kill;
    private TextView increase_speed;
    private TextView Allpercent;
    private String percentnum;
    private TextView release_memory;
    private String Clearmemory;
    private LinearLayout clear_endlayout;
    private RelativeLayout Clearing_layout;
    private static float MemorySurPlus;
    private static float TotalMemory;
    private MemoryInfo info;
    private int allpercent;
    private Boolean ISRound = true;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_FINISH:
                    break;
                case CLEAR_FINISH:
                    ISRound = false;
                    Animation animation = null;
                    Round_img.setAnimation(animation);
                    Clearing_layout.setVisibility(View.GONE);
                    clear_endlayout.setVisibility(View.VISIBLE);
                    increase_speed.setText(percentnum + "%");
                    release_memory.setText(Clearmemory + "MB");
                    Start_kill.setText("清理完成");
                    break;
                case NEEDENT_CLEAR:
                    percentnum = 0 + "";
                    Clearmemory = 0 + "";
                    Toast.makeText(EliminateMainActivity.this, "当前不需要清理", Toast.LENGTH_LONG).show();
                    break;
                case PERCENT_CHANGE:
                    Allpercent.setText(allpercent + "%");
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
        Round_img = (ImageView) findViewById(R.id.eliminate_roundimg);
        Start_kill = (Button) findViewById(R.id.start_killtask);
        release_memory = (TextView) findViewById(R.id.relase_memory);
        increase_speed = (TextView) findViewById(R.id.increase_speed);
        Allpercent = (TextView) findViewById(R.id.all_percent);
        clear_endlayout = (LinearLayout) findViewById(R.id.clear_endlayout);
        Clearing_layout = (RelativeLayout) findViewById(R.id.clearing_layout);
        Animation animation = AnimationUtils.loadAnimation(EliminateMainActivity.this, R.anim.eliminatedialog_anmiation);
        TotalMemory = GetTotalMemory();
        Round_img.setAnimation(animation);
        Start_kill.setClickable(false);
        Start_kill.setOnClickListener(new OnClickListener() {

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
                    allpercent = (int) ((float) (MemorySurPlus / TotalMemory) * 100);
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
                UserTaskInfo = taskInfoProvider.GetAllTask(appProcessInfo);
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
        long MemorySize = info.availMem;
        MemorySurPlus = (float) MemorySize / 1024 / 1024;
        return MemorySize;
    }

    public float GetTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader fileReader = new FileReader(str1);
            BufferedReader bufferedReader = new BufferedReader(fileReader, 8192);
            str2 = bufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            initial_memory = Integer.valueOf(arrayOfString[1]);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return (float) (initial_memory / 1024);
    }

    private void KillTask() {
        for (TaskInfo info : UserTaskInfo) {
            if (!info.getIsSystemProcess()) {
                activityManager.killBackgroundProcesses(info.getPackageName());
                // 高级清理
                // try {
                // Method method =
                // Class.forName("android.app.ActivityManager").getMethod("forceStopPackage",
                // String.class);
                // method.invoke(activityManager, info.getPackageName());
                // } catch (Exception e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
            }
        }
        MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);
        float MemorySize = (float) info.availMem / 1024 / 1024;
        float size = MemorySize - MemorySurPlus;
        if (size > 0) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            Clearmemory = decimalFormat.format(size);
            percentnum = decimalFormat.format((size / TotalMemory) * 100);
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
