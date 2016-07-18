package com.jacky.compatible.launcher.features.eliminateprocess;

import java.text.DecimalFormat;

public final class TextFormater {

    private TextFormater() throws InstantiationException {
        throw new InstantiationException("This class is not created for instantiation");
    }

    public static String longtoString(long size) {
        DecimalFormat format = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "byte";
        } else if (size < (1 << 20)) // 左移20位，相当于1024 * 1024
        {
            float kSize = size >> 10; // 右移10位，相当于除以1024
            return format.format(kSize) + "KB";
        } else if (size < (1 << 30)) // 左移30位，相当于1024 * 1024 * 1024
        {
            float mSize = size >> 20;// 右移20位，相当于除以1024再除以1024
            return format.format(mSize) + "MB";
        } else if (size < (1 << 40)) {
            float gSize = size >> 30;
            return format.format(gSize) + "GB";
        } else {
            return "size error";
        }
    }

    public static String floattoString(float size) {
        if (size < 0) {
            return String.valueOf(0);
        } else {
            return size + "MB";
        }
    }
}
