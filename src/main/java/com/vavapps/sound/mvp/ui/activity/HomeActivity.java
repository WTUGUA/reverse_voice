package com.vavapps.sound.mvp.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.ark.dict.ConfigMapLoader;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.nova.permissionutils.KvPermisson;
import com.nova.permissionutils.PermissionUtils;
import com.vavapps.sound.R;
import com.vavapps.sound.app.SoundApp;
import com.vavapps.sound.app.utils.FileUtils;
import com.vavapps.sound.app.utils.GetRealTimeUtils;
import com.vavapps.sound.app.utils.HeaderSpf;
import com.vavapps.sound.app.utils.MobclickEvent;
import com.vavapps.sound.app.utils.RxUtils;
import com.vavapps.sound.di.component.DaggerHomeComponent;
import com.vavapps.sound.mvp.contract.HomeContract;
import com.vavapps.sound.mvp.event.WeixinPayEvent;
import com.vavapps.sound.mvp.model.entity.AudioEntity;
import com.vavapps.sound.mvp.model.view.YsDialog;
import com.vavapps.sound.mvp.presenter.HomePresenter;
import com.vavapps.sound.mvp.ui.adapter.AudioHistoryAdapter;
import com.vavapps.sound.mvp.ui.widget.SpacesItemDecoration;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/05/2019 18:52
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class HomeActivity extends BaseActivity<HomePresenter> implements HomeContract.View {

    @BindView(R.id.content)
    RecyclerView ryContent;
    @BindView(R.id.rl_make_audio)
    ImageView rlMakeAudio;
    @BindView(R.id.tv_under)
    TextView tvUnder;
    @BindView(R.id.test)
    ImageView test;
    private AudioHistoryAdapter adapter;
    private List<AudioEntity> datas;
    private MediaPlayer mPlayer;
    private  ImageView setting;
    final RxPermissions rxPermissions = new RxPermissions(this);
//    String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
//    private final int mRequestCode = 100;//权限请求码
    String[] permission = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CHANGE_WIFI_STATE
    };
    //2、创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
    List<String> mPermissionList = new ArrayList<>();




    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @OnClick(R.id.rl_make_audio)
    public void goMakeAudio() {
        MobclickAgent.onEvent(this, MobclickEvent.HomePageCreate);
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        launchActivity(intent);
    }
    @OnClick(R.id.test)
    public void goRevsrse(){
        MobclickAgent.onEvent(HomeActivity.this, MobclickEvent.AudioPlaysBack);
        Intent intent=new Intent();
        intent.setClass(this, ReverseAudioActivity.class);
        startActivity(intent);
    }
    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_home; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setupViews();
    }

    private void initPayer() {
        mPlayer = new MediaPlayer();

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                for (int i = 0; i < datas.size(); i++) {
                    datas.get(i).setPlaying(false);
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void setupViews() {

        tvUnder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ArmsUtils.makeText(getApplicationContext(), SoundApp.umeng_channel);
                return true;
            }
        });



        datas = new ArrayList<>();
        adapter = new AudioHistoryAdapter(datas);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MobclickAgent.onEvent(HomeActivity.this, MobclickEvent.HomePageList);
            }
        });

        adapter.addAudioPlayCallback(new AudioHistoryAdapter.AudioPlayCallback() {
            @Override
            public void play(int position) {
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).isPlaying()) {
                        datas.get(i).setPlaying(false);
                    }
                }
                datas.get(position).setPlaying(true);
                adapter.notifyDataSetChanged();
                MobclickAgent.onEvent(HomeActivity.this, MobclickEvent.HomePagePlay);
                try {
                    if (mPlayer.isPlaying()) {
                        mPlayer.stop();
                    }
                    mPlayer.reset();
                    mPlayer.setDataSource(datas.get(position).getAudio_url());
                    mPlayer.prepare();
                    mPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void share(int position) {
                MobclickAgent.onEvent(HomeActivity.this, MobclickEvent.HomePageVoiceShare);
                Disposable subscribe = rxPermissions.request(permission)
                        .compose(RxUtils.applySchedulers(HomeActivity.this))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                //cacheUrl;
                                String audio_url = datas.get(position).getAudio_url();
                                File srcFile = new File(audio_url);
                                String name = srcFile.getName();
                                boolean copyFile = FileUtils.copyFile(srcFile, FileUtils.ExportMp3Url, name);
                                String sharePath = FileUtils.ExportMp3Url + name;
                                Uri uri = Uri.parse(sharePath);
                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("audio/*");
                                share.putExtra(Intent.EXTRA_STREAM, uri);
                                startActivity(Intent.createChooser(share, "Share Sound File"));
                            } else {
                                ArmsUtils.makeText(getApplication(), "需要读写sd卡权限");
                            }
                        });
            }
        });

        ArmsUtils.configRecyclerView(ryContent, new LinearLayoutManager(this));
        ryContent.addItemDecoration(new SpacesItemDecoration(ArmsUtils.dip2px(this, 15)));
        ryContent.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onEvent(this, MobclickEvent.HomePageView);
        initPayer();
        if (mPresenter != null) {
            mPresenter.getAudioHistory();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReleasePlayer();
    }

    private void ReleasePlayer() {
        if (mPlayer != null) {
            mPlayer.setOnCompletionListener(null);
            mPlayer.stop();
            //关键语句
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
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
    public void onBackPressed() {
//        super.onBackPressed();
        killMyself();
    }

    @Override
    public void showAudios(List<AudioEntity> audioHistories) {
        if (audioHistories != null && audioHistories.size() > 0) {
            datas.clear();
            Collections.reverse(audioHistories);
            datas.addAll(audioHistories);
            adapter.notifyDataSetChanged();
        } else {
            View noOrders = LayoutInflater.from(this).inflate(R.layout.item_audio_empty,
                    null, false);
            adapter.setEmptyView(noOrders);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        PermissionUtils.getInstance(HomeActivity.this, new KvPermisson() {
            @Override
            public void allGranted(boolean isAllGranted) {

            }

            @Override
            public void disGranted(String... disGrantedPermissions) {

            }
        }).showCheckDialog(permission);

//        initPermission();
        int xieyi=getxieyi("showtime");
        int yingsi=getyingsi();
        int code=getcode("code");
        if(xieyi==0) {
            showDialog();
        }
        if(code<yingsi){
            setcode("code",yingsi);
            showDialog();
            Toast.makeText(HomeActivity.this,"隐私政策已更新",Toast.LENGTH_SHORT).show();
        }
        setting=findViewById(R.id.btn_settings);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

    }
    //权限判断和申请
//    private void initPermission() {
//
//        mPermissionList.clear();//清空没有通过的权限
//
//        //逐个判断你要的权限是否已经通过
//        for (int i = 0; i < permissions.length; i++) {
//            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
//                mPermissionList.add(permissions[i]);//添加还未授予的权限
//            }
//        }
//
//        //申请权限
//        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
//            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
//        } else {
//            //说明权限都已经通过，可以做你想做的事情去
//        }
//    }
    public void showDialog() {
        //实例化自定义对话框
        final YsDialog mdialog = new YsDialog(this);
        mdialog.setCanceledOnTouchOutside(false);
        mdialog.setCancelable(false);
        //对话框中确认按钮事件
        mdialog.setOnAgreeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果对话框处于显示状态
                if (mdialog.isShowing()) {
                    //关闭对话框
                    setxieyi("showtime",1);
                    mdialog.dismiss();
                }
            }
        });
        //对话框中取消按钮事件
        mdialog.setOnUnagreeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mdialog != null && mdialog.isShowing()) {
                    setxieyi("showtime",0);
                    mdialog.dismiss();
                    finish();
                }
            }
        });
        mdialog.show();
    }
    public int getxieyi(String key) {
        SharedPreferences sp = getSharedPreferences("showtime", Context.MODE_PRIVATE);
        int str = sp.getInt(key, 0);
        return str;
    }

    public void setxieyi(String key, int values) {
        SharedPreferences sp = getSharedPreferences("showtime", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putInt(key, values);
        et.commit();
    }
    public static int getyingsi() {
        String ad_openwindow_set = ConfigMapLoader.getInstance().getResponseMap().get("private_policy_update_version");
        if (ad_openwindow_set == null) {
            return 10;
        }
        return Integer.parseInt(ad_openwindow_set);
    }
    public int getcode(String key) {
        SharedPreferences sp = getSharedPreferences("code", Context.MODE_PRIVATE);
        int str = sp.getInt(key, 10);
        return str;
    }

    public void setcode(String key, int values) {
        SharedPreferences sp = getSharedPreferences("code", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putInt(key, values);
        et.commit();
    }
}
