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

    private static SharedPreferences getDefaultPreferences() {
        return Utils.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void clear() {
        getDefaultPreferences().edit().clear().apply();
    }

    public static void saveReverse(int count) {
        getDefaultPreferences().edit().putInt(PREF_SESSION_ID, count).apply();
    }

    public static int getReverse() {
        return getDefaultPreferences().getInt(PREF_SESSION_ID, 0);
    }
}
