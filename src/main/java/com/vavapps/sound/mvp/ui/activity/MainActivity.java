package com.vavapps.sound.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.iflytek.cloud.*;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DataHelper;
import com.vavapps.sound.R;
import com.vavapps.sound.app.utils.*;
import com.vavapps.sound.di.component.DaggerMainComponent;
import com.vavapps.sound.mvp.contract.MainContract;
import com.vavapps.sound.mvp.presenter.MainPresenter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.umeng.analytics.MobclickAgent;
import timber.log.Timber;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.vavapps.sound.app.utils.FileUtils.PcmUrl;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/03/2019 17:37
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View,
        RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.toolbar_back)
    RelativeLayout toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.rl_complete)
    RelativeLayout rlComplete;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.bt_man)
    Button btMan;
    @BindView(R.id.bt_speed)
    Button btSpeed;
    @BindView(R.id.bt_voice)
    Button btVoice;
    @BindView(R.id.rl_nav)
    RelativeLayout rlNav;
    @BindView(R.id.iv_control)
    ImageView ivControl;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.pb_play)
    SeekBar pbPlay;
    @BindView(R.id.tv_end)
    TextView tvEnd;

    // 默认发音人
    private String voicer = "xiaoyan";


    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private DialogPlus speech_dialog;
    private DialogPlus voice_dialog;

    private boolean saveTag = false;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }




    private enum PlayStatus{

        //初始状态
        None,
        //播放中
        Playing,
        //暂停中
        Pause,
        //继续播放,
        Resume,


    }

    private PlayStatus playStatus = PlayStatus.None;


    @OnClick(R.id.iv_control)
    public void play() {

        switch (playStatus){
            case None:
                PlayNone();
                break;
            case Playing:
                //播放中，按下按钮，停止播放
                if (mTts!=null){
                    mTts.pauseSpeaking();
                    //播放
                        ivControl.setImageResource(R.drawable.ic_play_red_24dp);
                }
                break;
            case Pause:
                if (mTts!=null){
                    mTts.resumeSpeaking();
                    //播放
                    ivControl.setImageResource(R.drawable.ic_pause_red_24dp);
                }
                break;
            case Resume:
                if (mTts!=null){
                    mTts.pauseSpeaking();
                    //播放
                    ivControl.setImageResource(R.drawable.ic_play_red_24dp);
                }
                break;
        }


    }


    //初始播放状态
    private void PlayNone(){
        String texts = etContent.getText().toString();
        if (texts.length() ==0){
            ArmsUtils.makeText(getApplicationContext(),"请先输入内容");
            return;
        }
        //播放
        ivControl.setImageResource(R.drawable.ic_pause_red_24dp);
        // 设置参数
        setParam(null);
        int code = mTts.startSpeaking(texts, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            showMessage("语音合成失败,错误码: " + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
        }

    }

    @SuppressLint("SetTextI18n")
    private void resetPlay() {
        ivControl.setImageResource(R.drawable.ic_play_red_24dp);
        pbPlay.setProgress(0);
        tvStart.setText("00:00");
    }

    @SuppressLint("SetTextI18n")
    private void resetPlayNeedUpdate() {
        playStatus = PlayStatus.None;
        ivControl.setImageResource(R.drawable.ic_play_red_24dp);
        pbPlay.setProgress(0);
        tvStart.setText("00:00");
    }

    @OnClick(R.id.rl_complete)
    public void complete() {



        MobclickAgent.onEvent(this,MobclickEvent.MakeVoiceSave);
        saveTag = true;
        String texts = etContent.getText().toString();
        if (texts.length() == 0) {
            ArmsUtils.makeText(getApplicationContext(), "请先输入内容");
            return;
        }
        // 设置参数
        setParam(null);
        int code = mTts.synthesizeToUri(texts, PcmUrl, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            showMessage("语音合成失败,错误码: " + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
        }
    }

    @OnClick(R.id.bt_man)
    public void showMan() {

        MobclickAgent.onEvent(this,MobclickEvent.MakeVoiceAnnouncer);
        if (mTts.isSpeaking()){
            mTts.stopSpeaking();
        }

        Intent intent = new Intent();
        intent.setClass(this, ChoosePlayerActivity.class);
        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.bt_speed)
    public void showSpeed() {
        MobclickAgent.onEvent(this,MobclickEvent.MakeVoiceSpeed);
        if (speech_dialog != null) {
            setupDialog();
            speech_dialog.show();
        }
    }

    @OnClick(R.id.bt_voice)
    public void showVoice() {
        MobclickAgent.onEvent(this,MobclickEvent.MakeVoiceVolume);
        if (voice_dialog != null) {
            setupDialog();
            voice_dialog.show();
        }
    }

    /**
     * 参数设置
     *
     * @return
     */
    private void setParam(String speed) {


        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //支持实时音频返回，仅在synthesizeToUri条件下支持
            //mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");

            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            if (speed == null) {
                mTts.setParameter(SpeechConstant.SPEED, DataHelper.getStringSF(this, "speed_preference"));
            } else {
                mTts.setParameter(SpeechConstant.SPEED, speed);
            }


            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, DataHelper.getStringSF(this, "pitch_preference"));
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, DataHelper.getStringSF(this, "volume_preference"));

            mTts.setParameter("ttp", "cssml");
            mTts.setParameter("tte", "utf8");
//			mTts.setParameter(SpeechConstant.TEXT_ENCODING, "GB2312");
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");

        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, DataHelper.getStringSF(this, "stream_preference"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, PcmUrl);
    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Timber.d("InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showMessage("初始化失败,错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        private long pcm_length = 0;

        @Override
        public void onSpeakBegin() {
            //开始播放
            playStatus = PlayStatus.Playing;
            showMessage("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            //暂停播放
            playStatus = PlayStatus.Pause;
            showMessage("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            //恢复播放
            playStatus = PlayStatus.Resume;
            showMessage("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;
            showMessage(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering));
            if (saveTag && percent == 100) {
                launchActivity(SaveAudioActivity.getIntent(MainActivity.this, etContent.getText().toString().trim()));
            }
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
//             播放进度
            mPercentForPlaying = percent;
            long fileSize = FileUtils.getFileSize(PcmUrl);
            pcm_length = fileSize / 32;
            String timeend = TimeUtils.timeParse(pcm_length);
            tvEnd.setText(timeend);
            long current_progress = pcm_length * percent / 100;
            String timestart = TimeUtils.timeParse(current_progress);
            tvStart.setText(timestart);
            pbPlay.setProgress(mPercentForPlaying);
//            SpannableStringBuilder style=new SpannableStringBuilder(texts);
//            Timber.e("beginPos = " + beginPos + "  endPos = " + endPos);
//            style.setSpan(new BackgroundColorSpan(Color.RED),beginPos,endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ((EditText) findViewById(R.id.tts_text)).setText(style);
        }

        @Override
        public void onCompleted(SpeechError error) {
            playStatus = PlayStatus.None;
            //播放完成 播放状态重置
            if (error == null) {
                showMessage("播放完成");
                resetPlay();
            } else {

                showMessage(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initParams();
        setupView();
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(MainActivity.this, mTtsInitListener);
    }

    private void initParams() {
        //设置默认发音人
        voicer = DataHelper.getStringSF(this, Constans.VoicerPreference);
        if (voicer == null) {
            voicer = "xiaoyan";
            DataHelper.setStringSF(this, Constans.VoicerPreference, "xiaoyan");
        }
        String speed_preference = DataHelper.getStringSF(this, Constans.SpeedPreference);
        if (speed_preference == null) {
            DataHelper.setStringSF(this, Constans.SpeedPreference, "50");
        }
        String pitch_preference = DataHelper.getStringSF(this, "pitch_preference");
        if (pitch_preference == null) {
            DataHelper.setStringSF(this, "pitch_preference", "50");
        }
        String volume_preference = DataHelper.getStringSF(this, "volume_preference");
        if (volume_preference == null) {
            DataHelper.setStringSF(this, "volume_preference", "50");
        }
    }


    //临时选择的声音
    private String tempSpeech = "50";
    private int tempProgress = 50;

    @SuppressLint("SetTextI18n")
    private void setupView() {
        ShadowOutlineUtil.addDefalutShadowOutLine(this, rlNav);
        rlComplete.setVisibility(View.VISIBLE);


        pbPlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        setupDialog();

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                resetPlayNeedUpdate();
            }
        });
    }

    private void setupDialog() {

        if (speech_dialog == null) {
            speech_dialog = DialogPlus.newDialog(this)
                    .setContentHolder(new ViewHolder(R.layout.dialog_choose_speech))
                    .setGravity(Gravity.BOTTOM)
                    .create();
        }


        ((TextView) speech_dialog.findViewById(R.id.tv_dialog_title)).setText("选择语速");
        ((RadioGroup) speech_dialog.findViewById(R.id.rg_speech)).setOnCheckedChangeListener(null);

        switch (DataHelper.getStringSF(this, Constans.SpeedPreference)) {

            case "25":
                RadioButton radioButton25 = (RadioButton) speech_dialog.findViewById(R.id.speech_25);
                radioButton25.setChecked(true);
                break;
            case "50":
                RadioButton radioButton50 = (RadioButton) speech_dialog.findViewById(R.id.speech_50);
                radioButton50.setChecked(true);
                break;
            case "75":
                RadioButton radioButton75 = (RadioButton) speech_dialog.findViewById(R.id.speech_75);
                radioButton75.setChecked(true);
                break;
            case "100":
                RadioButton radioButton100 = (RadioButton) speech_dialog.findViewById(R.id.speech_100);
                radioButton100.setChecked(true);
                break;
        }
        ((RadioGroup) speech_dialog.findViewById(R.id.rg_speech)).setOnCheckedChangeListener(this);


        if (voice_dialog == null) {
            voice_dialog = DialogPlus.newDialog(this)
                    .setContentHolder(new ViewHolder(R.layout.dialog_choose_voice))
                    .setGravity(Gravity.BOTTOM)
                    .create();
        }


        ((TextView) voice_dialog.findViewById(R.id.tv_dialog_title)).setText("选择音量");
        TextView tv_progress = (TextView) voice_dialog.findViewById(R.id.tv_progress);
        tv_progress.setText(String.format("%s%%", DataHelper.getStringSF(this, "volume_preference")));

        SeekBar seekBar = (SeekBar) voice_dialog.findViewById(R.id.pb_play);
        seekBar.setProgress(Integer.valueOf(DataHelper.getStringSF(this, "volume_preference")));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tempProgress = progress;
                tv_progress.setText(tempProgress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        voice_dialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(MainActivity.this,MobclickEvent.MakeVoiceVolumeCancel);
                voice_dialog.dismiss();
            }
        });

        voice_dialog.findViewById(R.id.iv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(MainActivity.this,MobclickEvent.MakeVoiceVolumeSure);
                if (tempProgress != Integer.valueOf(DataHelper.getStringSF(MainActivity.this, "volume_preference"))) {
                    DataHelper.setStringSF(MainActivity.this, "volume_preference", String.valueOf(tempProgress));
                    resetPlayNeedUpdate();
                }
                voice_dialog.dismiss();
            }
        });

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String man = DataHelper.getStringSF(this, Constans.VoicerPreference);
        voicer = man;
        resetPlayNeedUpdate();
        ivControl.performClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onEvent(this, MobclickEvent.MakeVoiceView);

        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(MainActivity.this,MobclickEvent.MakeVoiceBack);
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.speech_25:
                tempSpeech = "25";
                break;
            case R.id.speech_50:
                tempSpeech = "50";
                break;
            case R.id.speech_75:
                tempSpeech = "75";
                break;
            case R.id.speech_100:
                tempSpeech = "100";
                break;
            default:
                break;
        }
        MobclickAgent.onEvent(this,MobclickEvent.MakeVoicePlay);
        String texts = etContent.getText().toString();

        MobclickAgent.onEvent(MainActivity.this,MobclickEvent.MakeVoiceSpeedSure);
        //如果设置变化了 需要保存配置，并且重新生成配音
        DataHelper.setStringSF(MainActivity.this, "speed_preference", tempSpeech);
        resetPlayNeedUpdate();
        if (texts.length() == 0){
            ArmsUtils.makeText(getApplicationContext(),"请先输入内容");
            return;
        }
        // 设置参数
        setParam(tempSpeech);
        int code = mTts.startSpeaking(texts, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            showMessage("语音合成失败,错误码: " + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
        }
    }
}
