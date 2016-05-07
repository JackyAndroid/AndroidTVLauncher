package com.jacky.launcher.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jacky.uikit.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * @author jacky
 * @version v1.0
 * @since 2016.4.1
 */
public class MainActivityAdapter extends FragmentStatePagerAdapter {
    private ArrayList<BaseFragment> mFragments;
    private FragmentManager fm;

    public MainActivityAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
        super(fm);
        mFragments = fragments;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

}
