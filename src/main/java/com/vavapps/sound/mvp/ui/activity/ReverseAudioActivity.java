package com.vavapps.sound.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ark.dict.ConfigMapLoader;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.vavapps.sound.R;
import com.vavapps.sound.app.SoundApp;
import com.vavapps.sound.app.player.MusicPlayer;
import com.vavapps.sound.app.player.PlayerListener;
import com.vavapps.sound.app.utils.ArrayUtils;
import com.vavapps.sound.app.utils.CountThread;
import com.vavapps.sound.app.utils.FilesUtil;
import com.vavapps.sound.app.utils.GetRealTimeUtils;
import com.vavapps.sound.app.utils.HeaderSpf;
import com.vavapps.sound.app.utils.MobclickEvent;
import com.vavapps.sound.mvp.event.WeixinPayEvent;
import com.vavapps.sound.mvp.model.AliOrderEntity;
import com.vavapps.sound.mvp.model.Music;
import com.vavapps.sound.mvp.model.PayResult;
import com.vavapps.sound.mvp.model.VipPriceEntity;
import com.vavapps.sound.mvp.model.WechatOrderEntity;
import com.vavapps.sound.mvp.model.view.ChoosePayDialog;
import com.vavapps.sound.mvp.model.view.PayStatuDialog;
import com.vavapps.sound.mvp.model.view.SaveDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
import io.microshow.rxffmpeg.RxFFmpegInvoke;
import io.microshow.rxffmpeg.RxFFmpegSubscriber;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tech.oom.idealrecorder.IdealRecorder;
import tech.oom.idealrecorder.StatusListener;

public class ReverseAudioActivity extends AppCompatActivity {


    private String path;
    private CountThread countThread;

    private boolean isRecording = false;

    private boolean isPlaying = false;


    private boolean isReversing = false;
    /**
     * IdealRecorder的实例
     */
    private IdealRecorder idealRecorder;
    private IdealRecorder.RecordConfig recordConfig;
    private String output;
    private LinearLayout ll_record;
    private LinearLayout ll_play;
    private LinearLayout ll_reverse;
    private ImageView iv_slash;
    private TextView tvTime;
    private TextView tvNo;
    private long time;
    private Disposable timerDisable;
    private View btRemove;
    private PayStatuDialog payStatuDialog;
    private ChoosePayDialog choosePayDialog;
    private int daytrytime;

    private int currentCount = 0;
    private int tryTime;
    private Disposable disposable;


    public void onBack(View view) {
        finish();
    }

    static String vipUrl = "https://vipserver.adesk.com";
    private VipPriceEntity vipPriceEntity;
    final String pacakgeName = "com.vavapps.sound";
    final String platform = "android";


    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。

