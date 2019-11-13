package com.vavapps.sound.app.utils;

import android.content.Context;

import com.vavapps.sound.R;

import cafe.adriel.androidaudioconverter.model.AudioFormat;

public class ArrayUtils {
    public static AudioFormat[] formats = new AudioFormat[]{AudioFormat.WAV,AudioFormat.M4A, AudioFormat.MP3, AudioFormat.AAC, AudioFormat.WMA, AudioFormat.FLAC};
    public static int[] sampleRate = new int[]{8000, 11025, 16000, 22050, 32000, 44100, 48000, 88200, 96000,76400, 192000, 352800,384000};
    public static boolean[] mono = new boolean[]{false, true};

    public static int defFormat = 0;
    public static int defSampleRate = 2;
    public static int defMono = 0;

    public static String[] getFormatTip(Context context) {
        return context.getResources().getStringArray(R.array.format);
    }

    public static String[] getSampleRateTip(Context context) {
        return context.getResources().getStringArray(R.array.sample_rate);
    }

    public static String[] getMonoTip(Context context) {
        return context.getResources().getStringArray(R.array.momo);
    }
}