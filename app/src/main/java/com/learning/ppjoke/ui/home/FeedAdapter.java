package com.learning.ppjoke.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.ppjoke.databinding.LayoutFeedTypeImageBinding;
import com.learning.ppjoke.databinding.LayoutFeedTypeVideoBinding;
import com.learning.ppjoke.model.Feed;

import java.util.Objects;

public class FeedAdapter extends PagedListAdapter<Feed, FeedAdapter.ViewHolder> {


    private final LayoutInflater mInflater;
    private String mCategory;

    protected FeedAdapter(Context context,String category) {
        //DiffUtil 做差分异处理
        super(new DiffUtil.ItemCallback<Feed>() {
            @Override
            public boolean areItemsTheSame(@NonNull Feed oldItem, @NonNull Feed newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Feed oldItem, @NonNull Feed newItem) {
                return Objects.equals(oldItem,newItem);
            }
        });

        mInflater = LayoutInflater.from(context);
        mCategory = category;
    }

    @Override
    public int getItemViewType(int position) {
        Feed feed = getItem(position);
        return feed.itemType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = null;

        if(viewType == Feed.TYPE_IMAGE_TEXT){
            binding = LayoutFeedTypeImageBinding.inflate(mInflater);
        }else{
            binding = LayoutFeedTypeVideoBinding.inflate(mInflater);
        }
        return new ViewHolder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ViewDataBinding mBinding;

        public ViewHolder(@NonNull View itemView, ViewDataBinding binding) {
            super(itemView);
            mBinding = binding;
        }

        public void bindData(Feed feed){
            if(mBinding instanceof LayoutFeedTypeImageBinding){
                LayoutFeedTypeImageBinding imageBinding = (LayoutFeedTypeImageBinding) mBinding;
                imageBinding.setFeed(feed);
                imageBinding.ivFeedImage.bindData(feed.width,feed.height,16,feed.cover);
            }else if(mBinding instanceof  LayoutFeedTypeVideoBinding){
                LayoutFeedTypeVideoBinding videoBinding = (LayoutFeedTypeVideoBinding) mBinding;
                videoBinding.setFeed(feed);
                videoBinding.listPlayerView.bindData(mCategory,feed.width,feed.height,feed.cover,feed.url);
            }
        }
    }
}
