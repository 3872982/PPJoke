package com.learning.ppjoke.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.libcommon.view.EmptyView;
import com.learning.ppjoke.AbsViewModel;
import com.learning.ppjoke.R;
import com.learning.ppjoke.databinding.LayoutRefreshViewBinding;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public abstract class AbsListFragment<T,M extends AbsViewModel<T>> extends Fragment implements OnLoadMoreListener, OnRefreshListener {

    private LayoutRefreshViewBinding mBinding;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout mRefershLayout;
    private EmptyView mEmptyView;
    protected PagedListAdapter<T,RecyclerView.ViewHolder> mAdapter;
    protected M mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = LayoutRefreshViewBinding.inflate(inflater, container, false);

        mRefershLayout = mBinding.refershLayout;
        mRecyclerView = mBinding.recyclerView;
        mEmptyView = mBinding.emptyView;

        //设置RefreshLayout
        mRefershLayout.setEnableRefresh(true);
        mRefershLayout.setEnableLoadMore(true);
        mRefershLayout.setOnRefreshListener(this);
        mRefershLayout.setOnLoadMoreListener(this);

        //设置RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(null);

        //设置分隔线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.list_divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        //设置adapter
        mAdapter = getAdapter();
        mRecyclerView.setAdapter(mAdapter);

        genericViewModel();
        return mBinding.getRoot();
    }

    protected void genericViewModel(){
        //通过子类传递过来的泛型参数 实例化 AbsViewModel
        ParameterizedType type = (ParameterizedType)getClass().getGenericSuperclass();
        Type[] actualTypeArguments = type.getActualTypeArguments();
        if(actualTypeArguments.length > 1){
            Type argument = actualTypeArguments[1];
            Class clzz = ((Class) argument).asSubclass(AbsViewModel.class);

            mViewModel = (M)ViewModelProviders.of(this).get(clzz);

            mViewModel.getPageData().observe(getViewLifecycleOwner(), new Observer<PagedList<T>>() {
                @Override
                public void onChanged(PagedList<T> pagedList) {
                    submitList(pagedList);
                }
            });

            mViewModel.getBoundaryPageData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean hasData) {
                    finishRefresh(hasData);
                }
            });
        }
    }

    public void submitList(PagedList<T> pagedList){
        boolean hasData = (pagedList != null && pagedList.size() > 0);
        if(hasData){
            mAdapter.submitList(pagedList);
        }

        finishRefresh(hasData);
    }

    public void finishRefresh(boolean hasData){
        PagedList<T> currentList = mAdapter.getCurrentList();
        boolean dataNotNull = hasData || (currentList != null && currentList.size() > 0);

        RefreshState state = mRefershLayout.getState();
        //如果是底部则调用
        if(state.isFooter && state.isOpening){
            mRefershLayout.finishLoadMore();
        }else if(state.isHeader && state.isOpening){
            mRefershLayout.finishRefresh();
        }

        if(dataNotNull){
            mEmptyView.setVisibility(View.GONE);
        }else{
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    public abstract PagedListAdapter getAdapter();
}
