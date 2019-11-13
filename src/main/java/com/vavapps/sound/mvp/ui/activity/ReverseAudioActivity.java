package com.vavapps.sound.mvp.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vavapps.sound.R;
import com.vavapps.sound.app.player.MusicPlayer;
import com.vavapps.sound.app.player.PlayerListener;
import com.vavapps.sound.app.utils.ArrayUtils;
import com.vavapps.sound.app.utils.CountThread;
import com.vavapps.sound.app.utils.FilesUtil;
//import com.kuina.audio.app.utils.HeaderSpf;
import com.vavapps.sound.app.utils.MobclickEvent;
import com.vavapps.sound.mvp.model.Music;
import com.vavapps.sound.mvp.model.view.SaveDialog;
import com.umeng.analytics.MobclickAgent;


import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
import io.microshow.rxffmpeg.RxFFmpegInvoke;
import io.microshow.rxffmpeg.RxFFmpegSubscriber;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
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

    private int currentCount = 0;
    //广告
  //  private TTRewardVideoAd mttRewardVideoAd;
  //  private TTAdNative mTTAdNative;
    private int tryTime;
    private Disposable disposable;


    public void onBack(View view) {
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reverse_audio);
        setStatusBarColor(0,ReverseAudioActivity.this);
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
        //广告相关
//        Map<String, String> responseMap = ConfigMapLoader.getInstance().getResponseMap();
//
//        if (responseMap.get("ad_vido_csj_num")!=null){
//            tryTime = Integer.parseInt(responseMap.get("ad_vido_csj_num"));
//        }

       // initAd();
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

    }

//    private void initAd() {
//
//        TTAdManager ttAdManager = TTAdManagerHolder.getInstance(this, "5035460");
//        //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
//        ttAdManager.requestPermissionIfNecessary(this);
////        //step3:创建TTAdNative对象,用于调用广告请求接口
//        mTTAdNative = ttAdManager.createAdNative(getApplicationContext());
////
//        loadAd("935460773", TTAdConstant.VERTICAL);
//
//    }

//    private void loadAd(String codeId, int orientation) {
//        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
//        AdSlot adSlot = new AdSlot.Builder()
//                .setCodeId(codeId)
//                .setSupportDeepLink(true)
//                .setImageAcceptedSize(1080, 1920)
//                .setRewardName("金币") //奖励的名称
//                .setRewardAmount(3)  //奖励的数量
//                .setUserID("1992")//用户id,必传参数
//                .setMediaExtra("media_extra") //附加参数，可选
//                .setOrientation(orientation) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
//                .build();
//        //step5:请求广告
//        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
//
//            @Override
//            public void onError(int code, String message) {
//
//            }
//
//            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
//            @Override
//            public void onRewardVideoCached() {
//
//            }
//
//            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
//            @Override
//            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
//                mttRewardVideoAd = ad;
//                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {
//
//                    @Override
//                    public void onAdShow() {
//                    }
//
//                    @Override
//                    public void onAdVideoBarClick() {
//                    }
//
//                    @Override
//                    public void onAdClose() {
//                        HeaderSpf.saveReverse(0);
//                        disposable = Observable.timer(1000, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
//                            @Override
//                            public void accept(Long aLong) throws Exception {
//                                onReverse();
//                            }
//                        });
//
//                    }
//
//                    //视频播放完成回调
//                    @Override
//                    public void onVideoComplete() {
//
//
//                    }
//
//                    @Override
//                    public void onVideoError() {
//                    }
//
//                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
//                    @Override
//                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
//                    }
//
//                });
//            }
//        });
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerDisable != null) {
            timerDisable.dispose();
        }
    }

    public void onSlashRecord(View view) {

        if (isPlaying) {
            stopPlay();
        }
        if (isReversing) {
            stopReverse();
        }

        if (isRecording) {
            MobclickAgent.onEvent(this,MobclickEvent.RocordClose);
            stopRecord();
        } else {
            MobclickAgent.onEvent(this,MobclickEvent.RocordOpen);
            record();
        }
    }

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


    public void stopPlay() {
        isPlaying = false;
        ll_play.setBackground(getResources().getDrawable(R.drawable.icon_btn_bg_normal));
        ll_reverse.setBackground(getResources().getDrawable(R.drawable.icon_btn_bg_normal));
        iv_slash.setImageResource(R.drawable.icon_point_normal);
        MusicPlayer.getInstance().stop();
    }

    public void stopReverse() {
        isReversing = false;
        isPlaying = false;
        ll_play.setBackground(getResources().getDrawable(R.drawable.icon_btn_bg_normal));
        ll_reverse.setBackground(getResources().getDrawable(R.drawable.icon_btn_bg_normal));
        iv_slash.setImageResource(R.drawable.icon_point_normal);
        MusicPlayer.getInstance().stop();
    }

    public void onSlashPlay(View view) {
        MobclickAgent.onEvent(this,MobclickEvent.RocordPlay);
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
            //广告
            //int reverse = HeaderSpf.getReverse();
            if (path == null || !new File(path).exists()) {
                Toast.makeText(this, "请先录制", Toast.LENGTH_SHORT).show();
                return;
            }else{
                onReverse();
            }
//            if (reverse <=tryTime){
//                onReverse();
//            }else {
//                if (mttRewardVideoAd!=null){
//                    mttRewardVideoAd.showRewardVideoAd(this);
//                }
//
//            }


        }
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
//广告相关
//        int reverse = HeaderSpf.getReverse();
//        HeaderSpf.saveReverse(reverse +1);
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
                    } else  Toast.makeText(ReverseAudioActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
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
                                    } else  Toast.makeText(ReverseAudioActivity.this, "保存文件失败", Toast.LENGTH_SHORT).show();
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
}
