/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.util;

import android.accounts.Account;
import android.content.Context;
import android.content.SharedPreferences;

import java.security.GeneralSecurityException;
import java.util.Set;

/**
 * Utility class providing one-line access to {@link AccountPreferences} functionality.
 */
public class AccountPrefsUtils {

    private AccountPrefsUtils() {}

    /**
     * @see AccountPreferences#getString(String, String)
     */
    public static String getString(String key, String defaultValue, Account account,
                                   Context context) {
        return AccountPreferences.from(account, context).getString(key, defaultValue);
    }

    /**
     * @see AccountPreferences#getStringSet(String, Set)
     */
    public static Set<String> getStringSet(String key, Set<String> defaultValue, Account account,
                                           Context context) {
        return AccountPreferences.from(account, context).getStringSet(key, defaultValue);
    }

    /**
     * @see AccountPreferences#getInt(String, int)
     */
    public static int getInt(String key, int defaultValue, Account account, Context context) {
        return AccountPreferences.from(account, context).getInt(key, defaultValue);
    }

    /**
     * @see AccountPreferences#getLong(String, long)
     */
    public static long getLong(String key, long defaultValue, Account account, Context context) {
        return AccountPreferences.from(account, context).getLong(key, defaultValue);
    }

    /**
     * @see AccountPreferences#getFloat(String, float)
     */
    public static float getFloat(String key, float defaultValue, Account account, Context context) {
        return AccountPreferences.from(account, context).getFloat(key, defaultValue);
    }

    /**
     * @see AccountPreferences#getBoolean(String, boolean)
     */
    public static boolean getBoolean(String key, boolean defaultValue, Account account,
                                     Context context) {
        return AccountPreferences.from(account, context).getBoolean(key, defaultValue);
    }

    /**
     * @see AccountPreferences#contains(String)
     */
    public static boolean contains(String key, Account account, Context context) {
        return AccountPreferences.from(account, context).contains(key);
    }

    /**
     * @see AccountPreferences#putString(String, String)
     */
    public static void putString(String key, String value, Account account, Context context) {
        AccountPreferences.from(account, context).putString(key, value);
    }

    /**
     * @see AccountPreferences#putStringSet(String, Set)
     */
    public static void putStringSet(String key, Set<String> value, Account account,
                                    Context context) {
        AccountPreferences.from(account, context).putStringSet(key, value);
    }

    /**
     * @see AccountPreferences#putInt(String, int)
     */
    public static void putInt(String key, int value, Account account, Context context) {
        AccountPreferences.from(account, context).putInt(key, value);
    }

    /**
     * @see AccountPreferences#putLong(String, long)
     */
    public static void putLong(String key, long value, Account account, Context context) {
        AccountPreferences.from(account, context).putLong(key, value);
    }

    /**
     * @see AccountPreferences#putFloat(String, float)
     */
    public static void putFloat(String key, float value, Account account, Context context) {
        AccountPreferences.from(account, context).putFloat(key, value);
    }

    /**
     * @see AccountPreferences#putBoolean(String, boolean)
     */
    public static void putBoolean(String key, boolean value, Account account, Context context) {
        AccountPreferences.from(account, context).putBoolean(key, value);
    }

    /**
     * Get an {@link String} that will be deobfuscated by
     * {@link SecurityUtils#deobfuscate(String, Context)}.
     *
     * @see AccountPreferences#getString(String, String)
     * @see SecurityUtils#deobfuscate(String, Context)
     */
    public static String getStringObfuscated(String key, String defaultValue, Account account,
                                             Context context) {
        String value = getString(key, null, account, context);
        if (value != null) {
            try {
                return SecurityUtils.deobfuscate(value, context);
            } catch (GeneralSecurityException e) {
                BuildUtils.throwOrPrint(e);
            }
        }
        return defaultValue;
    }

    /**
     * Put an {@link String} that will be obfuscated by
     * {@link SecurityUtils#obfuscateIfNotNull(String, Context)}.
     *
     * @see AccountPreferences#putString(String, String)
     * @see SecurityUtils#obfuscateIfNotNull(String, Context)
     */
    public static void putStringObfuscated(String key, String value, Account account,
                                           Context context) {
        putString(key, SecurityUtils.obfuscateIfNotNull(value, context), account, context);
    }

    /**
     * @see AccountPreferences#remove(String)
     */
    public static void remove(String key, Account account, Context context) {
        AccountPreferences.from(account, context).remove(key);
    }

    /**
     * @see AccountPreferences#registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener)
     */
    public static void registerOnAccountMetadataChangedListener(
            AccountPreferences.OnSharedPreferenceChangeListener listener, Account account,
            Context context) {
        AccountPreferences.from(account, context)
                .registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * @see AccountPreferences#unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener)
     */
    public static void unregisterOnAccountMetadataChangedListener(
            AccountPreferences.OnSharedPreferenceChangeListener listener, Account account,
            Context context) {
        AccountPreferences.from(account, context)
                .unregisterOnSharedPreferenceChangeListener(listener);
    }
}
