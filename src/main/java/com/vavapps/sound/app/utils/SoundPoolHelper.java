package com.vavapps.sound.app.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import com.vavapps.sound.R;

import java.util.HashMap;

public class SoundPoolHelper {


    public static SoundPoolHelper soundPoolHelper = null;

    public static HashMap<Integer, Integer> sounddata = new HashMap<>();
    private  SoundPool sp;

    private Context mContext;

    private SoundPoolHelper(Context context) {
        mContext = context;
        SoundPool.Builder spb = new SoundPool.Builder();
        spb.setMaxStreams(1);
        //创建SoundPool对象
        sp = spb.build();
        int xiaoyan = sp.load(context, R.raw.xiaoyan, 1);
        int xiaoyu = sp.load(context, R.raw.xiaoyu, 1);
        int vixy = sp.load(context, R.raw.vixy, 1);
        int xiaoqi = sp.load(context, R.raw.xiaoqi, 1);
        int vixf = sp.load(context, R.raw.vixf, 1);
        int xiaomei = sp.load(context, R.raw.xiaomei, 1);


        int xiaoxin = sp.load(context, R.raw.xiaoxin, 1);
        int nannan = sp.load(context, R.raw.nannan, 1);
        int vils = sp.load(context, R.raw.vils, 1);

        int xiaolin = sp.load(context, R.raw.xiaolin, 1);
        int xiaorong = sp.load(context, R.raw.xiaorong, 1);
        int xiaoqian = sp.load(context, R.raw.xiaoqian, 1);
        int xiaokun = sp.load(context, R.raw.xiaokun, 1);
        int xiaoqiang = sp.load(context, R.raw.xiaoqiang, 1);
        int vixying = sp.load(context, R.raw.vixying, 1);

        int aisjiuxu = sp.load(context, R.raw.aisjiuxu, 1);
        int aisxping = sp.load(context, R.raw.aisxping, 1);
        int aisjinger = sp.load(context, R.raw.aisjinger, 1);
        int aisbabyxu = sp.load(context, R.raw.aisbabyxu, 1);


        sounddata.put(1, xiaoyan);
        sounddata.put(2, xiaoyu);
        sounddata.put(3, vixy);
        sounddata.put(4, xiaoqi);
        sounddata.put(5, vixf);
        sounddata.put(6, xiaomei);

        sounddata.put(7, xiaoxin);
        sounddata.put(8, nannan);
        sounddata.put(9, vils);


        sounddata.put(10, xiaolin);
        sounddata.put(11, xiaorong);
        sounddata.put(12, xiaoqian);
        sounddata.put(13, xiaokun);
        sounddata.put(14, xiaoqiang);
        sounddata.put(15, vixying);

        sounddata.put(16, aisjiuxu);
        sounddata.put(17, aisxping);
        sounddata.put(18, aisjinger);
        sounddata.put(19, aisbabyxu);


    }


    public static SoundPoolHelper getInstance(Context context) {
        if (soundPoolHelper == null) {
            synchronized (SoundPoolHelper.class) {
                if (soundPoolHelper == null) {
                    soundPoolHelper = new SoundPoolHelper(context);
                }
            }

        }
        return soundPoolHelper;
    }

    public synchronized void release() {
        if (sp !=null){
            if (currentPos!=-1){
                sp.stop(currentPos);
            }
            sp.release();
            sp=null;
            soundPoolHelper = null;
            mContext = null;
        }


    }

    private int currentPos = -1;

