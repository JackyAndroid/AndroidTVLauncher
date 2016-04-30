package com.jacky.launcher.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Adapter;

import java.lang.reflect.Field;

@SuppressLint("WrongCall")
abstract class AdapterView<T extends Adapter> extends android.widget.AdapterView<Adapter> {
    protected static final int TUI_TEXT_COLOR_GREY = -6710887;
    protected static final int TUI_TEXT_COLOR_WHITE = -1;
    protected static final int TUI_TEXT_SIZE_2 = 24;
    public static final int ITEM_VIEW_TYPE_IGNORE = -1;
    public static final int ITEM_VIEW_TYPE_HEADER_OR_FOOTER = -2;

    @ViewDebug.ExportedProperty(category = "scrolling")
    private int mFirstPosition = 0;
    private int mSpecificTop;
    private int mSyncPosition;
    private long mSyncRowId = -9223372036854775808L;
    private long mSyncHeight;
    private boolean mNeedSync = false;
    private int mSyncMode;
    private int mLayoutHeight;
    private boolean mInLayout = false;
    private AdapterView.OnItemSelectedListener mOnItemSelectedListener;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener;
    private boolean mDataChanged;

    @ViewDebug.ExportedProperty(category = "list")
    private int mNextSelectedPosition = -1;
    private long mNextSelectedRowId = -9223372036854775808L;

    @ViewDebug.ExportedProperty(category = "list")
    private int mSelectedPosition = -1;
    private long mSelectedRowId = -9223372036854775808L;
    private View mEmptyView;

    @ViewDebug.ExportedProperty(category = "list")
    private int mItemCount;
    private int mOldItemCount;
    public static final int INVALID_POSITION = -1;
    public static final long INVALID_ROW_ID = -9223372036854775808L;
    private int mOldSelectedPosition = -1;
    private long mOldSelectedRowId = -9223372036854775808L;
    private boolean mDesiredFocusableState;
    private boolean mDesiredFocusableInTouchModeState;
    private AdapterView<T>.SelectionNotifier mSelectionNotifier;
    private boolean mBlockLayoutRequests = false;

    public AdapterView(Context paramContext) {
        super(paramContext);
    }

