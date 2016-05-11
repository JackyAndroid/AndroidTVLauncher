package com.jacky.uikit.adapter;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

/**
 * Control Each Item of the view
 *
 * @date 2016.3.1
 */
public abstract class ViewHolder<T> {
    ViewHolderController<T> mAdapter;
	int mPosition;
	int mViewType = -1;

	public ViewHolder(ViewHolderController<T> controller) {
		mAdapter = controller;
	}

	/**
	 * Create specify the location of the view
	 *
	 * @param position
	 * @param viewType
     * @param convertView
     * @param parent
     */
	public abstract void create(int position, int viewType, View convertView, ViewGroup parent);

	/**
	 * Update specify the location of the view
	 *
     * @param position
     * @param viewType
     * @param obj
     * @param itemView
     */
	public abstract void update(int position, int viewType, T obj, Bundle extra, ViewGroup itemView);

	/**
	 * @param position
     * @param itemView
	 */
	void updateInternal(int position, int viewType, ViewGroup itemView) {
		mPosition = position;
        mViewType = viewType;
        T data = mAdapter.getItem(position);
        Bundle bundle = null;
        if (mAdapter.getViewObject(position) != null) {
            bundle = mAdapter.getViewObject(position).mExtraData;
        }

		update(position, viewType, data, bundle, itemView);
	}

	public int getPosition() {
		return mPosition;
	}

	public T getData() {
		return mAdapter.getItem(mPosition);
	}

    /**
     * Each Item of data encapsulation, provide an extension attribute configuration {@code mExtraData}
     *
     * @author kevin
     * @date 14-1-8
     */
    public static class ViewObject<T> {

        public T mData;
        public Bundle mExtraData;

        public ViewObject(T data) {
            mData = data;
            mExtraData = new Bundle();
        }

        public ViewObject(T data, Bundle bundle) {
            mData = data;
            if (bundle != null) {
                mExtraData = new Bundle(bundle);
            } else {
                mExtraData = new Bundle();
            }
        }
    }
}