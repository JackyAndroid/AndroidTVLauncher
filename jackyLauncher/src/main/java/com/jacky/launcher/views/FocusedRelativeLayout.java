package com.jacky.launcher.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.util.HashMap;
import java.util.Map;

/**
 * 色块高亮布局，但是有焦点问题存在，目前项目没有采用
 */

public class FocusedRelativeLayout extends RelativeLayout implements FocusedBasePositionManager.PositionInterface {
    public static final String TAG = "FocusedRelativeLayout";
    public static final int HORIZONTAL_SINGEL = 1;
    public static final int HORIZONTAL_FULL = 2;
    private long KEY_INTERVEL = 20L;
    private long mKeyTime = 0L;
    public int mIndex = -1;
    private boolean mOutsieScroll = false;
    private boolean mInit = false;
    private HotScroller mScroller;
    private int mScreenWidth;
    private int mViewRight = 20;
    private int mViewLeft = 0;
    private int mStartX;
    private long mScrollTime = 0L;
    private int mHorizontalMode = -1;
    private FocusedBasePositionManager.FocusItemSelectedListener mOnItemSelectedListener = null;
    private FocusedLayoutPositionManager mPositionManager;
    private OnScrollListener mScrollerListener = null;
    private int mLastScrollState = 0;
    private Map<View, NodeInfo> mNodeMap = new HashMap();
    private boolean isKeyDown = false;

    public FocusedRelativeLayout(Context paramContext) {
        super(paramContext);
        setChildrenDrawingOrderEnabled(true);
        this.mScroller = new HotScroller(paramContext, new DecelerateInterpolator());
        this.mScreenWidth = paramContext.getResources().getDisplayMetrics().widthPixels;
        this.mPositionManager = new FocusedLayoutPositionManager(paramContext, this);
    }

    public FocusedRelativeLayout(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        setChildrenDrawingOrderEnabled(true);
        this.mScroller = new HotScroller(paramContext, new DecelerateInterpolator());
        this.mScreenWidth = paramContext.getResources().getDisplayMetrics().widthPixels;
        this.mPositionManager = new FocusedLayoutPositionManager(paramContext, this);
    }

