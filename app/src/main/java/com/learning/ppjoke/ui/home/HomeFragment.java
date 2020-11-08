package com.learning.ppjoke.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.libnavannotation.FragmentDestination;
import com.learning.ppjoke.MutableDataSource;
import com.learning.ppjoke.R;
import com.learning.ppjoke.model.Feed;
import com.learning.ppjoke.ui.AbsListFragment;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

@FragmentDestination(pageUrl = "main/tabs/home", asStart = true)
public class HomeFragment extends AbsListFragment<Feed,HomeViewModel> {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel.getCacheLiveData().observe(getViewLifecycleOwner(), new Observer<PagedList<Feed>>() {
            @Override
            public void onChanged(PagedList<Feed> feeds) {
                mAdapter.submitList(feeds);
            }
        });
    }

    @Override
    public PagedListAdapter getAdapter() {
        String feedType = getArguments() == null ? "all" : getArguments().getString("feedType");
        return new FeedAdapter(getContext(),feedType);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        Feed feed = mAdapter.getCurrentList().get(mAdapter.getItemCount()-1);
        mViewModel.loadAfter(feed.id, new ItemKeyedDataSource.LoadCallback<Feed>() {

            @Override
            public void onResult(@NonNull List<Feed> data) {
                if(data !=null && data.size()>0){
                    PagedList.Config config = mAdapter.getCurrentList().getConfig();
                    MutableDataSource<Integer,Feed> mutableDataSource = new MutableDataSource<>();
                    mutableDataSource.sourceData.addAll(data);
                    PagedList<Feed> feeds = mutableDataSource.buildPagedList(config);
                    mViewModel.getPageData().observe(getViewLifecycleOwner(), new Observer<PagedList<Feed>>() {
                        @Override
                        public void onChanged(PagedList<Feed> feeds) {
                            submitList(feeds);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        //invalidate 之后Paging会重新创建一个DataSource 重新调用它的loadInitial方法加载初始化数据
        //详见：LivePagedListBuilder#compute方法
        mViewModel.getSource().invalidate();
    }
}