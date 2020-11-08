package com.learning.ppjoke;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public abstract class AbsViewModel<T> extends ViewModel {

    private final LiveData<PagedList<T>> mPageData;
    private DataSource mSource;
    private MutableLiveData<Boolean> mBoundaryPageData = new MutableLiveData<>();
    private final DataSource.Factory mFactory;
    protected final PagedList.Config mConfig;

    public AbsViewModel(){
        mConfig = new PagedList.Config.Builder()
                .setPageSize(5)
                .setInitialLoadSizeHint(7) //数字比pagesize大，则不会立即加载下一页
                //.setMaxSize(100)  //一般情况下，我们不可能知道总共多少数据，不设
                //.setPrefetchDistance() //加载到第几条时请求下一页数据
                //.setEnablePlaceholders(true)  //UI占位符
                .build();

        mFactory = new DataSource.Factory() {
            @NonNull
            @Override
            public DataSource create() {
                if(mSource == null || mSource.isInvalid()){
                    mSource = createDataSource();
                }
                return mSource;
            }
        };

        mPageData = new LivePagedListBuilder(mFactory, mConfig)
                .setInitialLoadKey(0)
                .setBoundaryCallback(mBoundaryCallback)
                .build();
    }

    public LiveData<PagedList<T>> getPageData() {
        return mPageData;
    }

    public DataSource getSource() {
        return mSource;
    }

    public MutableLiveData<Boolean> getBoundaryPageData() {
        return mBoundaryPageData;
    }

    PagedList.BoundaryCallback mBoundaryCallback = new PagedList.BoundaryCallback<T>(){
        //当PagedList返回的数据集为空值的时候回调
        //新提交的PagedList中没有数据
        @Override
        public void onZeroItemsLoaded() {
            getBoundaryPageData().postValue(false);
        }

        //最前面的一个item被加载的时候调用
        //新提交的PagedList中第一条数据被加载到列表上
        @Override
        public void onItemAtFrontLoaded(@NonNull T itemAtFront) {
            getBoundaryPageData().postValue(true);
        }

        //最后一个item被加载的时候调用
        //新提交的PagedList中最后一条数据被加载到列表上
        @Override
        public void onItemAtEndLoaded(@NonNull T itemAtEnd) {
            super.onItemAtEndLoaded(itemAtEnd);
        }
    };



    protected abstract DataSource createDataSource();
}
