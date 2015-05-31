/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

public class UriImageLoader extends SimpleAsyncTaskLoader<Bitmap> {

    private Uri uri;

    public UriImageLoader(Uri uri, Context context) {
        super(context);

        this.uri = uri;
    }

    @Override
    public Bitmap loadInBackground() {
        return DrawableUtils.decodeBitmapFromUri(uri, getContext());
    }

    @Override
    protected void onReleaseResult(Bitmap result) {
        if (result != null && !result.isRecycled()) {
            result.recycle();
        }
    }
}
