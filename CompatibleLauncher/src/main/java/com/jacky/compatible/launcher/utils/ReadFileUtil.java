package com.jacky.compatible.launcher.utils;

import com.jacky.compatible.launcher.model.NetworkSpeedInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public final class ReadFileUtil {

    private ReadFileUtil() throws InstantiationException {
        throw new InstantiationException("This class is not created for instantiation");
    }

    public static byte[] ReadFileFromURL(String url, NetworkSpeedInfo info) {
        int fileLenth = 0;
        long startTime = 0;
        long intervalTime = 0;
        byte[] b = null;
        java.net.URL mUrl = null;
        URLConnection mUrlConnection = null;
        InputStream inputStream = null;
        try {
            mUrl = new URL(url);
            mUrlConnection = mUrl.openConnection();
            mUrlConnection.setConnectTimeout(15000);
            mUrlConnection.setReadTimeout(15000);
            fileLenth = mUrlConnection.getContentLength();
            inputStream = mUrlConnection.getInputStream();
            NetworkSpeedInfo.totalBytes = fileLenth;
            b = new byte[fileLenth];
            startTime = System.currentTimeMillis();
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(mUrlConnection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            byte[] buffer;
            while (NetworkSpeedInfo.FILECANREAD && ((line = bufferReader.readLine()) != null) && fileLenth > NetworkSpeedInfo.FinishBytes) {
                buffer = line.getBytes(StandardCharsets.UTF_8);
                intervalTime = System.currentTimeMillis() - startTime;
                NetworkSpeedInfo.FinishBytes = NetworkSpeedInfo.FinishBytes + buffer.length;
                if (intervalTime == 0) {
                    NetworkSpeedInfo.Speed = 1000;
                } else {
                    NetworkSpeedInfo.Speed = NetworkSpeedInfo.FinishBytes / intervalTime;
                    double a = (double) NetworkSpeedInfo.FinishBytes / NetworkSpeedInfo.totalBytes * 100;
                    NetworkSpeedInfo.progress = (int) a;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return b;
    }
}
