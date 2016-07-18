package com.jacky.compatible.launcher.adapter;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacky.launcher.R;

import java.util.List;
import java.util.Map;

/**
 * Bluetooth Adapter
 *
 * @author jacky
 * @version v1.0
 * @since 2016.4.1
 */

public class MyBluetoothAdapter extends BaseAdapter {

    private final List<Map<String, Object>> list;
    private final Context context;
    private Holder holder;

    public MyBluetoothAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_bluetooth, null);
            holder.name = (TextView) convertView
                    .findViewById(R.id.item_bluetooth_name);
            holder.icon = (ImageView) convertView
                    .findViewById(R.id.item_bluetooth_iv);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Map<String, Object> map = list.get(position);
        String name = (String) map.get("name");
        if (name != null) {
            holder.name.setText((String) map.get("name"));
        }
        int type = (Integer) map.get("type");
        //根据设备类型 设置相应图片
        if (type > BluetoothClass.Device.PHONE_UNCATEGORIZED && type < BluetoothClass.Device.PHONE_ISDN) {
            holder.icon.setBackgroundResource(R.drawable.phone);
        } else if (type > BluetoothClass.Device.COMPUTER_UNCATEGORIZED && type < BluetoothClass.Device.COMPUTER_WEARABLE) {
            holder.icon.setBackgroundResource(R.drawable.pc);
        } else if (type > BluetoothClass.Device.TOY_UNCATEGORIZED && type < BluetoothClass.Device.TOY_GAME) {
            holder.icon.setBackgroundResource(R.drawable.handle);
        }
        return convertView;
    }

    private class Holder {
        private TextView name;
        private ImageView icon;
    }
}
