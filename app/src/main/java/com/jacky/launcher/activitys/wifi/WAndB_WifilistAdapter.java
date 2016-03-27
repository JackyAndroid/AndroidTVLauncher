package com.jacky.launcher.activitys.wifi;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacky.launcher.R;

import java.util.List;

public class WAndB_WifilistAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<ScanResult> scanResults;
	private Viewholder viewholder;
	private Activity context;
	public WAndB_WifilistAdapter(Activity context,List<ScanResult> scanResults){
		inflater= LayoutInflater.from(context);
		this.scanResults=scanResults;
		this.context=context;
	}
	@Override
	public int getCount() {
		if(scanResults.size()==0){
			return 0;
		}
		return scanResults.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		if(view==null){
			viewholder=new Viewholder();
			view=inflater.inflate(R.layout.wandb_wifilist_item, null);
			viewholder.WifiName=(TextView)view.findViewById(R.id.wannb_wifilist_item_wifiname);
			view.setTag(viewholder);
		}else{
			viewholder=(Viewholder)view.getTag();
		}
		viewholder.WifiName.setText(scanResults.get(arg0).SSID);
		viewholder.ArrowTop=(ImageView)context.findViewById(R.id.wifi_arrowtop);
		viewholder.ArrowBottom=(ImageView)context.findViewById(R.id.wifi_arrowbottom);
		if(arg0==scanResults.size()-1){
			viewholder.ArrowBottom.setVisibility(View.INVISIBLE);
		}else{
			viewholder.ArrowBottom.setVisibility(View.VISIBLE);
		}
		return view;
	}
	class Viewholder{
		public TextView WifiName;
		public ImageView ArrowTop;
		public ImageView ArrowBottom;
	}
}
