package com.vavapps.sound.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.OnClick;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.vavapps.sound.R;
import com.vavapps.sound.app.utils.FileUtils;
import com.vavapps.sound.app.utils.MobclickEvent;
import com.vavapps.sound.app.utils.RxUtils;
import com.vavapps.sound.di.component.DaggerSaveAudioComponent;
import com.vavapps.sound.mvp.contract.SaveAudioContract;
import com.vavapps.sound.mvp.model.entity.AudioEntity;
import com.vavapps.sound.mvp.presenter.SaveAudioPresenter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;
import io.microshow.rxffmpeg.RxFFmpegInvoke;
import io.microshow.rxffmpeg.RxFFmpegSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import java.io.File;
import java.io.IOException;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/06/2019 10:43
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class SaveAudioActivity extends BaseActivity<SaveAudioPresenter> implements SaveAudioContract.View, SeekBar.OnSeekBarChangeListener {
    @BindView(R.id.toolbar_back)
    RelativeLayout toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.rl_complete)
    RelativeLayout rlComplete;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_control)
    ImageView ivControl;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.pb_play)
    SeekBar pbPlay;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.et_file_name)
    EditText etFileName;
    private Long id;
    private static final String Content = "content";
    private MediaPlayer mPlayer;
    private boolean hadDestroy = false;



    final RxPermissions rxPermissions = new RxPermissions(this);
    String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};




    private int mPlayerDuration;
    private String stringContent;
    private DialogPlus exit_dialog;

    public static Intent getIntent(Context context, String content) {
        Intent intent = new Intent();
        intent.setClass(context, SaveAudioActivity.class);
        intent.putExtra(Content, content);
        return intent;
    }

    private String getFileName(){
        String fileName = "";
        if ("".equals(etFileName.getText().toString())){
            if (stringContent != null) {
                if (stringContent.length() > 5) {
                    fileName = stringContent.substring(0, 5);
                } else {
                    fileName = stringContent;
                }
            }
        }else {
            fileName = etFileName.getText().toString().trim();
        }
        if ("".equals(fileName)){
            return System.currentTimeMillis()+"";
        }
        return fileName;
    }
    @OnClick(R.id.bt_save_share)
    public void share() {
        final String fileName = getFileName();
        MobclickAgent.onEvent(this,MobclickEvent.VoiceShareSave);
        Disposable subscribe = rxPermissions.request(permission)
                .compose(RxUtils.applySchedulers(this))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        boolean copyFile = FileUtils.copyFile(new File(FileUtils.CacheMp3Url), FileUtils.ExportMp3Url, fileName + ".mp3");
                        if (copyFile) {
                            ArmsUtils.makeText(getApplication(), String.format("文件保存到%s目录下", FileUtils.ExportMp3Url));
                        }
                        boolean saveHisotry = FileUtils.copyFile(new File(FileUtils.CacheMp3Url), FileUtils.AudioHistoryMp3Url, fileName + ".mp3");
                        //不仅保存到 sdcard 还要保存到本地cache中
                        if (saveHisotry) {
                            AudioEntity audioEntity = new AudioEntity();
                            if (isSaved) {
                                audioEntity.setPid(id);
                            }
                            audioEntity.setAudio_url(FileUtils.AudioHistoryMp3Url +fileName + ".mp3");
                            audioEntity.setAudio_duration(mPlayerDuration);
                            audioEntity.setAudio_content(stringContent);
                            audioEntity.setAudio_title(fileName);
                            if (mPresenter != null) {
                                mPresenter.saveAudioAndShare(audioEntity);
                            }
                            isSaved = true;

                        }
                    } else {
                        ArmsUtils.makeText(getApplication(), "需要读写sd卡权限");
                    }
                });

    }

    @OnClick(R.id.bt_save)
    public void save() {

        final  String fileName = getFileName();
        MobclickAgent.onEvent(this,MobclickEvent.VoiceSave);
        Disposable subscribe = rxPermissions.request(permission)
                .compose(RxUtils.applySchedulers(this))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        //不仅保存到 sdcard 还要保存到本地cache中
                        boolean copyFile = FileUtils.copyFile(new File(FileUtils.CacheMp3Url), FileUtils.ExportMp3Url, fileName+ ".mp3");
                        boolean saveHisotry = FileUtils.copyFile(new File(FileUtils.CacheMp3Url), FileUtils.AudioHistoryMp3Url, fileName+ ".mp3");
                        if (copyFile) {
                            ArmsUtils.makeText(getApplication(), String.format("文件保存到%s目录下", FileUtils.ExportMp3Url));
                        }
                        if (saveHisotry) {
                            AudioEntity audioEntity = new AudioEntity();
                            if (isSaved) {
                                audioEntity.setPid(id);
                            }
                            audioEntity.setAudio_url(FileUtils.AudioHistoryMp3Url +fileName + ".mp3");
                            audioEntity.setAudio_duration(mPlayerDuration);
                            audioEntity.setAudio_content(stringContent);
                            audioEntity.setAudio_title(fileName);
                            if (mPresenter != null) {
                                mPresenter.saveAudio(audioEntity);
                            }
                            isSaved = true;
                        }
                    } else {
                        ArmsUtils.makeText(getApplication(), "需要读写sd卡权限");
                    }
                });
    }


    private ProgressDialog mProgressDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSaveAudioComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_save_audio; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        stringContent = getIntent().getStringExtra(Content);


        etFileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(SaveAudioActivity.this,MobclickEvent.SaveVoiceTitle);
            }
        });
        coverPcmToMp3();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


    private void coverPcmToMp3() {
        String pcmUrl = FileUtils.PcmUrl;
        String outputPath = FileUtils.CacheMp3Url;
        String cmd = String.format("ffmpeg -y -f s16be -ac 1 -ar 16000 -acodec pcm_s16le -i %s %s", pcmUrl, outputPath);
        String[] commands = cmd.split(" ");
        openProgressDialog();
        RxFFmpegInvoke.getInstance()
                .runCommandRxJava(commands)
                .compose(RxUtils.applyFlowSchedulers(this))
                .subscribe(new RxFFmpegSubscriber() {
                    @Override
                    public void onFinish() {
                        if (mProgressDialog != null)
                            mProgressDialog.cancel();
                        initPlayer();
                    }

                    @Override
                    public void onProgress(int progress, long progressTime) {
                        if (mProgressDialog != null) {
                            mProgressDialog.setProgress(progress);
                            mProgressDialog.setMessage("已处理progressTime=" + (double) progressTime / 1000000 + "秒");
                        }
                    }

//                    @Override
//                    public void onProgress(int progress) {
//                        if (mProgressDialog != null) {
//                            mProgressDialog.setProgress(progress);
//                            mProgressDialog.setMessage("已处理progressTime=" + (double) progressTime / 1000000 + "秒");
//                        }
//                    }

                    @Override
                    public void onCancel() {
                        if (mProgressDialog != null)
                            mProgressDialog.cancel();
                    }

                    @Override
                    public void onError(String message) {
                        if (mProgressDialog != null)
                            mProgressDialog.cancel();
                    }
                });
    }

    public void openProgressDialog() {
        mProgressDialog = openProgressDialog(this);
    }

    public static ProgressDialog openProgressDialog(Context context) {
        ProgressDialog mProgressDialog = new ProgressDialog(context);
        final int totalProgressTime = 100;
        mProgressDialog.setMessage("正在处理音频，请稍后...");
        mProgressDialog.setButton("取消", (dialog, which) -> {
            //中断 ffmpeg
            RxFFmpegInvoke.getInstance().exit();
        });
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setProgressNumberFormat("");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMax(totalProgressTime);
        mProgressDialog.show();
        return mProgressDialog;
    }

    @SuppressLint("DefaultLocale")
    private void initPlayer() {
        mPlayer = new MediaPlayer();
        pbPlay.setOnSeekBarChangeListener(this);

        File file = new File(FileUtils.CacheMp3Url);
        try {
            mPlayer.setDataSource(file.getPath());
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPlayer.setOnCompletionListener(mp -> {
            //播放完reset
            isPlaying = false;
            ivControl.setImageResource(R.drawable.ic_play_red_24dp);
            tvStart.setText("00:00");
            pbPlay.setProgress(0);
        });

        mPlayerDuration = mPlayer.getDuration();
        int totalTime = Math.round(mPlayer.getDuration() / 1000);
        String str = String.format("%02d:%02d", totalTime / 60,
                totalTime % 60);
        tvEnd.setText(str);
        pbPlay.setMax(mPlayer.getDuration());
    }


    private boolean isPlaying = false;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x01:
                    break;
                default:
                    break;
            }
        }
    };
    @SuppressLint("DefaultLocale")
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!hadDestroy) {
                if (mPlayer!=null && pbPlay!=null&&tvStart!=null){
                    mHandler.postDelayed(this, 1000);
                    if (mPlayer.isPlaying()){
                        int currentTime = Math
                                .round(mPlayer.getCurrentPosition() / 1000);
                        String currentStr = String.format("%02d:%02d", currentTime / 60, currentTime % 60);
                        tvStart.setText(currentStr);

                        pbPlay.setProgress(mPlayer.getCurrentPosition());
                    }

                }

            }
        }
    };

    @SuppressLint("DefaultLocale")
    @OnClick(R.id.iv_control)
    public void play() {
        if (!isPlaying) {
            //设置播放中按钮
            isPlaying = true;
            ivControl.setImageResource(R.drawable.ic_pause_red_24dp);


            MobclickAgent.onEvent(this,MobclickEvent.SaveVoicePlay);
            if (!mPlayer.isPlaying()) {
                mPlayer.start();
                mHandler.postDelayed(runnable, 1000);
            }
        } else {
            isPlaying = false;
            ivControl.setImageResource(R.drawable.ic_play_red_24dp);
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
            }
        }
    }


    private boolean isSaved = false;

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (isSaved) {
            ArmsUtils.killAll();
            Intent intent = new Intent(this, HomeActivity.class);
            launchActivity(intent);
        }else {
            if (exit_dialog == null){
                exit_dialog = DialogPlus.newDialog(this)
                        .setContentHolder(new ViewHolder(R.layout.dialog_back))
                        .setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setGravity(Gravity.CENTER)
                        .create();

                exit_dialog.findViewById(R.id.bt_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArmsUtils.killAll();
                        Intent intent = new Intent(SaveAudioActivity.this, HomeActivity.class);
                        launchActivity(intent);
                    }
                });

                exit_dialog.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exit_dialog.dismiss();
                    }
                });
            }
            exit_dialog.show();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

        MobclickAgent.onEvent(this, MobclickEvent.SaveVoiceView);

        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser){
            if (mPlayer != null) {
                mPlayer.seekTo(seekBar.getProgress());
            }
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReleasePlayer();
        if (mPlayer != null) {
            hadDestroy = true;
        }
        RxFFmpegInvoke.getInstance().setFFmpegListener(null) ;
    }

    private void ReleasePlayer() {
        if (mPlayer != null) {
            mPlayer.stop();
            //关键语句
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void saveSuccess(Long aLong) {
        id = aLong;
    }

    @Override
    public void saveAndrShare(Long along) {
        id = along;
        String sharePath = FileUtils.ExportMp3Url +getFileName()+ ".mp3";
        Uri uri = Uri.parse(sharePath);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("audio/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Sound File"));
    }
}
