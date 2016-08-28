
package com.jacky.catlauncher.model;

import android.graphics.drawable.Drawable;

public class TaskInfo {

    private int id;
    private int memory;
    private Boolean isCheck;
    private Boolean isSystemProcess;// 是否为系统进程
    private String name;
    private String packageName;
    private Drawable icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public Boolean getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Boolean getIsSystemProcess() {
        return isSystemProcess;
    }

    public void setIsSystemProcess(Boolean isSystemProcess) {
        this.isSystemProcess = isSystemProcess;
    }
}
