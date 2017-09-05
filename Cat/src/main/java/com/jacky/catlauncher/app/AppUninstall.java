package com.jacky.catlauncher.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jacky.catlauncher.R;
import com.jacky.catlauncher.util.Tools;
import com.jacky.catlauncher.adapter.AppUninstallAdapter;

import java.util.List;

/**
 * 应用卸载类
 * @author jacky
 * @version 1.0
 * @since 2016.4.1
 */
public class AppUninstall extends Activity implements View.OnClickListener {

    private ListView listView;
    private AppUninstallAdapter adapter;
    private List<AppModel> mAppList;
    private Context context;
    private Receiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.app_uninstall);
        context = this;
        init();
    }

    private void init() {
        listView = (ListView) findViewById(R.id.app_uninstall_lv);
        AppDataManage getAppInstance = new AppDataManage(context);
        mAppList = getAppInstance.getUninstallAppList();
        adapter = new AppUninstallAdapter(context, mAppList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri packageURI = Uri.parse("package:" + mAppList.get(position).getPackageName());
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
                        packageURI);
                context.startActivity(uninstallIntent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        receiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addDataScheme("package");
        this.registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //接收安装广播
            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {

                String packageName = intent.getDataString();
                List<ResolveInfo> list = Tools.findActivitiesForPackage(context, packageName);
                ResolveInfo info = list.get(0);
                PackageManager localPackageManager = context.getPackageManager();
                AppModel localAppBean = new AppModel();
                localAppBean.setIcon(info.activityInfo.loadIcon(localPackageManager));
                localAppBean.setName(info.activityInfo.loadLabel(localPackageManager).toString());
                localAppBean.setPackageName(info.activityInfo.packageName);
                localAppBean.setDataDir(info.activityInfo.applicationInfo.publicSourceDir);

                mAppList.add(localAppBean);
            }
            //接收卸载广播
            if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
                String receiverName = intent.getDataString();
                receiverName = receiverName.substring(8);
                AppModel appBean;
                for (int i = 0; i < mAppList.size(); i++) {
                    appBean = mAppList.get(i);
                    String packageName = appBean.getPackageName();
                    if (packageName.equals(receiverName)) {
                        mAppList.remove(i);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }
}