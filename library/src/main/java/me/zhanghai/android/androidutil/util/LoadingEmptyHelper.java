/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.androidutil.util;

import android.database.Cursor;
import android.view.View;

public class LoadingEmptyHelper {

    private View loadingView;
    private View emptyView;

    public LoadingEmptyHelper(View loadingView, View emptyView) {
        this.loadingView = loadingView;
        this.emptyView = emptyView;
    }

    public LoadingEmptyHelper(View parent, int loadingViewId, int emptyViewId) {
        this(parent.findViewById(loadingViewId), parent.findViewById(emptyViewId));
    }

    public View getLoadingView() {
        return loadingView;
    }

    public View getEmptyView() {
        return emptyView;
    }

    public void setLoading() {
        emptyView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
    }

    public void setFinished(boolean empty) {
        loadingView.setVisibility(View.GONE);
        emptyView.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    public void setFinished(Cursor cursor) {
        setFinished(cursor == null || cursor.getCount() == 0);
    }
}
