package com.jacky.launcher.features.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jacky.launcher.R;
import com.jacky.launcher.model.AppBean;

import java.util.List;

public class ManagerApp extends LinearLayout implements View.OnClickListener {

	public static int position = -1;
	private PopupWindow mPopupWindow = null;
	public static final String TAG = "ManagerPagerItemLayout";
	private static final boolean d =false;
	private Button mLauncherButton,mFavoriteButton, mUpdateButton,
	mRemoveButton;

	public ManagerApp(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ManagerApp(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private Context mContext;
	private ImageView appIcons[] = new ImageView[15];
	private LinearLayout appItems[] = new LinearLayout[15];
	int iconIds[] = { R.id.app_icon0, R.id.app_icon1, R.id.app_icon2,
			R.id.app_icon3, R.id.app_icon4, R.id.app_icon5, R.id.app_icon6,
			R.id.app_icon7, R.id.app_icon8, R.id.app_icon9, R.id.app_icon10,
			R.id.app_icon11, R.id.app_icon12, R.id.app_icon13, R.id.app_icon14 };
	private TextView appNames[] = new TextView[15];
	int nameIds[] = { R.id.app_name0, R.id.app_name1, R.id.app_name2,
			R.id.app_name3, R.id.app_name4, R.id.app_name5, R.id.app_name6,
			R.id.app_name7, R.id.app_name8, R.id.app_name9, R.id.app_name10,
			R.id.app_name11, R.id.app_name12, R.id.app_name13, R.id.app_name14 };
	int itemIds[] = { R.id.app_item0, R.id.app_item1, R.id.app_item2,
			R.id.app_item3, R.id.app_item4, R.id.app_item5, R.id.app_item6,
			R.id.app_item7, R.id.app_item8, R.id.app_item9, R.id.app_item10,
			R.id.app_item11, R.id.app_item12, R.id.app_item13, R.id.app_item14 };

	public ManagerApp(Context context) {
		super(context);
		mContext = context;
	}

	private List<AppBean> mAppList = null;
	private int mPagerIndex = -1;
	private int mPagerCount = -1;

	public void setAppList(List<AppBean> list, int pagerIndex, int pagerCount) {
		mAppList = list;
		mPagerIndex = pagerIndex;
		mPagerCount = pagerCount;
	}

	public void ManagerAppInit() {
		View vvv = LayoutInflater.from(mContext).inflate(
				R.layout.item_pager_layout_managerapp, null);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		int itemCount = -1;
		if (mPagerIndex < mPagerCount - 1) {
			itemCount = 15;
		} else {
			itemCount = (mAppList.size() - (mPagerCount - 1) * 15);
		}
		for (int i = 0; i < itemCount; i++) {
			appIcons[i] = (ImageView) vvv.findViewById(iconIds[i]);
			appNames[i] = (TextView) vvv.findViewById(nameIds[i]);
			appIcons[i].setImageDrawable(mAppList.get(mPagerIndex * 15 + i)
					.getIcon());
			appNames[i].setText(mAppList.get(mPagerIndex * 15 + i).getName());
			appItems[i] = (LinearLayout) vvv.findViewById(itemIds[i]);
			appItems[i].setVisibility(View.VISIBLE);
			appItems[i].setOnClickListener(this);
		}
		createPopuWindow();
		addView(vvv);
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		switch (id) {
		case R.id.app_item0:
			position = mPagerIndex * 15 + 0;
			break;
		case R.id.app_item1:
			position = mPagerIndex * 15 + 1;
			break;
		case R.id.app_item2:
			position = mPagerIndex * 15 + 2;
			break;
		case R.id.app_item3:
			position = mPagerIndex * 15 + 3;
			break;
		case R.id.app_item4:
			position = mPagerIndex * 15 + 4;
			break;
		case R.id.app_item5:
			position = mPagerIndex * 15 + 5;
			break;
		case R.id.app_item6:
			position = mPagerIndex * 15 + 6;
			break;
		case R.id.app_item7:
			position = mPagerIndex * 15 + 7;
			break;
		case R.id.app_item8:
			position = mPagerIndex * 15 + 8;
			break;
		case R.id.app_item9:
			position = mPagerIndex * 15 + 9;
			break;
		case R.id.app_item10:
			position = mPagerIndex * 15 + 10;
			break;
		case R.id.app_item11:
			position = mPagerIndex * 15 + 11;
			break;
		case R.id.app_item12:
			position = mPagerIndex * 15 + 12;
			break;
		case R.id.app_item13:
			position = mPagerIndex * 15 + 13;
			break;
		case R.id.app_item14:
			position = mPagerIndex * 15 + 14;
			break;
		default:
			break;
		}
		mPopupWindow.showAtLocation(getRootView(), Gravity.CENTER, 0, 0);
		if(mAppList.get(position).isSysApp()){
			mRemoveButton.setVisibility(View.GONE);
		}
	}

	private void createPopuWindow() {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.item_pager_manager_pop_window, null);
		mPopupWindow = new PopupWindow(view, 650, 200);
		mPopupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(
				android.R.color.transparent));
		mPopupWindow.setFocusable(true);
		mLauncherButton = (Button) view.findViewById(R.id.start_btn);
		mUpdateButton = (Button) view.findViewById(R.id.update_btn);
		mFavoriteButton = (Button) view.findViewById(R.id.favorite_btn);
		mRemoveButton = (Button) view.findViewById(R.id.remove_btn);
		mLauncherButton
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {

					@Override
					public void onFocusChange(View arg0, boolean arg1) {
						if (arg1 == true) {
							ViewPropertyAnimator propertyAnimator = mLauncherButton.animate();
							propertyAnimator.scaleX(1.2f);
							propertyAnimator.scaleY(1.2f);
							propertyAnimator.setDuration(100);
							propertyAnimator.start();
						}else{
							ViewPropertyAnimator propertyAnimator = mLauncherButton.animate();
							propertyAnimator.scaleX(1.0f);
							propertyAnimator.scaleY(1.0f);
							propertyAnimator.setDuration(100);
							propertyAnimator.start();
						}
					}
				});
		mUpdateButton
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {

					@Override
					public void onFocusChange(View arg0, boolean arg1) {
						if (arg1 == true) {
							ViewPropertyAnimator propertyAnimator = mUpdateButton.animate();
							propertyAnimator.scaleX(1.2f);
							propertyAnimator.scaleY(1.2f);
							propertyAnimator.setDuration(100);
							propertyAnimator.start();
						}else{
							ViewPropertyAnimator propertyAnimator = mUpdateButton.animate();
							propertyAnimator.scaleX(1.0f);
							propertyAnimator.scaleY(1.0f);
							propertyAnimator.setDuration(100);
							propertyAnimator.start();
						}
					}
				});
		mFavoriteButton
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {

					@Override
					public void onFocusChange(View arg0, boolean arg1) {
						if (arg1 == true) {
							ViewPropertyAnimator propertyAnimator = mFavoriteButton.animate();
							propertyAnimator.scaleX(1.2f);
							propertyAnimator.scaleY(1.2f);
							propertyAnimator.setDuration(100);
							propertyAnimator.start();
						}else{
							ViewPropertyAnimator propertyAnimator = mFavoriteButton.animate();
							propertyAnimator.scaleX(1.0f);
							propertyAnimator.scaleY(1.0f);
							propertyAnimator.setDuration(100);
							propertyAnimator.start();
						}
					}
				});
		mRemoveButton
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {

					@Override
					public void onFocusChange(View arg0, boolean arg1) {
						if (arg1 == true) {
							ViewPropertyAnimator propertyAnimator = mRemoveButton.animate();
							propertyAnimator.scaleX(1.2f);
							propertyAnimator.scaleY(1.2f);
							propertyAnimator.setDuration(100);
							propertyAnimator.start();
						}else{
							ViewPropertyAnimator propertyAnimator = mRemoveButton.animate();
							propertyAnimator.scaleX(1.0f);
							propertyAnimator.scaleY(1.0f);
							propertyAnimator.setDuration(100);
							propertyAnimator.start();
						}
					}
				});

		mLauncherButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				PackageManager manager = mContext.getPackageManager();
				String packageName = mAppList.get(position).getPackageName();
				Intent intent = new Intent();
				intent = manager.getLaunchIntentForPackage(packageName);
				mContext.startActivity(intent);
				mPopupWindow.dismiss();
			}
		});
		mUpdateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(mContext, "更新-更新", Toast.LENGTH_SHORT).show();
				mPopupWindow.dismiss();
			}
		});
		mFavoriteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(mContext, "收藏-收藏", Toast.LENGTH_SHORT).show();
				mPopupWindow.dismiss();
			}
		});
		mRemoveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(mContext, "卸载--卸载", Toast.LENGTH_SHORT).show();
				String packageName = mAppList.get(position).getPackageName();
				Log.i(TAG, "packageName===" + packageName);
				Uri packageURI = Uri.parse("package:" + packageName);
				Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
						packageURI);
				mContext.startActivity(uninstallIntent);
//				appIcons[position].setVisibility(view.INVISIBLE); 
//				appNames[position].setVisibility(view.INVISIBLE);
				mPopupWindow.dismiss();
			}
		});
	}
}
