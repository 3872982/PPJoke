package com.learning.libnetwork.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CacheManager {


    public static <T> void delete(String key,T data){
        Cache cache = new Cache();
        cache.key = key;
        cache.data = toByteArray(data);
        CacheDatabase.getInstance().getCacheDao().deleteCache(cache);
    }

    public static <T> void save(String key,T data){
        Cache cache = new Cache();
        cache.key = key;
        cache.data = toByteArray(data);
        CacheDatabase.getInstance().getCacheDao().saveCache(cache);
    }

    public static Object getCache(String key){
        Cache cache = CacheDatabase.getInstance().getCacheDao().getCache(key);
        if(cache != null && cache.data != null){
            return toObject(cache.data);
        }
        return null;
    }

    /**
     * 将二进制数据转换为对象
     * @param data
     * @return
     */
    private static Object toObject(byte[] data) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;

        try{
            bais = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bais);
            Object object = ois.readObject();
            return object;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally{
            try {
                if (bais != null) {
                    bais.close();
                }
                if(ois != null){
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 将序列化数据转换为二进制数组
     * @param data
     * @param <T>  数据要实现序列化 Serializable
     * @return
     */
    private static <T> byte[] toByteArray(T data) {
        ByteArrayOutputStream  baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(data);
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(baos != null){
                    baos.close();
                }
                if(oos != null){
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }
}
