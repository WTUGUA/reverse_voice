package com.vavapps.sound.mvp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.ark.adkit.polymers.ttad.config.TTAdManagerHolder;
import com.ark.dict.ConfigMapLoader;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.vavapps.sound.R;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        int splash=getSplash();
        String lsvs=getLastVs();
        try {
            String versionname = getVersionName();
            if (versionname.equals(lsvs)) {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            }else {
                if (splash == 0) {
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    FrameLayout flAd = findViewById(R.id.fl_ad);
                    FrameLayout rootView = findViewById(R.id.root_view);
                    TTAdManager ttAdManager = TTAdManagerHolder.getInstance(this, "5037105");
                    ttAdManager.requestPermissionIfNecessary(this);
                    TTAdNative ttAdNative = ttAdManager.createAdNative(getApplicationContext());
                    AdSlot adSlot = new AdSlot.Builder()
                            .setCodeId("837105566")
                            .setSupportDeepLink(true)
                            .setImageAcceptedSize(1080, 1920)
                            .build();
                    ttAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
                        @Override
                        public void onError(int i, String s) {


                        }

                        @Override
                        public void onTimeout() {


                        }

                        @Override
                        public void onSplashAdLoad(final TTSplashAd ttSplashAd) {
                            View splashAdView = ttSplashAd.getSplashView();
                            flAd.addView(splashAdView);
                            ttSplashAd.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                                @Override
                                public void onAdClicked(View view, int i) {

                                }

                                @Override
                                public void onAdShow(View view, int i) {
                                }

                                @Override
                                public void onAdSkip() {

                                    launchMain();
                                }

                                @Override
                                public void onAdTimeOver() {
                                    launchMain();
                                }
                            });
                        }
                    }, 5000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        launchMain();
    }
    private void launchMain() {
        if (isFinishing()) {
            return;
        }
        Intent intent =new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
    public static int getSplash() {
        String ad_openwindow_set = ConfigMapLoader.getInstance().getResponseMap().get("ad_openwindow_set");

        if (ad_openwindow_set == null) {
            return 0;
        }
        return Integer.parseInt(ad_openwindow_set);
    }
    public static String getLastVs() {
        String latest_version = ConfigMapLoader.getInstance().getResponseMap().get("latest_version");

        if (latest_version == null) {
            return "2.6";
        }
        return latest_version;
    }
    private int getVersionCode() throws Exception {
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packageInfo.versionCode;
    }
    private String getVersionName() throws Exception {
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packInfo.versionName;
    }
}
