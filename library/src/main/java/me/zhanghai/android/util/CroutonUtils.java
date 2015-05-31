/*
 * Copyright (c) 2014 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.util;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class CroutonUtils {

    private enum Method {
        INFO,
        OK,
        ERROR
    }


    public static void info(Activity activity, int textRes){
        Crouton.showText(activity, textRes, getStyle(Method.INFO, activity));
    }

    public static void info(Activity activity, int textRes, int viewGroupRes){
        Crouton.showText(activity, textRes, getStyle(Method.INFO, activity), viewGroupRes);
    }

    public static void info(Activity activity, CharSequence text) {
        Crouton.showText(activity, text, getStyle(Method.INFO, activity));
    }

    public static void info(Activity activity, CharSequence text, ViewGroup viewGroup) {
        Crouton.showText(activity, text, getStyle(Method.INFO, activity), viewGroup);
    }

    public static void ok(Activity activity, int textRes){
        Crouton.showText(activity, textRes, getStyle(Method.OK, activity));
    }

    public static void ok(Activity activity, int textRes, int viewGroupRes){
        Crouton.showText(activity, textRes, getStyle(Method.OK, activity), viewGroupRes);
    }

    public static void ok(Activity activity, CharSequence text) {
        Crouton.showText(activity, text, getStyle(Method.OK, activity));
    }

    public static void ok(Activity activity, CharSequence text, ViewGroup viewGroup) {
        Crouton.showText(activity, text, getStyle(Method.OK, activity), viewGroup);
    }

    public static void error(Activity activity, int textRes){
        Crouton.showText(activity, textRes, getStyle(Method.ERROR, activity));
    }

    public static void error(Activity activity, int textRes, int viewGroupRes){
        Crouton.showText(activity, textRes, getStyle(Method.ERROR, activity), viewGroupRes);
    }

    public static void error(Activity activity, CharSequence text) {
        Crouton.showText(activity, text, getStyle(Method.ERROR, activity));
    }

    public static void error(Activity activity, CharSequence text, ViewGroup viewGroup) {
        Crouton.showText(activity, text, getStyle(Method.ERROR, activity), viewGroup);
    }

    // NOTICE: Every Activity using Crouton should call this function in its onDestroy().
    public static void clear(Activity activity) {
        Crouton.clearCroutonsForActivity(activity);
    }

    public static void clearAll() {
        Crouton.cancelAllCroutons();
    }

    private static Style getStyle(Method method, Context themedContext) {

        int textColorAttrResId = -1, backgroundColorAttrResId = -1;
        switch (method) {
            case INFO:
                textColorAttrResId = R.attr.croutonInfoTextColor;
                backgroundColorAttrResId = R.attr.croutonInfoBackgroundColor;
                break;
            case OK:
                textColorAttrResId = R.attr.croutonOkTextColor;
                backgroundColorAttrResId = R.attr.croutonOkBackgroundColor;
                break;
            case ERROR:
                textColorAttrResId = R.attr.croutonErrorTextColor;
                backgroundColorAttrResId = R.attr.croutonErrorBackgroundColor;
                break;
        }

        return buildStyle(ThemeUtils.getResId(themedContext, textColorAttrResId),
                ThemeUtils.getResId(themedContext, backgroundColorAttrResId));
    }

    private static Style buildStyle(int textColorResId, int backgroundColorResId) {
        return new Style.Builder()
                .setTextColor(textColorResId)
                .setBackgroundColor(backgroundColorResId)
                .build();
    }


    private CroutonUtils() {}
}
