package com.pulltorefresh.tyk.library.footer;


import com.pulltorefresh.tyk.library.listener.OnLoadListener;

/**
 * 刷新ui操作
 * Created by tyk on 2016/11/24 0024.
 */

public interface PtrLoadUIHandle {

    /**
     * 允许加载更多
     */
    int LOAD = 1;

    /**
     * 暂无更多数据
     */
    int NOMORE = 2;

    /**
     * 加载失败
     */
    int LOADFAIL = 3;

    /**
     * @return 获取底部当前状态
     */
    int getState();

    void onLoading();

    void onNoMore();

    void onLoadFail(OnLoadListener listener);


}
