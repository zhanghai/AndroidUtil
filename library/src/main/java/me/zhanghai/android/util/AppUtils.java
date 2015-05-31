/*
 * Copyright (c) 2014 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.CookieManager;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.myqsc.mobile3.main.info.DrawerManager;
import com.myqsc.mobile3.main.ui.MainActivity;
import com.myqsc.mobile3.ui.RestartApplicationActivity;

import java.util.ArrayList;
import java.util.List;

public class AppUtils {

    public enum ActivityTransitionType {
        ACTIVITY_OPEN,
        ACTIVITY_CLOSE,
        SLIDE_IN_UP,
        SLIDE_OUT_DOWN,
        FADE
    }

    public static final int ACTIONBAR_DISPLAY_OPTIONS = ActionBar.DISPLAY_SHOW_HOME
            | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE;

    public static final int ACTIONBAR_DISPLAY_OPTIONS_NO_UP = ACTIONBAR_DISPLAY_OPTIONS
            & ~ActionBar.DISPLAY_HOME_AS_UP;

    private static final int RESTART_APPLICATION_DELAY = 250;

    public static void crossfadeViews(final View fromView, final View toView) {

        int duration = fromView.getResources()
                .getInteger(android.R.integer.config_shortAnimTime);

        fromView.setVisibility(View.VISIBLE);
        fromView.animate()
                .setDuration(duration)
                .alpha(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fromView.setVisibility(View.GONE);
                    }
                });

        toView.setVisibility(View.VISIBLE);
        toView.animate()
                .setDuration(duration)
                .alpha(1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {

                        toView.setVisibility(View.VISIBLE);
                    }
                });
    }

    public static List<Integer> getAbsListViewCheckedItemPositions(AbsListView absListView) {
        SparseBooleanArray checked  = absListView.getCheckedItemPositions();
        List<Integer> positions = new ArrayList<>();
        int checkedSize = checked.size();
        for (int i = 0; i < checkedSize; ++i) {
            if (checked.valueAt(i)) {
                positions.add(checked.keyAt(i));
            }
        }
        return positions;
    }

    public static Drawable getActionBarBackground(Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarStyle, outValue, true);
        TypedArray typedArray = context.obtainStyledAttributes(outValue.resourceId,
                new int[] {android.R.attr.background});
        Drawable background = typedArray.getDrawable(0);
        typedArray.recycle();
        return background;
    }

    public static Drawable getSplitActionBarBackground(Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarStyle, outValue, true);
        TypedArray typedArray = context.obtainStyledAttributes(outValue.resourceId,
                new int[] {android.R.attr.backgroundSplit});
        Drawable background = typedArray.getDrawable(0);
        typedArray.recycle();
        return background;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static FragmentManager getChildFragmentManagerIfAvailable(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return fragment.getChildFragmentManager();
        } else {
            return fragment.getFragmentManager();
        }
    }

    public static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // Should not happen.
            throw new RuntimeException(e);
        }
    }

    public static void installShortcut(int iconRes, int nameRes, Class<?> intentClass,
                                       Context context) {
        Intent intent = IntentUtils.makeInstallShortcutWithAction(iconRes, nameRes, intentClass,
                context);
        context.sendBroadcast(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // From 4.1.1_r1, launcher will not make a toast for successful shortcut installation.
            String message = context.getString(R.string.shortcut_installed_format,
                    context.getString(nameRes));
            ToastUtils.show(message, context);
        }
    }

    // Set the intent for shortcut installation as result and then finish the Activity. See the
    // users of this method for its usage.
    public static void installShortcutAsActivity(Activity activity, int iconRes, int nameRes,
                                                 Class<?> intentClass) {
        activity.setResult(Activity.RESULT_OK, IntentUtils.makeInstallShortcut(iconRes, nameRes,
                intentClass, activity));
        activity.finish();
    }

    public static boolean isFragmentAttached(Fragment fragment) {
        return fragment.getActivity() != null;
    }

    public static View makeDoneCancelLayout(View.OnClickListener onDoneListener,
                                            View.OnClickListener onCancelListener,
                                            LayoutInflater layoutInflater) {
        @SuppressLint("InflateParams")
        LinearLayout doneDiscardLayout = (LinearLayout)layoutInflater.inflate(R.layout.done_cancel,
                null);
        doneDiscardLayout.setLayoutParams(new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
        doneDiscardLayout.findViewById(R.id.done_cancel_done).setOnClickListener(onDoneListener);
        doneDiscardLayout.findViewById(R.id.done_cancel_cancel)
                .setOnClickListener(onCancelListener);
        return doneDiscardLayout;
    }

    public static View makeDoneCancelLayout(View.OnClickListener onDoneListener,
                                            View.OnClickListener onCancelListener,
                                            Activity activity) {
        return makeDoneCancelLayout(onDoneListener, onCancelListener, activity.getLayoutInflater());
    }

    // From http://developer.android.com/training/implementing-navigation/ancestral.html#NavigateUp .
    public static void navigateUp(Activity activity, Bundle extras) {
        Intent upIntent = NavUtils.getParentActivityIntent(activity);
        if (upIntent == null) {
            LogUtils.w("No parent found for activity, will just call finish(): "
                    + activity.getClass().getName());
        } else {
            if (extras != null) {
                upIntent.putExtras(extras);
            }
            if (NavUtils.shouldUpRecreateTask(activity, upIntent)) {
                LogUtils.i("Creating new task");
                // This activity is NOT part of this app's task, so create a new task
                // when navigating up, with a synthesized back stack.
                TaskStackBuilder.create(activity)
                        // Add all of this activity's parents to the back stack.
                        .addNextIntentWithParentStack(upIntent)
                                // Navigate up to the closest parent.
                        .startActivities();
            } else {
                // This activity is part of this app's task, so simply
                // navigate up to the logical parent activity.
                LogUtils.i("Using original task");
                // According to http://stackoverflow.com/a/14792752/2420519
                //NavUtils.navigateUpTo(activity, upIntent);
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(upIntent);
            }
        }
        activity.finish();
    }

    public static void navigateUp(Activity activity) {
        navigateUp(activity, null);
    }

    public static void navigateUpToMainActivityWithDrawerItemId(Activity activity,
                                                                int drawerItemId) {
        Bundle mainExtras = new Bundle();
        int drawerItemPosition = DrawerManager.getInstance().getPosition(drawerItemId);
        mainExtras.putInt(MainActivity.EXTRA_DRAWER_POSITION, drawerItemPosition);
        AppUtils.navigateUp(activity, mainExtras);
    }

    // Should be called after startActivity() and in onPause().
    public static void overrideActivityTransition(Activity activity,
                                                  ActivityTransitionType type) {
        int enterAnim, exitAnim;
        switch (type) {
            case ACTIVITY_OPEN:
                enterAnim = R.anim.activity_open_enter_holo;
                exitAnim = R.anim.activity_open_exit_holo;
                break;
            case ACTIVITY_CLOSE:
                enterAnim = R.anim.activity_close_enter_holo;
                exitAnim = R.anim.activity_close_exit_holo;
                break;
            case SLIDE_IN_UP:
                enterAnim = R.anim.slide_in_up;
                exitAnim = R.anim.remain;
                break;
            case SLIDE_OUT_DOWN:
                enterAnim = R.anim.remain;
                exitAnim = R.anim.slide_out_down;
                break;
            case FADE:
                enterAnim = R.anim.activity_fade_in;
                exitAnim = R.anim.activity_fade_out;
                break;
            default:
                throw new IllegalArgumentException("Unknown activity transition type: " + type);
        }
        activity.overridePendingTransition(enterAnim, exitAnim);
    }

    public static void overrideActivityTransitionForCompat(Activity activity, boolean isOpen) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            int enterAnim, exitAnim;
            if (isOpen) {
                enterAnim = R.anim.activity_open_enter_compat;
                exitAnim = R.anim.activity_open_exit_compat;
            } else {
                enterAnim = R.anim.activity_close_enter_compat;
                exitAnim = R.anim.activity_close_exit_compat;
            }
            activity.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    public static void post(Runnable runnable) {
        new Handler().post(runnable);
    }

    public static void postDelayed(Runnable runnable, long delayMillis) {
        new Handler().postDelayed(runnable, delayMillis);
    }

    public static void postOnPreDraw(View view, final Runnable runnable) {
        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }
                runnable.run();
                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void removeAllCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(null);
        } else {
            //noinspection deprecation
            cookieManager.removeAllCookie();
        }
    }

    public static void restartActivity(Activity activity) {
        activity.recreate();
    }

    @Deprecated
    public static void restartApplication(Context context) {
        // NOTE: Flag NEW_TASK is required for CLEAR_TASK, and the latter one can clear our tasks so
        // that Samsumg ROMs will not automatically recreate our root activity when we exit(0).
        Intent intent = new Intent(context, RestartApplicationActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void restartApplicationDelayed(final Context context) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                //noinspection deprecation
                restartApplication(context);
            }
        }, RESTART_APPLICATION_DELAY);
    }

    public static void setAbsListViewAllItemsChecked(AbsListView absListView) {
        int count = absListView.getCount();
        for (int position = 0; position < count; ++position) {
            absListView.setItemChecked(position, true);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setAlarmExact(Context context, int type, long triggerAtMillis,
                                     PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(type, triggerAtMillis, pendingIntent);
        } else {
            alarmManager.set(type, triggerAtMillis, pendingIntent);
        }
    }

    // NOTE: Can be used to enable / disable a BroadcastReceiver.
    public static void setComponentEnabled(Class<?> componentClass, boolean enabled,
                                           Context context) {
        ComponentName componentName = new ComponentName(context, componentClass);
        PackageManager packageManager = context.getPackageManager();
        int state = enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        packageManager.setComponentEnabledSetting(componentName, state,
                PackageManager.DONT_KILL_APP);
    }

    // Workaround stupid Spinner, always fire onItemSelected().
    public static void setSpinnerSelection(Spinner spinner, int position) {
        if (spinner.getSelectedItemPosition() != position) {
            spinner.setSelection(position);
        } else {
            spinner.getOnItemSelectedListener().onItemSelected(spinner, spinner.getSelectedView(),
                    position, spinner.getAdapter().getItemId(position));
        }
    }

    public static void setStatusBarColor(Window window, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(color);
        }
    }

    public static void setStatusBarColorRes(Window window, int colorId, Context context) {
        setStatusBarColor(window, context.getResources().getColor(colorId));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setViewBackground(View view, Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            //noinspection deprecation
            view.setBackgroundDrawable(background);
        }
    }

    public static void setViewLayoutParamsHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public static void setupActionBar(ActionBar actionBar) {
        actionBar.setDisplayOptions(ACTIONBAR_DISPLAY_OPTIONS);
        actionBar.setHomeButtonEnabled(true);
        // NOTE:
        // There is a bug in the handling of ActionView (SearchView) icon inside ActionBarImpl, so
        // we have to workaround it by setting the icon to logo programmatically.
        actionBar.setIcon(R.drawable.logo);
    }

    public static void setupActionBar(Activity activity) {
        setupActionBar(activity.getActionBar());
    }

    public static void setupActionBarIfHas(Activity activity) {
        ActionBar actionBar = activity.getActionBar();
        if (actionBar != null) {
            setupActionBar(actionBar);
        }
    }

    public static void setupActionBarNoUp(ActionBar actionBar) {
        actionBar.setDisplayOptions(ACTIONBAR_DISPLAY_OPTIONS_NO_UP);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setIcon(R.drawable.logo);
    }

    public static void setupActionBarNoUp(Activity activity) {
        setupActionBarNoUp(activity.getActionBar());
    }

    public static void setupActionBarDoneCancel(Activity activity,
                                                View.OnClickListener onDoneListener,
                                                View.OnClickListener onCancelListener) {
        ActionBar actionBar = activity.getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(makeDoneCancelLayout(onDoneListener, onCancelListener, activity));
    }

    public static void startActivity(Intent intent, Context context) {
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            ToastUtils.show(R.string.activity_not_found, context);
        }
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode) {
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            ToastUtils.show(R.string.activity_not_found, activity);
        }
    }

    // NOTE: ListView should make hasStableIds() return true for transition to apply.
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void startDelayedTransitionIfAvailable(ViewGroup viewGroup, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(viewGroup, new CrossFade(context));
        }
    }

    public static void swapViewGroupChildren(ViewGroup viewGroup, View firstView, View secondView) {
        int firstIndex = viewGroup.indexOfChild(firstView);
        int secondIndex = viewGroup.indexOfChild(secondView);
        if (firstIndex < secondIndex) {
            viewGroup.removeViewAt(secondIndex);
            viewGroup.removeViewAt(firstIndex);
            viewGroup.addView(secondView, firstIndex);
            viewGroup.addView(firstView, secondIndex);
        } else {
            viewGroup.removeViewAt(firstIndex);
            viewGroup.removeViewAt(secondIndex);
            viewGroup.addView(firstView, secondIndex);
            viewGroup.addView(secondView, firstIndex);
        }
    }


    private AppUtils() {}


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static class CrossFade extends TransitionSet {

        public CrossFade(Context context) {
            setOrdering(ORDERING_TOGETHER);
            int duration = context.getResources().getInteger(
                    android.R.integer.config_shortAnimTime);
            setDuration(duration);
            addTransition(new Fade(Fade.OUT));
            addTransition(new Fade(Fade.IN));
        }
    }
}
