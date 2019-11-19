package com.vavapps.sound.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

//import com.ark.dict.Utils;

import com.ark.dict.Utils;


/**
 * Created by lijianglong on 2017/9/5.
 */

public class HeaderSpf {

    private static final String PREF_SESSION_ID = "pref_reverse_id";
    private static final String PREF_NAME = "pref_reverse";
    private static final String VipTime = "pref_vipTime";
    private static final String CurrentTime = "pref_CurrentTime";

    private static SharedPreferences getDefaultPreferences() {
        return Utils.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void clear() {
        getDefaultPreferences().edit().clear().apply();
    }

    public static void saveReverse(int count) {
        getDefaultPreferences().edit().putInt(PREF_SESSION_ID, count).apply();
    }

    public static void saveVipTime(Long time) {
        getDefaultPreferences().edit().putLong(VipTime, time).apply();
    }

    public static Long getVipTime() {
        return getDefaultPreferences().getLong(VipTime, 0);
    }


    public static Long getCurrentTime(){
        return getDefaultPreferences().getLong(CurrentTime, 0);
    }
    public static void saveCurrentTime(Long currentTime){
        getDefaultPreferences().edit().putLong(CurrentTime, currentTime);
    }


    public static int getReverse() {
        return getDefaultPreferences().getInt(PREF_SESSION_ID, 0);
    }
}
