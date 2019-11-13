package com.vavapps.sound.app;

import android.content.Context;

import com.iflytek.cloud.SpeechUtility;
import com.jess.arms.base.BaseApplication;
import com.leon.channel.helper.ChannelReaderUtil;
import com.vavapps.sound.R;
import com.vavapps.sound.mvp.model.database.AppDatabase;
import com.umeng.commonsdk.UMConfigure;

import tech.oom.idealrecorder.IdealRecorder;

public class SoundApp extends BaseApplication {

    private static Context mContext;
    public static String umeng_channel;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        // 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
        // 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
        // 参数间使用半角“,”分隔。
        // 设置你申请的应用appid,请勿在'='与appid之间添加空格及空转义符

        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误
        SpeechUtility.createUtility(SoundApp.this, "appid=" + getString(R.string.app_id));
        // 以下语句用于设置日志开关（默认开启），设置成false时关闭语音云SDK日志打印
        // Setting.setShowLog(false);
        super.onCreate();
        mContext = this;
        umeng_channel = ChannelReaderUtil.getChannel(getApplicationContext());
        UMConfigure.init(mContext, "5db801693fc195fca200002e", umeng_channel, UMConfigure.DEVICE_TYPE_PHONE, null);
        IdealRecorder.init(this);

//        try {
//            ApplicationInfo activityInfo = getPackageManager().getApplicationInfo(getPackageName(),
//                    PackageManager.GET_META_DATA);
//            String umeng_channel = activityInfo.metaData.getString("UMENG_CHANNEL");
//            Timber.d("channel=%s", umeng_channel);
//
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
    }


    public static AppDatabase getDatabase() {
        return AppDatabase.getInstance(mContext);
    }
}
