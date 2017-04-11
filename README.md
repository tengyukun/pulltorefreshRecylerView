
# 下拉刷新上拉加载控件+Material Design使用+FlexboxLayout


## Material Design控件使用

前几天转载了两篇Material Design控件使用的文章，这里就不多做叙述，下面是传送门，想要学习的小伙伴可以去看下：


https://juejin.im/entry/58d8d4d344d90400687c134d/detail#comment
https://juejin.im/entry/58d9cdf044d9040068813c2d/detail#comment


---------------------------------------------------------------------------------------------------------------
#### 更新：依赖库已更新，如果想使用最新版的小伙伴做下修改
#### 1.当列表只有一页数据的时候，底部的 footer 加载中无法隐藏；
#### 2.添加隐藏/显示底部footer方法（具体在下文）
---------------------------------------------------------------------------------------------------------------



### 更新：添加头部header隐藏/显示方法（具体在下文）
---------------------------------------------------------------------------------------------------------------



## FlexboxLayout使用
前几天在掘金上看到依然饭特稀西大神写的[Android可伸缩布局－FlexboxLayout(支持RecyclerView集成)](https://juejin.im/post/58d1035161ff4b00603ca9c4)，试了一下，很是好用啊，这里也不做叙述，下面的传送门，想学习的小伙伴可以去看一下。
https://juejin.im/post/58d1035161ff4b00603ca9c4


## pulltorefreshRecylerView
## 下拉刷新上拉加载控件

### 描述
这是一个下拉刷新上拉加载更多框架，头部用的秋哥的android-Ultra-Pull-To-Refresh，底部和没有数据的状态自己实现的。


### 特点
* 可定制的头部 (可以查看android-Ultra-Pull-To-Refresh文档)
* 可定制的底部 (加载中/没有数据/加载失败 三种状态的定制)
* 可定制的没有数据状态显示 (目前只有一个状态)
* 可定制列表智能预加载
* 目前提供一个实现好的ItemDecoration(头部吸附效果) 



### 效果预览
#### 下拉刷新上拉加载
![](/PulltoRefreshRecyclerView/images/ptr.gif)
#### 头部吸附效果
![](/PulltoRefreshRecyclerView/images/ptr_custom_decoration.gif)
#### 列表智能预加载
![](/PulltoRefreshRecyclerView/images/ptr1.gif)


## 使用方式

### 依赖
#### Step 1. 在project的build的repositories{}中添加maven { url 'https://jitpack.io' }

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
#### Step 2. 在module中添加compile 'com.github.tengyukun:pulltorefreshRecylerView:v1.1'	
	
	dependencies {
	        //compile 'com.github.tengyukun:pulltorefreshRecylerView:v1.0'
	        //更新解决当列表只有一页数据的时候，底部的 footer 加载中无法隐藏
	        compile 'com.github.tengyukun:pulltorefreshRecylerView:v1.1'
	}
	
	
### 配置

#### 目前有两个参数可以配置

* app:emply_layout 
没有数据时候布局

* app:number_load_more 
最后可见条目 + number_load_more > total 触发加载更多;默认值为4

#### xml中配置示例

	<com.pulltorefresh.tyk.library.PtrDefRecyclerView
        	android:id="@+id/erv_fragmentthree_content"
        	android:layout_height="match_parent"
        	android:layout_width="match_parent"
        	app:emply_layout="@layout/erv_default_empty"
        	app:number_load_more="5"/>


### activity代码配置

* 基本设置
	
		erv_fragmentthree_content.setAdapter(rvAdapter = new RvAdapter());//设置adapter
		erv_fragmentthree_content.setLastUpdateTimeRelateObject(this);//传入参数类名作为记录刷新时间key
		erv_fragmentthree_content.setOnRefreshListener(this);//设置刷新监听
		erv_fragmentthree_content.setOnLoadListener(this);//设置加载更多监听
	
* 隐藏/显示空状态界面

		erv_fragmentthree_content.hideEmptyView();
		erv_fragmentthree_content.showEmptyView();
		
* （更新）隐藏/显示底部footer

    		erv_fragmentthree_content.hideFooterView();
		erv_fragmentthree_content.showFooterView();
		
		

* 刷新设置
		
		erv_fragmentthree_content.refreshComplete();//下拉刷新结束   
		erv_fragmentthree_content.loadComplete();//上拉加载结束
		erv_fragmentthree_content.loading();//上拉加载显示"加载中"		
		erv_fragmentthree_content.loadFail();//上拉加载显示"加载失败，点击重试"
		erv_fragmentthree_content.noMore();//上拉加载显示"暂无更多数据"
		
		
		
由于这里使用的PtrDefRecyclerView,头部就是默认经典样式所以需要调用,使用其他头部时不需要调用

	erv_fragmentthree_content.setLastUpdateTimeRelateObject(this);//传入参数类名作为记录刷新时间key
	
使用MaterialHeader

	MaterialHeader materialHeader = new MaterialHeader(activity);
        materialHeader.setPadding(0, 40,0,40);
        erv_fragmentthree_content.setHeaderView(materialHeader);
        erv_fragmentthree_content.setPinContent(true);


### adapter代码配置

#### adapter需要实现CommonAdapter或者MultipleAdapter抽象方法

	//创建ViewHolder
	public abstract VH createCustomViewHolder(ViewGroup parent, int viewType);
	//ViewHolder设置数据
	public abstract void bindCustomViewHolder(VH holder, T t, int position);
	
	
#### 目前提供了下面这些方法操作adapter数据,具体实现可以在CommonAdapter中查看
* 新增数据
public void add(@NonNull T object)
public void addAll(@NonNull Collection collection)
public void addAll(@NonNull T… items)
public void insert(@NonNull T object, int index)
public void insertAll(@NonNull Collection collection, int index)

* 删除数据
public void remove(int index)
public boolean remove(@NonNull T object)
public void clear()

* 修改数据
public void update(@NonNull List mDatas)

* 查看数据
public T getItem(int position)
public int getPosition(T item)
public List getData()

* 排序
public void sort(Comparator comparator)

* 加载布局
public View inflateView(@LayoutRes int resId, ViewGroup parent)

#### adapter中ViewHolder需要继承BaseViewHolder 

### 其他配置
头部吸附效果
![](/PulltoRefreshRecyclerView/images/ptr_custom_decoration.gif)

	  mItemDecoration = new StickItemDecoration(context,dataList) {
		      @Override
		      public String getTag(int position) {
			  return "吸附头部显示的文字";
		      }
	    }
	  erv_fragmentthree_content.addItemDecoration(mItemDecoration);

#### 这里StickItemDecoration提供了如下方法来定制吸附效果
	//设置吸附条目高度
	public void setStickHeight(int mStickHeight)
	//设置吸附条目背景
	public void setStickBackgroundColor(int mStickBackgroundColor)
	//设置吸附文字颜色
	public void setStickTextColor(int mStickTextColor)
	//设置吸附文字大小
	public void setStickTextSize(int mStickTextSize)
	//设置吸附文字leftmargin
	public void setStickTextoffset(int mStickTextoffset)
	
	
### 自定义

头部使用秋哥的android-Ultra-Pull-To-Refresh 
秋哥默认已经实现了3个头部

* MaterialHeader
* PtrClassicDefaultHeader
* StoreHouseHeader
一般情况下这些样式应该够了,如果有特殊需求可以自定义头部.然后erv.setHeaderView(view);

底部的话目前我只实现了一个PtrDefaultFooter,自定义的话需要实现PtrLoadUIHandle接口.写法可以参考PtrDefaultFooter

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

	    void onLoading();//loading状态实现

	    void onNoMore();//没有数据状态实现

	    void onLoadFail(OnLoadListener listener);//加载失败实现


	}
实现后调用setFooterView()方法设置 

