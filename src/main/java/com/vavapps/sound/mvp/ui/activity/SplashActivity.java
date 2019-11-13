package com.vavapps.sound.mvp.ui.activity;

import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //去除标题栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //去除状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_splash);
//
//
//       // FrameLayout flAd = findViewById(R.id.fl_ad);
//        FrameLayout rootView = findViewById(R.id.root_view);
//
//        TTAdManager ttAdManager = TTAdManagerHolder.getInstance(this, "5035460");
//
//        ttAdManager.requestPermissionIfNecessary(this);
//        TTAdNative ttAdNative = ttAdManager.createAdNative(getApplicationContext());
//        AdSlot adSlot = new AdSlot.Builder()
//                .setCodeId("835460468")
//                .setSupportDeepLink(true)
//                .setImageAcceptedSize(1080, 1920)
//                .build();
//        ttAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
//            @Override
//            public void onError(int i, String s) {
//            }
//
//            @Override
//            public void onTimeout() {
//            }
//
//            @Override
//            public void onSplashAdLoad(final TTSplashAd ttSplashAd) {
//                View splashAdView = ttSplashAd.getSplashView();
//                flAd.addView(splashAdView);
//                ttSplashAd.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
//                    @Override
//                    public void onAdClicked(View view, int i) {
//                    }
//
//                    @Override
//                    public void onAdShow(View view, int i) {
//
//                    }
//
//                    @Override
//                    public void onAdSkip() {
//                        launchMain();
//                    }
//
//                    @Override
//                    public void onAdTimeOver() {
//                        launchMain();
//                    }
//                });
//            }
//        }, 5000);
//
//
//    }
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        launchMain();
//    }
//    private void launchMain() {
//        if (isFinishing()) {
//            return;
//        }
//        Intent intent =new Intent(this,MainActivity.class);
//        startActivity(intent);
//        finish();
//    }
}
