package com.learning.ppjoke.utils;

import android.content.res.AssetManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.learning.libcommon.globals.AppGlobal;
import com.learning.ppjoke.model.BottomBar;
import com.learning.ppjoke.model.Destination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AppConfig {
    private static HashMap<String, Destination> mDestinationConfig;
    private static BottomBar mBottomBar;

    public static HashMap<String,Destination> getDestinationConfig(){
        if(mDestinationConfig == null) {
            String json = parseFile("destination.json");
            mDestinationConfig = JSON.parseObject(json, new TypeReference<HashMap<String, Destination>>() {
            });
        }

        return mDestinationConfig;
    }

    public static BottomBar getBottomBarConfig(){
        if(mBottomBar == null){
            String json = parseFile("main_tabs_config.json");
            mBottomBar = JSON.parseObject(json,BottomBar.class);
        }
        return mBottomBar;
    }

    private static String parseFile(String fileName){
        AssetManager manager = AppGlobal.getApplication().getAssets();

        InputStream is = null;
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = manager.open(fileName);
            reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while((line = reader.readLine())!= null){
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(is != null){
                    is.close();
                }

                if(reader != null){
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return sb.toString();
    }
}
