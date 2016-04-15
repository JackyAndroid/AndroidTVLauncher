package com.jacky.launcher.features.speedtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ReadFileUtil {
    public static byte[] ReadFileFromURL(String URL, NetworkSpeedInfo info) {
        int FileLenth = 0;
        long startTime = 0;
        long intervalTime = 0;
        byte[] b = null;
        java.net.URL mUrl = null;
        URLConnection mUrlConnection = null;
        InputStream inputStream = null;
        try {
            mUrl = new URL(URL);
            mUrlConnection = mUrl.openConnection();
            mUrlConnection.setConnectTimeout(15000);
            mUrlConnection.setReadTimeout(15000);
            FileLenth = mUrlConnection.getContentLength();
            inputStream = mUrlConnection.getInputStream();
            NetworkSpeedInfo.totalBytes = FileLenth;
            b = new byte[FileLenth];
            startTime = System.currentTimeMillis();
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(mUrlConnection.getInputStream()));
            String line;
            byte buffer[];
            while (NetworkSpeedInfo.FILECANREAD&&((line = bufferReader.readLine()) != null)&&FileLenth>NetworkSpeedInfo.FinishBytes) {
                buffer = line.getBytes();
                intervalTime = System.currentTimeMillis() - startTime;
                NetworkSpeedInfo.FinishBytes = NetworkSpeedInfo.FinishBytes + buffer.length;
                if (intervalTime == 0) {
                    NetworkSpeedInfo.Speed = 1000;
                } else {
                    NetworkSpeedInfo.Speed = NetworkSpeedInfo.FinishBytes / intervalTime;
                    double a=(double)NetworkSpeedInfo.FinishBytes/NetworkSpeedInfo.totalBytes*100;
                    NetworkSpeedInfo.progress=(int) a;
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
