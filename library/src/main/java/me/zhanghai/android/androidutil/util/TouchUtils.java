/*
 * Copyright (c) 2014 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.androidutil.util;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class TouchUtils {

    private static final int TOUCH_TIME_MILLIS = 500;

    private TouchUtils() {}

    // Adapted from android.test.TouchUtils
    public static void touchAndCancel(final View view) {

        int[] xy = new int[2];
        view.getLocationOnScreen(xy);
        final float x = xy[0] + (view.getWidth() / 2.0f);
        final float y = xy[1] + (view.getHeight() / 2.0f);

        final long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();

        MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y,
                0);
        view.dispatchTouchEvent(event);
        event.recycle();

        AppUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                long eventTime = SystemClock.uptimeMillis();
                int touchSlop = ViewConfiguration.get(view.getContext()).getScaledTouchSlop();
                MotionEvent event = MotionEvent.obtain(downTime, eventTime,
                        MotionEvent.ACTION_CANCEL,
                        x + (touchSlop / 2.0f), y + (touchSlop / 2.0f), 0);
                view.dispatchTouchEvent(event);
                event.recycle();
            }
        }, TOUCH_TIME_MILLIS);
    }
}
