package com.jacky.uikit.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * <p>Data Adapter<p/> contains ViewHolder<br/> override{@link BaseUIAdapter#newView(int)}gain new view<br/> access{@link
 * UIAdapter#findViewById(int)}find view in the viewHolder<br/>
 *
 * @version v1.0
 * @since 2016.3.1
 */
public abstract class UIAdapter<T> extends BaseUIAdapter<T, UIAdapter.UIViewHolder<T>> {

    private UIViewHolder<T> mCurrentViewHolder;

    public UIAdapter(Context context, List<T> data) {
        this(context, 0, data);
    }

    public UIAdapter(Context context, int itemLayoutResId, List<T> data) {
        super(context, itemLayoutResId, data);
    }

    @Override
    public final UIViewHolder<T> newViewHolder(int position) {
        return new UIViewHolder<T>(this, mViewHolderController);
    }

    /**
     * gain viewHolder's view
     *
     * @param viewId
     * @return
     */
    public View findViewById(int viewId) {
        if (mCurrentViewHolder != null) {
            return mCurrentViewHolder.findViewById(viewId);
        }
        return null;
    }

    public View getItemView() {
        if (mCurrentViewHolder != null) {
            return mCurrentViewHolder.getView();
        }
        return null;
    }

    private void preUpdate(UIViewHolder<T> uiViewHolder) {
        mCurrentViewHolder = uiViewHolder;
    }

    /**
     * update data
     *
     * @param position
     * @param data
     */
    public void updateView(int position, int viewType, T data, Bundle extra) {
    }

    /**
     * <p>UI ViewHolder<p/>
     *
     * @param <T>
     */
    public static class UIViewHolder<T> extends ViewHolder<T> {

        private View mCurrentConvertView;
        private UIAdapter<T> mAdapter;

        public UIViewHolder(UIAdapter<T> adapter, ViewHolderController<T> controller) {
            super(controller);
            mAdapter = adapter;
        }

        @Override
        public void create(int position, int viewType, View convertView, ViewGroup parent) {
        }

        @Override
        public void update(int position, int viewType, T obj, Bundle extra, ViewGroup itemView) {
            mAdapter.preUpdate(this);
            mCurrentConvertView = itemView;
            mAdapter.updateView(position, viewType, obj, extra);
        }

        /**
         * find view
         *
         * @param viewId
         * @return
         */
        public View findViewById(int viewId) {
            if (mCurrentConvertView != null) {
                return mCurrentConvertView.findViewById(viewId);
            }
            return null;
        }

        public View getView() {
            return mCurrentConvertView;
        }
    }

}
