package com.jacky.catlauncher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacky.catlauncher.R;
import com.jacky.catlauncher.app.AppModel;

import java.util.List;

public class AppUninstallAdapter extends BaseAdapter {

    private final List<AppModel> appBeanList;
    private final Context context;

    public AppUninstallAdapter(Context context, List<AppModel> appBeanList) {
        this.context = context;
        this.appBeanList = appBeanList;
    }

    @Override
    public int getCount() {
        return appBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return appBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_app_uninstall, null);
            holder.name = (TextView) convertView
                    .findViewById(R.id.item_app_uninstall_name);
            holder.icon = (ImageView) convertView
                    .findViewById(R.id.item_app_uninstall_iv);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        AppModel appBean = appBeanList.get(position);
        holder.icon.setBackgroundDrawable(appBean.getIcon());
        holder.name.setText(appBean.getName());
        return convertView;
    }

    private class Holder {
        private TextView name;
        private ImageView icon;
    }
}
