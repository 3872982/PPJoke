package com.learning.ppjoke.utils;

import android.app.Application;

import java.lang.reflect.InvocationTargetException;

/**
 * 这种方式获取全局的Application 是一种拓展思路。
 * <p>
 * 对于组件化项目,不可能把项目实际的Application下沉到Base,而且各个module也不需要知道Application真实名字
 * <p>
 * 这种一次反射就能获取全局Application对象的方式相比于在Application#OnCreate保存一份的方式显示更加通用了
 */
public class AppGlobal {
    private static Application mApplication;

    public static Application getApplication(){
        if(mApplication == null) {
            try {
                mApplication = (Application) Class.forName("android.app.ActivityThread")
                        .getDeclaredMethod("currentApplication")
                        .invoke(null, (Object[]) null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return mApplication;
    }
}