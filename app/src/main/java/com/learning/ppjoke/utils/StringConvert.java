package com.learning.ppjoke.utils;

public class StringConvert {

    public static String convertFeedUgc(int count){
        if(count < 10000){
            return String.valueOf(count);
        }

        return count/10000 + "万";
    }

    public static String convertTagFeedList(int count) {
        if(count < 10000){
            return count + "人观看";
        }

        return count/10000 + "万人观看";
    }
}
