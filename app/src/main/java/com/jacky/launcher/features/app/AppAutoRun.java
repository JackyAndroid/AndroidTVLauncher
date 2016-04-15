package com.jacky.launcher.features.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.jacky.launcher.R;
import com.jacky.launcher.adapter.AppAutoRunAdapter;
import com.jacky.launcher.model.AppBean;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author Droid
 * 应用开机自启动管理
 */

public class AppAutoRun extends Activity implements View.OnClickListener {

    private static final String TAG = "UPDATE";
    private static final boolean d = false;
    private ListView listView;
    private AppAutoRunAdapter adapter;
    private List<AppBean> mAppList;
    private Context context;
    private boolean first = true;
    private boolean clickFlag = false;
    private int clickPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.app_auto_run);
        context = this;
        init();
    }

    private void init() {
        listView = (ListView) findViewById(R.id.app_auto_run_lv);
        GetAppList getAppInstance = new GetAppList(context);
        mAppList = getAppInstance.getAutoRunAppList();
        adapter = new AppAutoRunAdapter(context, mAppList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ImageView flag = (ImageView) view.findViewById(R.id.item_app_auto_run_flag);
                if (first) {
                    first = false;
                    clickPosition = position;
                    boolean b = manageBoot(mAppList.get(position).getPackageName(),false);
                    flag.setBackgroundResource(R.drawable.switch_off);
                    clickFlag = true;
                } else {
                    if (clickPosition == position) {
                        if (clickFlag) {
                           boolean b = manageBoot(mAppList.get(position).getPackageName(),true);
                            flag.setBackgroundResource(R.drawable.switch_on);
                        } else {
                            flag.setBackgroundResource(R.drawable.switch_off);
                            boolean b = manageBoot(mAppList.get(position).getPackageName(),false);
                        }
                        clickFlag = !clickFlag;
                    } else {
                        clickFlag = true;
                        clickPosition = position;
                        flag.setBackgroundResource(R.drawable.switch_off);
                        boolean b = manageBoot(mAppList.get(position).getPackageName(),false);
                    }
                }
            }
        });
    }


    public boolean manageBoot(String pkg,boolean able) {
        Process process = null;
        DataOutputStream dos = null;
        String command = null;
        try {
            process = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(process.getOutputStream());
            dos.flush();
            command = "export LD_LIBRARY_PATH=/vendor/lib:/system/lib  \n";
            dos.writeBytes(command);
            //(有些cls含有$，需要处理一下，不然会禁止失败，比如微信)
            //但是获取应用是否允许或者禁止开机启动的时候就不用处理cls，否则得不到状态值
//            cls = cls.replace("$", "\\$");
//            command = "pm disable " + pkg + "/" + cls + " \n";
            if(able){
                command = "pm enable " + pkg;
            }else{
                command = "pm disable " + pkg;
            }
            dos.writeBytes(command);
            dos.writeBytes("exit " + "\n");
            dos.flush();
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int exitValue = process.exitValue();
            try {
                if (exitValue == 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "============onRestart========");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "============onStart========");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "============onDestroy========");
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

    }
}