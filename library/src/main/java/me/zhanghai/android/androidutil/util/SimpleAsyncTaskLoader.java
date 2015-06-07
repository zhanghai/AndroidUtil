/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.androidutil.util;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class SimpleAsyncTaskLoader<D> extends AsyncTaskLoader<D> {

    private D result;

    public SimpleAsyncTaskLoader(Context context) {
        super(context);
    }

    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override
    public void deliverResult(D newResult) {

        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (newResult != null) {
                onReleaseResult(newResult);
            }
        }
        D oldResult = result;
        result = newResult;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(newResult);
        }

        // At this point we can release the resources associated with
        // old result if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldResult != result && oldResult != null) {
            onReleaseResult(oldResult);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        if (result != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(result);
        }

        if (takeContentChanged() || result == null) {
            // If the data has changed since the last time it was loaded
            // or is not currently available, start a load.
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override
    public void onCanceled(D result) {
        super.onCanceled(result);

        // At this point we can release the resources associated with result
        // if needed.
        if (result != null) {
            onReleaseResult(result);
        }
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'result'
        // if needed.
        if (result != null) {
            onReleaseResult(result);
            result = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResult(D result) {}
}
