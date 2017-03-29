# pulltorefreshRecylerView

## 下拉刷新+Material Design控件使用

### 描述
这是一个下拉刷新上拉加载更多框架，头部用的秋哥的android-Ultra-Pull-To-Refresh，底部和没有数据的状态自己实现的。


### 特点
* 可定制的头部 (可以查看android-Ultra-Pull-To-Refresh文档)
* 可定制的底部 (加载中/没有数据/加载失败 三种状态的定制)
* 可定制的没有数据状态显示 (目前只有一个状态)
* 可定制列表智能预加载
* 目前提供一个实现好的ItemDecoration(头部吸附效果) 



### 效果预览：
#### 下拉刷新上拉加载
![](/PulltoRefreshRecyclerView/images/ptr.gif)
#### 头部吸附效果
![](/PulltoRefreshRecyclerView/images/ptr_custom_decoration.gif)
#### 列表智能预加载
![](/PulltoRefreshRecyclerView/images/ptr1.gif)


### 使用方式：
Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}Copy
Step 2. Add the dependency

	dependencies {
	        compile 'com.github.tengyukun:pulltorefreshRecylerView:v1.0'
	}
