/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.util;

import android.content.Context;

import com.myqsc.mobile3.umeng.util.UmengUtils;

public class BuildUtils {

    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    public static void throwOrPrint(Throwable throwable) {
        if (BuildConfig.DEBUG) {
            throwRuntimeException(throwable);
        } else {
            throwable.printStackTrace();
        }
    }

    private static void throwRuntimeException(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        } else {
            throw new RuntimeException(throwable);
        }
    }

    // NOTE: If in productive build, this method prints stack trace before reporting.
    public static void throwOrPrintAndReport(Throwable throwable, Context context) {
        if (BuildConfig.DEBUG) {
            throwRuntimeException(throwable);
        } else {
            throwable.printStackTrace();
            UmengUtils.reportError(throwable, context);
        }
    }


    private BuildUtils() {}
}
