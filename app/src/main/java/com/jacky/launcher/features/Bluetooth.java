package com.jacky.launcher.features;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jacky.launcher.R;
import com.jacky.launcher.adapter.MyBluetoothAdapter;
import com.jacky.launcher.utils.Tools;
import com.jacky.uikit.alarm.ToastAlarm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 蓝牙管理
 * @author jacky
 * @version 1.0
 * @since 2016.4.3
 */

public class Bluetooth extends Activity implements View.OnClickListener {

    private static final String TAG = "UPDATE";
    private static final boolean d = false;

    private RelativeLayout openRL;
    private RelativeLayout detectionRL;
    private RelativeLayout searchRL;
    private RelativeLayout pairRL;
    private RelativeLayout searchDeviceRL;

    private ImageView openIV;
    private ImageView detectionIV;
    private ImageView searchIV;
    private TextView pairTV;
    private TextView pairTVName;
    private TextView searchDeviceTV;
    private ListView searchDeviceLV;
    private Set<BluetoothDevice> bondedDevices;
    private BluetoothDevice pairDevice;

    private boolean openFlag;
    private boolean detectionFlag;
    private int pairPosition = -1;

    private Context context;

    private BluetoothAdapter bluetoothAdapter;
    private MyBluetoothAdapter itemAdapter;

    private List<Map<String, Object>> list;

