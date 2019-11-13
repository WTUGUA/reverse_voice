package com.vavapps.sound.mvp.model;

import android.app.Application;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.vavapps.sound.app.SoundApp;
import com.vavapps.sound.mvp.contract.SaveAudioContract;
import com.vavapps.sound.mvp.model.entity.AudioEntity;


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
@ActivityScope
public class SaveAudioModel extends BaseModel implements SaveAudioContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public SaveAudioModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Long saveAudio(AudioEntity audioEntity) {
        return SoundApp.getDatabase().audioHistoryDao().insert(audioEntity);
    }

    @Override
    public Long saveAudioAndShare(AudioEntity audioEntity) {
        return SoundApp.getDatabase().audioHistoryDao().insert(audioEntity);
    }
}