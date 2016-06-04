package com.jacky.launcher.features.wifi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jacky.launcher.R;
import com.jacky.launcher.adapter.WAndB_WifilistAdapter;
import com.jacky.launcher.utils.WiFiAdmin;

import java.util.List;

public class WifiActivity extends Activity implements OnClickListener,OnItemClickListener {
	private static final int WIFI_OPEN_FINISH=1;//开启完成
	private static final int WIFI_FOUND_FINISH=0;//查找完成
	private static final int WIFI_SCAN=2;//wifi扫描
	private static final int WIFI_CLOSE=3;//关闭wifi
	private static final int WIFI_INFO=4;
	private static final int WIFI_STATE_INIT=5;//加载页面
	private ListView wifiListView;
	private WAndB_WifilistAdapter adapter;
	private List<ScanResult> scanResults;
	private WiFiAdmin wiFiAdmin;
	private Switch wifiSwitch;
	private String connectSSID ="";
	private TextView wifiStateDisplay;
	private ImageView arrowTop;
	private Dialog connectDialog;
	private int netId;//WIFI连接状态
	@SuppressLint("HandlerLeak")
	private final Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WIFI_FOUND_FINISH:
				scanResults=wiFiAdmin.GetWifilist();
				adapter.notifyDataSetChanged();
				break;
			 case WIFI_STATE_INIT:  
			    	int wifiState=wiFiAdmin.GetWifiState();
			    	if(wifiState==wiFiAdmin.getWifiManager().WIFI_STATE_DISABLED){  //wifi不可用啊
				    	wifiStateDisplay.setText("WiFi 网卡未打开");
			    	}else if(wifiState==wiFiAdmin.getWifiManager().WIFI_STATE_UNKNOWN){//wifi 状态未知
			    		wifiStateDisplay.setText("WiFi 网卡状态未知");
			    	}else if(wifiState==wiFiAdmin.getWifiManager().WIFI_STATE_ENABLED){//OK 可用
			        	wifiSwitch.setChecked(true);
			        	wiFiAdmin.StartScan();
			    		scanResults =wiFiAdmin.GetWifilist();
				        handler.sendEmptyMessageDelayed(WIFI_SCAN, 1000);

			    		if(wiFiAdmin.isWifiEnable()){
			               Toast.makeText(WifiActivity.this, "wifi已经打开", Toast.LENGTH_SHORT).show();

			    		}else { 
				             Toast.makeText(WifiActivity.this, "请 开启 wifi", Toast.LENGTH_SHORT).show();
			    		}		        	
			    	}
			    	
			    break;
			case WIFI_OPEN_FINISH:
				scanResults=wiFiAdmin.GetWifilist();
				adapter=new WAndB_WifilistAdapter(WifiActivity.this, scanResults);
				wifiListView.setAdapter(adapter);
				break;
			case  WIFI_SCAN:
				wiFiAdmin.StartScan();
				scanResults=wiFiAdmin.GetWifilist();
				wifiStateDisplay.setText("正在扫描附近的WIFI...");
				if(scanResults==null){
					handler.sendEmptyMessageDelayed(WIFI_SCAN, 1000);
				}else if(scanResults.size()==0){
					handler.sendEmptyMessageDelayed(WIFI_SCAN, 1000);
					SetScanResult();
				}else{
					wifiStateDisplay.setText("附近WiFi");
					adapter=new WAndB_WifilistAdapter(WifiActivity.this, scanResults);
					wifiListView.setAdapter(adapter);
				}
				break;
			case WIFI_CLOSE:
				SetScanResult();
				wifiStateDisplay.setText("WIFI已关闭");
				break;
			case WIFI_INFO:
				if(wiFiAdmin.GetSSID().endsWith("<unknown ssid>")||wiFiAdmin.GetSSID().endsWith("NULL")){
					wiFiAdmin.getWifiConnectInfo();
					wifiStateDisplay.setText("无WIFI连接");
					handler.sendEmptyMessageDelayed(WIFI_INFO, 2500);
				}else if(wiFiAdmin.GetSSID().equals("NULL")){
					wiFiAdmin.getWifiConnectInfo();
					wifiStateDisplay.setText("无连接,请选择合适的WiFi连接");
		    		handler.sendEmptyMessageDelayed(WIFI_INFO, 2500);
		    	}else{
		    		wiFiAdmin.getWifiConnectInfo();
		    		if(wiFiAdmin.GetIntIp().equals("")){
		    			handler.sendEmptyMessageDelayed(WIFI_INFO, 2500);
		    		}
		    		wifiStateDisplay.setText("已连接到"+wiFiAdmin.GetSSID()+"若切换有线网络请连接网线");
		    		connectDialog.dismiss();
		    		connectSSID =wiFiAdmin.GetSSID();
		    		Toast.makeText(WifiActivity.this, connectSSID, Toast.LENGTH_SHORT).show();
		    		Toast.makeText(WifiActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
		    	}
				break;
			default:
				break;
			
			}
			 super.handleMessage(msg);
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wandb_wifipager);
		 if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
			  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			 }
		 InitData();
		 handler.sendEmptyMessageDelayed(WIFI_STATE_INIT, 1000);
	}
