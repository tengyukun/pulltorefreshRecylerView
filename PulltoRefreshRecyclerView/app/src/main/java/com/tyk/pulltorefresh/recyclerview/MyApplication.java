package com.tyk.pulltorefresh.recyclerview;

import android.app.Application;

import org.xutils.x;


/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);//初始化
    }
}
