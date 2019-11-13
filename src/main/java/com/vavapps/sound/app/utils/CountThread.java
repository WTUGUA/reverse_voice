package com.vavapps.sound.app.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

public abstract class CountThread implements Runnable{

    protected Activity activity;
    private static CountHandler handler;
    private boolean isFinish = true;
    private long speed = 1000;

    public CountThread(Activity activity, long count){
        this.activity = activity;
        activity.isFinishing();
        handler = new CountHandler(count) {
            @Override
            protected void onCount(long count) {
                CountThread.this.onCount(count);
            }

            @Override
            protected void onFinish() {
                CountThread.this.onFinish();
            }
        };
    }

    public CountThread(Activity activity, long count, long speed){
        this.activity = activity;
        activity.isFinishing();
        handler = new CountHandler(count,speed) {
            @Override
            protected void onCount(long count) {
                CountThread.this.onCount(count);
            }

            @Override
            protected void onFinish() {
                CountThread.this.onFinish();
            }
        };
    }



    public void setCount(long count){
        handler.setCount(count);
    }

    public void start(){
        isFinish = false;
        new Thread(this).start();
    }

    public void stop(){
        isFinish = true;
    }

    @Override
    public void run() {
        while (!activity.isFinishing() && !isFinish) {
            try {
                Thread.sleep(speed);
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void onCount(long count);

    protected void onFinish(){}

    abstract class CountHandler extends Handler {

        private long count;
        private long speed = 1000;

        CountHandler(long count){
            this.count = count;
        }

        CountHandler(long count,long speed){
            this.count = count;
            this.speed = speed;
        }

        public void setCount(long count){
            this.count = count;
        }

        protected abstract void onCount(long count);

        protected abstract void onFinish();

        public void handleMessage(Message msg){
            switch (msg.what) {
                case 1:
                    count = count + speed;
                    if (count >= 0){
                        onCount(count);
                    }else {
                        CountThread.this.isFinish = true;
                        onFinish();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
