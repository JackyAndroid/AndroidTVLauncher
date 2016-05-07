
package com.jacky.launcher.features.indicator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jacky.launcher.R;

import java.util.ArrayList;

public class TabIndicator extends LinearLayout implements TabButton.OnTabButtonChangeListener, TabButton.OnTabButtonClickListener {

    private Context mContext;

    private onTabChangeListener mOnTabChangeListener;
    private onTabClickListener mOnTabClickListener;

    private int mCurrentIndex;

    private ArrayList<TabButton> mTabButtonList = new ArrayList<>();

    public interface onTabChangeListener {
        void onTabChange(int index);
    }

    public interface onTabClickListener {
        void onTabClick(int index);
    }

    public void setOnTabChangeListener(onTabChangeListener onTabChangeListener) {
        mOnTabChangeListener = onTabChangeListener;
    }

    public void setOnTabClickListener(onTabClickListener onTabClickListener) {
        mOnTabClickListener = onTabClickListener;
    }

    public TabIndicator(Context context) {
        super(context);
        mContext = context;
        initUI();
    }

    public TabIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initUI();
    }

    private void initUI() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_indicator, this);

        int[] ids = {
                R.id.indicator_tab1,
                R.id.indicator_tab2,
                R.id.indicator_tab3,
        };

        for (int id : ids) {
            TabButton tabButton = (TabButton) view.findViewById(id);
            tabButton.setOnTabButtonChangeListener(this);
            tabButton.setOnTabButtonClickListener(this);
            if (!(tabButton.getVisibility() == View.GONE)) {
                mTabButtonList.add(tabButton);
            }
        }
    }

    public int getVisibleChildCount() {
        return mTabButtonList.size();
    }

    public void requestTabFocus(int index) {
        mTabButtonList.get(index).requestFocus();
    }

    private void setTabSelectedTextColor(int position) {
        for (int i = 0; i < mTabButtonList.size(); i++) {
            if (position == i) {
                mTabButtonList.get(i).setSelectedTextColor();
            } else {
                mTabButtonList.get(i).setNormalTextColor();
            }
        }
    }

    public void setNoFocusState() {
        setTabSelectedTextColor(mCurrentIndex);
    }

    public void setCurrentIndex(int index) {
        mCurrentIndex = index;
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    @Override
    public void onTabButtonClick(View v) {
        for (int i = 0; i < mTabButtonList.size(); i++) {
            if (v == mTabButtonList.get(i)) {
                mOnTabClickListener.onTabClick(i);
            }
        }
    }

    @Override
    public void onTabButtonChange(View v) {
        for (int i = 0; i < mTabButtonList.size(); i++) {
            if (v == mTabButtonList.get(i)) {
                mOnTabChangeListener.onTabChange(i);
            }
        }
    }
}
