package com.vavapps.sound.app.player;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MusicPlayer implements MediaPlayer.OnCompletionListener{

    private static MusicPlayer musicPlayer;
    private volatile MediaPlayer mediaPlayer;
    private ArrayList<PlayerListener> listeners = new ArrayList<>();
    public static SimpleDateFormat sdfDuration= new SimpleDateFormat("mm:ss", Locale.CHINA);

    private MusicPlayer(){}

    public static synchronized MusicPlayer getInstance(){
        if (musicPlayer == null) musicPlayer = new MusicPlayer();
        return musicPlayer;
    }

    public void play(String path){
        prepareSource(path);
        start();
    }

    public void prepareSource(String path) {
        MediaPlayer player = getMediaPlayer();
        player.reset();
        try {
            player.setDataSource(path);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        getMediaPlayer().start();
    }

    public void pause() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying())
                mediaPlayer.pause();
            for(PlayerListener listener : listeners) listener.onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying())
                mediaPlayer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void release(){
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void addListener(PlayerListener listener){
        listeners.add(listener);
    }

    public void clearListener(){
        listeners.clear();
    }

    public void seekTo(int msec){
        if (mediaPlayer != null) mediaPlayer.seekTo(msec);
    }

    public boolean isPlaying(){
        if (mediaPlayer != null)
            return mediaPlayer.isPlaying();
        return false;
    }

    public int getCurrentPosition(){
        if (mediaPlayer != null)
            return mediaPlayer.getCurrentPosition();
        else return 0;
    }

    public int getDuration(){
        if (mediaPlayer != null)
            return mediaPlayer.getDuration();
        else return 0;
    }

    public String getDurationString(){
        if (mediaPlayer != null)
            return sdfDuration.format(mediaPlayer.getDuration());
        else return "00:00";
    }

    public int getPercentage(){
        if (mediaPlayer != null)
            return (int) (100.0 * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration());
        else return 0;
    }

    private MediaPlayer getMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        return mediaPlayer;
    }


    public void setOnErrorListener(MediaPlayer.OnErrorListener listener){
        getMediaPlayer().setOnErrorListener(listener);
    }
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        for(PlayerListener listener : listeners) listener.onCompletion();
    }
}
