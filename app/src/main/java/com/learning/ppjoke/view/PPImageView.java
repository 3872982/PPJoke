package com.learning.ppjoke.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.learning.libcommon.utils.PixUtils;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class PPImageView extends AppCompatImageView {
    public PPImageView(@NonNull Context context) {
        super(context);
    }

    public PPImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PPImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageUrl(String imageUrl){
        setImageUrl(this,imageUrl,false);
    }

    @BindingAdapter(value = {"imageUrl","isCircle"},requireAll = false)
    public static void setImageUrl(PPImageView view, String imageUrl,boolean isCircle){
        RequestBuilder<Drawable> builder = Glide.with(view).load(imageUrl);

        if(isCircle){
            builder.transform(new CircleCrop());
        }

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if(layoutParams != null && layoutParams.width > 0 && layoutParams.height > 0){
            builder.override(layoutParams.width,layoutParams.height);
        }

        builder.into(view);
    }

    public void bindData(int widthPx, int heightPx, final int marginLeft, String imageUrl) {
        bindData(widthPx,heightPx,marginLeft, PixUtils.getScreenWidth(), PixUtils.getScreenHeight(),imageUrl);
    }

    public void bindData(int widthPx, int heightPx, final int marginLeft, final int maxWidth, final int maxHeight, String imageUrl) {
        if(TextUtils.isEmpty(imageUrl)){
            setVisibility(GONE);
            return;
        }else{
            setVisibility(VISIBLE);
        }

        //如果width,height小于0的话，则监听图片加载，获取图片的真实宽高
        if(widthPx <= 0 || heightPx <= 0){
            Glide.with(this).load(imageUrl).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    //获取图片资源的原本宽高
                    int height = resource.getIntrinsicHeight();
                    int width = resource.getIntrinsicWidth();

                    setSize(width,height,marginLeft,maxWidth,maxHeight);
                    setImageDrawable(resource);
                }
            });
        }

        setSize(widthPx,heightPx,marginLeft,maxWidth,maxHeight);
        setImageUrl(this,imageUrl,false);
    }

    private void setSize(int width, int height, int marginLeft, int maxWidth, int maxHeight) {
        int finalWidth,finalHeight;

        //如果宽度大，则宽度以最大值进行缩放，否则高度设为最大值
        if(width > height){
            finalWidth = maxWidth;
            finalHeight = (int) (height / ( width*1.0f/maxWidth));
        }else{
            finalHeight = maxHeight;
            finalWidth = (int) (width/ (height * 1.0f /maxHeight));
        }

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = finalHeight;
        params.width = finalWidth;

        if (params instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) params).leftMargin = height > width ? PixUtils.dp2px(marginLeft) : 0;
        } else if (params instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) params).leftMargin = height > width ? PixUtils.dp2px(marginLeft) : 0;
        }

        setLayoutParams(params);
    }

    @BindingAdapter(value = {"blur_url","radius"})
    public static void setBlurImageUrl(ImageView imageView,String blurUrl,int radius){
        Glide.with(imageView).load(blurUrl).override(radius)
                .transform(new BlurTransformation())
                .dontAnimate()
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setBackground(resource);
                    }
                });
    }
}
