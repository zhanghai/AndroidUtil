/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.androidutil.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

// TODO: Provide scrolling at edge, see
// https://github.com/justasm/DragLinearLayout/blob/master/library/src/main/java/com/jmedeisis/draglinearlayout/DragLinearLayout.java
// and
// https://github.com/nhaarman/ListViewAnimations/blob/master/lib-manipulation/src/main/java/com/nhaarman/listviewanimations/itemmanipulation/dragdrop/DragAndDropHandler.java
public class DragUtils {

    private DragUtils() {}

    public static void setupDragSort(View view, final DragListener listener) {
        view.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(final View view, DragEvent event) {
                ViewGroup viewGroup = (ViewGroup)view.getParent();
                DragState dragState = (DragState)event.getLocalState();
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        if (view == dragState.view) {
                            view.setVisibility(View.INVISIBLE);
                            listener.onDragStarted();
                        }
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION: {
                        if (view == dragState.view){
                            break;
                        }
                        int index = viewGroup.indexOfChild(view);
                        if ((index > dragState.index && event.getY() > view.getHeight() / 2)
                                || (index < dragState.index && event.getY() < view.getHeight() / 2)) {
                            swapViews(viewGroup, view, index, dragState);
                        } else {
                            swapViewsBetweenIfNeeded(viewGroup, index, dragState);
                        }
                        break;
                    }
                    case DragEvent.ACTION_DRAG_ENDED:
                        if (view == dragState.view) {
                            view.setVisibility(View.VISIBLE);
                            listener.onDragEnded();
                        }
                        break;
                }
                return true;
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.startDrag(null, new View.DragShadowBuilder(view), new DragState(view), 0);
                return true;
            }
        });
    }

    public static void setupDragDelete(View view, final ViewGroup viewGroup,
                                       final OnDragDeletedListener listener) {
        view.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        view.setActivated(true);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        view.setActivated(false);
                        break;
                    case DragEvent.ACTION_DROP:
                        DragState dragState = (DragState)event.getLocalState();
                        removeView(viewGroup, dragState);
                        listener.onDragDeleted();
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        // NOTE: Needed because ACTION_DRAG_EXITED may not be sent when the drag
                        // ends within the view.
                        view.setActivated(false);
                        break;
                }
                return true;
            }
        });
    }

    private static void swapViewsBetweenIfNeeded(ViewGroup viewGroup, int index,
                                                 DragState dragState) {
        if (index - dragState.index > 1) {
            int indexAbove = index - 1;
            swapViews(viewGroup, viewGroup.getChildAt(indexAbove), indexAbove, dragState);
        } else if (dragState.index - index > 1) {
            int indexBelow = index + 1;
            swapViews(viewGroup, viewGroup.getChildAt(indexBelow), indexBelow, dragState);
        }
    }

    private static void swapViews(ViewGroup viewGroup, final View view, int index,
                                  DragState dragState) {
        swapViewsBetweenIfNeeded(viewGroup, index, dragState);
        final float viewY = view.getY();
        AppUtils.swapViewGroupChildren(viewGroup, view, dragState.view);
        dragState.index = index;
        AppUtils.postOnPreDraw(view, new Runnable() {
            @Override
            public void run() {
                ObjectAnimator
                        .ofFloat(view, View.Y, viewY, view.getTop())
                        .setDuration(getDuration(view))
                        .start();
            }
        });
    }

    private static void removeView(final ViewGroup viewGroup, DragState dragState) {

        final int oldViewGroupLayoutParamsHeight = viewGroup.getLayoutParams().height;
        final int oldViewGroupHeight = viewGroup.getHeight();
        viewGroup.removeView(dragState.view);

        int childCount = viewGroup.getChildCount();
        for (int i = dragState.index; i < childCount; ++i) {
            final View view = viewGroup.getChildAt(i);
            final float viewY = view.getY();
            AppUtils.postOnPreDraw(view, new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator
                            .ofFloat(view, View.Y, viewY, view.getTop())
                            .setDuration(getDuration(view))
                            .start();
                }
            });
        }

        final int newViewGroupHeight = measureViewGroupHeight(viewGroup);
        if (viewGroup.getChildCount() > 0) {
            // Prevent the flash of the new height before the start of our animation.
            AppUtils.setViewLayoutParamsHeight(viewGroup, oldViewGroupHeight);
            // Wait until the OnPreDraw of the last child is called for syncing the two animations on
            // View and ViewGroup.
            AppUtils.postOnPreDraw(viewGroup.getChildAt(viewGroup.getChildCount() - 1), new Runnable() {
                @Override
                public void run() {
                    animateViewGroupHeight(viewGroup, oldViewGroupLayoutParamsHeight,
                            oldViewGroupHeight, newViewGroupHeight);
                }
            });
        } else {
            // Animate now since there is no children.
            animateViewGroupHeight(viewGroup, oldViewGroupLayoutParamsHeight, oldViewGroupHeight,
                    newViewGroupHeight);
        }
    }

    private static int measureViewGroupHeight(ViewGroup viewGroup) {
        View parent = (View)viewGroup.getParent();
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                parent.getMeasuredWidth() - parent.getPaddingLeft() - parent.getPaddingRight(),
                View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        viewGroup.measure(widthMeasureSpec, heightMeasureSpec);
        return viewGroup.getMeasuredHeight();
    }

    private static void animateViewGroupHeight(final ViewGroup viewGroup,
                                               final int oldLayoutParamsHeight, int oldHeight,
                                               int newHeight) {
        ValueAnimator viewGroupAnimator = ValueAnimator
                .ofInt(oldHeight, newHeight)
                .setDuration(getDuration(viewGroup));
        viewGroupAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        viewGroupAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                AppUtils.setViewLayoutParamsHeight(viewGroup, animatedValue);
            }
        });
        viewGroupAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                AppUtils.setViewLayoutParamsHeight(viewGroup, oldLayoutParamsHeight);
            }
        });
        viewGroupAnimator.start();
    }

    private static int getDuration(View view) {
        return view.getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    public interface DragListener {
        void onDragStarted();
        void onDragEnded();
    }

    public interface OnDragDeletedListener {
        void onDragDeleted();
    }

    private static class DragState {

        public View view;
        public int index;

        private DragState(View view) {
            this.view = view;
            index = ((ViewGroup)view.getParent()).indexOfChild(view);
        }
    }
}
