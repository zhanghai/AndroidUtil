/*
 * Copyright (c) 2014 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.androidutil.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import com.myqsc.mobile3.settings.info.SharedPrefsContract;

import java.util.Locale;

public class LocaleUtils {

    // NOTICE:
    // Framework language will fallback to en-US directly if the locale combination is not found, so
    // we have to localize it ourselves.

    // Android resource system have no support for language variant code, so we have to do this
    // dirty hack. (https://groups.google.com/forum/#!topic/android-contrib/AU9DMpt5Ewo)
    // We are using country code "AD" (Andorra) (the first element in officially assigned code
    // elements of ISO 3166-1 alpha-2) here, sorry if this will hurt their feelings.
    public static final Locale LOCALE_ZH_CN_MOE = new Locale("zh", "AD");

    public static final Locale[] LOCALES = new Locale[] {
        null, // For system default.
        Locale.CHINA,
        LOCALE_ZH_CN_MOE,
        Locale.ENGLISH
    };

    private LocaleUtils() {}

    public static Locale getLocale(Context context) {

        int index = Integer.valueOf(SharedPrefsUtils.getString(SharedPrefsContract.KEY_LANGUAGE,
                SharedPrefsContract.DEFAULT_LANGUAGE, context));

        if (index == 0) {
            return Locale.getDefault();
        } else {
            return LOCALES[index];
        }
    }

    public static void applyLocaleToConfiguration(Context context, Configuration configuration) {
        applyLocaleToConfiguration(configuration, getLocale(context));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void applyLocaleToConfiguration(Configuration configuration, Locale locale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // For layout direction (Framework preferred way).
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
    }

    public static void applyLocale(Context context) {

        Locale locale = getLocale(context);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        applyLocaleToConfiguration(configuration, locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
