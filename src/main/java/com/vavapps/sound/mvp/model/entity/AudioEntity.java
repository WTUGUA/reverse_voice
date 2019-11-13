package com.vavapps.sound.mvp.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "audio_history")
public class AudioEntity {

    @PrimaryKey(autoGenerate = true)
    private Long pid;

    //保存音频制作结果到cache中
    @ColumnInfo(name = "audio_url")
    private String audio_url;

    @ColumnInfo(name = "audio_title")
    private String audio_title;

    @ColumnInfo(name = "audio_content")
    private String audio_content;

    @ColumnInfo(name = "audio_duration")
    private int audio_duration;




    @Ignore
    private boolean isPlaying = false;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getAudio_content() {
        return audio_content;
    }

    public void setAudio_content(String audio_content) {
        this.audio_content = audio_content;
    }

    public int getAudio_duration() {
        return audio_duration;
    }

    public void setAudio_duration(int audio_duration) {
        this.audio_duration = audio_duration;
    }


    public String getAudio_title() {
        return audio_title;
    }

    public void setAudio_title(String audio_title) {
        this.audio_title = audio_title;
    }
}
