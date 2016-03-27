package com.jacky.launcher.activitys;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * @author Droid
 */
public abstract class BaseActivity extends FragmentActivity {

	protected void showLongToast(String pMsg) {
		Toast.makeText(this, pMsg, Toast.LENGTH_LONG).show();
	}

	protected void showShortToast(String pMsg) {
		Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
	}
	
}