    public AdapterView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public AdapterView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener paramOnItemClickListener) {
        this.mOnItemClickListener = paramOnItemClickListener;
    }

    public boolean performItemClick(View paramView, int paramInt, long paramLong) {
        if (this.mOnItemClickListener != null) {
            playSoundEffect(0);
            if (paramView != null)
                paramView.sendAccessibilityEvent(1);
            this.mOnItemClickListener.onItemClick(this, paramView, paramInt, paramLong);
            return true;
        }
        return false;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener paramOnItemLongClickListener) {
        if (!isLongClickable())
            setLongClickable(true);
        this.mOnItemLongClickListener = paramOnItemLongClickListener;
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener paramOnItemSelectedListener) {
        this.mOnItemSelectedListener = paramOnItemSelectedListener;
    }

    public abstract T getAdapter();

    public void addView(View paramView) {
        throw new UnsupportedOperationException("addView(View) is not supported in AdapterView");
    }

    public void addView(View paramView, int paramInt) {
        throw new UnsupportedOperationException("addView(View, int) is not supported in AdapterView");
    }

    public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
        throw new UnsupportedOperationException("addView(View, LayoutParams) is not supported in AdapterView");
    }

    public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
        throw new UnsupportedOperationException("addView(View, int, LayoutParams) is not supported in AdapterView");
    }

    public void removeView(View paramView) {
        throw new UnsupportedOperationException("removeView(View) is not supported in AdapterView");
    }

    public void removeViewAt(int paramInt) {
        throw new UnsupportedOperationException("removeViewAt(int) is not supported in AdapterView");
    }

    public void removeAllViews() {
        throw new UnsupportedOperationException("removeAllViews() is not supported in AdapterView");
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        this.mLayoutHeight = getHeight();
    }

    @ViewDebug.CapturedViewProperty
    public int getSelectedItemPosition() {
        return this.mNextSelectedPosition;
    }

    @ViewDebug.CapturedViewProperty
    public long getSelectedItemId() {
        return this.mNextSelectedRowId;
    }

    public abstract View getSelectedView();

    public Object getSelectedItem() {
        Adapter localAdapter = getAdapter();
        int i = getSelectedItemPosition();
        if ((localAdapter != null) && (localAdapter.getCount() > 0) && (i >= 0))
            return localAdapter.getItem(i);
        return null;
    }

    @ViewDebug.CapturedViewProperty
    public int getCount() {
        return this.mItemCount;
    }

    public int getPositionForView(View paramView) {
        Object localObject = paramView;
        try {
            View localView;
            while (!(localView = (View) ((View) localObject).getParent()).equals(this))
                localObject = localView;
        } catch (ClassCastException localClassCastException) {
            return -1;
        }
        int i = getChildCount();
        for (int j = 0; j < i; j++)
            if (getChildAt(j).equals(localObject))
                return this.mFirstPosition + j;
        return -1;
    }

    public int getFirstVisiblePosition() {
        return this.mFirstPosition;
    }

    public int getLastVisiblePosition() {
        return this.mFirstPosition + getChildCount() - 1;
    }

    public abstract void setSelection(int paramInt);

    public void setEmptyView(View paramView) {
        this.mEmptyView = paramView;
        Adapter localAdapter = getAdapter();
        boolean bool = (localAdapter == null) || (localAdapter.isEmpty());
        updateEmptyStatus(bool);
    }

    public View getEmptyView() {
        return this.mEmptyView;
    }

    boolean isInFilterMode() {
        return false;
    }

    public void setFocusable(boolean paramBoolean) {
        Adapter localAdapter = getAdapter();
        int i = (localAdapter == null) || (localAdapter.getCount() == 0) ? 1 : 0;
        this.mDesiredFocusableState = paramBoolean;
        if (!paramBoolean)
            this.mDesiredFocusableInTouchModeState = false;
        super.setFocusable((paramBoolean) && ((i == 0) || (isInFilterMode())));
    }

    public void setFocusableInTouchMode(boolean paramBoolean) {
        Adapter localAdapter = getAdapter();
        int i = (localAdapter == null) || (localAdapter.getCount() == 0) ? 1 : 0;
        this.mDesiredFocusableInTouchModeState = paramBoolean;
        if (paramBoolean)
            this.mDesiredFocusableState = true;
        super.setFocusableInTouchMode((paramBoolean) && ((i == 0) || (isInFilterMode())));
    }

    void checkFocus() {
        Adapter localAdapter = getAdapter();
        int i = (localAdapter == null) || (localAdapter.getCount() == 0) ? 1 : 0;
        int j = (i == 0) || (isInFilterMode()) ? 1 : 0;
        super.setFocusableInTouchMode((j != 0) && (this.mDesiredFocusableInTouchModeState));
        super.setFocusable((j != 0) && (this.mDesiredFocusableState));
        if (this.mEmptyView != null)
            updateEmptyStatus((localAdapter == null) || (localAdapter.isEmpty()));
    }

    private void updateEmptyStatus(boolean paramBoolean) {
        if (isInFilterMode())
            paramBoolean = false;
        if (paramBoolean) {
            if (this.mEmptyView != null) {
                this.mEmptyView.setVisibility(0);
                setVisibility(8);
            } else {
                setVisibility(0);
            }
            if (this.mDataChanged) {
                onLayout(false, getLeft(), getTop(), getRight(), getBottom());
            }
        } else {
            if (this.mEmptyView != null)
                this.mEmptyView.setVisibility(8);
            setVisibility(0);
        }
    }

    public Object getItemAtPosition(int paramInt) {
        Adapter localAdapter = getAdapter();
        return (localAdapter == null) || (paramInt < 0) ? null : localAdapter.getItem(paramInt);
    }

    public long getItemIdAtPosition(int paramInt) {
        Adapter localAdapter = getAdapter();
        return (localAdapter == null) || (paramInt < 0) ? -9223372036854775808L : localAdapter.getItemId(paramInt);
    }

    public void setOnClickListener(View.OnClickListener paramOnClickListener) {
        throw new RuntimeException("Don't call setOnClickListener for an AdapterView. You probably want setOnItemClickListener instead");
    }

    protected void dispatchSaveInstanceState(SparseArray<Parcelable> paramSparseArray) {
        dispatchFreezeSelfOnly(paramSparseArray);
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> paramSparseArray) {
        dispatchThawSelfOnly(paramSparseArray);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.mSelectionNotifier);
    }

    void selectionChanged() {
        if (this.mOnItemSelectedListener != null)
            if ((this.mInLayout) || (this.mBlockLayoutRequests)) {
                if (this.mSelectionNotifier == null)
                    this.mSelectionNotifier = new SelectionNotifier();
                post(this.mSelectionNotifier);
            } else {
                fireOnSelected();
            }
        if ((this.mSelectedPosition != -1) && (isShown()) && (!isInTouchMode()))
            sendAccessibilityEvent(4);
    }

    private void fireOnSelected() {
        if (this.mOnItemSelectedListener == null)
            return;
        int i = getSelectedItemPosition();
        if ((i >= 0) && (i < getCount())) {
            View localView = getSelectedView();
            this.mOnItemSelectedListener.onItemSelected(this, localView, i, getAdapter().getItemId(i));
        } else {
            this.mOnItemSelectedListener.onNothingSelected(this);
        }
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
        View localView = getSelectedView();
        return (localView != null) && (localView.getVisibility() == 0) && (localView.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent));
    }

    public boolean onRequestSendAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent) {
        if (super.onRequestSendAccessibilityEvent(paramView, paramAccessibilityEvent)) {
            AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain();
            onInitializeAccessibilityEvent(localAccessibilityEvent);
            paramView.dispatchPopulateAccessibilityEvent(localAccessibilityEvent);
            paramAccessibilityEvent.appendRecord(localAccessibilityEvent);
            return true;
        }
        return false;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
        paramAccessibilityNodeInfo.setScrollable(isScrollableForAccessibility());
        View localView = getSelectedView();
        if (localView != null)
            paramAccessibilityNodeInfo.setEnabled(localView.isEnabled());
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
        super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
        paramAccessibilityEvent.setScrollable(isScrollableForAccessibility());
        View localView = getSelectedView();
        if (localView != null)
            paramAccessibilityEvent.setEnabled(localView.isEnabled());
        paramAccessibilityEvent.setCurrentItemIndex(getSelectedItemPosition());
        paramAccessibilityEvent.setFromIndex(getFirstVisiblePosition());
        paramAccessibilityEvent.setToIndex(getLastVisiblePosition());
        paramAccessibilityEvent.setItemCount(getCount());
    }

    private boolean isScrollableForAccessibility() {
        Adapter localAdapter = getAdapter();
        if (localAdapter != null) {
            int i = localAdapter.getCount();
            return (i > 0) && ((getFirstVisiblePosition() > 0) || (getLastVisiblePosition() < i - 1));
        }
        return false;
    }

    protected boolean canAnimate() {
        return (super.canAnimate()) && (this.mItemCount > 0);
    }

    void handleDataChanged() {
        int i = this.mItemCount;
        int j = 0;
        if (i > 0) {
            int k;
            int m;
            if (this.mNeedSync) {
                this.mNeedSync = false;
                k = findSyncPosition();
                if (k >= 0) {
                    m = lookForSelectablePosition(k, true);
                    if (m == k) {
                        setNextSelectedPositionInt(k);
                        j = 1;
                    }
                }
            }
            if (j == 0) {
                k = getSelectedItemPosition();
                if (k >= i)
                    k = i - 1;
                if (k < 0)
                    k = 0;
                m = lookForSelectablePosition(k, true);
                if (m < 0)
                    m = lookForSelectablePosition(k, false);
                if (m >= 0) {
                    setNextSelectedPositionInt(m);
                    checkSelectionChanged();
                    j = 1;
                }
            }
        }
        if (j == 0) {
            this.mSelectedPosition = -1;
            this.mSelectedRowId = -9223372036854775808L;
            this.mNextSelectedPosition = -1;
            this.mNextSelectedRowId = -9223372036854775808L;
            this.mNeedSync = false;
            checkSelectionChanged();
        }
    }

    void checkSelectionChanged() {
        if ((this.mSelectedPosition != this.mOldSelectedPosition) || (this.mSelectedRowId != this.mOldSelectedRowId)) {
            selectionChanged();
            this.mOldSelectedPosition = this.mSelectedPosition;
            this.mOldSelectedRowId = this.mSelectedRowId;
        }
    }

    int findSyncPosition() {
        int i = this.mItemCount;
        if (i == 0)
            return -1;
        long l1 = this.mSyncRowId;
        int j = this.mSyncPosition;
        if (l1 == -9223372036854775808L)
            return -1;
        j = Math.max(0, j);
        j = Math.min(i - 1, j);
        long l2 = SystemClock.uptimeMillis() + 100L;
        int k = j;
        int m = j;
        int n = 0;
        Adapter localAdapter = getAdapter();
        if (localAdapter == null)
            return -1;
        while (SystemClock.uptimeMillis() <= l2) {
            long l3 = localAdapter.getItemId(j);
            if (l3 == l1)
                return j;
            int i2 = m == i - 1 ? 1 : 0;
            int i1 = k == 0 ? 1 : 0;
            if ((i2 != 0) && (i1 != 0))
                break;
            if ((i1 != 0) || ((n != 0) && (i2 == 0))) {
                m++;
                j = m;
                n = 0;
            } else if ((i2 != 0) || ((n == 0) && (i1 == 0))) {
                k--;
                j = k;
                n = 1;
            }
        }
        return -1;
    }

    int lookForSelectablePosition(int paramInt, boolean paramBoolean) {
        return paramInt;
    }

    void setSelectedPositionInt(int paramInt) {
        this.mSelectedPosition = paramInt;
        this.mSelectedRowId = getItemIdAtPosition(paramInt);
    }

    void setNextSelectedPositionInt(int paramInt) {
        this.mNextSelectedPosition = paramInt;
        this.mNextSelectedRowId = getItemIdAtPosition(paramInt);
        if ((this.mNeedSync) && (this.mSyncMode == 0) && (paramInt >= 0)) {
            this.mSyncPosition = paramInt;
            this.mSyncRowId = this.mNextSelectedRowId;
        }
    }

    void rememberSyncState() {
        if (getChildCount() > 0) {
            this.mNeedSync = true;
            this.mSyncHeight = this.mLayoutHeight;
            View localView;
            if (this.mSelectedPosition >= 0) {
                localView = getChildAt(this.mSelectedPosition - this.mFirstPosition);
                this.mSyncRowId = this.mNextSelectedRowId;
                this.mSyncPosition = this.mNextSelectedPosition;
                if (localView != null)
                    this.mSpecificTop = localView.getTop();
                this.mSyncMode = 0;
            } else {
                localView = getChildAt(0);
                Adapter localAdapter = getAdapter();
                if ((this.mFirstPosition >= 0) && (this.mFirstPosition < localAdapter.getCount()))
                    this.mSyncRowId = localAdapter.getItemId(this.mFirstPosition);
                else
                    this.mSyncRowId = -1L;
                this.mSyncPosition = this.mFirstPosition;
                if (localView != null)
                    this.mSpecificTop = localView.getTop();
                this.mSyncMode = 1;
            }
        }
    }

    protected int getGroupFlags() {
        try {
            Class localClass = Class.forName("android.view.ViewGroup");
            Field localField = localClass.getDeclaredField("mGroupFlags");
            localField.setAccessible(true);
            return localField.getInt(this);
        } catch (SecurityException localSecurityException) {
            localSecurityException.printStackTrace();
        } catch (NoSuchFieldException localNoSuchFieldException) {
            localNoSuchFieldException.printStackTrace();
        } catch (IllegalArgumentException localIllegalArgumentException) {
            localIllegalArgumentException.printStackTrace();
        } catch (IllegalAccessException localIllegalAccessException) {
            localIllegalAccessException.printStackTrace();
        } catch (ClassNotFoundException localClassNotFoundException) {
            localClassNotFoundException.printStackTrace();
        }
        return 0;
    }

    protected void setGroupFlags(int paramInt) {
        try {
            Class localClass = Class.forName("android.view.ViewGroup");
            Field localField = localClass.getDeclaredField("mGroupFlags");
            localField.setAccessible(true);
            localField.setInt(this, paramInt);
        } catch (SecurityException localSecurityException) {
            localSecurityException.printStackTrace();
        } catch (NoSuchFieldException localNoSuchFieldException) {
            localNoSuchFieldException.printStackTrace();
        } catch (IllegalArgumentException localIllegalArgumentException) {
            localIllegalArgumentException.printStackTrace();
        } catch (IllegalAccessException localIllegalAccessException) {
            localIllegalAccessException.printStackTrace();
        } catch (ClassNotFoundException localClassNotFoundException) {
            localClassNotFoundException.printStackTrace();
        }
    }

    private class SelectionNotifier implements Runnable {
        private SelectionNotifier() {
        }

        public void run() {
            if (AdapterView.this.mDataChanged) {
                if (AdapterView.this.getAdapter() != null)
                    AdapterView.this.post(this);
            } else
                AdapterView.this.fireOnSelected();
        }
    }

    class AdapterDataSetObserver extends DataSetObserver {
        private Parcelable mInstanceState = null;

        AdapterDataSetObserver() {
        }

        public void onChanged() {
            AdapterView.this.mDataChanged = true;
            AdapterView.this.mOldItemCount = AdapterView.this.mItemCount;
            AdapterView.this.mItemCount = AdapterView.this.getAdapter().getCount();
            if ((AdapterView.this.getAdapter().hasStableIds()) && (this.mInstanceState != null) && (AdapterView.this.mOldItemCount == 0) && (AdapterView.this.mItemCount > 0)) {
                AdapterView.this.onRestoreInstanceState(this.mInstanceState);
                this.mInstanceState = null;
            } else {
                AdapterView.this.rememberSyncState();
            }
            AdapterView.this.checkFocus();
            AdapterView.this.requestLayout();
        }

        public void onInvalidated() {
            AdapterView.this.mDataChanged = true;
            if (AdapterView.this.getAdapter().hasStableIds())
                this.mInstanceState = AdapterView.this.onSaveInstanceState();
            AdapterView.this.mOldItemCount = AdapterView.this.mItemCount;
            AdapterView.this.mItemCount = 0;
            AdapterView.this.mSelectedPosition = -1;
            AdapterView.this.mSelectedRowId = -9223372036854775808L;
            AdapterView.this.mNextSelectedPosition = -1;
            AdapterView.this.mNextSelectedRowId = -9223372036854775808L;
            AdapterView.this.mNeedSync = false;
            AdapterView.this.checkFocus();
            AdapterView.this.requestLayout();
        }

        public void clearSavedState() {
            this.mInstanceState = null;
        }
    }

    public static class AdapterContextMenuInfo implements ContextMenu.ContextMenuInfo {
        public View targetView;
        public int position;
        public long id;

        public AdapterContextMenuInfo(View paramView, int paramInt, long paramLong) {
            this.targetView = paramView;
            this.position = paramInt;
            this.id = paramLong;
        }
    }
}

/*
 * Location: C:\Users\Administrator\Desktop\AliTvAppSdk.jar Qualified Name:
 * com.yunos.tv.app.widget.AdapterView JD-Core Version: 0.6.2
 */