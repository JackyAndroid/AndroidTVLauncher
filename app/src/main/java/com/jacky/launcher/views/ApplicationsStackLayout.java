/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jacky.launcher.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jacky.launcher.R;
import com.jacky.launcher.model.ApplicationInfo;

import java.util.List;

/**
 * The ApplicationsStackLayout is a specialized layout used for the purpose of the home screen
 * only. This layout stacks various icons in three distinct areas: the recents, the favorites
 * (or faves) and the button.
 * <p/>
 * This layout supports two different orientations: vertical and horizontal. When horizontal,
 * the areas are laid out this way:
 * <p/>
 * [RECENTS][FAVES][BUTTON]
 * <p/>
 * When vertical, the layout is the following:
 * <p/>
 * [RECENTS]
 * [FAVES]
 * [BUTTON]
 * <p/>
 * The layout operates from the "bottom up" (or from right to left.) This means that the button
 * area will first be laid out, then the faves area, then the recents. When there are too many
 * favorites, the recents area is not displayed.
 * <p/>
 * The following attributes can be set in XML:
 * <p/>
 * orientation: horizontal or vertical
 * marginLeft: the left margin of each element in the stack
 * marginTop: the top margin of each element in the stack
 * marginRight: the right margin of each element in the stack
 * marginBottom: the bottom margin of each element in the stack
 */
public class ApplicationsStackLayout extends ViewGroup implements View.OnClickListener {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private View mButton;
    private LayoutInflater mInflater;

    private int mFavoritesEnd;
    private int mFavoritesStart;

    private List<ApplicationInfo> mFavorites;
    private List<ApplicationInfo> mRecents;

    private int mOrientation = VERTICAL;

    private int mMarginLeft;
    private int mMarginTop;
    private int mMarginRight;
    private int mMarginBottom;

    private Rect mDrawRect = new Rect();

    private Drawable mBackground;
    private int mIconSize;

    public ApplicationsStackLayout(Context context) {
        super(context);
        initLayout();
    }

    public ApplicationsStackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a =
                context.obtainStyledAttributes(attrs, R.styleable.ApplicationsStackLayout);

        mOrientation = a.getInt(R.styleable.ApplicationsStackLayout_stackOrientation, VERTICAL);

        mMarginLeft = a.getDimensionPixelSize(R.styleable.ApplicationsStackLayout_marginLeft, 0);
        mMarginTop = a.getDimensionPixelSize(R.styleable.ApplicationsStackLayout_marginTop, 0);
        mMarginRight = a.getDimensionPixelSize(R.styleable.ApplicationsStackLayout_marginRight, 0);
        mMarginBottom = a.getDimensionPixelSize(R.styleable.ApplicationsStackLayout_marginBottom, 0);

        a.recycle();

        mIconSize = 42; //(int) getResources().getDimension(android.R.dimen.app_icon_size);

