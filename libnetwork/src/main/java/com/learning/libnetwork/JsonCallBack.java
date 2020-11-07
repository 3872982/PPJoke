package com.learning.libnetwork;

public abstract class JsonCallBack<T> {

    public void onSuccess(ApiResponse<T> response){

    }

    public void onFailure(ApiResponse<T> response){

    }

    public void onCacheSuccess(ApiResponse<T> response){

    }
}
