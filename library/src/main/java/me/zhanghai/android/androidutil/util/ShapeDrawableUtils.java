/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.androidutil.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;

import com.myqsc.mobile3.ui.DrawableShapeDrawable;
import com.myqsc.mobile3.ui.TextShapeDrawable;

public class ShapeDrawableUtils {

    private ShapeDrawableUtils() {}

    public static TextShapeDrawable makeText(String text) {
        return new TextShapeDrawable.Builder()
                .setColor(ColorUtils.fromText(text))
                .setText(text.substring(0, 1).toUpperCase())
                .asOval()
                .build();
    }

    public static DrawableShapeDrawable makeChecked(Context context) {
        return new DrawableShapeDrawable.Builder()
                .setDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_white_24dp))
                .asOval()
                .build();
    }

    public static StateListDrawable makeSelectable(String text, Drawable checkedDrawable) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[] {android.R.attr.state_activated}, checkedDrawable);
        drawable.addState(new int[] {}, makeText(text));
        return drawable;
    }
}
