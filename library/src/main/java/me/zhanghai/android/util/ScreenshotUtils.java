/*
 * Copyright (c) 2014 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.util;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

// NOTE: Using JPEG against PNG gives a performance of about 1 second when wallpaper is to be
// attached.
public class ScreenshotUtils {

    public static Bitmap screenshot(View view, Rect clipRect) {
        Bitmap bitmap = Bitmap.createBitmap(clipRect.width(), clipRect.height(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(-clipRect.left, -clipRect.top);
        view.draw(canvas);
        return bitmap;
    }

    public static Bitmap screenshot(View view) {
        return screenshot(view, new Rect(0, 0, view.getWidth(), view.getHeight()));
    }

    public static Bitmap screenshot(Activity activity) {
        //View activityView = activity.findViewById(android.R.id.content).getRootView();
        View activityView = activity.getWindow().getDecorView();
        Rect frame = new Rect();
        activityView.getWindowVisibleDisplayFrame(frame);
        return screenshot(activityView, frame);
    }

    public static boolean saveBitmap(Bitmap snapshot, File file,
                                     Bitmap.CompressFormat compressFormat, int quality) {
        try {
            FileOutputStream snapshotOutput = new FileOutputStream(file);
            if (!snapshot.compress(compressFormat, quality, snapshotOutput)) {
                return false;
            }
            snapshotOutput.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean screenshotToFile(View view, File file,
                                           Bitmap.CompressFormat compressFormat, int quality) {
        return saveBitmap(screenshot(view), file, compressFormat, quality);
    }

    public static boolean screenshotToFile(Activity activity, File file,
                                           Bitmap.CompressFormat compressFormat, int quality) {
        return saveBitmap(screenshot(activity), file, compressFormat, quality);
    }

    public static boolean screenshotToFile(View view, File file) {
        return screenshotToFile(view, file, Bitmap.CompressFormat.JPEG, 95);
    }

    public static boolean screenshotToFile(Activity activity, File file) {
        return screenshotToFile(activity, file, Bitmap.CompressFormat.JPEG, 95);
    }

    // It seems impossible to attach wallpaper to screenshot; live wallpapers seems impossible be
    // drawn, while static wallpapers lose its clip bounds.

    public static Bitmap screenshotWithWallpaper(Context context, View view, Rect clipRect) {

        Bitmap bitmap = Bitmap.createBitmap(clipRect.width(), clipRect.height(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Translate for clipRect.
        canvas.translate(-clipRect.left, -clipRect.top);

        Drawable wallpaper = WallpaperManager.getInstance(context).getFastDrawable();
        // Center wallpaper on screen, as in launcher.
        DisplayMetrics displayMetrics = view.getResources().getDisplayMetrics();
        wallpaper.setBounds(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        // Translate the canvas to draw wallpaper on the correct location.
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        canvas.save();
        canvas.translate(-location[0], -location[1]);
        wallpaper.draw(canvas);
        canvas.restore();

        view.draw(canvas);

        return bitmap;
    }

    public static Bitmap screenshotWithWallpaper(Activity activity) {
        //View activityView = activity.findViewById(android.R.id.content).getRootView();
        View activityView = activity.getWindow().getDecorView();
        Rect frame = new Rect();
        activityView.getWindowVisibleDisplayFrame(frame);
        return screenshotWithWallpaper(activity, activityView, frame);
    }

    public static boolean screenshotToFileWithWallpaper(Activity activity, File file,
                                                        Bitmap.CompressFormat compressFormat, int quality) {
        return saveBitmap(screenshotWithWallpaper(activity), file, compressFormat, quality);
    }

    public static boolean screenshotToFileWithWallpaper(Activity activity, File file) {
        return screenshotToFileWithWallpaper(activity, file, Bitmap.CompressFormat.JPEG, 95);
    }

    public static boolean screenshotToFileWithWallpaperIf(Activity activity, File file,
                                                          Bitmap.CompressFormat compressFormat,
                                                          int quality) {
        if (ThemeUtils.isWallpaperTheme(activity)) {
            return screenshotToFileWithWallpaper(activity, file, compressFormat, quality);
        } else {
            return screenshotToFile(activity, file, compressFormat, quality);
        }
    }

    public static boolean screenshotToFileWithWallpaperIf(Activity activity, File file) {
        return screenshotToFileWithWallpaperIf(activity, file, Bitmap.CompressFormat.JPEG, 95);
    }


    private ScreenshotUtils() {}
}
