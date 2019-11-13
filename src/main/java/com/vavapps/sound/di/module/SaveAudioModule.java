package com.vavapps.sound.di.module;

import dagger.Binds;
import dagger.Module;

import com.vavapps.sound.mvp.contract.SaveAudioContract;
import com.vavapps.sound.mvp.model.SaveAudioModel;


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
@Module
public abstract class SaveAudioModule {

    @Binds
    abstract SaveAudioContract.Model bindSaveAudioModel(SaveAudioModel model);
}