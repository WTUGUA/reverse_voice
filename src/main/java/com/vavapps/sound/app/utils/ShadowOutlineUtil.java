package com.vavapps.sound.app.utils;

import android.content.Context;
import android.view.View;
import com.jess.arms.utils.ArmsUtils;
import com.vavapps.sound.R;

public class ShadowOutlineUtil {



    public static void addDefalutShadowOutLine(Context context, View view){
        ShadowOutlineProvider shadowOutlineProvider = new ShadowOutlineProvider();
        shadowOutlineProvider.setRadius(ArmsUtils.dip2px(context,4));
        shadowOutlineProvider.setShadowElevation(ArmsUtils.dip2px(context,4));
        shadowOutlineProvider.setShadowAlpha(0.2f);
        int dp2 = ArmsUtils.dip2px(context, 2);
        shadowOutlineProvider.setOutlineInsetLeft(-dp2);
        shadowOutlineProvider.setOutlineInsetTop(-dp2);
        shadowOutlineProvider.setOutlineInsetRight(-dp2);
        shadowOutlineProvider.setShadowColorInner(view, R.color.shade_color);
        shadowOutlineProvider.invalidateRadiusAndShadow(view);
    }

    public static void addDefalutBottomShadowOutLine(Context context, View view){
        ShadowOutlineProvider shadowOutlineProvider = new ShadowOutlineProvider();
        shadowOutlineProvider.setRadius(ArmsUtils.dip2px(context,4));
        shadowOutlineProvider.setShadowElevation(ArmsUtils.dip2px(context,4));
        shadowOutlineProvider.setShadowAlpha(0.2f);
        int dp2 = ArmsUtils.dip2px(context, 2);
        shadowOutlineProvider.setOutlineInsetLeft(-dp2);
        shadowOutlineProvider.setOutlineInsetTop(-dp2);
        shadowOutlineProvider.setOutlineInsetRight(-dp2);
        shadowOutlineProvider.setShadowColorInner(view, R.color.shade_color);
        shadowOutlineProvider.invalidateRadiusAndShadow(view);
    }

}
