package com.jacky.launcher.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

public abstract class FocusedBasePositionManager {
    public static final String TAG = "FocusedBasePositionManager";
    private static final int DEFAULT_FRAME_RATE = 6;
    private static final int DEFAULT_FRAME = 1;
    public static final int FOCUS_SYNC_DRAW = 0;
    public static final int FOCUS_ASYNC_DRAW = 1;
    public static final int FOCUS_STATIC_DRAW = 2;
    public static final int STATE_IDLE = 0;
    public static final int STATE_DRAWING = 1;
    public static final int SCALED_FIXED_COEF = 1;
    public static final int SCALED_FIXED_X = 2;
    public static final int SCALED_FIXED_Y = 3;
    private boolean DEBUG = true;
    private int mCurrentFrame = DEFAULT_FRAME;
    private int mFrameRate = DEFAULT_FRAME_RATE;
    private int mFocusFrameRate = 2;
    private int mScaleFrameRate = 2;
    private float mScaleXValue = 1.0F;
    private float mScaleYValue = 1.0F;
    private int mScaledMode = SCALED_FIXED_X;
    private int mFixedScaledX = 30;
    private int mFixedScaledY = 30;
    private int mState = 0;
    private boolean mNeedDraw = false;
    private int mode = FOCUS_ASYNC_DRAW;
    private Rect mSelectedPaddingRect = new Rect();
    private Rect mManualSelectedPaddingRect = new Rect();
    protected Drawable mMySelectedDrawable = null;
    private Drawable mMySelectedDrawableShadow;
    private Rect mMySelectedPaddingRectShadow;
    private boolean mIsFirstFrame = true;
    private boolean mConstrantNotDraw = false;
    private boolean mIsLastFrame = false;
    private View mSelectedView;
    private View mLastSelectedView;
    private View mContainerView;
    private boolean mHasFocus = false;
    private boolean mTransAnimation = false;
    private Context mContext;
    private Rect mLastFocusRect;
    private Rect mFocusRect;
    private Rect mCurrentRect;
    private boolean mScaleCurrentView = true;
    private boolean mScaleLastView = true;
    private boolean mIsScale = true;

    public FocusedBasePositionManager(Context paramContext, View paramView) {
        this.mContext = paramContext;
        this.mContainerView = paramView;
    }

    public void drawFrame(Canvas paramCanvas) {
        if (FOCUS_SYNC_DRAW == this.mode) {
            drawSyncFrame(paramCanvas);
        } else if (FOCUS_ASYNC_DRAW == this.mode) {
            drawAsyncFrame(paramCanvas);
        } else if (FOCUS_STATIC_DRAW == this.mode) {
            drawStaticFrame(paramCanvas);
        }
    }

    public void setScaleMode(int paramInt) {
        this.mScaledMode = paramInt;
    }

    public void setScale(boolean paramBoolean) {
        this.mIsScale = paramBoolean;
    }

    public void setScaleCurrentView(boolean paramBoolean) {
        this.mScaleCurrentView = paramBoolean;
    }

    public void setScaleLastView(boolean paramBoolean) {
        this.mScaleLastView = paramBoolean;
    }

    public boolean isLastFrame() {
        return this.mIsLastFrame;
    }

    public void setContrantNotDraw(boolean paramBoolean) {
        this.mConstrantNotDraw = paramBoolean;
    }

    public boolean getContrantNotDraw() {
        return this.mConstrantNotDraw;
    }

    public void setFocusDrawableVisible(boolean paramBoolean1, boolean paramBoolean2) {
        this.mMySelectedDrawable.setVisible(paramBoolean1, paramBoolean2);
    }

    public void setFocusDrawableShadowVisible(boolean paramBoolean1, boolean paramBoolean2) {
        this.mMySelectedDrawableShadow.setVisible(paramBoolean1, paramBoolean2);
    }

    public void setLastSelectedView(View paramView) {
        this.mLastSelectedView = paramView;
    }

    public void setTransAnimation(boolean paramBoolean) {
        this.mTransAnimation = paramBoolean;
    }

    public void setNeedDraw(boolean paramBoolean) {
        this.mNeedDraw = paramBoolean;
    }

    public void setFocusResId(int paramInt) {
        this.mMySelectedDrawable = this.mContext.getResources().getDrawable(paramInt);
        this.mSelectedPaddingRect = new Rect();
        this.mMySelectedDrawable.getPadding(this.mSelectedPaddingRect);
    }

    public void setFocusShadowResId(int paramInt) {
        this.mMySelectedDrawableShadow = this.mContext.getResources().getDrawable(paramInt);
        this.mMySelectedPaddingRectShadow = new Rect();
        this.mMySelectedDrawableShadow.getPadding(this.mMySelectedPaddingRectShadow);
    }

