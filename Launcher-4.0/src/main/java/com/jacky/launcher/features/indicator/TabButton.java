
package com.jacky.launcher.features.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jacky.launcher.R;


public class TabButton extends RelativeLayout implements View.OnFocusChangeListener, View.OnClickListener {
    private String mTitle;
    private TextView mTitleView;
    private Context mContext;

    private OnTabButtonChangeListener mOnTabButtonChangeListener;
    private OnTabButtonClickListener mOnTabButtonClickListener;

    public TabButton(Context context) {
        super(context);
    }

    public TabButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.tabButton);
        mTitle = typedArray.getString(R.styleable.tabButton_buttonTitle);
        typedArray.recycle();
        mContext = context;
        initUI();
    }

    public interface OnTabButtonChangeListener {
        void onTabButtonChange(View v);
    }

    public interface OnTabButtonClickListener {
        void onTabButtonClick(View v);
    }

    public void setOnTabButtonChangeListener(OnTabButtonChangeListener onTabButtonChangeListener) {
        mOnTabButtonChangeListener = onTabButtonChangeListener;
    }

    public void setOnTabButtonClickListener(OnTabButtonClickListener OnTabButtonClickListener) {
        mOnTabButtonClickListener = OnTabButtonClickListener;
    }

    private void initUI() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_indicator_item, this);
        setOnFocusChangeListener(this);
        setOnClickListener(this);
        mTitleView = (TextView) view.findViewById(R.id.tab_text);
        mTitleView.setText(mTitle);
    }

    public void setSelectedTextColor() {
        mTitleView.setTextColor(getResources().getColor(R.color.font_blue));
        mTitleView.setTypeface(null, Typeface.BOLD);
    }

    public void setNormalTextColor() {
        mTitleView.setTextColor(getResources().getColor(R.color.font_dark));
        mTitleView.setTypeface(null, Typeface.NORMAL);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == this) {
            if (hasFocus) {
                mOnTabButtonChangeListener.onTabButtonChange(v);
                mTitleView.setTextColor(Color.WHITE);
                mTitleView.setTypeface(null, Typeface.BOLD);
            } else {
                mTitleView.setTextColor(getResources().getColor(R.color.font_dark));
                mTitleView.setTypeface(null, Typeface.NORMAL);
            }
        }
    }

    @Override
    public void onClick(View v) {
        mOnTabButtonClickListener.onTabButtonClick(v);
    }
}