    public synchronized void playSount(String name)  {
        if (sounddata == null) {
            return;
        }
        AudioManager am = (AudioManager) mContext

                .getSystemService(Context.AUDIO_SERVICE);
        float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volumnRatio = 1;
        switch (name) {
            case "xiaoyan":
                currentPos = sp.play(sounddata.get(1),
                        volumnRatio,// 左声道音量
                        volumnRatio,// 右声道音量
                        1, // 优先级
                        0,// 循环播放次数
                        1);// 回放速度，该值在0.5-2.0之间 1为正常速度
                break;

            case "xiaoyu":
                currentPos = sp.play(sounddata.get(2),
                        volumnRatio,// 左声道音量
                        volumnRatio,// 右声道音量
                        1, // 优先级
                        0,// 循环播放次数
                        1);// 回放速度，该值在0.5-2.0之间 1为正常速度
                break;
            case "vixy":
                currentPos = sp.play(sounddata.get(3),
                        volumnRatio,// 左声道音量
                        volumnRatio,// 右声道音量
                        1, // 优先级
                        0,// 循环播放次数
                        1);// 回放速度，该值在0.5-2.0之间 1为正常速度
                break;
            case "xiaoqi":
                currentPos = sp.play(sounddata.get(4),
                        volumnRatio,// 左声道音量
                        volumnRatio,// 右声道音量
                        1, // 优先级
                        0,// 循环播放次数
                        1);// 回放速度，该值在0.5-2.0之间 1为正常速度
                break;
            case "vixf":
                currentPos = sp.play(sounddata.get(5),
                        volumnRatio,// 左声道音量
                        volumnRatio,// 右声道音量
                        1, // 优先级
                        0,// 循环播放次数
                        1);// 回放速度，该值在0.5-2.0之间 1为正常速度
                break;
            case "xiaomei":
                currentPos = sp.play(sounddata.get(6),
                        volumnRatio,// 左声道音量
                        volumnRatio,// 右声道音量
                        1, // 优先级
                        0,// 循环播放次数
                        1);// 回放速度，该值在0.5-2.0之间 1为正常速度
                break;
            case "xiaoxin":
                currentPos = sp.play(sounddata.get(7),
                        volumnRatio,// 左声道音量
                        volumnRatio,// 右声道音量
                        1, // 优先级
                        0,// 循环播放次数
                        1);// 回放速度，该值在0.5-2.0之间 1为正常速度
                break;
            case "nannan":
                currentPos = sp.play(sounddata.get(8),
                        volumnRatio,// 左声道音量
                        volumnRatio,// 右声道音量
                        1, // 优先级
                        0,// 循环播放次数
                        1);// 回放速度，该值在0.5-2.0之间 1为正常速度
                break;
            case "vils":
                currentPos = sp.play(sounddata.get(9),
                        volumnRatio,// 左声道音量
                        volumnRatio,// 右声道音量
                        1, // 优先级
                        0,// 循环播放次数
                        1);// 回放速度，该值在0.5-2.0之间 1为正常速度
                break;
            case "xiaolin":
                currentPos = sp.play(sounddata.get(10),
                        volumnRatio,// 左声道音量
                        volumnRatio,// 右声道音量
                        1, // 优先级
                        0,// 循环播放次数
                        1);// 回放速度，该值在0.5-2.0之间 1为正常速度
                break;
            case "xiaorong":
                currentPos = sp.play(sounddata.get(11),
                        volumnRatio,// 左声道音量
                        volumnRatio,// 右声道音量
                        1, // 优先级
                        0,// 循环播放次数
                        1);// 回放速度，该值在0.5-2.0之间 1为正常速度
                break;
            case "xiaoqian":
                currentPos = sp.play(sounddata.get(12),
                        volumnRatio,// 左声道音量
                        volumnRatio,// 右声道音量
                        1, // 优先级
                        0,// 循环播放次数
                        1);// 回放速度，该值在0.5-2.0之间 1为正常速度
                break;
            case "xiaokun":
                currentPos = sp.play(sounddata.get(13),
                        volumnRatio,// 左声道音量
                        volumnRatio,// 右声道音量
                        1, // 优先级
                        0,// 循环播放次数
                        1);// 回放速度，该值在0.5-2.0之间 1为正常速度
                break;
            case "xiaoqiang":
                currentPos = sp.play(sounddata.get(14),
                        volumnRatio,// 左声道音量
                        volumnRatio,// 右声道音量
                        1, // 优先级
                        0,// 循环播放次数
                        1);// 回放速度，该值在0.5-2.0之间 1为正常速度
                break;
            case "vixying":
                currentPos = sp.play(sounddata.get(15),
                        volumnRatio,// 左声道音量
                        volumnRatio,// 右声道音量
                        1, // 优先级
                        0,// 循环播放次数
                        1);// 回放速度，该值在0.5-2.0之间 1为正常速度
                break;

            case "aisjiuxu":
                currentPos = sp.play(sounddata.get(16),
                        volumnRatio,// 左声道音量
                        volumnRatio,// 右声道音量
                        1, // 优先级
                        0,// 循环播放次数
                        1);// 回放速度，该值在0.5-2.0之间 1为正常速度
                break;
            case "aisxping":
                currentPos = sp.play(sounddata.get(17),
                        volumnRatio,// 左声道音量
                        volumnRatio,// 右声道音量
                        1, // 优先级
                        0,// 循环播放次数
                        1);// 回放速度，该值在0.5-2.0之间 1为正常速度

                break;
            case "aisjinger":
                currentPos = sp.play(sounddata.get(18),
                        volumnRatio,// 左声道音量
                        volumnRatio,// 右声道音量
                        1, // 优先级
                        0,// 循环播放次数
                        1);// 回放速度，该值在0.5-2.0之间 1为正常速度

                break;

            case "aisbabyxu":
                currentPos = sp.play(sounddata.get(19),
                        volumnRatio,// 左声道音量
                        volumnRatio,// 右声道音量
                        1, // 优先级
                        0,// 循环播放次数
                        1);// 回放速度，该值在0.5-2.0之间 1为正常速度

                break;

        }


    }


}
