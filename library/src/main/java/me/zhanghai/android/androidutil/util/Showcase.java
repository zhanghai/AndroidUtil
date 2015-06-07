/*
 * Copyright (c) 2014 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.androidutil.util;

import android.app.Activity;
import android.view.View;

import com.espian.showcaseview.ShowcaseView;
import com.espian.showcaseview.ShowcaseViewBuilder;
import com.espian.showcaseview.ShowcaseViews;

public class Showcase {

    public enum ShowcaseId {
        Calendar_DayView_Add,
        Calendar_DayView_Goto,
        Courses,
        AccountManagement,
        Home_Edit
    }

    private static final float SCALE_DEFAULT = 1;

    private static final float SCALE_ACTION_ITEM = 0.5f;

    private Activity activity;

    private ShowcaseViews showcaseViews;

    public Showcase(Activity activity) {
        this.activity = activity;
        showcaseViews = new ShowcaseViews(activity);
    }

    public Showcase addActionItem(ShowcaseId showcaseId, int actionItemId, int titleResId,
                                  int detailResId, float scale) {
        showcaseViews.addView(new ShowcaseViewBuilder(activity)
                .setShowcaseItem(ShowcaseView.ITEM_ACTION_ITEM, actionItemId, activity)
                .setText(titleResId, detailResId)
                .setShowcaseIndicatorScale(scale)
                .setConfigOptions(makeConfigOptions(showcaseId))
                .build());
        return this;
    }

    public Showcase addActionItem(ShowcaseId showcaseId, int actionItemId, int titleResId,
                                  int detailResId) {
        return addActionItem(showcaseId, actionItemId, titleResId, detailResId, SCALE_ACTION_ITEM);
    }

    public Showcase addView(ShowcaseId showcaseId, View view, int titleResId, int detailResId,
                            float scale) {
        if (view == null) {
            LogUtils.e("addView called with view == null, adding no view");
            return addNoView(showcaseId, titleResId, detailResId);
        } else {
            showcaseViews.addView(new ShowcaseViewBuilder(activity)
                    .setShowcaseView(view)
                    .setText(titleResId, detailResId)
                    .setShowcaseIndicatorScale(scale)
                    .setConfigOptions(makeConfigOptions(showcaseId))
                    .build());
            return this;
        }
    }

    public Showcase addView(ShowcaseId showcaseId, View view, int titleResId, int detailResId) {
        return addView(showcaseId, view, titleResId, detailResId, SCALE_DEFAULT);
    }

    public Showcase addView(ShowcaseId showcaseId, int viewId, int titleResId, int detailResId,
                            float scale) {
        return addView(showcaseId, activity.findViewById(viewId), titleResId, detailResId, scale);
    }

    public Showcase addView(ShowcaseId showcaseId, int viewId, int titleResId, int detailResId) {
        return addView(showcaseId, viewId, titleResId, detailResId, SCALE_DEFAULT);
    }

    public Showcase addPosition(ShowcaseId showcaseId, int x, int y, int titleResId,
                                int detailResId, float scale) {
        showcaseViews.addView(new ShowcaseViewBuilder(activity)
                .setShowcasePosition(x, y)
                .setText(titleResId, detailResId)
                .setShowcaseIndicatorScale(scale)
                .setConfigOptions(makeConfigOptions(showcaseId))
                .build());
        return this;
    }

    public Showcase addPosition(ShowcaseId showcaseId, int x, int y, int titleResId,
                                int detailResId) {
        return addPosition(showcaseId, x, y, titleResId, detailResId, SCALE_DEFAULT);
    }

    public Showcase addNoView(ShowcaseId showcaseId, int titleResId, int detailResId) {
        showcaseViews.addView(new ShowcaseViewBuilder(activity)
                .setShowcaseNoView()
                .setText(titleResId, detailResId)
                .setShowcaseIndicatorScale(SCALE_DEFAULT)
                .setConfigOptions(makeConfigOptions(showcaseId))
                .build());
        return this;
    }

    public void show() {
        showcaseViews.show();
    }

    public void postShow() {
        AppUtils.post(new Runnable() {
            @Override
            public void run() {
                show();
            }
        });
    }

    private ShowcaseView.ConfigOptions makeConfigOptions(ShowcaseId showcaseId) {
        ShowcaseView.ConfigOptions configOptions = new ShowcaseView.ConfigOptions();
        configOptions.block = true;
        configOptions.shotType = ShowcaseView.TYPE_ONE_SHOT;
        configOptions.showcaseId = showcaseId.ordinal();
        return configOptions;
    }
}
