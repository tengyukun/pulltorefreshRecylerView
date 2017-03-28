package com.tyk.pulltorefresh.recyclerview.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tyk.pulltorefresh.recyclerview.R;
import com.tyk.pulltorefresh.recyclerview.adapter.MainAdapter;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class OneFragment extends BaseFragment {


    @ViewInject(R.id.rv_fragmentone_content)
    private RecyclerView rv_fragmentone_content;

    @Override
    public View initView() {

        View view=View.inflate(activity, R.layout.fragment_one,null);
        x.view().inject(this,view);

        initRecylerView();
        return view;
    }

    private void initRecylerView() {

        rv_fragmentone_content.setLayoutManager(new LinearLayoutManager(activity));
        MainAdapter adapter=new MainAdapter(activity);
        rv_fragmentone_content.setAdapter(adapter);
        rv_fragmentone_content.setItemAnimator(new DefaultItemAnimator());


    }
}
