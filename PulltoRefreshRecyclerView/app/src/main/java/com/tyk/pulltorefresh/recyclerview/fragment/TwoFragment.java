package com.tyk.pulltorefresh.recyclerview.fragment;

import android.view.View;

import com.tyk.pulltorefresh.recyclerview.R;

import org.xutils.x;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class TwoFragment extends BaseFragment {


    @Override
    public View initView() {
        View view=View.inflate(activity, R.layout.fragment_two,null);
        x.view().inject(this,view);
        return view;
    }

}