    public void setItemScaleValue(float paramFloat1, float paramFloat2) {
        this.mScaleXValue = paramFloat1;
        this.mScaleYValue = paramFloat2;
    }

    public void setItemScaleFixedX(int paramInt) {
        this.mFixedScaledX = paramInt;
    }

    public void setItemScaleFixedY(int paramInt) {
        this.mFixedScaledY = paramInt;
    }

    public float getItemScaleXValue() {
        return this.mScaleXValue;
    }

    public float getItemScaleYValue() {
        return this.mScaleYValue;
    }

    public Rect getCurrentRect() {
        return this.mCurrentRect;
    }

    public void setState(int paramInt) {
        synchronized (this) {
            this.mState = paramInt;
        }
    }

    public int getState() {
        synchronized (this) {
            return this.mState;
        }
    }

    public void setManualPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        this.mManualSelectedPaddingRect.left = paramInt1;
        this.mManualSelectedPaddingRect.right = paramInt3;
        this.mManualSelectedPaddingRect.top = paramInt2;
        this.mManualSelectedPaddingRect.bottom = paramInt4;
    }

    public int getManualPaddingLeft() {
        return this.mManualSelectedPaddingRect.left;
    }

    public int getManualPaddingRight() {
        return this.mManualSelectedPaddingRect.right;
    }

    public int getManualPaddingTop() {
        return this.mManualSelectedPaddingRect.top;
    }

    public int getManualPaddingBottom() {
        return this.mManualSelectedPaddingRect.bottom;
    }

    public int getSelectedPaddingLeft() {
        return this.mSelectedPaddingRect.left;
    }

    public int getSelectedPaddingRight() {
        return this.mSelectedPaddingRect.right;
    }

    public int getSelectedPaddingTop() {
        return this.mSelectedPaddingRect.top;
    }

    public int getSelectedPaddingBottom() {
        return this.mSelectedPaddingRect.bottom;
    }

    public int getSelectedShadowPaddingLeft() {
        return this.mMySelectedPaddingRectShadow.left;
    }

    public int getSelectedShadowPaddingRight() {
        return this.mMySelectedPaddingRectShadow.right;
    }

    public int getSelectedShadowPaddingTop() {
        return this.mMySelectedPaddingRectShadow.top;
    }

    public int getSelectedShadowPaddingBottom() {
        return this.mMySelectedPaddingRectShadow.bottom;
    }

    public void setFocus(boolean paramBoolean) {
        this.mHasFocus = paramBoolean;
    }

    public boolean hasFocus() {
        return this.mHasFocus;
    }

    public void setFocusMode(int paramInt) {
        this.mode = paramInt;
    }

    public void setFrameRate(int paramInt) {
        this.mFrameRate = paramInt;
        if (paramInt % 2 == 0) {
            this.mScaleFrameRate = (paramInt / 2);
            this.mFocusFrameRate = (paramInt / 2);
        } else {
            this.mScaleFrameRate = (paramInt / 2);
            this.mFocusFrameRate = (paramInt / 2 + 1);
        }
    }

    public void setFrameRate(int paramInt1, int paramInt2) {
        this.mFrameRate = (paramInt1 + paramInt2);
        this.mScaleFrameRate = paramInt1;
        this.mFocusFrameRate = paramInt2;
    }

    public void setSelectedView(View paramView) {
        this.mSelectedView = paramView;
    }

    public View getSelectedView() {
        return this.mSelectedView;
    }

    private void drawSyncFrame(Canvas paramCanvas) {
        if (getSelectedView() != null) {
            if ((this.mCurrentFrame < this.mFrameRate) && (this.mNeedDraw)) {
                if (this.mIsFirstFrame) {
                    drawFirstFrame(paramCanvas, true, true);
                } else {
                    drawOtherFrame(paramCanvas, true, true);
                }
            } else if ((this.mCurrentFrame == this.mFrameRate) && (this.mNeedDraw)) {
                drawLastFrame(paramCanvas, true, true);
            } else if (hasFocus()) {
                Rect localRect = getDstRectBeforeScale(true);
                if (localRect != null) {
                    this.mLastFocusRect = localRect;
                    drawFocus(paramCanvas, false);
                }
            }
        } else {
        }
    }

    private void drawAsyncFrame(Canvas paramCanvas) {
        if (getSelectedView() != null) {
            boolean bool = this.mCurrentFrame > this.mFocusFrameRate;
            if ((this.mCurrentFrame < this.mFrameRate) && (this.mNeedDraw)) {
                if (this.mIsFirstFrame) {
                    drawFirstFrame(paramCanvas, bool, !bool);
                } else {
                    drawOtherFrame(paramCanvas, bool, !bool);
                }
            } else if (this.mCurrentFrame == this.mFrameRate) {
                drawLastFrame(paramCanvas, bool, !bool);
            } else if (!this.mConstrantNotDraw) {
                if (hasFocus()) {
                    Rect localRect = getDstRectBeforeScale(true);
                    if (localRect != null) {
                        this.mLastFocusRect = localRect;
                        drawFocus(paramCanvas, false);
                    }
                }
                return;
            }
        } else {
        }
    }

    private void drawStaticFrame(Canvas paramCanvas) {
        if (getSelectedView() != null) {
            if ((this.mCurrentFrame < this.mFrameRate) && (this.mNeedDraw)) {
                if (this.mIsFirstFrame) {
                    drawFirstFrame(paramCanvas, true, false);
                } else {
                    drawOtherFrame(paramCanvas, true, false);
                }
            } else if ((this.mCurrentFrame == this.mFrameRate) && (this.mNeedDraw)) {
                drawLastFrame(paramCanvas, true, false);
            } else if (!this.mConstrantNotDraw) {
                if (hasFocus()) {
                    Rect localRect = getDstRectBeforeScale(true);
                    if (localRect != null) {
                        this.mLastFocusRect = localRect;
                        drawFocus(paramCanvas, false);
                    }
                }
            }
        } else {
        }
    }

    private void drawFirstFrame(Canvas paramCanvas, boolean paramBoolean1, boolean paramBoolean2) {
        boolean bool = paramBoolean2;
        if (FOCUS_ASYNC_DRAW == this.mode) {
            bool = false;
        }
        this.mIsLastFrame = false;
        Rect localRect;
        if (bool) {
            localRect = getDstRectBeforeScale(!bool);
            if (null == localRect) {
                return;
            }
            this.mFocusRect = localRect;
        } else {
            localRect = getDstRectAfterScale(!bool);
            if (null == localRect) {
                return;
            }
            this.mFocusRect = localRect;
        }
        this.mCurrentRect = this.mFocusRect;
        drawScale(paramBoolean1);
        if (hasFocus()) {
            drawFocus(paramCanvas, paramBoolean2);
        }
        this.mIsFirstFrame = false;
        this.mCurrentFrame += 1;
        this.mContainerView.invalidate();
    }

    private void drawOtherFrame(Canvas paramCanvas, boolean paramBoolean1, boolean paramBoolean2) {
        this.mIsLastFrame = false;
        drawScale(paramBoolean1);
        if (hasFocus()) {
            drawFocus(paramCanvas, paramBoolean2);
        }
        this.mCurrentFrame += 1;
        this.mContainerView.invalidate();
    }

    private void drawLastFrame(Canvas paramCanvas, boolean paramBoolean1, boolean paramBoolean2) {
        Rect localRect = getDstRectBeforeScale(true);
        if (null == localRect) {
            return;
        }
        this.mIsLastFrame = true;
        drawScale(paramBoolean1);
        if (hasFocus()) {
            drawFocus(paramCanvas, paramBoolean2);
        }
        this.mCurrentFrame = 1;
        this.mScaleLastView = this.mScaleCurrentView;
        this.mLastSelectedView = getSelectedView();
        this.mNeedDraw = false;
        this.mIsFirstFrame = true;
        this.mLastFocusRect = localRect;
        setState(STATE_IDLE);
    }

    private void scaleSelectedView() {
        View localView = getSelectedView();
        if (localView != null) {
            float f1 = this.mScaleXValue - 1.0F;
            float f2 = this.mScaleYValue - 1.0F;
            int i = this.mFrameRate;
            int j = this.mCurrentFrame;
            if (1 == this.mode) {
                i = this.mScaleFrameRate;
                j -= this.mFocusFrameRate;
                if (j <= 0) {
                    return;
                }
            }
            float f3 = 1.0F + f1 * j / i;
            float f4 = 1.0F + f2 * j / i;
            localView.setScaleX(f3);
            localView.setScaleY(f4);
        }
    }

    private void scaleLastSelectedView() {
        if (this.mLastSelectedView != null) {
            float f1 = this.mScaleXValue - 1.0F;
            float f2 = this.mScaleYValue - 1.0F;
            int i = this.mFrameRate;
            int j = this.mCurrentFrame;
            if (1 == this.mode) {
                i = this.mScaleFrameRate;
                if (j > i) {
                    return;
                }
            }
            j = i - j;
            float f3 = 1.0F + f1 * j / i;
            float f4 = 1.0F + f2 * j / i;
            this.mLastSelectedView.setScaleX(f3);
            this.mLastSelectedView.setScaleY(f4);
        }
    }

    private void drawScale(boolean paramBoolean) {
        if ((hasFocus()) && (paramBoolean) && (this.mScaleCurrentView) && (this.mIsScale)) {
            scaleSelectedView();
        }
        if ((this.mScaleLastView) && (this.mIsScale)) {
            scaleLastSelectedView();
        }
    }

    private void drawFocus(Canvas paramCanvas, boolean paramBoolean) {
        if (this.mConstrantNotDraw) {
            return;
        }
        if ((paramBoolean) && (this.mTransAnimation) && (this.mLastFocusRect != null) && (getState() != 0) && (!isLastFrame())) {
            drawDynamicFocus(paramCanvas);
        } else {
            drawStaticFocus(paramCanvas);
        }
    }

    private void drawStaticFocus(Canvas paramCanvas) {
        float f1 = this.mScaleXValue - 1.0F;
        float f2 = this.mScaleYValue - 1.0F;
        int i = this.mFrameRate;
        int j = this.mCurrentFrame;
        float f3 = 1.0F + f1 * j / i;
        float f4 = 1.0F + f2 * j / i;
        Rect localRect = getDstRectAfterScale(true);
        if (null == localRect) {
            return;
        }
        this.mFocusRect = localRect;
        this.mCurrentRect = localRect;
        if (isLastFrame()) {
            this.mMySelectedDrawableShadow.setBounds(localRect);
            this.mMySelectedDrawableShadow.draw(paramCanvas);
            this.mMySelectedDrawableShadow.setVisible(true, true);
        } else {
            this.mMySelectedDrawable.setBounds(localRect);
            this.mMySelectedDrawable.draw(paramCanvas);
            this.mMySelectedDrawable.setVisible(true, true);
        }
        if ((this.mSelectedView != null) && (paramCanvas != null) && ((this.mState == 0) || (isLastFrame()))) {
            drawChild(paramCanvas);
        }
    }

    private void drawDynamicFocus(Canvas paramCanvas) {
        Rect localRect = new Rect();
        int i = this.mFrameRate;
        if (FOCUS_ASYNC_DRAW == this.mode) {
            i = this.mFocusFrameRate;
        }
        int j = this.mFocusRect.left - this.mLastFocusRect.left;
        int k = this.mFocusRect.right - this.mLastFocusRect.right;
        int m = this.mFocusRect.top - this.mLastFocusRect.top;
        int n = this.mFocusRect.bottom - this.mLastFocusRect.bottom;
        localRect.left = (this.mLastFocusRect.left + j * this.mCurrentFrame / i);
        localRect.right = (this.mLastFocusRect.right + k * this.mCurrentFrame / i);
        localRect.top = (this.mLastFocusRect.top + m * this.mCurrentFrame / i);
        localRect.bottom = (this.mLastFocusRect.bottom + n * this.mCurrentFrame / i);
        this.mCurrentRect = localRect;
        this.mMySelectedDrawable.setBounds(localRect);
        this.mMySelectedDrawable.draw(paramCanvas);
        this.mMySelectedDrawable.setVisible(true, true);
        if ((this.mSelectedView != null) && (paramCanvas != null) && ((this.mState == 0) || (isLastFrame()))) {
            drawChild(paramCanvas);
        }
    }

    public void computeScaleXY() {
        if ((SCALED_FIXED_X == this.mScaledMode) || (SCALED_FIXED_Y == this.mScaledMode)) {
            View localView = getSelectedView();
            int[] arrayOfInt = new int[2];
            localView.getLocationOnScreen(arrayOfInt);
            int i = localView.getWidth();
            int j = localView.getHeight();
            if (SCALED_FIXED_X == this.mScaledMode) {
                this.mScaleXValue = ((i + this.mFixedScaledX) / i);
                this.mScaleYValue = this.mScaleXValue;
            } else if (SCALED_FIXED_Y == this.mScaledMode) {
                this.mScaleXValue = ((j + this.mFixedScaledY) / j);
                this.mScaleYValue = this.mScaleXValue;
            }
        }
    }

    public abstract Rect getDstRectBeforeScale(boolean paramBoolean);

    public abstract Rect getDstRectAfterScale(boolean paramBoolean);

    public abstract void drawChild(Canvas paramCanvas);

    public static abstract interface FocusItemSelectedListener {
        public abstract void onItemSelected(View paramView1, int paramInt, boolean paramBoolean, View paramView2);
    }

    public static abstract interface PositionInterface {
        public abstract void setManualPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4);

        public abstract void setFrameRate(int paramInt);

        public abstract void setFocusResId(int paramInt);

        public abstract void setFocusShadowResId(int paramInt);

        public abstract void setItemScaleValue(float paramFloat1, float paramFloat2);

        public abstract void setFocusMode(int paramInt);

        public abstract void setFocusViewId(int paramInt);

        public abstract void setOnItemClickListener(AdapterView.OnItemClickListener paramOnItemClickListener);

        public abstract void setOnItemSelectedListener(FocusedBasePositionManager.FocusItemSelectedListener paramFocusItemSelectedListener);
    }
}