    public FocusedRelativeLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        setChildrenDrawingOrderEnabled(true);
        this.mScroller = new HotScroller(paramContext, new DecelerateInterpolator());
        this.mScreenWidth = paramContext.getResources().getDisplayMetrics().widthPixels;
        this.mPositionManager = new FocusedLayoutPositionManager(paramContext, this);
    }

    public void setManualPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        this.mPositionManager.setManualPadding(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    public void setFrameRate(int paramInt) {
        this.mPositionManager.setFrameRate(paramInt);
    }

    public void setFrameRate(int paramInt1, int paramInt2) {
        this.mPositionManager.setFrameRate(paramInt1, paramInt2);
    }

    public void setScaleMode(int paramInt) {
        this.mPositionManager.setScaleMode(paramInt);
    }

    public void setScale(boolean paramBoolean) {
        this.mPositionManager.setScale(paramBoolean);
    }

    public void setFocusResId(int paramInt) {
        this.mPositionManager.setFocusResId(paramInt);
    }

    public void setFocusShadowResId(int paramInt) {
        this.mPositionManager.setFocusShadowResId(paramInt);
    }

    public void setItemScaleValue(float paramFloat1, float paramFloat2) {
        this.mPositionManager.setItemScaleValue(paramFloat1, paramFloat2);
    }

    public void setScrollerListener(OnScrollListener paramOnScrollListener) {
        this.mScrollerListener = paramOnScrollListener;
    }

    public void setOnItemSelectedListener(FocusedBasePositionManager.FocusItemSelectedListener paramFocusItemSelectedListener) {
        this.mOnItemSelectedListener = paramFocusItemSelectedListener;
    }

    private void performItemSelect(View paramView, boolean paramBoolean) {
        if (this.mOnItemSelectedListener != null)
            this.mOnItemSelectedListener.onItemSelected(paramView, -1, paramBoolean, this);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener paramOnItemClickListener) {
    }

    public void setItemScaleFixedX(int paramInt) {
        this.mPositionManager.setItemScaleFixedX(paramInt);
    }

    public void setItemScaleFixedY(int paramInt) {
        this.mPositionManager.setItemScaleFixedY(paramInt);
    }

    public void setFocusMode(int paramInt) {
        this.mPositionManager.setFocusMode(paramInt);
    }

    public void setFocusViewId(int paramInt) {
    }

    public void setHorizontalMode(int paramInt) {
        this.mHorizontalMode = paramInt;
    }

    private void setInit(boolean paramBoolean) {
        synchronized (this) {
            this.mInit = paramBoolean;
        }
    }

    private boolean isInit() {
        synchronized (this) {
            return this.mInit;
        }
    }

    public void setViewRight(int paramInt) {
        this.mViewRight = paramInt;
    }

    public void setViewLeft(int paramInt) {
        this.mViewLeft = paramInt;
    }

    public void setOutsideSroll(boolean paramBoolean) {
        this.mScrollTime = System.currentTimeMillis();
        this.mOutsieScroll = paramBoolean;
    }

    protected int getChildDrawingOrder(int paramInt1, int paramInt2) {
        int i = this.mIndex;
        if (i < 0)
            return paramInt2;
        if (paramInt2 < i)
            return paramInt2;
        if (paramInt2 >= i)
            return paramInt1 - 1 - paramInt2 + i;
        return paramInt2;
    }

    private synchronized void init() {
        if ((hasFocus()) && (!this.mOutsieScroll) && (!isInit())) {
            int[] arrayOfInt = new int[2];
            int i = 65536;
            for (int j = 0; j < getChildCount(); j++) {
                View localView = getChildAt(j);
                if (!this.mNodeMap.containsKey(localView)) {
                    NodeInfo localNodeInfo = new NodeInfo();
                    localNodeInfo.index = j;
                    this.mNodeMap.put(localView, localNodeInfo);
                }
                localView.getLocationOnScreen(arrayOfInt);
                if (arrayOfInt[0] < i)
                    i = arrayOfInt[0];
            }
            this.mStartX = i;
            setInit(true);
        }
    }

    public void release() {
        this.mNodeMap.clear();
    }

    public void dispatchDraw(Canvas paramCanvas) {
        super.dispatchDraw(paramCanvas);
        if (0 == getVisibility())
            this.mPositionManager.drawFrame(paramCanvas);
    }

    protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect) {
        synchronized (this) {
            this.mKeyTime = System.currentTimeMillis();
        }
        this.mPositionManager.setFocus(paramBoolean);
        this.mPositionManager.setTransAnimation(true);
        this.mPositionManager.setNeedDraw(true);
        this.mPositionManager.setState(1);
        if (!paramBoolean) {
            this.mPositionManager.drawFrame(null);
            this.mPositionManager.setFocusDrawableVisible(false, true);
            invalidate();
        } else {
            if (-1 == this.mIndex) {
                this.mIndex = 0;
                this.mPositionManager.setSelectedView(getSelectedView());
            }
            View v = getSelectedView();
            if ((v instanceof ScalePostionInterface)) {
                ScalePostionInterface localScalePostionInterface = (ScalePostionInterface) v;
                this.mPositionManager.setScaleCurrentView(localScalePostionInterface.getIfScale());
            }
            this.mPositionManager.setLastSelectedView(null);
            invalidate();
        }
    }

    public void getFocusedRect(Rect paramRect) {
        View localView = getSelectedView();
        if (localView != null) {
            localView.getFocusedRect(paramRect);
            offsetDescendantRectToMyCoords(localView, paramRect);
            return;
        }
        super.getFocusedRect(paramRect);
    }

    public View getSelectedView() {
        int i = this.mIndex;
        return getChildAt(i);
    }

    public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
        if (((23 == paramInt) || (96 == paramInt)) && (this.isKeyDown) && (getSelectedView() != null))
            getSelectedView().performClick();
        this.isKeyDown = false;
        return super.onKeyUp(paramInt, paramKeyEvent);
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (paramKeyEvent.getRepeatCount() == 0)
            this.isKeyDown = true;
        synchronized (this) {
            if ((System.currentTimeMillis() - this.mKeyTime <= this.KEY_INTERVEL) || (this.mPositionManager.getState() == 1) || (System.currentTimeMillis() - this.mScrollTime < 100L) || (!this.mScroller.isFinished())) {
                return true;
            }
            this.mKeyTime = System.currentTimeMillis();
        }
        if (!isInit()) {
            init();
            return true;
        }
        View v = getSelectedView();
        NodeInfo localNodeInfo1 = (NodeInfo) this.mNodeMap.get(v);
        View localView = null;
        int i;
        switch (paramInt) {
            case 21:
                if (localNodeInfo1.fromLeft != null)
                    localView = localNodeInfo1.fromLeft;
                else
                    localView = ((View) v).focusSearch(17);
                i = 17;
                break;
            case 22:
                if (localNodeInfo1.fromRight != null)
                    localView = localNodeInfo1.fromRight;
                else
                    localView = ((View) v).focusSearch(66);
                i = 66;
                break;
            case 20:
                if (localNodeInfo1.fromDown != null)
                    localView = localNodeInfo1.fromDown;
                else
                    localView = ((View) v).focusSearch(130);
                i = 130;
                break;
            case 19:
                if (localNodeInfo1.fromUp != null)
                    localView = localNodeInfo1.fromUp;
                else
                    localView = ((View) v).focusSearch(33);
                i = 33;
                break;
            default:
                return super.onKeyDown(paramInt, paramKeyEvent);
        }
        if ((localView != null) && (this.mNodeMap.containsKey(localView))) {
            NodeInfo localNodeInfo2 = (NodeInfo) this.mNodeMap.get(localView);
            this.mIndex = localNodeInfo2.index;
            if (v != null) {
                ((View) v).setSelected(false);
                performItemSelect((View) v, false);
                View.OnFocusChangeListener localObject2 = ((View) v).getOnFocusChangeListener();
                if (localObject2 != null)
                    ((View.OnFocusChangeListener) localObject2).onFocusChange((View) v, false);
            }
            Object localObject2 = getSelectedView();
            localObject2 = getSelectedView();
            if (localObject2 != null) {
                ((View) localObject2).setSelected(true);
                performItemSelect((View) localObject2, true);
                View.OnFocusChangeListener localOnFocusChangeListener = ((View) localObject2).getOnFocusChangeListener();
                if (localOnFocusChangeListener != null)
                    localOnFocusChangeListener.onFocusChange((View) localObject2, true);
            }
            switch (paramInt) {
                case 21:
                    localNodeInfo2.fromRight = ((View) v);
                    break;
                case 22:
                    localNodeInfo2.fromLeft = ((View) v);
                    break;
                case 20:
                    localNodeInfo2.fromUp = ((View) v);
                    break;
                case 19:
                    localNodeInfo2.fromDown = ((View) v);
                default:
                    break;
            }
            boolean bool = true;
            if ((localObject2 instanceof ScalePostionInterface)) {
                ScalePostionInterface localScalePostionInterface = (ScalePostionInterface) localObject2;
                bool = localScalePostionInterface.getIfScale();
            }
            this.mPositionManager.setSelectedView(getSelectedView());
            this.mPositionManager.computeScaleXY();
            this.mPositionManager.setScaleCurrentView(bool);
            horizontalScroll();
            this.mPositionManager.setTransAnimation(true);
            this.mPositionManager.setNeedDraw(true);
            this.mPositionManager.setState(1);
            invalidate();
        } else {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(i));
            return super.onKeyDown(paramInt, paramKeyEvent);
        }
        playSoundEffect(SoundEffectConstants.getContantForFocusDirection(i));
        return true;
    }

    private void horizontalScroll() {
        if (1 == this.mHorizontalMode)
            scrollSingel();
        else if (2 == this.mHorizontalMode)
            scrollFull();
    }

    void scrollFull() {
        int[] arrayOfInt = new int[2];
        getSelectedView().getLocationOnScreen(arrayOfInt);
        int i = arrayOfInt[0];
        int j = arrayOfInt[0] + getSelectedView().getWidth();
        int k = getSelectedView().getWidth();
        i = (int) (i + (1.0D - this.mPositionManager.getItemScaleXValue()) * k / 2.0D);
        j = (int) (i + k * this.mPositionManager.getItemScaleXValue());
        getLocationOnScreen(arrayOfInt);
        int m;
        int n;
        if ((j - this.mScreenWidth > 3) && (!this.mOutsieScroll)) {
            m = i - this.mStartX - this.mViewLeft;
            if (m + this.mScroller.getFinalX() > arrayOfInt[0] + getWidth())
                m = arrayOfInt[0] + getWidth() - this.mScroller.getFinalX();
            n = m * 100 / 300;
            smoothScrollBy(m, n);
            return;
        }
        if ((this.mStartX - i > 3) && (!this.mOutsieScroll)) {
            m = j - this.mScreenWidth;
            if (this.mScroller.getCurrX() < Math.abs(m))
                m = -this.mScroller.getCurrX();
            n = -m * 100 / 300;
            smoothScrollBy(m, n);
        }
    }

    void scrollSingel() {
        int[] arrayOfInt = new int[2];
        getSelectedView().getLocationOnScreen(arrayOfInt);
        int i = arrayOfInt[0];
        int j = arrayOfInt[0] + getSelectedView().getWidth();
        int k = getSelectedView().getWidth();
        i = (int) (i + (1.0D - this.mPositionManager.getItemScaleXValue()) * k / 2.0D);
        j = (int) (i + k * this.mPositionManager.getItemScaleXValue());
        int m;
        if ((j >= this.mScreenWidth) && (!this.mOutsieScroll)) {
            m = j - this.mScreenWidth + this.mViewRight;
            smoothScrollBy(m, 100);
            return;
        }
        getLocationOnScreen(arrayOfInt);
        if ((i < this.mStartX) && (!this.mOutsieScroll)) {
            m = i - this.mStartX;
            if (this.mScroller.getCurrX() > Math.abs(m))
                smoothScrollBy(m, 100);
            else
                smoothScrollBy(-this.mScroller.getCurrX(), 100);
        }
    }

    private boolean containView(View paramView) {
        Rect localRect1 = new Rect();
        Rect localRect2 = new Rect();
        getGlobalVisibleRect(localRect1);
        paramView.getGlobalVisibleRect(localRect2);
        return (localRect1.left <= localRect2.left) && (localRect1.right >= localRect2.right) && (localRect1.top <= localRect2.top) && (localRect1.bottom >= localRect2.bottom);
    }

    public void smoothScrollTo(int paramInt1, int paramInt2) {
        int i = paramInt1 - this.mScroller.getFinalX();
        smoothScrollBy(i, paramInt2);
    }

    public void smoothScrollBy(int paramInt1, int paramInt2) {
        this.mScroller.startScroll(this.mScroller.getFinalX(), this.mScroller.getFinalY(), paramInt1, this.mScroller.getFinalY(), paramInt2);
        reportScrollStateChange(2);
        invalidate();
    }

    void reportScrollStateChange(int paramInt) {
        if ((paramInt != this.mLastScrollState) && (this.mScrollerListener != null)) {
            this.mLastScrollState = paramInt;
            this.mScrollerListener.onScrollStateChanged(this, paramInt);
        }
    }

    private boolean checkFocusPosition() {
        if ((null == this.mPositionManager.getCurrentRect()) || (!hasFocus()))
            return false;
        Rect localRect = this.mPositionManager.getDstRectAfterScale(true);
        return (Math.abs(localRect.left - this.mPositionManager.getCurrentRect().left) > 5) || (Math.abs(localRect.right - this.mPositionManager.getCurrentRect().right) > 5)
                || (Math.abs(localRect.top - this.mPositionManager.getCurrentRect().top) > 5) || (Math.abs(localRect.bottom - this.mPositionManager.getCurrentRect().bottom) > 5);
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
        }
        if (this.mScroller.isFinished())
            reportScrollStateChange(0);
        super.computeScroll();
    }

    public static abstract interface OnScrollListener {
        public static final int SCROLL_STATE_IDLE = 0;
        public static final int SCROLL_STATE_TOUCH_SCROLL = 1;
        public static final int SCROLL_STATE_FLING = 2;

        public abstract void onScrollStateChanged(ViewGroup paramViewGroup, int paramInt);

        public abstract void onScroll(ViewGroup paramViewGroup, int paramInt1, int paramInt2, int paramInt3);
    }

    class FocusedLayoutPositionManager extends FocusedBasePositionManager {
        public FocusedLayoutPositionManager(Context paramView, View arg3) {
            super(paramView, arg3);
        }

        public Rect getDstRectBeforeScale(boolean paramBoolean) {
            View localView = getSelectedView();
            if (null == localView)
                return null;
            Rect localRect1 = new Rect();
            Rect localRect2 = new Rect();
            if ((localView instanceof FocusedRelativeLayout.ScalePostionInterface)) {
                FocusedRelativeLayout.ScalePostionInterface localScalePostionInterface = (FocusedRelativeLayout.ScalePostionInterface) localView;
                if (localScalePostionInterface.getIfScale())
                    localRect1 = localScalePostionInterface.getScaledRect(getItemScaleXValue(), getItemScaleYValue(), true);
                else
                    localRect1 = localScalePostionInterface.getScaledRect(getItemScaleXValue(), getItemScaleYValue(), false);
            } else {
                localView.getGlobalVisibleRect(localRect1);
                int i = localRect1.right - localRect1.left;
                int j = localRect1.bottom - localRect1.top;
                if (!paramBoolean) {
                    localRect1.left = ((int) (localRect1.left + (1.0D - getItemScaleXValue()) * i / 2.0D));
                    localRect1.top = ((int) (localRect1.top + (1.0D - getItemScaleYValue()) * j / 2.0D));
                    localRect1.right = ((int) (localRect1.left + i * getItemScaleXValue()));
                    localRect1.bottom = ((int) (localRect1.top + j * getItemScaleYValue()));
                }
            }
            FocusedRelativeLayout.this.getGlobalVisibleRect(localRect2);
            localRect1.left -= localRect2.left;
            localRect1.right -= localRect2.left;
            localRect1.top -= localRect2.top;
            localRect1.bottom -= localRect2.top;
            localRect1.left += FocusedRelativeLayout.this.mScroller.getCurrX();
            localRect1.right += FocusedRelativeLayout.this.mScroller.getCurrX();
            localRect1.top -= getSelectedPaddingTop();
            localRect1.left -= getSelectedPaddingLeft();
            localRect1.right += getSelectedPaddingRight();
            localRect1.bottom += getSelectedPaddingBottom();
            localRect1.left += getManualPaddingLeft();
            localRect1.right += getManualPaddingRight();
            localRect1.top += getManualPaddingTop();
            localRect1.bottom += getManualPaddingBottom();
            return localRect1;
        }

        public Rect getDstRectAfterScale(boolean paramBoolean) {
            View localView = getSelectedView();
            if (null == localView)
                return null;
            Rect localRect1 = new Rect();
            Rect localRect2 = new Rect();
            if ((localView instanceof FocusedRelativeLayout.ScalePostionInterface)) {
                FocusedRelativeLayout.ScalePostionInterface localScalePostionInterface = (FocusedRelativeLayout.ScalePostionInterface) localView;
                localRect1 = localScalePostionInterface.getScaledRect(getItemScaleXValue(), getItemScaleYValue(), false);
            } else {
                localView.getGlobalVisibleRect(localRect1);
            }
            FocusedRelativeLayout.this.getGlobalVisibleRect(localRect2);
            localRect1.left -= localRect2.left;
            localRect1.right -= localRect2.left;
            localRect1.top -= localRect2.top;
            localRect1.bottom -= localRect2.top;
            localRect1.left += FocusedRelativeLayout.this.mScroller.getCurrX();
            localRect1.right += FocusedRelativeLayout.this.mScroller.getCurrX();
            if ((paramBoolean) && (isLastFrame())) {
                localRect1.top -= getSelectedShadowPaddingTop();
                localRect1.left -= getSelectedShadowPaddingLeft();
                localRect1.right += getSelectedShadowPaddingRight();
                localRect1.bottom += getSelectedShadowPaddingBottom();
            } else {
                localRect1.top -= getSelectedPaddingTop();
                localRect1.left -= getSelectedPaddingLeft();
                localRect1.right += getSelectedPaddingRight();
                localRect1.bottom += getSelectedPaddingBottom();
            }
            localRect1.left += getManualPaddingLeft();
            localRect1.right += getManualPaddingRight();
            localRect1.top += getManualPaddingTop();
            localRect1.bottom += getManualPaddingBottom();
            return localRect1;
        }

        public void drawChild(Canvas paramCanvas) {
        }
    }

    class NodeInfo {
        public int index;
        public View fromLeft;
        public View fromRight;
        public View fromUp;
        public View fromDown;

        NodeInfo() {
        }
    }

    public static abstract interface ScalePostionInterface {
        public abstract Rect getScaledRect(float paramFloat1, float paramFloat2, boolean paramBoolean);

        public abstract boolean getIfScale();
    }

    class HotScroller extends Scroller {
        public HotScroller(Context paramInterpolator, Interpolator paramBoolean, boolean arg4) {
            super(paramInterpolator, paramBoolean, arg4);
        }

        public HotScroller(Context paramInterpolator, Interpolator arg3) {
            super(paramInterpolator, arg3);
        }

        public HotScroller(Context arg2) {
            super(arg2);
        }

        public boolean computeScrollOffset() {
            boolean bool1 = isFinished();
            boolean bool2 = FocusedRelativeLayout.this.checkFocusPosition();
            if ((FocusedRelativeLayout.this.mOutsieScroll) || (!bool1) || (bool2))
                FocusedRelativeLayout.this.invalidate();
            FocusedRelativeLayout.this.init();
            return super.computeScrollOffset();
        }
    }
}