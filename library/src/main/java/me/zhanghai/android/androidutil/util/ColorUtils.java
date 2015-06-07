/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.androidutil.util;

public class ColorUtils {

    public enum Material300 {
        RED(0xffe57373),
        PINK(0xfff06292),
        PURPLE(0xffba68c8),
        DEEP_PURPLE(0xff9575cd),
        INDIGO(0xff7986cb),
        BLUE(0xff64b5f6),
        CYAN(0xff4dd0e1),
        LIGHT_BLUE(0xff4fc3f7),
        TEAL(0xff4db6ac),
        GREEN(0xff81c784),
        LIGHT_GREEN(0xffaed581),
        LIME(0xffdce775),
        YELLOW(0xfffff176),
        AMBER(0xffffd54f),
        ORANGE(0xffffb74d),
        DEEP_ORANGE(0xffff8a65),
        BROWN(0xffa1887f),
        GREY(0xffe0e0e0),
        BLUE_GREY(0xff90a4ae);

        private int color;

        Material300(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }
    }

    private ColorUtils() {}

    public static int fromText(String text) {
        return Material300.values()[text.hashCode() % Material300.values().length].getColor();
    }
}
