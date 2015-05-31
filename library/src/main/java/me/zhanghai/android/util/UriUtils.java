/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class UriUtils {

    public static InputStream openInputStream(Uri uri, Context context) {
        try {
            return context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static OpenableInfo queryOpenableInfo(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        OpenableInfo info = new OpenableInfo(
                cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)),
                cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE)));
        cursor.close();
        return info;
    }

    public static class OpenableInfo {

        private String displayName;
        private long size;

        public OpenableInfo(String displayName, long size) {
            this.displayName = displayName;
            this.size = size;
        }

        public String getDisplayName() {
            return displayName;
        }

        public long getSize() {
            return size;
        }
    }
}
