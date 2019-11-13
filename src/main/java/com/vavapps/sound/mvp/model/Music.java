package com.vavapps.sound.mvp.model;

import com.vavapps.sound.app.utils.FilesUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Music {
    public static final int STATE_PREPARE = 0;
    public static final int STATE_EXECUTE = 1;
    public static final int STATE_DONE = 2;
    public static final int STATE_FAILED = 3;
    public static SimpleDateFormat sdfDate= new SimpleDateFormat("yyyy年MM月dd日 ", Locale.CHINA);
    public static SimpleDateFormat sdfCurentData= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ", Locale.CHINA);
    public static SimpleDateFormat sdfDuration= new SimpleDateFormat("mm:ss", Locale.CHINA);
    private String name;
    private String date;
    private String path;
    private String durationSting;
    private boolean isPlaying = false;
    private long duration;
    private String size;
    private boolean isSelected = false;
    private int state = STATE_PREPARE;
    private String suffix;

    public Music(String name, String path, long duration) {
        this.name = name;
        this.date = sdfDate.format(new File(path).lastModified());
        this.size = FilesUtil.getAutoFileOrFilesSize(new File(path));
        this.path = path;
        this.duration = duration;
        this.durationSting = sdfDuration.format(duration);
        this.suffix = FilesUtil.getSuffix(path);
    }

    public Music(String name, String path, String size, long duration) {
        this.name = name;
        this.date = sdfDate.format(new File(path).lastModified());
        this.size = size;
        this.path = path;
        this.duration = duration;
        this.durationSting = sdfDuration.format(duration);
        this.suffix = FilesUtil.getSuffix(path);
    }

    public Music(String path) {
        this.name = FilesUtil.getFileName(path);
        this.date = sdfDate.format(new File(path).lastModified());
        this.size = FilesUtil.getAutoFileOrFilesSize(new File(path));
        this.path = path;
        this.suffix = FilesUtil.getSuffix(path);
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getPath() {
        return path;
    }

    public long getDuration() {
        return duration;
    }

    public String getDurationSting() {
        return durationSting;
    }

    public String getSize() {
        return size;
    }

    public String getSuffix() {
        return suffix;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