public void InitData(){
	wiFiAdmin=new WiFiAdmin(WifiActivity.this);
	connectDialog =new AlertDialog.Builder(WifiActivity.this).create();
	wifiListView =(ListView)findViewById(R.id.wandb_wifi_listview);
	wifiSwitch =(Switch)findViewById(R.id.wifi_switch);
	arrowTop =(ImageView)findViewById(R.id.wifi_arrowtop);
	wifiStateDisplay =(TextView)findViewById(R.id.wifi_statedispaly);
	wifiListView.setOnItemClickListener(this);
	wifiListView.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if(arg2==0){
				arrowTop.setVisibility(View.INVISIBLE);
			}else{
				arrowTop.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	});
	wifiSwitch.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
		if(wifiSwitch.isChecked()){
			wiFiAdmin.OpenWifi();
			wiFiAdmin.StartScan();
			scanResults=wiFiAdmin.GetWifilist();
			handler.sendEmptyMessageDelayed(WIFI_SCAN, 1000);
			 Toast.makeText(WifiActivity.this, "打开 WiFi", Toast.LENGTH_SHORT).show();
		}else{
			wiFiAdmin.CloseWifi();
			handler.sendEmptyMessage(WIFI_CLOSE);
			Toast.makeText(WifiActivity.this, "关闭 WiFi", Toast.LENGTH_SHORT).show();
		}
		}
	});
}
public void SetScanResult(){
	wiFiAdmin.StartScan();
	scanResults=wiFiAdmin.GetWifilist();
	adapter=new WAndB_WifilistAdapter(WifiActivity.this, scanResults);
	wifiListView.setAdapter(adapter);
}
@Override
public void onClick(View arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	// TODO Auto-generated method stub
	if(GetNowWifiSSID().equals("\""+scanResults.get(arg2).SSID+"\"")){
		Toast.makeText(WifiActivity.this, "当前已连接此网络", Toast.LENGTH_SHORT).show();
		}else{
	final int num=arg2;
	LayoutInflater layoutInflater= LayoutInflater.from(WifiActivity.this);
	View view=(RelativeLayout)layoutInflater.inflate(R.layout.connect_wifi_dialog, null);
	TextView wifiName=(TextView)view.findViewById(R.id.wifidialog_name);
	wifiName.setText(scanResults.get(arg2).SSID);
	connectDialog.show();
	connectDialog.getWindow().setContentView(view);
	Window dialogwWindow= connectDialog.getWindow();
	WindowManager.LayoutParams params=dialogwWindow.getAttributes();
	dialogwWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
	params.x=0;
	params.y=0;
	params.width=750;//宽
	params.height=400;//高
	params.softInputMode=0;
	dialogwWindow.setAttributes(params);
	connectDialog.show();
	Button cancel=(Button)view.findViewById(R.id.wifi_dialog_cancel);
	Button connect=(Button)view.findViewById(R.id.wifi_dialog_connect);
	final EditText password=(EditText)view.findViewById(R.id.wifi_dialog_password);
	cancel.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			connectDialog.dismiss();
		}
	});
	connect.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			String wifiPassword = password.getText().toString();
			netId =wiFiAdmin.AddNetwork(wiFiAdmin.CreatConfiguration(scanResults.get(num).SSID, wifiPassword, 3));
			if(netId ==0){
				Toast.makeText(WifiActivity.this, "无线网卡不可用", Toast.LENGTH_LONG).show();
			}else if(netId ==1){
				Toast.makeText(WifiActivity.this, "密码错误", Toast.LENGTH_LONG).show();
			}else if(netId ==2){
				Toast.makeText(WifiActivity.this, "正在连接", Toast.LENGTH_LONG).show();
                 connectDialog.dismiss();
			}else if(netId ==-1){
				Toast.makeText(WifiActivity.this, "连接失败", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(WifiActivity.this, "正在连接", Toast.LENGTH_LONG).show();
                connectDialog.dismiss();
			}
			handler.sendEmptyMessageDelayed(WIFI_INFO, 2000);
		}
	});
	password.setOnFocusChangeListener(new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View arg0, boolean hasFocus) {
			// TODO Auto-generated method stub
			if(hasFocus){
		        connectDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
			}else{
				
			}		
		}
	});
		}
}
public String GetNowWifiSSID(){
	WifiManager mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	 WifiInfo wifiInfo = mWifi.getConnectionInfo();
	 return wifiInfo.getSSID();
}
}
