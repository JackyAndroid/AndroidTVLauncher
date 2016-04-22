package com.jacky.launcher.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/*
 * 自定义wIFI管理类
 */
public class WiFiAdmin {
    // wifimanager对象
    private WifiManager mWifiManager;
    // wifiInfo对象
    private WifiInfo mWifiInfo;
    // 扫描出的网络连接列表
    private List<ScanResult> mWifiList;
    // 网络连接列表
    private List<WifiConfiguration> mWifiConfigurations;
    private WifiLock mwifiLock;

    public WiFiAdmin(Context context) {
        // 取得wifimannager
        mWifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        // 取得wifiinfo
        mWifiInfo = mWifiManager.getConnectionInfo();
        mWifiList = new ArrayList<ScanResult>();
        mWifiConfigurations = new ArrayList<WifiConfiguration>();
    }

    // 打开WIFI
    public void OpenWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    // 关闭WIFI
    public void CloseWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    // 得到WIFI当前状态
    public int GetWifiState() {
        return mWifiManager.getWifiState();
    }

    // 锁定wifilock
    public void AcquireWifiLock() {
        mwifiLock.acquire();
    }

    // 释放wifilock
    public void RelaseWifiLock() {
        if (mwifiLock.isHeld()) {
            mwifiLock.release();
        }
    }

    // 创建一个wifilock
    public void CreatWifilock() {
        mwifiLock = mWifiManager.createWifiLock("WIFILOCK");
    }

    // 得到配置好的网络
    public List<WifiConfiguration> getConfigurations() {
        return mWifiConfigurations;
    }

    // 指定配置好的网络进行连接
    public void ConnectConfiguration(int index) {
        // 输入的索引大于配置的索引则返回
        if (index > mWifiConfigurations.size()) {
            return;
        }
        // 连接到指定的网络
        mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId,
                true);
    }

    public void StartScan() {
        mWifiManager.startScan();
        // 得到扫描结果
        mWifiList = mWifiManager.getScanResults();
        // 得到配置好的网络连接
        mWifiConfigurations = mWifiManager.getConfiguredNetworks();
        Log.v("mWifiManager", mWifiManager + "");
        Log.v("mWifiList", mWifiList + "");
        Log.v("mWifiConfigurations", mWifiConfigurations + "");
//		String [] str=new String[mWifiList.size()];
//		String tempstring=null;
//		for(int i=0;i<mWifiList.size();i++){
//			tempstring=mWifiList.get(i).SSID;
//			if(null!=mWifiInfo&&tempstring.equals(mWifiInfo.getSSID())){
//				tempstring=tempstring+"已连接";
//			}
//			str[i]=tempstring;
//		}
    }

    public void getWifiConnectInfo() {
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    // 得到网络列表
    public List<ScanResult> GetWifilist() {
        return mWifiList;
    }

    // 查看扫描结果
    public StringBuilder CheckupScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++) {
            stringBuilder
                    .append("Index_" + new Integer(i + 1).toString() + ":");
            // 将Scanresult转换成一个字符串包
            // 其中包括:BSSID SSID capabilities frequency level
            stringBuilder.append(mWifiList.get(i).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }

    // 得到MAC地址
    public String GetMacAdress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    //得到SSID
    public String GetSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getSSID();
    }

    // 得到接入点的BSSID
    public String GetBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    // 得到ip地址
    public int GetIpAdress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    // 得打连接的ID
    public int GetNetworkID() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    // 得到wifiinfo的所有信息包
    public String GetWifiinfo() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    // 添加一个网络并连接
    public int AddNetwork(WifiConfiguration configuration) {
        int configurationId = mWifiManager.addNetwork(configuration);
        boolean b = mWifiManager.enableNetwork(configurationId, true);
        System.out.println("configurationId---------->" + configurationId);
        System.out.println("b---------->" + b);
        return configurationId;
    }

    // 断开指定ID的网络
    public void disconnectWifi(int networkid) {
        mWifiManager.disableNetwork(networkid);
        mWifiManager.disconnect();
    }

    public WifiConfiguration CreatConfiguration(String ssid, String password,
                                                int type) {
        WifiConfiguration configuration = new WifiConfiguration();
        configuration.allowedAuthAlgorithms.clear();
        configuration.allowedGroupCiphers.clear();
        configuration.allowedKeyManagement.clear();
        configuration.allowedPairwiseCiphers.clear();
        configuration.allowedProtocols.clear();
        configuration.SSID = "\"" + ssid + "\"";
        WifiConfiguration tempConfiguration = IsExits(ssid, mWifiManager);
        if (tempConfiguration != null) {
            mWifiManager.removeNetwork(tempConfiguration.networkId);
        }
        // WIFICIPHER_NOPASS
        if (type == 1) {
            configuration.wepKeys[0] = "";
            configuration.allowedKeyManagement
                    .set(WifiConfiguration.KeyMgmt.NONE);
            configuration.wepTxKeyIndex = 0;
        }
        // WIFICIPHER_WEP
        if (type == 2) {
            configuration.hiddenSSID = true;
            configuration.wepKeys[0] = "\"" + password + "\"";
            configuration.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
            configuration.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.CCMP);
            configuration.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.TKIP);
            configuration.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP40);
            configuration.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);
            configuration.allowedKeyManagement
                    .set(WifiConfiguration.KeyMgmt.NONE);
            configuration.wepTxKeyIndex = 0;
        }
        // WIFICIPHER_WPA
        if (type == 3) {
            configuration.preSharedKey = "\"" + password + "\"";
            configuration.hiddenSSID = true;
            configuration.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            configuration.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.TKIP);
            configuration.allowedKeyManagement
                    .set(WifiConfiguration.KeyMgmt.WPA_PSK);
            configuration.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            configuration.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.CCMP);
            configuration.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            configuration.status = WifiConfiguration.Status.ENABLED;
        }
        return configuration;
    }

    //判断wifi是否存在
    private static WifiConfiguration IsExits(String ssid, WifiManager manager) {
        List<WifiConfiguration> exitsConfigurations = manager
                .getConfiguredNetworks();
        for (WifiConfiguration configuration : exitsConfigurations) {
            if (configuration.SSID.equals("\"" + ssid + "\"")) {
                return configuration;
            }
        }
        return null;
    }

    public WifiManager getWifiManager() {
        return mWifiManager;
    }

    public boolean isWifiEnable() {
        return mWifiManager.isWifiEnabled();
    }
//转换IP地址

    public String GetIntIp() {
        int i = GetIpAdress();
        if (i == 0) {
            return "";
        }
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + ((i >> 24) & 0xFF);
    }
}
