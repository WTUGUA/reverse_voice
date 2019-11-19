package com.vavapps.sound.mvp.event;

public class WeixinPayEvent {
    private int errorCode;

    public WeixinPayEvent(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
