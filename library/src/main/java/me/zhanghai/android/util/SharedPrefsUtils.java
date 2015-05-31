/*
 * Copyright (c) 2014 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.security.GeneralSecurityException;
import java.util.Set;

public class SharedPrefsUtils {

    public static SharedPreferences getSharedPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getString(String key, String defaultValue, Context context) {
        return getSharedPrefs(context).getString(key, defaultValue);
    }

    public static Set<String> getStringSet(String key, Set<String> defaultValue, Context context) {
        return getSharedPrefs(context).getStringSet(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue, Context context) {
        return getSharedPrefs(context).getInt(key, defaultValue);
    }

    public static long getLong(String key, long defaultValue, Context context) {
        return getSharedPrefs(context).getLong(key, defaultValue);
    }

    public static float getFloat(String key, float defaultValue, Context context) {
        return getSharedPrefs(context).getFloat(key, defaultValue);
    }

    public static boolean getBoolean(String key, boolean defaultValue, Context context) {
        return getSharedPrefs(context).getBoolean(key, defaultValue);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPrefs(context).edit();
    }

    public static void putString(String key, String value, Context context) {
        getEditor(context).putString(key, value).apply();
    }

    public static void putStringSet(String key, Set<String> value, Context context) {
        getEditor(context).putStringSet(key, value).apply();
    }

    public static void putInt(String key, int value, Context context) {
        getEditor(context).putInt(key, value).apply();
    }

    public static void putLong(String key, long value, Context context) {
        getEditor(context).putLong(key, value).apply();
    }

    public static void putFloat(String key, float value, Context context) {
        getEditor(context).putFloat(key, value).apply();
    }

    public static void putBoolean(String key, boolean value, Context context) {
        getEditor(context).putBoolean(key, value).apply();
    }

    public static void remove(String key, Context context) {
        getEditor(context).remove(key).apply();
    }

    public static void clear(Context context) {
        getEditor(context).clear().apply();
    }

    public static String getStringObfuscated(String key, String defaultValue, Context context) {
        String value = getString(key, null, context);
        if (value != null) {
            try {
                return SecurityUtils.deobfuscate(value, context);
            } catch (GeneralSecurityException e) {
                BuildUtils.throwOrPrintAndReport(e, context);
            }
        }
        return defaultValue;
    }

    public static void putStringObfuscated(String key, String value, Context context) {
        putString(key, SecurityUtils.obfuscateIfNotNull(value, context), context);
    }

    private SharedPrefsUtils() {}
}
