/*
 * Copyright (c) 2014 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.androidutil.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.doomonafireball.betterpickers.recurrencepicker.zh.RecurrencePickerDialog;
import com.doomonafireball.betterpickers.timezonepicker.TimeZoneInfo;
import com.doomonafireball.betterpickers.timezonepicker.TimeZonePickerDialog;
import com.myqsc.mobile3.calendar.info.RecurrenceRule;
import com.myqsc.mobile3.calendar.ui.DayFragmentAdapter;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

public class PickerUtils {

    private static boolean getUseDarkTheme(Context themedContext) {
        return ThemeUtils.getBoolean(themedContext, R.attr.betterPickersUseDarkTheme, false);
    }

    public static void pickDate(LocalDate presetDate, OnDateSetListener listener,
                                Activity activity) {

        CalendarDatePickerDialog datePickerDialog = CalendarDatePickerDialog.newInstance(
                new OnDateSetListenerWrapper(listener), presetDate.getYear(),
                presetDate.getMonthValue() - 1, presetDate.getDayOfMonth());

        int currentYear = LocalDate.now().getYear();
        int startYear = currentYear - DayFragmentAdapter.YEAR_OFFSET_LIMIT;
        int endYear = currentYear + DayFragmentAdapter.YEAR_OFFSET_LIMIT;
        datePickerDialog.setYearRange(startYear, endYear);

        datePickerDialog.setThemeDark(getUseDarkTheme(activity));

        datePickerDialog.show(activity.getFragmentManager(), null);
    }

    public static void pickTime(LocalTime presetTime, OnTimeSetListener listener,
                                Activity activity) {

        RadialTimePickerDialog timePickerDialog = RadialTimePickerDialog.newInstance(
                new OnTimeSetListenerWrapper(listener), presetTime.getHour(),
                presetTime.getMinute(), true);

        timePickerDialog.setThemeDark(getUseDarkTheme(activity));

        timePickerDialog.show(activity.getFragmentManager(), null);
    }

    public static void pickTimeZone(ZoneId presetTimeZone, OnTimeZoneSetListener listener,
                                    Activity activity) {

        TimeZonePickerDialog timeZonePickerDialog = new TimeZonePickerDialog();

        Bundle arguments = new Bundle();
        // Set start time to now so users see what the time now will be in other timezones.
        arguments.putLong(TimeZonePickerDialog.BUNDLE_START_TIME_MILLIS,
                TimeUtils.getNowEpochMilli());
        arguments.putString(TimeZonePickerDialog.BUNDLE_TIME_ZONE, presetTimeZone.getId());
        timeZonePickerDialog.setArguments(arguments);

        timeZonePickerDialog.setOnTimeZoneSetListener(new OnTimeZoneSetListenerWrapper(listener));

        timeZonePickerDialog.show(activity.getFragmentManager(), null);
    }

    // Supply start for appropriate defaults.
    public static void pickRecurrenceRule(RecurrenceRule presetRecurrenceRule, ZonedDateTime start,
                                          OnRecurrenceRuleSetListener listener, Activity activity) {

        RecurrencePickerDialog recurrencePickerDialog = new RecurrencePickerDialog();

        Bundle arguments = new Bundle();
        arguments.putLong(RecurrencePickerDialog.BUNDLE_START_TIME_MILLIS,
                TimeUtils.toEpochMilli(start));
        arguments.putString(RecurrencePickerDialog.BUNDLE_TIME_ZONE, start.getZone().getId());
        if (presetRecurrenceRule != null) {
            arguments.putString(RecurrencePickerDialog.BUNDLE_RRULE, presetRecurrenceRule.toString());
        }
        recurrencePickerDialog.setArguments(arguments);

        recurrencePickerDialog.setOnRecurrenceSetListener(new OnRecurrenceRuleSetListenerWrapper(
                listener));

        recurrencePickerDialog.show(activity.getFragmentManager(), null);
    }


    public interface OnDateSetListener {
        public void onDateSet(LocalDate date);
    }

    public interface OnTimeSetListener {
        public void onTimeSet(LocalTime time);
    }

    public interface OnTimeZoneSetListener {
        public void onTimeZoneSet(ZoneId timeZone);
    }

    public interface OnRecurrenceRuleSetListener {
        public void onRecurrenceRuleSet(RecurrenceRule recurrenceRule);
    }

    private static class OnDateSetListenerWrapper implements
            CalendarDatePickerDialog.OnDateSetListener {

        private OnDateSetListener onDateSetListener;

        public OnDateSetListenerWrapper(OnDateSetListener onDateSetListener) {
            this.onDateSetListener = onDateSetListener;
        }

        @Override
        public void onDateSet(CalendarDatePickerDialog dialog, int year, int month,
                              int dayOfMonth) {
            onDateSetListener.onDateSet(LocalDate.of(year, month + 1, dayOfMonth));
        }
    }

    private static class OnTimeSetListenerWrapper implements
            RadialTimePickerDialog.OnTimeSetListener {

        private OnTimeSetListener onTimeSetListener;

        public OnTimeSetListenerWrapper(OnTimeSetListener onTimeSetListener) {
            this.onTimeSetListener = onTimeSetListener;
        }

        @Override
        public void onTimeSet(RadialTimePickerDialog radialTimePickerDialog, int hour, int minute) {
            onTimeSetListener.onTimeSet(LocalTime.of(hour, minute));
        }
    }

    private static class OnTimeZoneSetListenerWrapper
            implements TimeZonePickerDialog.OnTimeZoneSetListener {

        private OnTimeZoneSetListener onTimeZoneSetListener;

        public OnTimeZoneSetListenerWrapper(OnTimeZoneSetListener onTimeZoneSetListener) {
            this.onTimeZoneSetListener = onTimeZoneSetListener;
        }

        @Override
        public void onTimeZoneSet(TimeZoneInfo timeZoneInfo) {
            onTimeZoneSetListener.onTimeZoneSet(ZoneId.of(timeZoneInfo.mTzId));
        }
    }

    private static class OnRecurrenceRuleSetListenerWrapper
            implements RecurrencePickerDialog.OnRecurrenceSetListener {

        private OnRecurrenceRuleSetListener onRecurrenceRuleSetListener;

        public OnRecurrenceRuleSetListenerWrapper(
                OnRecurrenceRuleSetListener onRecurrenceRuleSetListener) {
            this.onRecurrenceRuleSetListener = onRecurrenceRuleSetListener;
        }

        @Override
        public void onRecurrenceSet(String recurrenceRule) {
            LogUtils.i(recurrenceRule);
            onRecurrenceRuleSetListener.onRecurrenceRuleSet(TextUtils.isEmpty(recurrenceRule) ? null
                    : RecurrenceRule.parse(recurrenceRule));
        }
    }


    private PickerUtils() {}
}