                        Map<String, String> map = new HashMap<>();
                        map.put("method", "支付宝");
                        map.put("if_success", "成功");
                        GetRealTimeUtils.getNetTime(new GetRealTimeUtils.TimeCallBack() {
                            @Override
                            public void getTime(Long time) {
                                //保存vip支付成功的生效时间
                                HeaderSpf.saveVipTime(time);
                                //更新当前最新时间
                                HeaderSpf.saveCurrentTime(time);
                                Toast.makeText(getApplicationContext(), "支付成功" + time, Toast.LENGTH_SHORT).show();
                                setSharedPreference("Trydata",-1000);
                                boolean vip = GetRealTimeUtils.isVip(time);
                                if (vip) {
                                   // btRemove.setVisibility(View.GONE);
                                } else {
                                   // btRemove.setVisibility(View.VISIBLE);
                                }
                            }
                        });


                    } else {
                        Map<String, String> map = new HashMap<>();
                        map.put("method", "支付宝");
                        map.put("if_success", "失败");
                        Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    };

    public void closeVip(View view) {
        HeaderSpf.saveVipTime(0l);
    }

    public void openVip(View view) {

        payStatuDialog = new PayStatuDialog();
        payStatuDialog.setCancelable(false);
        payStatuDialog.show(getSupportFragmentManager(), "pay_statu");

        initWechatPay();
//        initAliPay();
//        e9f99566c96d0193bd7cb6987cc382995ffada96
    }

    private void dissDilaog() {
        if (payStatuDialog != null) {
            payStatuDialog.dismiss();
            payStatuDialog = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void WexinPayResult(WeixinPayEvent weixinPayEvent) {
        Map<String, String> map = new HashMap<>();
        map.put("method", "微信");
        if (weixinPayEvent.getErrorCode() == 0) {

            map.put("if_success", "成功");
            GetRealTimeUtils.getNetTime(new GetRealTimeUtils.TimeCallBack() {
                @Override
                public void getTime(Long time) {
                    //保存vip支付成功的生效时间
                    HeaderSpf.saveVipTime(time);
                    //更新当前最新时间
                    HeaderSpf.saveCurrentTime(time);
                    Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();

                    boolean vip = GetRealTimeUtils.isVip(time);
                    if (vip) {
                       // btRemove.setVisibility(View.GONE);
                    } else {
                       // btRemove.setVisibility(View.VISIBLE);
                    }
                }
            });


        } else if (weixinPayEvent.getErrorCode() == -1) {
            Toast.makeText(getApplicationContext(), "支付错误", Toast.LENGTH_SHORT).show();
            map.put("if_success", "失败");
        } else if (weixinPayEvent.getErrorCode() == -2) {
            map.put("if_success", "失败");
            Toast.makeText(getApplicationContext(), "支付取消", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerDisable != null) {
            timerDisable.dispose();
        }
        EventBus.getDefault().unregister(this);
    }

    private long currentTime = 0;

    @Override
    protected void onResume() {
        super.onResume();
        if (currentTime == 0) {
            Date date1 = new Date(System.currentTimeMillis());
            currentTime = HeaderSpf.getCurrentTime();
            currentTime = date1.getTime();
        }
        GetRealTimeUtils.getNetTime(new GetRealTimeUtils.TimeCallBack() {
            @Override
            public void getTime(Long time) {
                currentTime = time;
                HeaderSpf.saveCurrentTime(currentTime);
                boolean vip = GetRealTimeUtils.isVip(time);
                if (vip) {
                   // btRemove.setVisibility(View.GONE);
                } else {
                    //获取已尝试的次数
//                    int reverse = HeaderSpf.getReverse();
                    int reverse =getSharedPreference("Trydata");
                    //tyrTime(当天的免费次数)
                    int trytime=getTryTime();
                    if (reverse <= trytime) {
//                        btRemove.setVisibility(View.VISIBLE);
                       // btRemove.setVisibility(View.GONE);
                    } else {
//                        btRemove.setVisibility(View.GONE);
                       // btRemove.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void onRemoveAds(View view) {
        MobclickAgent.onEvent(this, MobclickEvent.BtnPay);
        if (choosePayDialog == null) {
            choosePayDialog = new ChoosePayDialog();
            Bundle bundle = new Bundle();
            bundle.putString("price", vipPriceEntity.getData().get(0).getPrice());
            choosePayDialog.setArguments(bundle);
            choosePayDialog.setPayTypeClick(new ChoosePayDialog.PayTypeClick() {
                @Override
                public void aliPay() {
                    initAliPay();
                }

                @Override
                public void wechatPay() {
                    initWechatPay();
                }
            });
        }
        choosePayDialog.show(getSupportFragmentManager(), "dialog");
    }

    private void initPrice() {
        Disposable disposable = Observable.create((ObservableOnSubscribe<VipPriceEntity>) emitter -> {

            String requestUrl = vipUrl + "/v1/price/list?platform=android&package=com.vavapps.sound";
            OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
            Request request = new Request.Builder()
                    .url(requestUrl)//请求链接
                    .build();//创建Request对象
            try {
                Response response = client.newCall(request).execute();//获取Response对象
                String string = response.body().string();

                VipPriceEntity vipPriceEntity = new Gson().fromJson(string, VipPriceEntity.class);
                emitter.onNext(vipPriceEntity);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> vipPriceEntity = o);
    }

    //获取后台次数数据
//    private void initTryTime(){
//        Disposable disposable = Observable.create((ObservableOnSubscribe<TryTimeEntity>) emitter -> {
//
//            String requestUrl = vipUrl + "/v1/price/list?platform=android&package=com.vavapps.sound";
//            OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
//            Request request = new Request.Builder()
//                    .url(requestUrl)//请求链接
//                    .build();//创建Request对象
//            try {
//                Response response = client.newCall(request).execute();//获取Response对象
//                String string = response.body().string();
//
//                TryTimeEntity tryTimeEntity = new Gson().fromJson(string, TryTimeEntity.class);
//                emitter.onNext(tryTimeEntity);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(o -> tryTimeEntity = o);
//    }
    //阿里支付
    private void initAliPay() {
        Disposable disposable = Observable.create((ObservableOnSubscribe<AliOrderEntity>) emitter -> {
            String openid = System.currentTimeMillis() + "";
            String alipay = vipUrl + "/v1/alipay/sign?platform=" + platform + "&package=" + pacakgeName + "&openid=" + openid + "&subject=" + "getVip" + "&price_id=" + vipPriceEntity.getData().get(0).getId();
            OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
            Request request = new Request.Builder()
                    .url(alipay)//请求链接
                    .build();//创建Request对象
            try {
                Response response = client.newCall(request).execute();//获取Response对象
                String string = response.body().string();
                dissDilaog();
                AliOrderEntity aliOrderEntity = new Gson().fromJson(string, AliOrderEntity.class);
                emitter.onNext(aliOrderEntity);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AliOrderEntity>() {
                    @Override
                    public void accept(AliOrderEntity aliOrderEntity) throws Exception {

                        if (aliOrderEntity.getCode() == 0) {
                            Runnable payRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    PayTask alipay = new PayTask(ReverseAudioActivity.this);
                                    Map<String, String> result = alipay.payV2(aliOrderEntity.getData(), true);
                                    Message msg = new Message();
                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                }
                            };
                            // 必须异步调用
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();
                        } else {
                            Toast.makeText(getApplicationContext(), aliOrderEntity.getMsg().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    //微信支付
    private void initWechatPay() {


        Disposable disposable = Observable.create((ObservableOnSubscribe<WechatOrderEntity>) emitter -> {

            String openid = System.currentTimeMillis() + "";
            String alipay = vipUrl + "/v1/wechat/sign?platform=" + platform + "&package=" + pacakgeName + "&openid=" + openid + "&subject=" + "getVip" + "&price_id=" + vipPriceEntity.getData().get(0).getId();
            OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
            Request request = new Request.Builder()
                    .url(alipay)//请求链接
                    .build();//创建Request对象
            try {
                Response response = client.newCall(request).execute();//获取Response对象
                String string = response.body().string();
                dissDilaog();
                WechatOrderEntity aliOrderEntity = new Gson().fromJson(string, WechatOrderEntity.class);
                emitter.onNext(aliOrderEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WechatOrderEntity>() {
                    @Override
                    public void accept(WechatOrderEntity wechatOrderEntity) throws Exception {

                        if (wechatOrderEntity != null) {
                            if (wechatOrderEntity.getCode() == 0) {
                                WechatOrderEntity.WechatOrderData wecatPayEntity = wechatOrderEntity.getData();
                                PayReq wxPayReq = new PayReq();
                                wxPayReq.appId = wecatPayEntity.getAppid();
                                wxPayReq.nonceStr = wecatPayEntity.getNoncestr();
                                wxPayReq.partnerId = wecatPayEntity.getPartnerid();
                                wxPayReq.packageValue = wecatPayEntity.getPackageX();
                                wxPayReq.sign = wecatPayEntity.getSign();
                                wxPayReq.prepayId = wecatPayEntity.getPrepayid();
                                wxPayReq.timeStamp = String.valueOf(wecatPayEntity.getTimestamp());
                                SoundApp.wxAPi.sendReq(wxPayReq);
                            } else {
                                Toast.makeText(getApplicationContext(), wechatOrderEntity.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                });


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reverse_audio);

        //btRemove = findViewById(R.id.bt_remove);
        int trytime=getTryTime();

//        if (AdsShowUtils.hasVideoAd()) {
//            btRemove.setVisibility(View.VISIBLE);
//        } else {
//            btRemove.setVisibility(View.GONE);
//        }
//        EventBus.getDefault().register(this);
//
//        tryTime = AdsShowUtils.getTryTime();
        int re=getSharedPreference("Trydata");
        if(re<=trytime){
           // btRemove.setVisibility(View.GONE);
        }else{
           // btRemove.setVisibility(View.VISIBLE);
        }
        initPrice();

        //设置状态栏颜色
        setStatusBarColor(0, ReverseAudioActivity.this);
        //权限申请
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.RECORD_AUDIO}, 1);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        ll_record = findViewById(R.id.ll_record);
        ll_play = findViewById(R.id.ll_play);
        ll_reverse = findViewById(R.id.ll_reverse);
        iv_slash = findViewById(R.id.iv_slash);
        tvTime = findViewById(R.id.tv_time);
        tvNo = findViewById(R.id.tv_no);
        timerDisable = Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        tvTime.setText(Music.sdfCurentData.format(new Date()));
                    }
                });
        idealRecorder = IdealRecorder.getInstance();
        countThread = new CountThread(this, 0) {
            @Override
            protected void onCount(long count) {

                if (!isRecording) {
                    iv_slash.setImageResource(R.drawable.icon_point_normal);
                } else {
                    if (count % 2000 == 0) {
                        iv_slash.setImageResource(R.drawable.icon_point_normal);
                    } else {
                        iv_slash.setImageResource(R.drawable.icon_point_recording);
                    }
                }

                time = count;
                tvNo.setText("NO." + currentCount + " " + Music.sdfDuration.format(new Date(time)));
            }
        };
        recordConfig = new IdealRecorder.RecordConfig(MediaRecorder.AudioSource.MIC, IdealRecorder.RecordConfig.SAMPLE_RATE_22K_HZ, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        MusicPlayer.getInstance().addListener(new PlayerListener() {
            @Override
            public void onPlay() {

            }

            @Override
            public void onPause() {

            }

            @Override
            public void onCompletion() {
                isPlaying = false;
                isReversing = false;
                ll_play.setBackground(getResources().getDrawable(R.drawable.icon_btn_bg_normal));
                ll_reverse.setBackground(getResources().getDrawable(R.drawable.icon_btn_bg_normal));
                iv_slash.setImageResource(R.drawable.icon_point_normal);
            }
        });
        //判断是否为第二天并重置次数
        Date date = new Date(System.currentTimeMillis());
        boolean isday = isDay(date);
        if (isday == false) {
            setSharedPreference("Trydata", 0);
        }
    }

    //是否播放
    public void onSlashRecord(View view) {

        if (isPlaying) {
            stopPlay();
        }
        if (isReversing) {
            stopReverse();
        }

        if (isRecording) {
            MobclickAgent.onEvent(this, MobclickEvent.RocordClose);
            stopRecord();
        } else {
            MobclickAgent.onEvent(this, MobclickEvent.RocordOpen);
            record();
        }
    }

    //录音
    private void record() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        //如果需要保存录音文件  设置好保存路径就会自动保存  也可以通过onRecordData 回调自己保存  不设置 不会保存录音
        path = FilesUtil.getSaveFilePath();
        idealRecorder.setRecordFilePath(path);
        //设置录音配置 最长录音时长 以及音量回调的时间间隔
        idealRecorder.setRecordConfig(recordConfig).setVolumeInterval(200);
        //设置录音时各种状态的监听
        idealRecorder.setStatusListener(statusListener);
        currentCount++;
        idealRecorder.start(); //开始录音
    }

    private void stopRecord() {
        idealRecorder.stop();
    }


    //暂停播放
    public void stopPlay() {
        isPlaying = false;
        ll_play.setBackground(getResources().getDrawable(R.drawable.icon_btn_bg_normal));
        ll_reverse.setBackground(getResources().getDrawable(R.drawable.icon_btn_bg_normal));
        iv_slash.setImageResource(R.drawable.icon_point_normal);
        MusicPlayer.getInstance().stop();
    }

    //暂停倒放
    public void stopReverse() {
        isReversing = false;
        isPlaying = false;
        ll_play.setBackground(getResources().getDrawable(R.drawable.icon_btn_bg_normal));
        ll_reverse.setBackground(getResources().getDrawable(R.drawable.icon_btn_bg_normal));
        iv_slash.setImageResource(R.drawable.icon_point_normal);
        MusicPlayer.getInstance().stop();
    }

    public void onSlashPlay(View view) {
        MobclickAgent.onEvent(this, MobclickEvent.RocordPlay);
        if (isRecording) {
            Toast.makeText(this, "请先暂停录制", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isReversing) {
            stopReverse();
        }

        if (isPlaying) {
            stopPlay();
        } else {
            if (path == null || !new File(path).exists()) {
                Toast.makeText(this, "请先录制", Toast.LENGTH_SHORT).show();
                return;
            }

            MusicPlayer.getInstance().prepareSource(path);
            ll_play.setBackground(getResources().getDrawable(R.drawable.icon_btn_bg_play));
            iv_slash.setImageResource(R.drawable.icon_point_play);
            isPlaying = true;
            MusicPlayer.getInstance().start();
        }


    }

    public void onPlayReverse(View view) {
        MobclickAgent.onEvent(this, MobclickEvent.RocordBack);
        if (isRecording) {
            Toast.makeText(this, "请先暂停录制", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isPlaying) {
            stopPlay();
        }

        if (isReversing) {
            stopReverse();
        } else {
            if (path == null || !new File(path).exists()) {
                Toast.makeText(this, "请先录制", Toast.LENGTH_SHORT).show();
                return;
            }
            if (GetRealTimeUtils.isVip(currentTime))
            {
                onReverse();
            } else
                {
                // int reverse = HeaderSpf.getReverse();
                //tyrTime(当天的免费次数)
//                if (reverse <= tryTime) {
//                    onReverse();
//                } else {
//                    Toast.makeText(this, "已达到免费次数，请购买会员后使用", Toast.LENGTH_SHORT).show();
//                }
                int reverse = getSharedPreference("Trydata");
                int trytime= getTryTime();
                //可动态设置最大次数
                if (reverse <= trytime) {
                    onReverse();
                    reverse = reverse+1;
                   // Toast.makeText(getApplicationContext(),reverse,Toast.LENGTH_SHORT).show();
                    setSharedPreference("Trydata", reverse);
                } else {
                  //  btRemove.setVisibility(View.VISIBLE);
                    onRemoveAds(ll_reverse);
                   // Toast.makeText(this, "已达到免费次数，请购买会员后使用", Toast.LENGTH_SHORT).show();
                }
            }
        }
        //次数加一
    }

    public void onReverse() {
        output = this.getExternalFilesDir(null).getAbsolutePath() + "/result.wav";
        File file = new File(path);

        File file1 = new File(output);
        if (file1.exists()) {
            file1.delete();
        }
        boolean exists = file.exists();
        if (!exists) return;
        String format = String.format("ffmpeg -i %s -vf reverse -af areverse %s", file.getAbsolutePath(), output);
        String text = format;
        String[] commands = text.split(" ");
        RxFFmpegInvoke.getInstance().runCommandRxJava(commands).subscribe(new RxFFmpegSubscriber() {
            @Override
            public void onFinish() {
                MusicPlayer.getInstance().prepareSource(output);
                ll_reverse.setBackground(getResources().getDrawable(R.drawable.icon_btn_bg_play));
                iv_slash.setImageResource(R.drawable.icon_point_play);
                isReversing = true;
                MusicPlayer.getInstance().start();
            }


            @Override
            public void onProgress(int progress, long progressTime) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String message) {

            }
        });

    }

    private boolean defPoint = false;

    private StatusListener statusListener = new StatusListener() {
        @Override
        public void onStartRecording() {
            Toast.makeText(ReverseAudioActivity.this, "开始录音", Toast.LENGTH_SHORT).show();

            ll_record.setBackground(getResources().getDrawable(R.drawable.icon_btn_bg_recording));
            isRecording = true;
            countThread.setCount(0);
            countThread.start();

        }

        @Override
        public void onRecordData(short[] data, int length) {

        }

        @Override
        public void onVoiceVolume(int volume) {

        }

        @Override
        public void onRecordError(int code, String errorMsg) {
            isRecording = false;
            ll_record.setBackground(getResources().getDrawable(R.drawable.icon_btn_bg_normal));
        }

        @Override
        public void onFileSaveFailed(String error) {
            Toast.makeText(ReverseAudioActivity.this, "文件保存失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFileSaveSuccess(String fileUri) {
            path = fileUri;
        }

        @Override
        public void onStopRecording() {
            isRecording = false;
            countThread.stop();
            ll_record.setBackground(getResources().getDrawable(R.drawable.icon_btn_bg_normal));
            iv_slash.setImageResource(R.drawable.icon_point_normal);
            Toast.makeText(ReverseAudioActivity.this, "结束录音", Toast.LENGTH_SHORT).show();
        }
    };

    public void onDone(View view) {
        new SaveDialog(ReverseAudioActivity.this) {
            @Override
            public void onSave(String name, cafe.adriel.androidaudioconverter.model.AudioFormat format) {
                if (format == cafe.adriel.androidaudioconverter.model.AudioFormat.WAV) {
                    String newPath = FilesUtil.PROJECT_DIR + name + ".wav";
                    if (FilesUtil.copyFile(path, newPath) != 0) {
                        Intent intent = new Intent();
                        intent.putExtra("path", newPath);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else
                        Toast.makeText(ReverseAudioActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                } else {
                    AndroidAudioConverter.with(getContext())
                            .setFile(new File(path))
                            .setFormat(ArrayUtils.formats[ArrayUtils.defFormat])
                            .setSampleRate(ArrayUtils.sampleRate[ArrayUtils.defSampleRate])
                            //.setBitRate(BitRate.s16)
                            .setMono(true)
                            .setCallback(new IConvertCallback() {
                                @Override
                                public void onSuccess(File convertedFile) {
                                    String newPath = FilesUtil.PROJECT_DIR + name + "." + format.getFormat();
                                    if (FilesUtil.copyFile(path, newPath) != 0) {
                                        Intent intent = new Intent();
                                        intent.putExtra("path", newPath);
                                        setResult(Activity.RESULT_OK, intent);
                                        finish();
                                    } else
                                        Toast.makeText(ReverseAudioActivity.this, "保存文件失败", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Exception error) {
                                    Toast.makeText(ReverseAudioActivity.this, "保存文件失败", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .convert();
                }
            }
        }.show();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(int statusColor, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            //取消设置Window半透明的Flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏为透明
            window.setStatusBarColor(Color.parseColor("#2c292e"));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    //创建共享数据存入次数
    public int getSharedPreference(String key) {
        SharedPreferences sp = getSharedPreferences("Trydata", Context.MODE_PRIVATE);
        int str = sp.getInt(key, 0);
        return str;
    }

    public void setSharedPreference(String key, int values) {
        SharedPreferences sp = getSharedPreferences("Trydata", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putInt(key, values);
        et.commit();
    }

    //存入日期数据
    public void setSharedPreference(String key, String date) {
        SharedPreferences sp = getSharedPreferences("date", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString(key, date);
        et.commit();
    }

    public String getSharedPreference1(String key) {
        SharedPreferences sp = getSharedPreferences("date", Context.MODE_PRIVATE);
        String str = sp.getString(key, " ");
        return str;
    }

    //获取当前日期，判断是否为第二天 更新共享数据
    public boolean isDay(Date date) {
        boolean isday = true;
        String data = getSharedPreference1("date");
        if (data.equals("")) {
            isday = true;
        } else {
            Date date1 = null;
            try {
                date1 = StringToDate(data);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int i = daysBetween(date1, date);
            if (i != 0) {
                isday = false;
            }
            setSharedPreference("date", date.toString());
        }
        setSharedPreference("date", date.toString());
        return isday;
    }
    private Date StringToDate(String time) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        date = format.parse(time);
        return date;
    }
    public static int daysBetween(Date date1,Date date2)
    {
        if(date1==null||date2==null){
            return 0;
        }else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            long time1 = cal.getTimeInMillis();
            cal.setTime(date2);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        }
    }
    public static int getTryTime() {
        String ad_vido_csj_num = ConfigMapLoader.getInstance().getResponseMap().get("ad_vido_csj_num");

        if (ad_vido_csj_num == null) {
            return 3;
        }

        return Integer.parseInt(ad_vido_csj_num);
    }
    public static int getWxSwitch() {
        String wx_pay_switch = ConfigMapLoader.getInstance().getResponseMap().get("wx_pay_switch");

        if (wx_pay_switch == null) {
            return 0;
        }
        return Integer.parseInt(wx_pay_switch);
    }
}
