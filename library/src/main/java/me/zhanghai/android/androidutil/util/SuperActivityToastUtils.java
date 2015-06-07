/*
 * Copyright (c) 2014 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.androidutil.util;

import android.app.Activity;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.OnClickWrapper;

public class SuperActivityToastUtils {

    public static void showWithButton(CharSequence message, int buttonIconRes,
                                      CharSequence buttonText, View.OnClickListener onClickListener,
                                      int duration, Activity activity) {
        SuperActivityToast toast = new SuperActivityToast(activity, SuperToast.Type.BUTTON);
        toast.setText(message);
        toast.setButtonIcon(buttonIconRes);
        toast.setButtonText(buttonText);
        ((Button)toast.getView().findViewById(com.github.johnpersano.supertoasts.R.id.button))
                .setAllCaps(true);
        toast.setOnClickWrapper(new OnClickWrapper(null, new OnClickListenerWrapper(
                onClickListener)));
        toast.setDuration(duration);
        toast.show();
    }

    public static void showWithButton(CharSequence message, int buttonIconRes,
                                      CharSequence buttonText, View.OnClickListener onClickListener,
                                      Activity activity) {
        showWithButton(message, buttonIconRes, buttonText, onClickListener,
                SuperToast.Duration.LONG, activity);
    }

    public static void showWithButton(int messageRes, int buttonIconRes, int buttonTextRes,
                                      View.OnClickListener onClickListener, int duration,
                                      Activity activity) {
        showWithButton(activity.getText(messageRes), buttonIconRes, activity.getText(buttonTextRes),
                onClickListener, duration, activity);
    }

    public static void showWithButton(int messageRes, int buttonIconRes, int buttonTextRes,
                                      View.OnClickListener onClickListener, Activity activity) {
        showWithButton(activity.getText(messageRes), buttonIconRes, activity.getText(buttonTextRes),
                onClickListener, activity);
    }

    public static void clear(Activity activity) {
        SuperActivityToast.clearSuperActivityToastsForActivity(activity);
    }

    public static void clear() {
        SuperActivityToast.cancelAllSuperActivityToasts();
    }

    private SuperActivityToastUtils() {}

    private static class OnClickListenerWrapper implements SuperToast.OnClickListener {

        private View.OnClickListener listener;

        private OnClickListenerWrapper(View.OnClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view, Parcelable parcelable) {
            listener.onClick(view);
        }
    }
}
