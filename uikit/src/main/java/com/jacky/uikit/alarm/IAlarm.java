// Copy right WeCook Inc.

package com.jacky.uikit.alarm;

import android.content.Context;

/**
 * 通知类接口
 *
 * @author jacky
 * @version v1.0
 * @since 2016.4.12
 */
public interface IAlarm {

    Context getContext();

    /**
     * 显示
     */
    void show();

    /**
     * 显示事件
     *
     * @param keepTime
     */
    void show(long keepTime);

    /**
     * 消失
     */
    void dismiss();

    /**
     * 取消显示
     */
    void cancel();

    /**
     * 设置监听
     *
     * @param listener
     */
    void setAlarmListener(AlarmListener listener);

    /**
     * 监听
     */
    interface AlarmListener {

        /**
         * 当显示成功的回调
         *
         * @param alarm
         */
        void onShow(IAlarm alarm);

        /**
         * 当消失完成的回调
         *
         * @param alarm
         */
        void onDismiss(IAlarm alarm);

        /**
         * 取消
         * 
         * @param alarm
         */
        void onCancel(IAlarm alarm);

    }
}
