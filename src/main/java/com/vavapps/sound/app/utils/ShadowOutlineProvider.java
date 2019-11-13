package com.vavapps.sound.app.utils;

import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

public class ShadowOutlineProvider {


    private static final int HIDE_RADIUS_SIDE_LEFT = 1;
    private static final int HIDE_RADIUS_SIDE_TOP = 2;
    private static final int HIDE_RADIUS_SIDE_RIGHT = 3;
    private static final int HIDE_RADIUS_SIDE_BOTTOM = 4;



    public void setRadius(float radius) {
        this.radius = radius;
    }
    public void setShadowElevation(float shadowElevation) {
        this.shadowElevation = shadowElevation;
    }


    private float radius = 0;
    private boolean isRadiusWithSideHidden = false;
    private int hideRadiusSide = -1;

    public void setOutlineInsetTop(int outlineInsetTop) {
        this.outlineInsetTop = outlineInsetTop;
    }

    public void setOutlineInsetLeft(int outlineInsetLeft) {
        this.outlineInsetLeft = outlineInsetLeft;
    }
    public void setOutlineInsetBottom(int outlineInsetBottom) {
        this.outlineInsetBottom = outlineInsetBottom;
    }

    public void setRadiusWithSideHidden(boolean radiusWithSideHidden) {
        isRadiusWithSideHidden = radiusWithSideHidden;
    }

    public void setOutlineInsetRight(int outlineInsetRight) {
        this.outlineInsetRight = outlineInsetRight;
    }

    // outlineInset
    private int outlineInsetTop = 0;


    private int outlineInsetBottom = 0;

    private int outlineInsetLeft = 0;


    private int outlineInsetRight = 0;
    private boolean isOutlineExcludePadding = false;

    public void setShadowAlpha(float shadowAlpha) {
        this.shadowAlpha = shadowAlpha;
    }

    // shadow
    private float shadowAlpha = 1f;




    private float shadowElevation = 0f;
    // 只有android P生效;
    private int shadowColor = -1;
    // offset;
    private int offsetX = 0;
    private int offsetY = 0;


    public void invalidateRadiusAndShadow(View owner) {

        if (shadowElevation == 0f || isRadiusWithSideHidden) {
            owner.setElevation(0f);
        } else {
            owner.setElevation(shadowElevation);
        }
        owner.setOutlineProvider(getOutline());
        owner.setClipToOutline( radius > 0);
        owner.invalidate();
    }

    public void setShadowColorInner(View view, int shadowColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (shadowColor > 0) {
                view.setOutlineAmbientShadowColor(shadowColor);
                view.setOutlineSpotShadowColor(shadowColor);
            }
        }
    }



    private ViewOutlineProvider getOutline(){
       return new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int w = view.getWidth();
                int h = view.getHeight();
                if (w == 0 || h == 0) {
                    return;
                }
                if (isRadiusWithSideHidden) {
                    int left = 0;
                    int top = 0;
                    int right = w;
                    int bottom = h;

                    switch (hideRadiusSide){
                        case HIDE_RADIUS_SIDE_LEFT:
                            left -= radius;
                            break;
                        case HIDE_RADIUS_SIDE_TOP:
                            top -= radius;
                            break;
                        case HIDE_RADIUS_SIDE_RIGHT:
                            right += radius;
                            break;
                        case HIDE_RADIUS_SIDE_BOTTOM:
                            bottom += radius;
                            break;
                    }

                    outline.setRoundRect(left, top, right, bottom, radius);
                    return;
                }

                int top = outlineInsetTop;
                int bottom = Math.max(top + 1, h - outlineInsetBottom);
                int left = outlineInsetLeft;
                int right = w - outlineInsetRight;
                if (isOutlineExcludePadding) {
                    left += view.getPaddingLeft();
                    top += view.getPaddingTop();
                    right = Math.max(left + 1, right - view.getPaddingRight());
                    bottom = Math.max(top + 1, bottom - view.getPaddingBottom());
                }

                float tempshadowAlpha = shadowAlpha;
                if (shadowElevation == 0f) {
                    // outline.setAlpha will work even if shadowElevation == 0
                    tempshadowAlpha = 1f;
                }

                outline.setAlpha(tempshadowAlpha);

                if (radius <= 0) {
                    outline.setRect(left, top, right, bottom);
                } else {
                    outline.setRoundRect(left, top, right, bottom, radius);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if (offsetX != 0 || offsetY != 0) {
                        outline.offset(offsetX, offsetY);
                    }
                }
            }
        };
    }

}
