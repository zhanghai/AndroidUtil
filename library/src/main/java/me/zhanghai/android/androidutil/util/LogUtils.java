/*
 * Copyright (c) 2014 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.androidutil.util;

import android.util.Log;

/**
 * Utility class for one-line logging.
 *
 * <p>Be sure to change {@link #TAG} before using the class. You can simply make a copy of this
 * class and then modify it.</p>
 *
 * @see Log
 */
public class LogUtils {

    private static final String TAG = "app_name";

    private LogUtils() {}

    /**
     * @see Log#d(String, String)
     */
    public static void d(String message) {
        Log.d(TAG, buildMessage(message));
    }

    /**
     * @see Log#e(String, String)
     */
    public static void e(String message) {
        Log.e(TAG, buildMessage(message));
    }

    /**
     * @see Log#i(String, String)
     */
    public static void i(String message) {
        Log.i(TAG, buildMessage(message));
    }

    /**
     * @see Log#v(String, String)
     */
    public static void v(String message) {
        Log.v(TAG, buildMessage(message));
    }

    /**
     * @see Log#w(String, String)
     */
    public static void w(String message) {
        Log.w(TAG, buildMessage(message));
    }

    /**
     * @see Log#wtf(String, String)
     */
    public static void wtf(String message) {
        Log.wtf(TAG, buildMessage(message));
    }

    /**
     * @see Log#println(int, String, String)
     */
    public static void println(String message) {
        Log.println(Log.INFO, TAG, message);
    }

    private static String buildMessage(String rawMessage) {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String fullClassName = caller.getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        return className + "." + caller.getMethodName() + "(): " + rawMessage;
    }
}
