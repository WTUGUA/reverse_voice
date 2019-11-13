package com.vavapps.sound.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.vavapps.sound.di.module.SaveAudioModule;
import com.vavapps.sound.mvp.contract.SaveAudioContract;

import com.jess.arms.di.scope.ActivityScope;
import com.vavapps.sound.mvp.ui.activity.SaveAudioActivity;


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
@Component(modules = SaveAudioModule.class, dependencies = AppComponent.class)
public interface SaveAudioComponent {
    void inject(SaveAudioActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SaveAudioComponent.Builder view(SaveAudioContract.View view);

        SaveAudioComponent.Builder appComponent(AppComponent appComponent);

        SaveAudioComponent build();
    }
}