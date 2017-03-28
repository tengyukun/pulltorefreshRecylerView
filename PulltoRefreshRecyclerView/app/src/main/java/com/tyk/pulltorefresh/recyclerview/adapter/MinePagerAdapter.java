package com.tyk.pulltorefresh.recyclerview.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class MinePagerAdapter extends FragmentPagerAdapter {


    private final Fragment[] fragments;
    private final String[] tabs;

    public MinePagerAdapter(FragmentManager fm, Fragment[] fragments, String[] tabs) {
        super(fm);
        this.fragments=fragments;
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabs[position];
    }
}
