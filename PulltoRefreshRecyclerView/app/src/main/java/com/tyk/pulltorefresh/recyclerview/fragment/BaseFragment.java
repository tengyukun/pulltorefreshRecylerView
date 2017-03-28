package com.tyk.pulltorefresh.recyclerview.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	public View mRootView;
	public Activity activity;

	/**
	 * 能够初始化fragment特有数据
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}
	/**
	 * 初始化特有的数据
	 */
	public void initData() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 能够能到附着的activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		activity = getActivity();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView();
		mRootView = view;
		return mRootView;
	}
	public abstract View initView();

}
