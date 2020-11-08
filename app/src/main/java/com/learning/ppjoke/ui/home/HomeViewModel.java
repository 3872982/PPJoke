package com.learning.ppjoke.ui.home;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;

import com.alibaba.fastjson.TypeReference;
import com.learning.libnetwork.ApiResponse;
import com.learning.libnetwork.ApiService;
import com.learning.libnetwork.JsonCallBack;
import com.learning.libnetwork.Request;
import com.learning.ppjoke.AbsViewModel;
import com.learning.ppjoke.MutableDataSource;
import com.learning.ppjoke.model.Feed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HomeViewModel extends AbsViewModel<Feed> {
    private volatile boolean mWithCache = true;
    private AtomicBoolean isloadAfter = new AtomicBoolean(false);

    private MutableLiveData<PagedList<Feed>> cacheLiveData = new MutableLiveData<>();

    @Override
    protected DataSource createDataSource() {
        return new FeedDataSource();
    }

    class FeedDataSource extends ItemKeyedDataSource<Integer, Feed> {
        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Feed> callback) {
            //加载初始化数据
            //Log.e("homeviewmodel", "loadInitial: "+params.requestedInitialKey);
            loadData(0,params.requestedLoadSize,callback);
            mWithCache = false;
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            //向后加载分页数据的
            //Log.e("homeviewmodel", "loadAfter: "+params.key);
            loadData(params.key,params.requestedLoadSize,callback);
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            //Log.e("homeviewmodel", "loadBefore: ");
            callback.onResult(Collections.emptyList());
            //能够向前加载数据的
        }

        @NonNull
        @Override
        public Integer getKey(@NonNull Feed item) {
            //Log.e("HomeViewModel", "getKey: "+item.toString() );
            return item.id;
        }
    }

    //加载数据，由于ItemKeyedDataSource中的方法已经开启子线程了，所以可以用同步请求的方式做网络请求
    private void loadData(int key, int requestedLoadSize, ItemKeyedDataSource.LoadCallback<Feed> callback) {
        //如果key大于0，说明是加载更多，LoadAfter进入了
        if(key > 0){
            isloadAfter.set(true);
        }

        // url:/feeds/queryHotFeedsList
        Request request = ApiService.get("/feeds/queryHotFeedsList")
                .addParam("feedType", null)
                .addParam("feedId", key)
                .addParam("pageCount", requestedLoadSize)
                .addParam("userId", 0)
                .responseType(new TypeReference<ArrayList<Feed>>() {
                }.getType());

        if(mWithCache){
            request.cacheStrategy(Request.CACHE_ONLY);
            request.execute(new JsonCallBack<List<Feed>>(){
                @Override
                public void onCacheSuccess(ApiResponse<List<Feed>> response) {
                    Log.e("HomeViewModel","loadData--OnCacheSuccess--key="+response.body.size());
                    List<Feed> body = response.body;
                    MutableDataSource<Integer,Feed> mutableDataSource = new MutableDataSource<>();
                    mutableDataSource.sourceData.addAll(body);
                    PagedList<Feed> cacheFeeds = mutableDataSource.buildPagedList(mConfig);
                    cacheLiveData.postValue(cacheFeeds);
                }
            });
        }

        try {
            Request netRequest = mWithCache ? request.clone() : request;
            //设定缓存策略，如果是下拉刷新，则需要缓存，否则不需要缓存数据
            netRequest.cacheStrategy(key==0?Request.NET_CACHE:Request.NET_ONLY);
            ApiResponse<List<Feed>> response = netRequest.execute();

            List<Feed> data = (response.body == null) ? Collections.emptyList() : response.body;
            callback.onResult(data);

            if(key > 0){
                //通过LiveData告诉UI去关闭上拉加载更多的动画
                getBoundaryPageData().postValue(data.size() > 0);
                isloadAfter.set(false);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        Log.e("HomeViewModel","loadData--key="+key);
    }

    @SuppressLint("RestrictedApi")
    protected void loadAfter(int id, ItemKeyedDataSource.LoadCallback<Feed> callback){
        if(isloadAfter.get()){
            callback.onResult(Collections.emptyList());
            return;
        }

        ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                loadData(id,mConfig.pageSize,callback);
            }
        });
    }

    public MutableLiveData<PagedList<Feed>> getCacheLiveData() {
        return cacheLiveData;
    }
}