package com.learning.libnetwork;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Type;

public class JsonConvert implements Convert{
    @Override
    public Object convert(String content, Type type) {
        JSONObject object = JSON.parseObject(content);
        JSONObject data = object.getJSONObject("data");

        if(data != null){
            Object data1 = data.get("data");
            if(data1 == null) return null;
            return JSON.parseObject(data1.toString(),type);
        }
        return null;
    }
}
