package com.jacky.uikit.adapter;

import android.database.DataSetObserver;

import java.util.ArrayList;
import java.util.List;

import static com.jacky.uikit.adapter.ViewHolder.ViewObject;

/**
 * Control ViewHolder Adapter
 *
 * @date 2016.3.1
 */
public class ViewHolderController<T> {

    private List<ViewObject<T>> mViewData;
    private List<T> mOriginalData;

    private DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            try {
                fillViewData(mOriginalData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public ViewHolderController(List<T> data) {
        mOriginalData = data;
        fillViewData(data);
    }

    public DataSetObserver getDataSetObserver() {
        return dataSetObserver;
    }

    /**
     * Convert raw data into ViewObject
     *
     * @param originalData
     */
    private void fillViewData(List<T> originalData) {
        List<ViewObject<T>> tempList = new ArrayList<ViewObject<T>>();
        if (mViewData == null) {
            mViewData = new ArrayList<ViewObject<T>>();
        }
        if (originalData != null) {
            if (!mViewData.isEmpty()) {
                for (T data : originalData) {

                    //判断是否有相同数据
                    ViewObject<T> viewObject = contain(data);

                    if (viewObject != null) {
                        tempList.add(new ViewObject<T>(data, viewObject.mExtraData));
                    } else {
                        tempList.add(new ViewObject<T>(data));
                    }

                }
            } else {
                for (T data : originalData) {
                    tempList.add(new ViewObject<T>(data));
                }
            }

            mViewData.clear();
            mViewData.addAll(tempList);
        }
    }

    private ViewObject<T> contain(T data) {
        for (ViewObject<T> viewObject : mViewData) {
            if (viewObject.mData == data) {
                return viewObject;
            }
        }
        return null;
    }

    public ViewObject<T> getViewObject(int position) {
        if (mViewData != null && position >= 0 && position < mViewData.size())
            return mViewData.get(position);
        return null;
    }

    public List<T> getListData() {
        return mOriginalData;
    }

    public List<ViewObject<T>> getViewListData() {
        return mViewData;
    }

    public int getCount() {
        if (mViewData != null) return mViewData.size();
        return 0;
    }


    public T getItem(int position) {
        if (mViewData != null && position >= 0 && position < mViewData.size())
            return mViewData.get(position).mData;
        return null;
    }


    /**
     * remove
     *
     * @param entrys
     */
    public boolean removeEntrys(List<T> entrys) {

        if (entrys == null || entrys.isEmpty()) {
            return false;
        }

        boolean needUpdate = false;
        for (T removeEntry : entrys) {
            for (int i = 0; i < mOriginalData.size(); i++) {
                T t = mOriginalData.get(i);
                if (t == removeEntry) {
                    mOriginalData.remove(t);
                    needUpdate = true;
                }
            }

            for (int i = 0; i < mViewData.size(); i++) {
                ViewObject<T> viewObject = mViewData.get(i);
                if (viewObject.mData == removeEntry) {
                    viewObject.mData = null;
                    mViewData.remove(viewObject);
                }
            }
        }

        return needUpdate;
    }

    /**
     * remove one
     *
     * @param entry
     */
    public boolean removeEntry(T entry) {
        int pos = mOriginalData.indexOf(entry);
        if (pos != -1) {
            mOriginalData.remove(entry);

            for (int i = 0; i < mViewData.size(); i++) {
                ViewObject<T> viewObject = mViewData.get(i);
                if (viewObject.mData == entry) {
                    viewObject.mData = null;
                    mViewData.remove(viewObject);
                }
            }

            return true;
        }
        return false;
    }

    /**
     * Add one
     *
     * @param entry
     */
    public boolean addEntry(T entry) {
        if (!mOriginalData.contains(entry)) {
            mOriginalData.add(entry);
            mViewData.add(new ViewObject<T>(entry));
            return true;
        }
        return false;
    }

    /**
     * Add one
     *
     * @param entry
     */
    public boolean addEntry(int index, T entry) {
        if (!mOriginalData.contains(entry)) {
            mOriginalData.add(index, entry);
            mViewData.add(index, new ViewObject<T>(entry));
            return true;
        }
        return false;
    }

    /**
     * Add a number of data
     *
     * @param entrys
     */
    public boolean addEntrys(List<T> entrys) {
        if (entrys == null || entrys.isEmpty()) {
            return false;
        }
        mOriginalData.addAll(entrys);
        for (T entry : entrys) {
            mViewData.add(new ViewObject<T>(entry));
        }
        return true;
    }


    public void release() {
        if (mOriginalData != null) {
            mOriginalData.clear();
        }
        if (mViewData != null) {
            mViewData.clear();
        }
    }
}
