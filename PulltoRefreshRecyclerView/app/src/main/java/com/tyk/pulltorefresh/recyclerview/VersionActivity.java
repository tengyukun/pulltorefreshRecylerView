package com.tyk.pulltorefresh.recyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;



public class VersionActivity extends AppCompatActivity {


    @ViewInject(R.id.tb_version_title)
    private Toolbar tb_version_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);

        x.view().inject(this);

        init();
    }

    private void init() {
        initView();
        initData();


    }

    private void initData() {

        tb_version_title.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initView() {
        tb_version_title.setTitle("版本");
        setSupportActionBar(tb_version_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
