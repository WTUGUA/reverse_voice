package com.vavapps.sound.mvp.model.view;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ark.dict.ConfigMapLoader;
import com.vavapps.sound.R;
import com.vavapps.sound.mvp.ui.activity.ReverseAudioActivity;

public class ChoosePayDialog extends DialogFragment {


    LinearLayout bt_sp;
    private PayTypeClick payTypeClick;
    private int sp;

    public static int getWxSwitch() {
        String wx_pay_switch = ConfigMapLoader.getInstance().getResponseMap().get("wx_pay_switch_bugfixed");

        if (wx_pay_switch == null) {
            return 0;
        }
        return Integer.parseInt(wx_pay_switch);
    }

    public static String getLastVs() {
        String latest_version = ConfigMapLoader.getInstance().getResponseMap().get("latest_version");

        if (latest_version == null) {
            return "2.4";
        }
        return latest_version;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                //这种方法也可以
                //return mNetworkInfo .getState()== NetworkInfo.State.CONNECTED
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public void setPayTypeClick(PayTypeClick payTypeClick) {
        this.payTypeClick = payTypeClick;
    }

    public void setsp(int sp) {
        this.sp = sp;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_choose_pay, container, false);
        TextView content = (TextView) view.findViewById(R.id.tv_content);
        TextView textView = view.findViewById(R.id.text_button);

        String price = getArguments().getString("price");
        String num = getArguments().getString("num");
        String format = String.format("今日免费次数已用完，打赏%s元支持我们，1天内您可以无限制使用倒放功能，感谢您的支持！", price);
        content.setText(format);
        String text = String.format("%s", num);
        textView.setText(text);

        Button bt_wechat;

        bt_sp = view.findViewById(R.id.bt_sp);
        bt_wechat = view.findViewById(R.id.bt_wechat);
        if (sp == 1) {
                bt_sp.setVisibility(View.GONE);
            }


        String lastvs=getLastVs();
        if(lastvs.equals("2.4")){
            bt_sp.setVisibility(View.GONE);
        }else{
            if (sp == 1) {
                bt_sp.setVisibility(View.GONE);
            }else {
                bt_sp.setVisibility(View.VISIBLE);
            }
        }

        int wxswitch = getWxSwitch();
        if (wxswitch == 0) {
            bt_wechat.setVisibility(View.GONE);
        } else {
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
        view.findViewById(R.id.bt_sp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                ChoosePayDialog.this.payTypeClick.getsplash();
            }
        });

        return view;
    }

    public interface PayTypeClick {
        void aliPay();

        void wechatPay();

        void getsplash();
    }

}