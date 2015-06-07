/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.androidutil.util;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.view.Gravity;

import java.io.InputStream;

public class DrawableUtils {

    private DrawableUtils() {}

    public static Bitmap decodeBitmapFromUri(Uri uri, Context context) {

        InputStream inputStream = UriUtils.openInputStream(uri, context);
        if (inputStream == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        IoUtils.close(inputStream);
        if (!(options.outWidth > 0 && options.outHeight > 0)) {
            return null;
        }

        inputStream = UriUtils.openInputStream(uri, context);
        if (inputStream == null) {
            return null;
        }
        // Canvas.getMaximumBitmapWidth() needs a hardware accelerated canvas to produce the right
        // result, so we simply use 2048x2048 instead.
        options.inSampleSize = computeInSampleSize(options, 2048, 2048);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        IoUtils.close(inputStream);

        return bitmap;
    }

    private static int computeInSampleSize(BitmapFactory.Options options, int maxWidth,
                                           int maxHeight) {

        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;

        while (width > maxWidth || height > maxHeight) {
            inSampleSize *= 2;
            width /= 2;
            height /= 2;
        }

        return inSampleSize;
    }

    // From Muzei, Copyright 2014 Google Inc.
    public static Drawable makeScrimDrawable(int baseColor, int numStops, int gravity) {

        numStops = Math.max(numStops, 2);

        PaintDrawable paintDrawable = new PaintDrawable();
        paintDrawable.setShape(new RectShape());

        final int[] stopColors = new int[numStops];

        int red = Color.red(baseColor);
        int green = Color.green(baseColor);
        int blue = Color.blue(baseColor);
        int alpha = Color.alpha(baseColor);

        for (int i = 0; i < numStops; i++) {
            float x = i * 1f / (numStops - 1);
            float opacity = MathUtils.constrain(0, 1, (float) Math.pow(x, 3));
            stopColors[i] = Color.argb((int) (alpha * opacity), red, green, blue);
        }

        final float x0, x1, y0, y1;
        switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.LEFT:
                x0 = 1;
                x1 = 0;
                break;
            case Gravity.RIGHT:
                x0 = 0;
                x1 = 1;
                break;
            default:
                x0 = 0;
                x1 = 0;
                break;
        }
        switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                y0 = 1;
                y1 = 0;
                break;
            case Gravity.BOTTOM:
                y0 = 0;
                y1 = 1;
                break;
            default:
                y0 = 0;
                y1 = 0;
                break;
        }

        paintDrawable.setShaderFactory(new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                return new LinearGradient(
                        width * x0,
                        height * y0,
                        width * x1,
                        height * y1,
                        stopColors, null,
                        Shader.TileMode.CLAMP);
            }
        });

        paintDrawable.setAlpha(Math.round(0.4f * 255));

        return paintDrawable;
    }

    public static Drawable makeScrimDrawable(int gravity) {
        return makeScrimDrawable(Color.BLACK, 9, gravity);
    }

    public static Drawable makeScrimDrawable() {
        return makeScrimDrawable(Gravity.BOTTOM);
    }

    public static void animateLoading(final Drawable drawable, int duration) {

        ImageLoadingEvaluator evaluator = new ImageLoadingEvaluator();
        evaluator.evaluate(0, null, null);
        final AnimateColorMatrixColorFilter filter = new AnimateColorMatrixColorFilter(
                evaluator.getColorMatrix());

        drawable.setColorFilter(filter.getColorFilter());
        ObjectAnimator animator = ObjectAnimator.ofObject(filter, "colorMatrix", evaluator,
                evaluator.getColorMatrix());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                drawable.setColorFilter(filter.getColorFilter());
            }
        });
        animator.setDuration(duration);
        animator.start();
    }

    public static void animateLoading(Drawable drawable, Context context) {
        animateLoading(drawable,
                context.getResources().getInteger(android.R.integer.config_longAnimTime));
    }

    private static class ImageLoadingEvaluator implements TypeEvaluator {

        private ColorMatrix colorMatrix = new ColorMatrix();
        private float[] elements = new float[20];

        public ColorMatrix getColorMatrix() {
            return colorMatrix;
        }

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {

            // There are 3 phases so we multiply fraction by that amount
            float phase = fraction * 3;

            // Compute the alpha change over period [0, 2]
            float alpha = Math.min(phase, 2f) / 2f;
            elements [19] = (float) Math.round(alpha * 255);

            // We subtract to make the picture look darker, it will automatically clamp
            // This is spread over period [0, 2.5]
            final int MaxBlacker = 100;
            float blackening = (float) Math.round((1 - Math.min(phase, 2.5f) / 2.5f) * MaxBlacker);
            elements [4] = elements [9] = elements [14] = -blackening;

            // Finally we desaturate over [0, 3], taken from ColorMatrix.setSaturation
            float invSat = 1 - Math.max(0.2f, fraction);
            float R = 0.213f * invSat;
            float G = 0.715f * invSat;
            float B = 0.072f * invSat;
            elements[0] = R + fraction; elements[1] = G;            elements[2] = B;
            elements[5] = R;            elements[6] = G + fraction; elements[7] = B;
            elements[10] = R;           elements[11] = G;           elements[12] = B + fraction;

            colorMatrix.set(elements);
            return colorMatrix;
        }
    }

    private static class AnimateColorMatrixColorFilter {

        private ColorMatrixColorFilter filter;
        private ColorMatrix matrix;

        public AnimateColorMatrixColorFilter(ColorMatrix matrix) {
            setColorMatrix(matrix);
        }

        public ColorMatrixColorFilter getColorFilter() {
            return filter;
        }

        public ColorMatrix getColorMatrix() {
            return matrix;
        }

        public void setColorMatrix(ColorMatrix matrix) {
            this.matrix = matrix;
            filter = new ColorMatrixColorFilter(matrix);
        }
    }
}
