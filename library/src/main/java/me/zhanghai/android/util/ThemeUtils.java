/*
 * Copyright (c) 2014 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.ContextThemeWrapper;

import com.myqsc.mobile3.settings.info.SharedPrefsContract;

import java.util.HashMap;
import java.util.Map;

public class ThemeUtils {

    private static final int[] THEME_IDS = new int[] {
        R.style.Theme_Default,
        R.style.Theme_Holo_Dark,
        R.style.Theme_Holo_Wallpaper
    };

    private static final Map<Integer, Integer> NO_ACTION_BAR_THEME_MAP;
    static {
        NO_ACTION_BAR_THEME_MAP = new HashMap<>();
        NO_ACTION_BAR_THEME_MAP.put(R.style.Theme_Default, R.style.Theme_Default_NoActionBar);
        NO_ACTION_BAR_THEME_MAP.put(R.style.Theme_Holo_Dark, R.style.Theme_Holo_Dark_NoActionBar);
        NO_ACTION_BAR_THEME_MAP.put(R.style.Theme_Holo_Wallpaper,
                R.style.Theme_Holo_Wallpaper_NoActionBar);
    }


    public static int getThemeId(Context context) {

        int index = Integer.valueOf(SharedPrefsUtils.getString(SharedPrefsContract.KEY_THEME,
                SharedPrefsContract.DEFAULT_THEME, context));

        return THEME_IDS[index];
    }

    public static int getNoActionBarThemeId(int themeId) {
        return NO_ACTION_BAR_THEME_MAP.get(themeId);
    }

    public static boolean isWallpaperTheme(int themeId) {
        return themeId == R.style.Theme_Holo_Wallpaper
                || themeId == R.style.Theme_Holo_Wallpaper_NoActionBar;
    }

    public static boolean isWallpaperTheme(Context context) {
        return isWallpaperTheme(getThemeId(context));
    }

    public static void applyTheme(Activity activity) {
        int themeId = getThemeId(activity);
        activity.setTheme(themeId);
        if (isWallpaperTheme(themeId)) {
            applyWallpaperDim(activity);
        }
    }

    public static void applyNoActionBarTheme(Activity activity) {
        int themeId = getNoActionBarThemeId(getThemeId(activity));
        activity.setTheme(themeId);
        if (isWallpaperTheme(themeId)) {
            applyWallpaperDim(activity);
        }
    }

    public static void applyWallpaperDim(Activity activity) {
        Drawable background = new ColorDrawable(Color.BLACK);
        int dim = SharedPrefsUtils.getInt(SharedPrefsContract.KEY_WALLPAPER_DIM,
                SharedPrefsContract.DEFAULT_WALLPAPER_DIM, activity);
        int alpha = Math.round((float)dim / SharedPrefsContract.MAX_WALLPAPER_DIM * 255);
        background.setAlpha(alpha);
        activity.getWindow().setBackgroundDrawable(background);
    }

    public static Context wrapContextWithTheme(Context context, int themeRes) {
        return new ContextThemeWrapper(context, themeRes);
    }

    public static Context wrapContextWithTheme(Context context) {
        return wrapContextWithTheme(context, getThemeId(context));
    }

    public static int getResId(Context themedContext, int attrRes) {
        // NOTE: Theme.resolveAttribute() and TypedValue.resourceId doesn't seem to work, with
        // resolveRefs set to either true or false.
        TypedArray attributes = themedContext.obtainStyledAttributes(new int[] {attrRes});
        int resId = attributes.getResourceId(0, -1);
        attributes.recycle();
        return resId;
    }

    public static boolean getBoolean(Context themedContext, int attrRes, boolean defaultValue) {
        TypedArray attributes = themedContext.obtainStyledAttributes(new int[] {attrRes});
        boolean result = attributes.getBoolean(0, defaultValue);
        attributes.recycle();
        return result;
    }

    public static int getColor(Context themedContext, int attrRes, int defaultValue) {
        TypedArray attributes = themedContext.obtainStyledAttributes(new int[] {attrRes});
        int result = attributes.getColor(0, defaultValue);
        attributes.recycle();
        return result;
    }


    private ThemeUtils() {}
}
