package com.learning.libnetwork;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class GetRequest<T> extends Request<T,GetRequest> {

    public GetRequest(String url) {
        super(url);
    }

    @Override
    protected okhttp3.Request generateRequest(okhttp3.Request.Builder builder) {
        //拼接查询参数到url后面
        String url = UrlCreator.createUrlFromParams(mUrl,mParams);
        okhttp3.Request request = builder.get().url(url).build();
        return request;
    }


}
