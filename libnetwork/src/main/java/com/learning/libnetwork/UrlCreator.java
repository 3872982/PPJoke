package com.learning.libnetwork;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class UrlCreator {
    public static String createUrlFromParams(String url, HashMap<String, Object> params) {

        StringBuilder sb = new StringBuilder();
        sb.append(url);

        //说明原本的url中已经带参数了
        if(url.indexOf('?')>0 || url.indexOf('&')>0){
            sb.append('&');
        }else{
            sb.append('?');
        }

        for(HashMap.Entry<String,Object> entry:params.entrySet()){
            try {
                String value = URLEncoder.encode(String.valueOf(entry.getValue()),"UTF-8");
                sb.append(entry.getKey()+"="+value+"&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        //删除最后多出来的一个？或者 &
        sb.deleteCharAt(sb.length()-1);

        return sb.toString();
    }
}
