package com.jacky.launcher.utils;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

public class FileCache {
	public File cacheDir;

	public FileCache(Context context) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"WoDouCache");
		} else {
			cacheDir = context.getCacheDir();
		}
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
	}

	public ArrayList<File> getFile() {
		File file[] = cacheDir.listFiles();
		ArrayList<File> list = new ArrayList<File>();
		for (int i = 0; i < file.length; i++) {
			list.add(file[i]);
		}
		return list;
	}
	
	public String getCacheDir(){
		return cacheDir.toString();
	}

	public void clear() {
		File[] files = cacheDir.listFiles();
		for (File f : files)
			f.delete();
	}

	public String getCacheSize() {
		long size = 0;
		if (cacheDir.exists()) {
			File[] files = cacheDir.listFiles();
			for (File f : files) {
				size += f.length();
			}
		}
		String cacheSize = String.valueOf(size / 1024 / 1024) + "M";
		return cacheSize;
	}

}