        initLayout();
    }

    private void initLayout() {
        mInflater = LayoutInflater.from(getContext());
        mButton = mInflater.inflate(R.layout.all_applications_button, this, false);
        addView(mButton);

        mBackground = getBackground();
        setBackgroundDrawable(null);
        setWillNotDraw(false);
    }

    /**
     * Return the current orientation, either VERTICAL (default) or HORIZONTAL.
     *
     * @return the stack orientation
     */
    public int getOrientation() {
        return mOrientation;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final Drawable background = mBackground;

        final int right = getWidth();
        final int bottom = getHeight();

        // Draw behind recents
        if (mOrientation == VERTICAL) {
            mDrawRect.set(0, 0, right, mFavoritesStart);
        } else {
            mDrawRect.set(0, 0, mFavoritesStart, bottom);
        }
        background.setBounds(mDrawRect);
        background.draw(canvas);

        // Draw behind favorites
        if (mFavoritesStart > -1) {
            if (mOrientation == VERTICAL) {
                mDrawRect.set(0, mFavoritesStart, right, mFavoritesEnd);
            } else {
                mDrawRect.set(mFavoritesStart, 0, mFavoritesEnd, bottom);
            }
            background.setBounds(mDrawRect);
            background.draw(canvas);
        }

        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("ApplicationsStackLayout can only be used with "
                    + "measure spec mode=EXACTLY");
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        removeAllApplications();

        LayoutParams layoutParams = mButton.getLayoutParams();
        final int widthSpec = MeasureSpec.makeMeasureSpec(layoutParams.width, MeasureSpec.EXACTLY);
        final int heightSpec = MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY);
        mButton.measure(widthSpec, heightSpec);

        if (mOrientation == VERTICAL) {
            layoutVertical();
        } else {
            layoutHorizontal();
        }
    }

    private void layoutVertical() {
        int childLeft = 0;
        int childTop = getHeight();

        int childWidth = mButton.getMeasuredWidth();
        int childHeight = mButton.getMeasuredHeight();

        childTop -= childHeight + mMarginBottom;
        mButton.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        childTop -= mMarginTop;
        mFavoritesEnd = childTop - mMarginBottom;

        int oldChildTop = childTop;
        childTop = stackApplications(mFavorites, childLeft, childTop);
        if (childTop != oldChildTop) {
            mFavoritesStart = childTop + mMarginTop;
        } else {
            mFavoritesStart = -1;
        }

        stackApplications(mRecents, childLeft, childTop);
    }

    private void layoutHorizontal() {
        int childLeft = getWidth();
        int childTop = 0;

        int childWidth = mButton.getMeasuredWidth();
        int childHeight = mButton.getMeasuredHeight();

        childLeft -= childWidth;
        mButton.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        childLeft -= mMarginLeft;
        mFavoritesEnd = childLeft - mMarginRight;

        int oldChildLeft = childLeft;
        childLeft = stackApplications(mFavorites, childLeft, childTop);
        if (childLeft != oldChildLeft) {
            mFavoritesStart = childLeft + mMarginLeft;
        } else {
            mFavoritesStart = -1;
        }

        stackApplications(mRecents, childLeft, childTop);
    }

    private int stackApplications(List<ApplicationInfo> applications, int childLeft, int childTop) {
        LayoutParams layoutParams;
        int widthSpec;
        int heightSpec;
        int childWidth;
        int childHeight;

        final boolean isVertical = mOrientation == VERTICAL;

        final int count = applications.size();
        for (int i = count - 1; i >= 0; i--) {
            final ApplicationInfo info = applications.get(i);
            final View view = createApplicationIcon(mInflater, this, info);

            layoutParams = view.getLayoutParams();
            widthSpec = MeasureSpec.makeMeasureSpec(layoutParams.width, MeasureSpec.EXACTLY);
            heightSpec = MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY);
            view.measure(widthSpec, heightSpec);

            childWidth = view.getMeasuredWidth();
            childHeight = view.getMeasuredHeight();

            if (isVertical) {
                childTop -= childHeight + mMarginBottom;

                if (childTop < 0) {
                    childTop += childHeight + mMarginBottom;
                    break;
                }
            } else {
                childLeft -= childWidth + mMarginRight;

                if (childLeft < 0) {
                    childLeft += childWidth + mMarginRight;
                    break;
                }
            }

            addViewInLayout(view, -1, layoutParams);

            view.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

            if (isVertical) {
                childTop -= mMarginTop;
            } else {
                childLeft -= mMarginLeft;
            }
        }

        return isVertical ? childTop : childLeft;
    }

    private void removeAllApplications() {
        final int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View view = getChildAt(i);
            if (view != mButton) {
                removeViewAt(i);
            }
        }
    }

    private View createApplicationIcon(LayoutInflater inflater,
                                       ViewGroup group, ApplicationInfo info) {

        TextView textView = (TextView) inflater.inflate(R.layout.favorite, group, false);

        info.icon.setBounds(0, 0, mIconSize, mIconSize);
        textView.setCompoundDrawables(null, info.icon, null, null);
        textView.setText(info.title);

        textView.setTag(info.intent);
        textView.setOnClickListener(this);

        return textView;
    }

    /**
     * Sets the list of favorites.
     *
     * @param applications the applications to put in the favorites area
     */
    public void setFavorites(List<ApplicationInfo> applications) {
        mFavorites = applications;
        requestLayout();
    }

    /**
     * Sets the list of recents.
     *
     * @param applications the applications to put in the recents area
     */
    public void setRecents(List<ApplicationInfo> applications) {
        mRecents = applications;
        requestLayout();
    }

    public void onClick(View v) {
        getContext().startActivity((Intent) v.getTag());
    }
}
