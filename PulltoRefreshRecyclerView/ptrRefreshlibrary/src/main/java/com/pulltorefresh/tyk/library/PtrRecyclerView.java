package com.pulltorefresh.tyk.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import com.pulltorefresh.tyk.library.adapter.CommonAdapter;
import com.pulltorefresh.tyk.library.footer.PtrLoadUIHandle;
import com.pulltorefresh.tyk.library.listener.OnLoadListener;
import com.pulltorefresh.tyk.library.listener.OnRefreshListener;
import com.pulltorefresh.tyk.library.ptrlib.PtrDefaultHandler;
import com.pulltorefresh.tyk.library.ptrlib.PtrFrameLayout;
import com.pulltorefresh.tyk.library.ptrlib.PtrHandler;
import com.pulltorefresh.tyk.library.ptrlib.PtrUIHandler;
import com.pulltorefresh.tyk.library.utils.LayoutManagerUtil;




/**
 * 刷新
 * Created by tyk on 2016/11/20 0020.
 */

public class PtrRecyclerView extends FrameLayout {

    private final String TAG = getClass().getSimpleName();
    //Erv属性
    private int mNumLoadMore;//最后可见条目 + mNumLoadMore > total 触发加载更多

    //Ptr属性
    private float mResistance;
    private int mDurationToClose;
    private int mDurationToCloseHeader;
    private float mRatio;
    private boolean mKeepHeaderWhenRefresh;
    private boolean mPullToRefresh;

    private boolean mLoadingMore = false;
    private boolean mReviseMovePoi = false;//是否需要修正滑动位置
    private int mMovePoi = 0;

    private PtrFrameLayout mPtrFrame;
    private RecyclerView mRecyclerView;
    private OnRefreshListener mRefreshListener;
    private OnLoadListener mOnLoadListener;
    private View mFooterView;
    private CommonAdapter mAdapter;
    private PtrLoadUIHandle mLoadUIHandler;
    private View mEmptyView;
    private ViewStub mStubEmpty;
    private int mEmptyRes;

    //2017年4月10日 添加是否滑动到底部
    private boolean isBottom=false;

    public PtrRecyclerView(Context context) {
        this(context, null);
    }

