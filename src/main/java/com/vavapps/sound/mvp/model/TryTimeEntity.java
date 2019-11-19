package com.vavapps.sound.mvp.model;

import java.util.Date;

public class TryTimeEntity {
    Date Time;
    int data;

    public TryTimeEntity(Date time) {
        Time = time;
    }

    public TryTimeEntity(int data) {
        this.data = data;
    }

    public TryTimeEntity() {
    }

    public TryTimeEntity(Date time, int data) {
        Time = time;
        this.data = data;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }
}
