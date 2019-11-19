package com.vavapps.sound.mvp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.vavapps.sound.R;
import com.vavapps.sound.app.SoundApp;
import com.vavapps.sound.app.utils.FileUtils;
import com.vavapps.sound.app.utils.MobclickEvent;
import com.vavapps.sound.app.utils.RxUtils;
import com.vavapps.sound.di.component.DaggerHomeComponent;
import com.vavapps.sound.mvp.contract.HomeContract;
import com.vavapps.sound.mvp.model.entity.AudioEntity;
import com.vavapps.sound.mvp.presenter.HomePresenter;
import com.vavapps.sound.mvp.ui.adapter.AudioHistoryAdapter;
import com.vavapps.sound.mvp.ui.widget.SpacesItemDecoration;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    final RxPermissions rxPermissions = new RxPermissions(this);
    String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};




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

    }
}
