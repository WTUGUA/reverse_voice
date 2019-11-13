package com.vavapps.sound.app.utils;

import android.util.Log;

public class LogUtils {
    public static boolean DEBUG = true;

    public static void d(String log){
        Log.d("com.novv.core",log);
    }

    public static void w(String log){
        Log.w("com.novv.core",log);
    }

    public static void e(String log){
        Log.e("com.novv.core",log);
    }

    public static void e(Exception ex){
        Log.e("com.novv.core",ex.getMessage());
    }
}

