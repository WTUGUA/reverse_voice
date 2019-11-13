package com.vavapps.sound.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;
import com.vavapps.sound.app.utils.RxUtils;
import com.vavapps.sound.mvp.model.entity.AudioEntity;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.observers.DefaultObserver;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.vavapps.sound.mvp.contract.SaveAudioContract;


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
public class SaveAudioPresenter extends BasePresenter<SaveAudioContract.Model, SaveAudioContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public SaveAudioPresenter(SaveAudioContract.Model model, SaveAudioContract.View rootView) {
        super(model, rootView);
    }


    public void saveAudio(AudioEntity audioEntity){
        Observable.create((ObservableOnSubscribe<Long>) emitter -> {
            Long aLong =   mModel.saveAudio(audioEntity);
            emitter.onNext(aLong);
        }).compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new DefaultObserver<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        mRootView.saveSuccess(aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.saveSuccess(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public void saveAudioAndShare(AudioEntity audioEntity){
        Observable.create((ObservableOnSubscribe<Long>) emitter -> {
            Long aLong =   mModel.saveAudioAndShare(audioEntity);
            emitter.onNext(aLong);
        }).compose(RxUtils.applySchedulers(mRootView))
                .subscribe(new DefaultObserver<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        mRootView.saveAndrShare(aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.saveAndrShare(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}
