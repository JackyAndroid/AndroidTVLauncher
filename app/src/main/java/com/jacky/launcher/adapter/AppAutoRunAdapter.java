package com.jacky.launcher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacky.launcher.R;
import com.jacky.launcher.model.AppBean;

import java.util.List;

public class AppAutoRunAdapter extends BaseAdapter {

    private List<AppBean> appBeanList = null;
    private Context context;
    public  static AppAutoRunHolder holder;

    public AppAutoRunAdapter(Context context, List<AppBean> appBeanList) {
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

        if (convertView == null) {
            holder = new AppAutoRunHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_app_auto_run, null);
            holder.name = (TextView) convertView
                    .findViewById(R.id.item_app_auto_run_name);
            holder.icon = (ImageView) convertView
                    .findViewById(R.id.item_app_auto_run_iv);
            holder.flag = (ImageView) convertView
                    .findViewById(R.id.item_app_auto_run_flag);
            convertView.setTag(holder);
        } else {
            holder = (AppAutoRunHolder) convertView.getTag();
        }
        AppBean appBean = appBeanList.get(position);
        holder.icon.setBackgroundDrawable(appBean.getIcon());
        holder.name.setText(appBean.getName());
        return convertView;
    }

    public class AppAutoRunHolder {
        private TextView name;
        private ImageView icon;
        private ImageView flag;
    }
}
