/*
 * Copyright (c) 2014 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.androidutil.util;

public class ArrayUtils {

    public static <T> int indexOf(T[] array, T value) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    public static int[] stringArrayToIntArray(String[] stringArray) {
        int[] intArray = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; ++i) {
            intArray[i] = Integer.parseInt(stringArray[i]);
        }
        return intArray;
    }


    private ArrayUtils() {}
}
