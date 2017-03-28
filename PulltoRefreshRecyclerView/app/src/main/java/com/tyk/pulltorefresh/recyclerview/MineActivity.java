package com.tyk.pulltorefresh.recyclerview;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tyk.pulltorefresh.recyclerview.adapter.MinePagerAdapter;
import com.tyk.pulltorefresh.recyclerview.fragment.FourFragment;
import com.tyk.pulltorefresh.recyclerview.fragment.OneFragment;
import com.tyk.pulltorefresh.recyclerview.fragment.ThreeFragment;
import com.tyk.pulltorefresh.recyclerview.fragment.TwoFragment;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class MineActivity extends AppCompatActivity {

    @ViewInject(R.id.abl_mine_title)
    private AppBarLayout abl_mine_title;

    @ViewInject(R.id.tb_mine_title)
    private Toolbar tb_mine_title;

    @ViewInject(R.id.tl_mine_title)
    private TabLayout tl_mine_title;

    @ViewInject(R.id.vp_mine_content)
    private ViewPager vp_mine_content;


    private Fragment[] fragments;
    private String[] tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        x.view().inject(this);

        initView();

        initData();
    }

    private void initData() {

        tb_mine_title.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void initView() {

        //标题栏
        initToolbar();
        //数据
        initViewpager();
        //选项卡
        initTab();
    }

    private void initViewpager() {

        fragments = new Fragment[]{
                new OneFragment(),
                new TwoFragment(),
                new ThreeFragment(),
                new FourFragment(),
                new OneFragment()
        };

    }

    private void initTab() {

        tabs = new String[]{
                "one",
                "two",
                "three",
                "four",
                "one"
        };

        MinePagerAdapter adapter=new MinePagerAdapter(getSupportFragmentManager(),fragments,tabs);
        vp_mine_content.setAdapter(adapter);
        tl_mine_title.setupWithViewPager(vp_mine_content);
//        tl_mine_title.setTabMode(TabLayout.MODE_SCROLLABLE);//可滑动模式
        tl_mine_title.setTabMode(TabLayout.MODE_FIXED);//全部显示(默认)
    }

    private void initToolbar() {
        tb_mine_title.setTitle("关于我");
        setSupportActionBar(tb_mine_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