    public PtrRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        initView(context);
    }

    public void initAttr(Context context, AttributeSet attrs) {
        TypedArray PtrAttr = context.obtainStyledAttributes(attrs,  R.styleable.PtrFrameLayout);
        TypedArray ErvAttr = context.obtainStyledAttributes(attrs, R.styleable.PtrRecyclerView);

        //初始化Erv
        mNumLoadMore = ErvAttr.getInteger(R.styleable.PtrRecyclerView_number_load_more, 4);
        mEmptyRes = ErvAttr.getResourceId(R.styleable.PtrRecyclerView_emply_layout, 0);

        //初始化Ptr
        mResistance = PtrAttr.getFloat(R.styleable.PtrFrameLayout_ptr_resistance, 1.7f);
        mRatio = PtrAttr.getFloat(R.styleable.PtrFrameLayout_ptr_ratio_of_header_height_to_refresh, 1.2f);
        mDurationToClose = PtrAttr.getInt(R.styleable.PtrFrameLayout_ptr_duration_to_close, 200);
        mDurationToCloseHeader = PtrAttr.getInt(R.styleable.PtrFrameLayout_ptr_duration_to_close_header, 1000);
        mKeepHeaderWhenRefresh = PtrAttr.getBoolean(R.styleable.PtrFrameLayout_ptr_keep_header_when_refresh, true);
        mPullToRefresh = PtrAttr.getBoolean(R.styleable.PtrFrameLayout_ptr_pull_to_fresh, false);

        ErvAttr.recycle();
        PtrAttr.recycle();
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.erv_layout, this);
        mPtrFrame = (PtrFrameLayout) view.findViewById(R.id.ptr);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mStubEmpty = (ViewStub) view.findViewById(R.id.stubEmpty);

        if (mEmptyRes != 0) {//加载没有数据状态下布局
            mStubEmpty.setLayoutResource(mEmptyRes);
            mEmptyView = mStubEmpty.inflate();
        }

        initPtr();
        initRecyclerView();
    }


    /**
     * 初始化ptr属性
     */
    private void initPtr() {
        mPtrFrame.setResistance(mResistance);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(mRatio);
        mPtrFrame.setDurationToClose(mDurationToClose);
        mPtrFrame.setDurationToCloseHeader(mDurationToCloseHeader);
        mPtrFrame.setKeepHeaderWhenRefresh(mKeepHeaderWhenRefresh);
        mPtrFrame.setPullToRefresh(mPullToRefresh);


        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (mRefreshListener != null) {
                    mRefreshListener.onRefreshListener();
                }
            }
        });
    }


    private void initRecyclerView() {

        mRecyclerView.addOnScrollListener(new ReviseMoveListener());
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mRecyclerView.getAdapter() != null && getLayoutManager() != null && mLoadUIHandler != null) {
                    int itemCount = mRecyclerView.getAdapter().getItemCount();
                    int lastVisibleItemPosition = LayoutManagerUtil.getLastVisibleItemPosition(getLayoutManager());
                    if (dy > 0 && itemCount != 0 && lastVisibleItemPosition + mNumLoadMore > itemCount - 1 &&
                            !mLoadingMore && mLoadUIHandler.getState() == PtrLoadUIHandle.LOAD) {
                        mLoadingMore = true;
                        if (mOnLoadListener != null) {
                            mOnLoadListener.onLoadListener();
                        }
                    }
                }

                //2017年4月10日 添加判断是否滑动到底部
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (dy > 0) //向下滚动
                {
                    int visibleItemCount = manager.getChildCount();
                    int totalItemCount = manager.getItemCount();
                    int pastVisiblesItems = manager.findFirstVisibleItemPosition();

                    Log.e("visibleItemCount",visibleItemCount+"");
                    Log.e("totalItemCount",totalItemCount+"");
                    Log.e("pastVisiblesItems",pastVisiblesItems+"");
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        isBottom=true;
                    }else{
                        isBottom=false;
                    }
                }
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //2017年4月10日 根据是否滑动到底部判断是都显示footer
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //是否显示底部加载进度
                    if (isBottom) {
                        showFooterView();
                    }else{
                        hideFooterView();
                    }
                }

            }
        });
    }

    public void setAdapter(CommonAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("adapter 不能为空");
        }
        this.mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                dataComplete();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                dataComplete();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                dataComplete();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                dataComplete();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                dataComplete();
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                dataComplete();
            }

            private void dataComplete() {
                refreshComplete();
                loadComplete();
                if (mEmptyView != null) {
                    if (mAdapter.getItemCount() == 0) {
                        showEmptyView();
                    } else {
                        hideEmptyView();
                    }
                }

            }
        });

        addFooter();
        //2017年4月10日 默认不显示底部footer
        hideFooterView();
    }

    public void showEmptyView() {
        mRecyclerView.setVisibility(View.GONE);
        mStubEmpty.setVisibility(View.VISIBLE);
    }

    public void hideEmptyView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mStubEmpty.setVisibility(View.GONE);
    }


    public void showFooterView(){
        if (mFooterView!=null){
            mFooterView.setVisibility(View.VISIBLE);
        }
    }

    public void hideFooterView(){
        if (mFooterView!=null){
            mFooterView.setVisibility(View.GONE);
        }
    }


    public void showHeaderView(){
        View headerView = getPtrFrame().getHeaderView();
        if (headerView!=null){
            headerView.setVisibility(View.VISIBLE);
        }
    }

    public void hideHeaderView(){
        View headerView = getPtrFrame().getHeaderView();
        if (headerView!=null){
            headerView.setVisibility(View.GONE);
        }
    }

    public void removeHeaderView(){
        View headerView = getPtrFrame().getHeaderView();
        if(headerView!=null){
            mPtrFrame.removeView(headerView);
            mPtrFrame.removePtrUIHandler((PtrUIHandler)headerView);
        }
    }


    public void setFooterView(View footerView) {
        if (!(footerView instanceof PtrLoadUIHandle)) {
            throw new RuntimeException("footerView 必须实现PtrLoadUIHandler");
        }
        this.mFooterView = footerView;
        this.mLoadUIHandler = (PtrLoadUIHandle) footerView;
        addFooter();
    }

    private void addFooter() {
        if (mAdapter != null && mFooterView != null) {
            mAdapter.setFooter(mFooterView);
        }
    }

    public void removeFooter() {
        if (mAdapter != null) {
            mAdapter.removeFooter();
        }
    }

    public void loading() {
        if (mLoadUIHandler != null) {
            mLoadUIHandler.onLoading();
        }
    }

    public void noMore() {
        if (mLoadUIHandler != null) {
            mLoadUIHandler.onNoMore();
        }
    }

    public void loadFail() {
        if (mOnLoadListener == null) {
            throw new RuntimeException("OnLoadListener 还未设置");
        }

        if (mLoadUIHandler != null) {
            mLoadUIHandler.onLoadFail(mOnLoadListener);
        }
    }

    public void setHeaderView(View headerView) {
        if (!(headerView instanceof PtrUIHandler)) {
            throw new RuntimeException("headerView 必须实现PtrUIHandler");
        }
        mPtrFrame.setHeaderView(headerView);
        mPtrFrame.addPtrUIHandler((PtrUIHandler) headerView);
    }

    public void setPtrHandler(PtrHandler ptrHandler) {
        mPtrFrame.setPtrHandler(ptrHandler);
    }

    public void autoRefresh() {
        mPtrFrame.autoRefresh(true, mDurationToCloseHeader);
    }

    public void autoRefresh(boolean atOnce) {
        mPtrFrame.autoRefresh(atOnce, mDurationToCloseHeader);
    }

    public PtrFrameLayout getPtrFrame() {
        return mPtrFrame;
    }

    public void setResistance(float resistance) {
        mPtrFrame.setResistance(resistance);
    }

    public void setRatioOfHeaderHeightToRefresh(float ratio) {
        mPtrFrame.setRatioOfHeaderHeightToRefresh(ratio);
    }

    public void setDurationToClose(int duration) {
        mPtrFrame.setDurationToClose(duration);
    }

    public void setDurationToCloseHeader(int duration) {
        mPtrFrame.setDurationToCloseHeader(duration);
    }

    public void setKeepHeaderWhenRefresh(boolean keepOrNot) {
        mPtrFrame.setKeepHeaderWhenRefresh(keepOrNot);
    }

    public void setPullToRefresh(boolean pullToRefresh) {
        mPtrFrame.setPullToRefresh(pullToRefresh);
    }

    public void setPinContent(boolean pinContent) {
        mPtrFrame.setPinContent(pinContent);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public void setLayoutManager(final RecyclerView.LayoutManager manager) {
        if (manager instanceof GridLayoutManager) {
            ((GridLayoutManager) manager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (mAdapter.getItemViewType(position)) {
                        case CommonAdapter.TYPE_FOOTER:
                            return ((GridLayoutManager) manager).getSpanCount();
                        default:
                            return 1;
                    }
                }
            });
        }
        mRecyclerView.setLayoutManager(manager);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
    }

    public void refreshComplete() {
        mPtrFrame.refreshComplete();
    }

    public void loadComplete() {
        mLoadingMore = false;
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mRefreshListener = listener;
    }

    public void setOnLoadListener(OnLoadListener listener) {
        this.mOnLoadListener = listener;
    }

    public void setOnEmptyViewClick(OnClickListener listener) {
        if (mEmptyView != null) {
            mEmptyView.setOnClickListener(listener);
        }
    }


    public void move(int position) {
        move(position, true);
    }

    public void move(int position, boolean isSmooth) {
        if (position < 0 || position >= mAdapter.getItemCount()) {
            Log.e(TAG, "move positon error");
            return;
        }
        mMovePoi = position;

        if (isSmooth) {
            smoothScrollPosition(position);
        } else {
            scrollPosition(position);
        }
    }


    private void scrollPosition(int position) {
        int firstVisibleItemPosition = LayoutManagerUtil.getFirstVisibleItemPosition(getLayoutManager());
        int lastVisibleItemPosition = LayoutManagerUtil.getLastVisibleItemPosition(getLayoutManager());

        if (position < firstVisibleItemPosition) {
            mRecyclerView.scrollToPosition(position);
        } else if (position < lastVisibleItemPosition) {
            int top = mRecyclerView.getChildAt(position - firstVisibleItemPosition).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mReviseMovePoi = true;
            //Todo scrollToPosition不触发RecyclerView.OnScrollListener监听所以没校正位置 改成smoothScrollToPosition()方法即可 这个问题后期google应该会修复所以这里未改成smoothScrollToPosition()
            mRecyclerView.scrollToPosition(position);
        }
    }

    private void smoothScrollPosition(int position) {
        int firstVisibleItemPosition = LayoutManagerUtil.getFirstVisibleItemPosition(getLayoutManager());
        int lastVisibleItemPosition = LayoutManagerUtil.getLastVisibleItemPosition(getLayoutManager());

        if (position < firstVisibleItemPosition) {
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position < lastVisibleItemPosition) {
            int top = mRecyclerView.getChildAt(position - firstVisibleItemPosition).getTop();
            mRecyclerView.smoothScrollBy(0, top);
        } else {
            mReviseMovePoi = true;
            mRecyclerView.smoothScrollToPosition(position);
        }
    }

    class ReviseMoveListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (mReviseMovePoi && newState == RecyclerView.SCROLL_STATE_IDLE) {
                mReviseMovePoi = false;
                int n = mMovePoi - LayoutManagerUtil.getFirstVisibleItemPosition(getLayoutManager());
                if (n >= 0 && n < mRecyclerView.getChildCount()) {
                    int top = mRecyclerView.getChildAt(n).getTop();
                    mRecyclerView.scrollBy(0, top);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        }
    }

}
