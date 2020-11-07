package com.learning.libnetwork;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;

import com.learning.libnetwork.cache.CacheManager;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public abstract class Request<T,R extends Request> implements Cloneable {

    protected  String mUrl;
    protected HashMap<String,String> mHeaders = new HashMap<>();
    protected HashMap<String,Object> mParams = new HashMap<>();

    //仅仅只访问本地缓存，即便本地缓存不存在，也不会发起网络请求
    public static final int CACHE_ONLY = 1;
    //先访问缓存，同时发起网络的请求，成功后缓存到本地
    public static final int CACHE_FIRST = 2;
    //仅仅只访问服务器，不存任何存储
    public static final int NET_ONLY = 3;
    //先访问网络，成功后缓存到本地
    public static final int NET_CACHE = 4;

    //缓存策略
    private int mCacheStrategy;
    private String mCacheKey;
    private Type mType;

    @IntDef({CACHE_ONLY,CACHE_FIRST,NET_ONLY,NET_CACHE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CacheStrategy{
    }

    public Request(String url){
        //user/list
        mUrl = url;
    }

    public R addHeader(String key,String value){
        mHeaders.put(key,value);
        return (R) this;
    }


    public R addParam(String key,Object value){
        if(key == null || value == null){
            return (R) this;
        }

        try {
            //int byte char short long double float boolean 和他们的包装类型，但是除了 String.class 所以要额外判断
            if(value.getClass() == String.class){
                mParams.put(key,value);
            }else{
                Field type = value.getClass().getField("TYPE");
                Class claz = (Class) type.get(null);

                if(claz.isPrimitive()){
                    mParams.put(key,value);
                }
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return (R) this;
    }

    public R cacheStrategy(@CacheStrategy int cacheStrategy){
        mCacheStrategy = cacheStrategy;
        return (R) this;
    }

    public R cacheKey(String key){
        mCacheKey = key;
        return (R) this;
    }

    public R responseType(Type type){
        mType = type;
        return (R) this;
    }

    public R responseType(Class claz){
        mType = claz;
        return (R) this;
    }

    private Call getCall() {
        OkHttpClient okHttpClient = ApiService.okHttpClient;
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        addHeaders(builder);
        okhttp3.Request request = generateRequest(builder);
        Call call = okHttpClient.newCall(request);

        return call;
    }

    /**
     * 参数为callback的execute为异步操作
     * @param callBack
     */
    @SuppressLint("RestrictedApi")
    public void execute(final JsonCallBack<T> callBack){
        //缓存策略不是NET_ONLY，则需要访问缓存
        if(mCacheStrategy != NET_ONLY){
            ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    ApiResponse<T> cacheResponse = readCache();
                    if(callBack != null && cacheResponse != null && cacheResponse.body != null){
                        callBack.onCacheSuccess(cacheResponse);
                    }
                }
            });
        }

        if(mCacheStrategy != CACHE_ONLY) {
            Call call = getCall();
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    ApiResponse<T> apiResponse = new ApiResponse<>();
                    apiResponse.message = e.getMessage();
                    callBack.onFailure(apiResponse);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    ApiResponse<T> apiResponse = parseResponse(response, callBack);

                    if (!apiResponse.success) {
                        callBack.onFailure(apiResponse);
                    } else {
                        callBack.onSuccess(apiResponse);
                    }
                }
            });
        }
    }

    /**
     * 无参为同步操作Execute
     * @return
     */
    public ApiResponse<T> execute(){
        //Type值必须设置，用来解析数据
        if(mType == null){
            throw new RuntimeException("同步方法,response 返回值 类型必须设置");
        }

        if(mCacheStrategy == CACHE_ONLY){
            return readCache();
        }else {
            Call call = getCall();
            ApiResponse<T> apiResponse = null;
            try {
                Response response = call.execute();
                apiResponse = parseResponse(response, null);
            } catch (IOException e) {
                //e.printStackTrace();
                apiResponse = new ApiResponse<>();
                apiResponse.success = false;
                apiResponse.status = 0;
                apiResponse.message = e.getMessage();
            } finally {
                return apiResponse;
            }
        }
    }

    protected ApiResponse<T> parseResponse(Response response, JsonCallBack<T> callBack){
        ApiResponse<T> apiResponse = new ApiResponse<>();
        String message = null;
        boolean isSuccess = response.isSuccessful();
        int status = response.code();

        try {
            String content = response.body().string();
            if(isSuccess){
                Convert convert = ApiService.sConvert;
                //成功的情况下，content为我们所请求的json结果主体，需要去转换Json
                if(callBack != null) {
                    ParameterizedType type = (ParameterizedType) callBack.getClass().getGenericSuperclass();
                    Type arg = type.getActualTypeArguments()[0];
                    apiResponse.body = (T) convert.convert(content,arg);
                }else if(mType != null){
                    apiResponse.body = (T) convert.convert(content, mType);
                }else{
                    Log.e("request", "parseResponse: 无法解析 ");
                }
            }else{
                message = content;
            }
        } catch (IOException e) {
            message = e.getMessage();
            status = 0;
            isSuccess = false;
        }

        apiResponse.success = isSuccess;
        apiResponse.status = status;
        apiResponse.message = message;

        if(mCacheStrategy != NET_ONLY && apiResponse.success && apiResponse.body != null && apiResponse.body instanceof Serializable){
            saveCache(apiResponse.body);
        }

        return apiResponse;
    }

    protected abstract okhttp3.Request generateRequest(okhttp3.Request.Builder builder);

    private void addHeaders(okhttp3.Request.Builder builder) {
        for(HashMap.Entry<String,String> entry:mHeaders.entrySet()){
            builder.addHeader(entry.getKey(),entry.getValue());
        }
    }

    private ApiResponse<T> readCache(){
        ApiResponse<T> apiResponse = null;
        //to do...读取本地数据库room
        T cache = (T) CacheManager.getCache(mCacheKey);
        apiResponse.body = cache;
        apiResponse.success = true;
        apiResponse.message = "缓存读取成功";
        apiResponse.status = 304;
        return apiResponse;
    }

    private void saveCache(T body){
        String key = TextUtils.isEmpty(mCacheKey) ? mCacheKey : generateCacheKey();
        //to do...保存本地数据库room
        CacheManager.save(key,body);
    }

    private String generateCacheKey() {
        String urlWithParams = UrlCreator.createUrlFromParams(mUrl, mParams);
        return urlWithParams;
    }

    @NonNull
    @Override
    protected Request clone() throws CloneNotSupportedException {
        return (Request<T,R>) super.clone();
    }
}
