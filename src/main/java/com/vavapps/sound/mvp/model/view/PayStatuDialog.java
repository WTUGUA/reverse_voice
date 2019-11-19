package com.vavapps.sound.mvp.model.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vavapps.sound.R;

public class PayStatuDialog extends DialogFragment {


    private View paying;
    private View pay_faile;
    private LinearLayout llParent;

    private enum PayStatu {
        PAYING,
        SUCCESS,
        FAILE,
    }
    //默认调用支付中状态
    private PayStatu payStatu = PayStatu.PAYING ;

    public void changeStatu(PayStatu payStatu) {
        this.payStatu = payStatu;
        llParent.removeAllViews();
        switch (payStatu){
            case FAILE:
                llParent.addView(pay_faile);
                break;
            case PAYING:
                llParent.addView(paying);
                break;
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pay, container, false);

        llParent = view.findViewById(R.id.ll_parent);
        if (getActivity()!=null){
            paying = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.dialog_paying, container, false);
            pay_faile = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.dialog_payfaile, container, false);
            changeStatu(payStatu);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
