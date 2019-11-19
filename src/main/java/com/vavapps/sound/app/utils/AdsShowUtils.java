package com.vavapps.sound.app.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.ark.dict.ConfigMapLoader;
import com.vavapps.sound.app.SoundApp;

public class AdsShowUtils {


    public static boolean isLatest_version() {
        String latest_version = ConfigMapLoader.getInstance().getResponseMap().get("latest_version");
        String versionName = getVersionName(SoundApp.getContext());
        //如果取不到最新参数 return true
        if (latest_version == null) {
            return true;
        }
        if (latest_version.equals(versionName)) {
            return true;
        }
        return false;
    }

    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


    public static boolean hasSplash() {
        if (isLatest_version()) {
            String latest_ad_openwindow_set = ConfigMapLoader.getInstance().getResponseMap().get("latest_ad_openwindow_set");
            if (latest_ad_openwindow_set == null) {
                return false;
            }
            return Integer.parseInt(latest_ad_openwindow_set) != 0;

        } else {
            String ad_openwindow_set = ConfigMapLoader.getInstance().getResponseMap().get("ad_openwindow_set");
            if (ad_openwindow_set == null) {
                return false;
            }
            return Integer.parseInt(ad_openwindow_set) != 0;
        }

    }


    public static boolean hasVideoAd() {

        if (isLatest_version()) {
            String latest_ad_video_set = ConfigMapLoader.getInstance().getResponseMap().get("latest_ad_video_set");
            if (latest_ad_video_set == null) {
                return false;
            }
            return Integer.parseInt(latest_ad_video_set) != 0;

        } else {
            String ad_video_set = ConfigMapLoader.getInstance().getResponseMap().get("ad_video_set");
            if (ad_video_set == null) {
                return false;
            }
            return Integer.parseInt(ad_video_set) != 0;
        }

    }

    public static int getTryTime() {
        String ad_vido_csj_num = ConfigMapLoader.getInstance().getResponseMap().get("ad_vido_csj_num");

        if (ad_vido_csj_num == null) {
            return 3;
        }
        return Integer.parseInt(ad_vido_csj_num);
    }
}
