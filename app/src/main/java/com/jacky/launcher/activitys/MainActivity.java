package com.jacky.launcher.activitys;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;

import com.jacky.launcher.R;
import com.jacky.launcher.activitys.app.AppFragment;
import com.jacky.launcher.activitys.setting.SettingFragment;
import com.jacky.launcher.adapter.MainActivityAdapter;
import com.jacky.launcher.LauncherApp;
import com.jacky.launcher.db.SharedPreferencesUtil;
import com.jacky.launcher.service.MainService;
import com.jacky.launcher.utils.FileCache;
import com.jacky.launcher.utils.NetWorkUtil;
import com.jacky.launcher.utils.Tools;
import com.jacky.launcher.utils.UpdateManager;
import com.jacky.launcher.views.MyViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivityGameLTV2";
    private MyViewPager mViewPager;
    private RadioButton localService;
    private RadioButton setting;
    private RadioButton app;
    private SQLiteDatabase mSQLiteDataBase;
    private LauncherApp mClientApp;
    private List<ContentValues> datas;//图片数据
    private int currentIndex;
    private static final int PAGE_NUMBER = 3;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private boolean d = true;// debug
    private SharedPreferencesUtil sp;
    private Context context;
    private FileCache fileCache;
    private String cacheDir;
    private View mViews[];
    private int mCurrentIndex = 0;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1://异常处理
                    initFragment("");
                    showShortToast("图片加载失败！");
                    break;
                case 2://图片数据解析
                    Bundle b = msg.getData();
                    String json = b.getString("mResponseJson");
                    try {
                        initFragment(json);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        mClientApp = (LauncherApp) this.getApplication();
        context = this;
        fileCache = new FileCache(context);
        cacheDir = fileCache.getCacheDir();
        sp = SharedPreferencesUtil.getInstance(context);
        initView();
        initData();
        context.registerReceiver(this.mConnReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mBindService();
        this.registerReceiver(updateUI,
                new IntentFilter("com.droid.updateUI"));
    }

    /**
     * 程序安装更新
     */
    private void installApk() {
        boolean installFlag = false;
        Log.d(TAG, "--installFlag1--" + installFlag);
        ArrayList<File> fileList = fileCache.getFile();
        for (File apk : fileList) {
            String name = apk.getName();
            if (name.substring(name.length() - 3, name.length()).equals("zip")) {
                continue;
            }
            name = name.substring(0, name.length() - 4);
            if (sp.getString(name + "packageName", "") != "") {
                PackageInfo info = Tools.getAPKVersionInfo(context,
                        sp.getString(name + "packageName", ""));
                if (info.versionCode < sp.getInt(name + "Version", 0)) {
                    installFlag = true;
                    Tools.installApk(context, apk,
                            sp.getString(name + "MD5", ""));
                }
            }
        }
        if (!installFlag) {
            UpdateManager updateManager = new UpdateManager(this);
            updateManager.checkUpdateInfo();
        }
    }

    /**
     * 更新UI receiver
     */
    private BroadcastReceiver updateUI = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String spResponseJson = sp.getString("url_str_json_data", " ");
            initFragment(spResponseJson);
        }
    };

    private void initData() {
        // 打开数据库
        openDataBase();
        if (isThereHaveUrlDataInDB()) {
            String data = getUrlDataFromDB();
            //将数据发送到Fragment
            initFragment(data);
            getUrlDataFromNetFlow();
        } else {
            getUrlDataFromNetFlow();
        }
    }

    private boolean isThereHaveUrlDataInDB() {
        boolean b = false;
        try {
            String s = getUrlDataFromDB();
            if (s.length() > 0)
                b = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    private void initView() {
        mViewPager = (MyViewPager) this.findViewById(R.id.main_viewpager);
        localService = (RadioButton) findViewById(R.id.main_title_local);
        setting = (RadioButton) findViewById(R.id.main_title_setting);
        app = (RadioButton) findViewById(R.id.main_title_app);
        localService.setSelected(true);
        mViews = new View[]{localService, setting, app};
        setListener();
    }

    private void setListener() {
        localService.setOnClickListener(this);
        setting.setOnClickListener(this);
        app.setOnClickListener(this);

        localService.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mViewPager.setCurrentItem(0);
                }
            }
        });
        setting.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mViewPager.setCurrentItem(1);
                }
            }
        });
        app.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mViewPager.setCurrentItem(2);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_title_local:
                currentIndex = 0;
                mViewPager.setCurrentItem(0);
                break;
            case R.id.main_title_setting:
                currentIndex = 3;
                mViewPager.setCurrentItem(1);
                break;
            case R.id.main_title_app:
                currentIndex = 4;
                mViewPager.setCurrentItem(2);
                break;
        }
    }

    /**
     * 初始化Fragment
     */
    private void initFragment(String url_data) {
        fragments.clear();//清空
        int count = PAGE_NUMBER;

        FragmentManager manager;
        FragmentTransaction transaction;

		/* 获取manager */
        manager = this.getSupportFragmentManager();
        /* 创建事物 */
        transaction = manager.beginTransaction();

        LocalServiceFragment interactTV = new LocalServiceFragment();
        SettingFragment setting = new SettingFragment();
        AppFragment app = new AppFragment();

         /*创建一个Bundle用来存储数据，传递到Fragment中*/
        Bundle bundle = new Bundle();
        /*往bundle中添加数据*/
        bundle.putString("url_data", url_data);
        /*把数据设置到Fragment中*/

        interactTV.setArguments(bundle);

        fragments.add(interactTV);
        fragments.add(setting);
        fragments.add(app);

        transaction.commitAllowingStateLoss();

        MainActivityAdapter mAdapter = new MainActivityAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(pageListener);
        mViewPager.setCurrentItem(0);
    }

    /**
     * ViewPager切换监听方法
     */
    public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            mViewPager.setCurrentItem(position);
            switch (position) {
                case 0:
                    currentIndex = 0;
                    localService.setSelected(true);
                    setting.setSelected(false);
                    app.setSelected(false);
                    break;
                case 1:
                    currentIndex = 1;
                    localService.setSelected(false);
                    setting.setSelected(true);
                    app.setSelected(false);
                    break;
                case 2:
                    currentIndex = 2;
                    localService.setSelected(false);
                    setting.setSelected(false);
                    app.setSelected(true);
                    break;
            }
        }
    };

    /**
     * 从网上获取Url数据流
     */
    private void getUrlDataFromNetFlow() {
        if (NetWorkUtil.isNetWorkConnected(context)) {
            //获取数据
            initFragment("");
        } else {
            initFragment("");
        }
    }

    private String getUrlDataFromDB() {
        Cursor cursor = mSQLiteDataBase.rawQuery("SELECT url_data FROM my_url_data", null);
        cursor.moveToLast();
        String a = cursor.getString(cursor.getColumnIndex("url_data"));
//        String s = cursor.getString(2);
        return a;
    }

    /* 打开数据库，创建表 */
    private void openDataBase() {
        mSQLiteDataBase = this.openOrCreateDatabase("myapp.db",
                MODE_PRIVATE, null);
        String CREATE_TABLE = "create table if not exists my_url_data (_id INTEGER PRIMARY KEY,url_data TEXT);";
        mSQLiteDataBase.execSQL(CREATE_TABLE);
        // 插入一条_id 为 1 的空数据
        String INSERT_ONE_DATA = "";
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSQLiteDataBase.close();
    }

    /**
     * 顶部焦点获取
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean focusFlag = false;
        for (View v : mViews) {
            if (v.isFocused()) {
                focusFlag = true;
            }
        }
        Log.d(TAG, "code:" + keyCode + " flag:" + focusFlag);
        if (focusFlag) {
            if (KeyEvent.KEYCODE_DPAD_LEFT == keyCode) {
                if (mCurrentIndex > 0) {
                    mViews[--mCurrentIndex].requestFocus();
                }
                return true;
            } else if (KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
                if (mCurrentIndex < 2) {
                    mViews[++mCurrentIndex].requestFocus();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void mBindService() {
        Intent intent = new Intent(this, MainService.class);
        if (d)
            Log.i(TAG, "=======bindService()========");
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private MainService.MyBinder myBinder;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (MainService.MyBinder) service;
            myBinder.startDownload();
        }
    };

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            NetworkInfo currentNetworkInfo = intent
                    .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

            if (currentNetworkInfo.isConnected()) {
                //连接网络更新数据
                installApk();
            } else {
                showShortToast("网络未连接");
                LauncherApp.netFlag = false;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mConnReceiver);
    }
}