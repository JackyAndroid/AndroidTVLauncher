package com.jacky.launcher.features.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jacky.launcher.R;
import com.jacky.launcher.model.AppBean;

import java.util.List;

@SuppressLint("NewApi")
public class AllApp extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    private ImageView appIcons[] = new ImageView[15];
    private LinearLayout appItems[] = new LinearLayout[15];
    int iconIds[] = {R.id.app_icon0, R.id.app_icon1, R.id.app_icon2,
            R.id.app_icon3, R.id.app_icon4, R.id.app_icon5,
            R.id.app_icon6, R.id.app_icon7, R.id.app_icon8,
            R.id.app_icon9, R.id.app_icon10, R.id.app_icon11,
            R.id.app_icon12, R.id.app_icon13, R.id.app_icon14};
    private TextView appNames[] = new TextView[15];
    int nameIds[] = {R.id.app_name0, R.id.app_name1, R.id.app_name2,
            R.id.app_name3, R.id.app_name4, R.id.app_name5,
            R.id.app_name6, R.id.app_name7, R.id.app_name8,
            R.id.app_name9, R.id.app_name10, R.id.app_name11,
            R.id.app_name12, R.id.app_name13, R.id.app_name14};
    int itemIds[] = {
            R.id.app_item0, R.id.app_item1, R.id.app_item2,
            R.id.app_item3, R.id.app_item4, R.id.app_item5,
            R.id.app_item6, R.id.app_item7, R.id.app_item8,
            R.id.app_item9, R.id.app_item10, R.id.app_item11,
            R.id.app_item12, R.id.app_item13, R.id.app_item14
    };

    private List<AppBean> mAppList = null;
    private int mPagerIndex = -1;
    private int mPagerCount = -1;

    public AllApp(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AllApp(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AllApp(Context context) {
        super(context);
        mContext = context;
    }

    public void setAppList(List<AppBean> list, int pagerIndex, int pagerCount) {
        mAppList = list;
        mPagerIndex = pagerIndex;
        mPagerCount = pagerCount;
    }

    public void managerAppInit() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_pager_layout, null);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        int itemCount = -1;
        if (mPagerIndex < mPagerCount - 1) {
            itemCount = 15;
        } else {
            itemCount = (mAppList.size() - (mPagerCount - 1) * 15);
        }
        for (int i = 0; i < itemCount; i++) {
            appIcons[i] = (ImageView) v.findViewById(iconIds[i]);
            appNames[i] = (TextView) v.findViewById(nameIds[i]);
            appIcons[i].setImageDrawable(mAppList.get(mPagerIndex * 15 + i).getIcon());
            appItems[i] = (LinearLayout) v.findViewById(itemIds[i]);
            appNames[i].setText(mAppList.get(mPagerIndex * 15 + i).getName());
            appItems[i].setVisibility(View.VISIBLE);
            appItems[i].setOnClickListener(this);
//            appItems[i].setOnFocusChangeListener(focusChangeListener);
        }
        addView(v);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View arg0) {
        int id = arg0.getId();
        int position = -1;
        switch (id) {
            case R.id.app_item0:
                position = mPagerIndex * 15 + 0;
                break;
            case R.id.app_item1:
                position = mPagerIndex * 15 + 1;
                break;
            case R.id.app_item2:
                position = mPagerIndex * 15 + 2;
                break;
            case R.id.app_item3:
                position = mPagerIndex * 15 + 3;
                break;
            case R.id.app_item4:
                position = mPagerIndex * 15 + 4;
                break;
            case R.id.app_item5:
                position = mPagerIndex * 15 + 5;
                break;
            case R.id.app_item6:
                position = mPagerIndex * 15 + 6;
                break;
            case R.id.app_item7:
                position = mPagerIndex * 15 + 7;
                break;
            case R.id.app_item8:
                position = mPagerIndex * 15 + 8;
                break;
            case R.id.app_item9:
                position = mPagerIndex * 15 + 9;
                break;
            case R.id.app_item10:
                position = mPagerIndex * 15 + 10;
                break;
            case R.id.app_item11:
                position = mPagerIndex * 15 + 11;
                break;
            case R.id.app_item12:
                position = mPagerIndex * 15 + 12;
                break;
            case R.id.app_item13:
                position = mPagerIndex * 15 + 13;
                break;
            case R.id.app_item14:
                position = mPagerIndex * 15 + 14;
                break;
            default:
                break;
        }
        if (position != -1) {
            PackageManager manager = mContext.getPackageManager();
            String packageName = mAppList.get(position).getPackageName();
            Intent intent;
            intent = manager.getLaunchIntentForPackage(packageName);
            mContext.startActivity(intent);
        }
    }
}