    private BroadcastReceiver searchReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            BluetoothDevice device = null;
            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device != null) {
            }
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("name", device.getName());
                    map.put("type", device.getBluetoothClass().getDeviceClass());
                    map.put("device", device);
                    if (list.indexOf(map) == -1) {// 防止重复添加
                        list.add(map);
                        itemAdapter = new MyBluetoothAdapter(context, list);
                        searchDeviceLV.setAdapter(itemAdapter);
                    }
                }
            } else if (device != null && device.getBondState() == BluetoothDevice.BOND_BONDING) {
                ToastAlarm.show("正在配对");
            } else if (device != null && device.getBondState() == BluetoothDevice.BOND_BONDED) {
                pairTVName.setText(device.getName());
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).get("device").equals(device)) {
                        pairPosition = i;
                        list.remove(i);
                        itemAdapter.notifyDataSetChanged();
                    }
                }
                ToastAlarm.show("配对完成");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_bluetooth);
        context = this;
        initViews();
        initData();
        setListener();
    }

    private void initData() {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        list = new ArrayList<Map<String, Object>>();
        if (bluetoothAdapter.isEnabled()) {
            openIV.setBackgroundResource(R.drawable.switch_on);
            detectionIV.setBackgroundResource(R.drawable.switch_off);
            searchIV.setVisibility(View.VISIBLE);
            pairTV.setVisibility(View.VISIBLE);
            pairRL.setVisibility(View.VISIBLE);
            searchDeviceTV.setVisibility(View.VISIBLE);
            searchDeviceRL.setVisibility(View.VISIBLE);
            bondedDevices = bluetoothAdapter.getBondedDevices();
            Iterator iterator = bondedDevices.iterator();
            if (iterator.hasNext()) {
                BluetoothDevice bond = (BluetoothDevice) iterator.next();
                pairDevice = bond;
                pairTVName.setText(bond.getName());
                pairPosition = -2;
            }
            openFlag = true;
        } else {
            openIV.setBackgroundResource(R.drawable.switch_off);
            detectionIV.setBackgroundResource(R.drawable.switch_off);
            searchIV.setVisibility(View.GONE);
            pairTV.setVisibility(View.GONE);
            pairRL.setVisibility(View.GONE);
            searchDeviceTV.setVisibility(View.GONE);
            searchDeviceRL.setVisibility(View.GONE);
            openFlag = false;
        }

        IntentFilter intent = new IntentFilter();
        // 用BroadcastReceiver来取得搜索结果
        intent.addAction(BluetoothDevice.ACTION_FOUND);
        //每当扫描模式变化的时候，应用程序可以为通过ACTION_SCAN_MODE_CHANGED值来监听全局的消息通知。
        // 比如，当设备停止被搜寻以后，该消息可以被系统通知給应用程序。
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        //每当蓝牙模块被打开或者关闭，应用程序可以为通过ACTION_STATE_CHANGED值来监听全局的消息通知。
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(searchReceiver, intent);

    }

    private void initViews() {

        openRL = (RelativeLayout) findViewById(R.id.bluetooth_rl_open);
        detectionRL = (RelativeLayout) findViewById(R.id.bluetooth_rl_detection);
        searchRL = (RelativeLayout) findViewById(R.id.bluetooth_rl_search);
        pairRL = (RelativeLayout) findViewById(R.id.bluetooth_rl_pair1);
        searchDeviceRL = (RelativeLayout) findViewById(R.id.bluetooth_rl_search_device);

        openIV = (ImageView) findViewById(R.id.bluetooth_iv_open);
        detectionIV = (ImageView) findViewById(R.id.bluetooth_iv_detection);
        searchIV = (ImageView) findViewById(R.id.bluetooth_iv_search);

        pairTV = (TextView) findViewById(R.id.bluetooth_tv_pair);
        pairTVName = (TextView) findViewById(R.id.bluetooth_tv_pair_name);
        searchDeviceTV = (TextView) findViewById(R.id.bluetooth_tv_search_device);
        searchDeviceLV = (ListView) findViewById(R.id.bluetooth_lv_search_device);

    }

    private void setListener() {

        openRL.setOnClickListener(this);
        detectionRL.setOnClickListener(this);
        searchRL.setOnClickListener(this);
        pairRL.setOnClickListener(this);

        searchDeviceLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = (BluetoothDevice) list.get(position).get("device");
                device.createBond();
                ToastAlarm.show("正在配对..");
            }
        });

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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(searchReceiver);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bluetooth_rl_open:
                if (!openFlag) {
                    bluetoothAdapter.disable();
                    openIV.setBackgroundResource(R.drawable.switch_off);
                    detectionIV.setBackgroundResource(R.drawable.switch_off);
                    searchIV.setVisibility(View.GONE);
                    pairTV.setVisibility(View.GONE);
                    pairRL.setVisibility(View.GONE);
                    searchDeviceTV.setVisibility(View.GONE);
                    searchDeviceRL.setVisibility(View.GONE);
                    openFlag = !openFlag;
                } else {
                    if (bluetoothAdapter != null) {
                        if (!bluetoothAdapter.isEnabled()) {
                            Intent intent;
                            try {
                                intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        ToastAlarm.show("蓝牙不可用！");
                    }
                    openIV.setBackgroundResource(R.drawable.switch_on);
                    searchIV.setVisibility(View.VISIBLE);
                    pairTV.setVisibility(View.VISIBLE);
                    pairRL.setVisibility(View.VISIBLE);
                    searchDeviceTV.setVisibility(View.VISIBLE);
                    searchDeviceRL.setVisibility(View.VISIBLE);
                    openFlag = !openFlag;
                }
                break;
            case R.id.bluetooth_rl_detection:
                if (detectionFlag) {
                    detectionIV.setBackgroundResource(R.drawable.switch_off);
                    detectionFlag = !detectionFlag;
                } else {
                    try {
                        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        startActivity(discoverableIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    detectionIV.setBackgroundResource(R.drawable.switch_on);
                    detectionFlag = !detectionFlag;
                }
                break;
            case R.id.bluetooth_rl_search:
                Animation rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate);
                searchIV.startAnimation(rotateAnimation);
                bluetoothAdapter.startDiscovery();
                break;
            case R.id.bluetooth_rl_pair1:
                if (pairPosition > -1) {
                    BluetoothDevice device = (BluetoothDevice) list.get(pairPosition).get("device");
                    try {
                        boolean b = Tools.removeBond(device.getClass(), device);
                        if (b) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("name", device.getName());
                            map.put("type", device.getBluetoothClass().getDeviceClass());
                            map.put("device", device);
                            list.add(map);
                            itemAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (pairPosition == -2) {
                    try {
                        ToastAlarm.show("正在取消配对..");
                        boolean b = Tools.removeBond(pairDevice.getClass(), pairDevice);
                        if (b) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("name", pairDevice.getName());
                            map.put("type", pairDevice.getBluetoothClass().getDeviceClass());
                            map.put("device", pairDevice);
                            pairTVName.setText("未配对");
                            list.add(map);
                            itemAdapter.notifyDataSetChanged();
                        } else {
                            ToastAlarm.show("取消配对失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

}