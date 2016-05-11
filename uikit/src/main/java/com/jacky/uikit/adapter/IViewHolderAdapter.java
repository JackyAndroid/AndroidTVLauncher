package com.jacky.uikit.adapter;

import java.util.List;

/**
 * <description>
 *
 * @date 16-1-6
 */
public interface IViewHolderAdapter<T,V extends ViewHolder> {
	/**
	 * Get the holder that have the child views of the layout view.
	 *
	 * @return
     * @param position
	 */
	public V newViewHolder(int position);

    public void addEntry(T data);

    public void addEntrys(List<T> datas);

    public void removeEntry(T data);

    public void removeEntrys(List<T> datas);

    public void release();
}
