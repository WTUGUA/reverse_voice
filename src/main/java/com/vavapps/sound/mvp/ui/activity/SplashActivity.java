package com.vavapps.sound.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.ark.adkit.polymers.ttad.config.TTAdManagerHolder;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.vavapps.sound.R;

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
                launchMain();
            }

            @Override
            public void onTimeout() {
                launchMain();
            }

            @Override
            public void onSplashAdLoad(final TTSplashAd ttSplashAd) {
                View splashAdView = ttSplashAd.getSplashView();
                flAd.addView(splashAdView);
                ttSplashAd.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int i) {
                        launchMain();
                    }

                    @Override
                    public void onAdShow(View view, int i) {
                        launchMain();
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
}
