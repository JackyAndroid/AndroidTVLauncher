package com.jacky.catlauncher.detail;

/**
 * @author jacky
 * @version v1.0
 * @since 16/8/28
 */

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

public class MediaDetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object itemData) {

        if(itemData instanceof MediaModel){
            MediaModel mediaModel = (MediaModel) itemData;
            viewHolder.getSubtitle().setText(mediaModel.getTitle());
            viewHolder.getBody().setText(mediaModel.getContent());
        }
    }
}