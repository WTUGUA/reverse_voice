package com.vavapps.sound.mvp.model.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ark.dict.ConfigMapLoader;
import com.vavapps.sound.R;

public class ChoosePayDialog extends DialogFragment {



    public  interface PayTypeClick{
        void aliPay();
        void wechatPay();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        Window window = getDialog().getWindow();
//        WindowManager.LayoutParams windowParams = window.getAttributes();
//        windowParams.dimAmount = 0.0f;
//        window.setAttributes(windowParams);
//    }

    private PayTypeClick payTypeClick;

    public void setPayTypeClick(PayTypeClick payTypeClick){
        this.payTypeClick = payTypeClick;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_choose_pay, container, false);
        TextView content = (TextView) view.findViewById(R.id.tv_content);

        String price = getArguments().getString("price");
        String format = String.format("今日免费次数已用完，打赏%s元支持我们，1天内您可以无限制使用倒放功能，感谢您的支持！", price);
        content.setText(format);
        Button bt_wechat;
        bt_wechat=view.findViewById(R.id.bt_wechat);
        int wxswitch=getWxSwitch();
        if(wxswitch==0){
            bt_wechat.setVisibility(View.GONE);
        }else{
            bt_wechat.setVisibility(View.VISIBLE);
        }

        view.findViewById(R.id.bt_ali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                ChoosePayDialog.this.payTypeClick.aliPay();
            }
        });

        view.findViewById(R.id.bt_wechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                ChoosePayDialog.this.payTypeClick.wechatPay();
            }
        });

        return view;
    }
    public static int getWxSwitch() {
        String wx_pay_switch = ConfigMapLoader.getInstance().getResponseMap().get("wx_pay_switch");

        if (wx_pay_switch == null) {
            return 0;
        }
        return Integer.parseInt(wx_pay_switch);
    }
}