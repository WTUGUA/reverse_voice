package com.vavapps.sound.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.vavapps.sound.app.SoundApp;
import com.vavapps.sound.mvp.event.WeixinPayEvent;

import org.simple.eventbus.EventBus;

public class WXpayEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SoundApp.wxAPi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        SoundApp.wxAPi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Log.i("WXPayEntryActivity", "BaseReq");
    }

    @Override
    public void onResp(BaseResp resp) {
        int result = 0;
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            EventBus.getDefault().post(new WeixinPayEvent(resp.errCode));
            finish();
        }
        finish();
    }
}
