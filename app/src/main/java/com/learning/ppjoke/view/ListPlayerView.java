package com.learning.ppjoke.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.learning.libcommon.utils.PixUtils;
import com.learning.ppjoke.R;

public class ListPlayerView extends FrameLayout {

    private PPImageView mBlur;
    private PPImageView mCover;
    private ImageView mPlayBtn;
    private ProgressBar mBufferView;
    private String mCategory;
    private int mWidthPx;
    private int mHeightPx;
    private String mVideoUrl;

    public ListPlayerView(@NonNull Context context) {
        this(context,null);
    }

    public ListPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ListPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public ListPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        View.inflate(context, R.layout.layout_player_view, this);

        mBlur = findViewById(R.id.iv_blur_background);
        mCover = findViewById(R.id.iv_cover);
        mPlayBtn = findViewById(R.id.iv_play_btn);
        mBufferView = findViewById(R.id.pb_buffer_view);

    }

    public void bindData(String category, int widthPx, int heightPx, String coverUrl, String videoUrl) {
        mCategory = category;
        mWidthPx = widthPx;
        mHeightPx = heightPx;
        mVideoUrl = videoUrl;
        mCover.setImageUrl(coverUrl);

        //如果视频高度大于宽度，则设置高斯模糊背景可见，否则不可见
        if(widthPx < heightPx){
            PPImageView.setBlurImageUrl(mBlur,coverUrl,50);
            mBlur.setVisibility(View.VISIBLE);
        }else{
            mBlur.setVisibility(View.GONE);
        }

        //如果视频高度大于宽度，则最大高度设为屏幕宽度
        //如果视频高度小于宽度，则最大宽度设为屏幕宽度
        setSize(widthPx,heightPx);
    }

    protected void setSize(int widthPx, int heightPx) {
        int maxWidth = PixUtils.getScreenWidth();
        int maxHeight = maxWidth;

        int layoutWidth = maxWidth;
        int layoutHeight = 0;

        int coverWidth;
        int coverHeight;

        if(heightPx >widthPx){
            layoutHeight  = layoutWidth;
            coverHeight = layoutHeight;
            coverWidth = (int) (widthPx / (heightPx * 1.0f / maxHeight));
        }else{
            layoutHeight = (int) (heightPx / (widthPx * 1.0f / maxWidth));
            coverWidth = maxWidth;
            coverHeight = layoutHeight;
        }

        //设置整体params
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = layoutWidth;
        params.height = layoutHeight;
        setLayoutParams(params);

        //设置blur模糊背景params
        ViewGroup.LayoutParams blurParams = mBlur.getLayoutParams();
        blurParams.height = layoutHeight;
        blurParams.width = layoutWidth;
        mBlur.setLayoutParams(blurParams);

        //设置cover封面的params
        LayoutParams coverParams = (LayoutParams)mCover.getLayoutParams();
        coverParams.height = coverHeight;
        coverParams.width = coverWidth;
        coverParams.gravity = Gravity.CENTER;
        mCover.setLayoutParams(coverParams);

        //设置播放按钮params
        LayoutParams playBtnParams = (LayoutParams)mPlayBtn.getLayoutParams();
        playBtnParams.gravity = Gravity.CENTER;
        mPlayBtn.setLayoutParams(playBtnParams);
    }
}
