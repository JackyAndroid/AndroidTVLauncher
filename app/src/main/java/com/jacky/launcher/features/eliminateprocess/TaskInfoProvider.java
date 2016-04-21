package com.jacky.launcher.features.eliminateprocess;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.jacky.launcher.model.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/*
 * by:kangzizhaung
 */
public class TaskInfoProvider {
	private PackageManager pmManager;
	private ActivityManager aManager;

	public TaskInfoProvider(Context context) {
		pmManager = context.getPackageManager();
		aManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
	}

	// 遍历传入的列表,将所有应用的信息传入taskinfo中
	public List<TaskInfo> GetAllTask(List<RunningAppProcessInfo> list) {
		List<TaskInfo> taskInfos = new ArrayList<>();
		for (RunningAppProcessInfo appProcessInfo : list) {
			TaskInfo info = new TaskInfo();
			int id = appProcessInfo.pid;
			info.setId(id);
			String packageName = appProcessInfo.processName;
			info.setPackageName(packageName);
			try {
				// ApplicationInfo是AndroidMainfest文件里面整个Application节点的封装װ
				ApplicationInfo applicationInfo = pmManager.getPackageInfo(
						packageName, 0).applicationInfo;
				Drawable icon = applicationInfo.loadIcon(pmManager);
				info.setIcon(icon);
				String name = applicationInfo.loadLabel(pmManager).toString();
				info.setName(name);
				info.setIsSystemProcess(!IsSystemApp(applicationInfo));
				android.os.Debug.MemoryInfo[] memoryInfo = aManager
						.getProcessMemoryInfo(new int[] { id });
				int memory = memoryInfo[0].getTotalPrivateDirty();
				info.setMemory(memory);
				taskInfos.add(info);
				info = null;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				info.setName(packageName);
				info.setIsSystemProcess(true);
			}
		}
		return taskInfos;
	}

	public Boolean IsSystemApp(ApplicationInfo info) {
		// 有些系统应用是可以更新的，如果用户自己下载了一个系统的应用来更新了原来的，
		// 它就不是系统应用啦，这个就是判断这种情况的
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			return true;
		}
		else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			return true;
		}
		return false;
	}
}
