package com.jacky.compatible.launcher.views;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;

import com.jacky.launcher.R;

import java.util.LinkedList;
import java.util.Queue;


public class FocusView extends View implements AnimatorListener {

    protected String focusTag = "focus";

    protected View mAnchorView;
    protected int mOffsetX;
    protected int mOffsetY;
    protected int mOffsetWidth;
    protected int mOffsetHeight;
    protected int mDuration = 200;

    private ObjectAnimator mObjectAnimator;
    private boolean mMoving;
    private boolean mMargin;
    private MarginLayoutParams mLayoutParams;
    private Queue<View> mQueue = new LinkedList<>();

    public FocusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(false);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.FocusView);
        mOffsetX = mTypedArray.getDimensionPixelOffset(R.styleable.FocusView_layoutOffsetX, 0);
        mOffsetY = mTypedArray.getDimensionPixelOffset(R.styleable.FocusView_layoutOffsetY, 0);
        mOffsetWidth = mTypedArray.getDimensionPixelOffset(R.styleable.FocusView_offsetWidth, 0);
        mOffsetHeight = mTypedArray.getDimensionPixelOffset(R.styleable.FocusView_offsetHeight, 0);
        mDuration = mTypedArray.getInt(R.styleable.FocusView_animDuration, 0);
        mTypedArray.recycle();
    }

    public void setLayoutOffset(int offsetX, int offsetY) {
        mOffsetX = offsetX;
        mOffsetY = offsetY;
    }

    public void setSizeOffset(int offsetWidth, int offsetHeight) {
        mOffsetWidth = offsetWidth;
        mOffsetHeight = offsetHeight;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public View getAnchorView() {
        return mAnchorView;
    }

    public void setAnchorView(View anchorView) {
        if (anchorView == null) {
            return;
        }
        if (mObjectAnimator != null) {
            mObjectAnimator.end();
        }
        setVisibility(GONE);
        mAnchorView = anchorView;

        View view = anchorView;
        if (view.findViewWithTag(focusTag) != null) {
            view = view.findViewWithTag(focusTag);
        }
        mLayoutParams = (MarginLayoutParams) getLayoutParams();
        int width = view.getWidth();
        int height = view.getHeight();

        int[] location = new int[2];
        view.getLocationInWindow(location);

        mLayoutParams.width = width + mOffsetWidth;
        mLayoutParams.height = height + mOffsetHeight;
        mLayoutParams.leftMargin = location[0] - mOffsetX;
        mLayoutParams.topMargin = location[1] - mOffsetY;

        setX(mLayoutParams.leftMargin);
        setY(mLayoutParams.topMargin);
        setTranslationX(0);
        setTranslationY(0);
        super.setLayoutParams(mLayoutParams);
        setVisibility(VISIBLE);
    }

    public synchronized void moveFocus(final View targetView) {
        if (targetView == null) {
            return;
        }
        if (mMoving) {
            mQueue.offer(targetView);
            return;
        }
        mMoving = true;
        if (mObjectAnimator != null) {
            mObjectAnimator.cancel();
        }
        setVisibility(VISIBLE);
        mAnchorView = targetView;
        View view = targetView;
        if (view.findViewWithTag(focusTag) != null) {
            view = view.findViewWithTag(focusTag);
        }
        int[] location = new int[2];
        view.getLocationInWindow(location);

        int width = view.getWidth();
        int height = view.getHeight();

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", location[0] - mOffsetX);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", location[1] - mOffsetY);
        PropertyValuesHolder pvhW = PropertyValuesHolder.ofFloat("width", getWidth(), width + mOffsetWidth);
        PropertyValuesHolder pvhH = PropertyValuesHolder.ofFloat("height", getHeight(), height + mOffsetHeight);
        mObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, pvhW, pvhH, pvhX, pvhY);
        mObjectAnimator.setDuration(mDuration);
        mObjectAnimator.addListener(this);
        mObjectAnimator.start();
    }

    public synchronized void moveMarginFocus(final View targetView) {
        if (targetView == null) {
            return;
        }
        if (mMoving) {
            mQueue.offer(targetView);
            return;
        }
        mMoving = true;
        if (mObjectAnimator != null) {
            mObjectAnimator.cancel();
        }
        setVisibility(VISIBLE);
        mAnchorView = targetView;
        View view = targetView;
        if (view.findViewWithTag(focusTag) != null) {
            view = view.findViewWithTag(focusTag);
        }
        int[] location = new int[2];
        view.getLocationInWindow(location);

        int width = view.getWidth();
        int height = view.getHeight();

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("xcoor", getX(), location[0] - mOffsetX);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("ycoor", getY(), location[1] - mOffsetY);
        PropertyValuesHolder pvhW = PropertyValuesHolder.ofFloat("width", getWidth(), width + mOffsetWidth);
        PropertyValuesHolder pvhH = PropertyValuesHolder.ofFloat("height", getHeight(), height + mOffsetHeight);
        mObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, pvhW, pvhH, pvhX, pvhY);
        mObjectAnimator.setDuration(mDuration);
        mObjectAnimator.addListener(this);
        mObjectAnimator.start();
    }

    public boolean canMove() {
        return mQueue.size() < 1;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        mMoving = true;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mMoving = false;
        if (!mQueue.isEmpty()) {
            View anchorView = mQueue.poll();
            if (mMargin)
                moveMarginFocus(anchorView);
            else
                moveFocus(anchorView);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        mMoving = false;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        // TODO Auto-generated method stub
    }

    public void setXcoor(float x) {
        if (mLayoutParams != null) {
            mLayoutParams.leftMargin = (int) x;
            super.setLayoutParams(mLayoutParams);
        }
    }

    public void setYcoor(float y) {
        if (mLayoutParams != null) {
            mLayoutParams.topMargin = (int) y;
            super.setLayoutParams(mLayoutParams);
        }
    }

    public void setWidth(float width) {
        if (mLayoutParams != null && mLayoutParams.width != (int) width) {
            mLayoutParams.width = (int) width;
            super.setLayoutParams(mLayoutParams);
        }
    }

    public void setHeight(float height) {
        if (mLayoutParams != null && mLayoutParams.height != (int) height) {
            mLayoutParams.height = (int) height;
            super.setLayoutParams(mLayoutParams);
        }
    }

    public void setMargin(boolean margin) {
        mMargin = margin;
    }
}
