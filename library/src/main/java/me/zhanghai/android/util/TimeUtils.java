/*
 * Copyright (c) 2014 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.util;

import android.content.Context;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public static final ZoneOffset ZJU_ZONE_OFFSET = ZoneOffset.of("+08:00");

    public static final ZoneId ZJU_ZONE_ID = ZoneId.ofOffset("GMT", ZJU_ZONE_OFFSET);

    public static long toEpochMilli(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toInstant().toEpochMilli();
    }

    public static long toEpochSecondInZju(LocalDateTime dateTime) {
        return dateTime.toEpochSecond(ZJU_ZONE_OFFSET);
    }

    public static long toEpochSecondInZju(LocalDate date, LocalTime time) {
        return toEpochSecondInZju(LocalDateTime.of(date, time));
    }

    public static ZonedDateTime toZonedDateTime(long epochSecond, ZoneId zoneId) {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), zoneId);
    }

    public static ZonedDateTime toZonedDateTimeInSystem(long epochSecond) {
        return toZonedDateTime(epochSecond, ZoneId.systemDefault());
    }

    public static ZonedDateTime toZonedDateTimeInZju(long epochSecond) {
        return toZonedDateTime(epochSecond, ZJU_ZONE_ID);
    }

    public static ZonedDateTime toZonedDateTimeInSystem(LocalDate date, LocalTime time) {
        return ZonedDateTime.of(date, time, ZoneId.systemDefault());
    }

    public static ZonedDateTime toZonedDateTimeInZju(LocalDate date, LocalTime time) {
        return ZonedDateTime.of(date, time, ZJU_ZONE_ID);
    }

    public static ZonedDateTime toZonedDateTimeInSystem(Instant instant) {
        return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static ZonedDateTime toZonedDateTimeInZju(Instant instant) {
        return ZonedDateTime.ofInstant(instant, ZJU_ZONE_ID);
    }

    public static ZonedDateTime withDayOfWeek(ZonedDateTime zonedDateTime, DayOfWeek dayOfWeek) {
        return zonedDateTime.plusDays(
                dayOfWeek.getValue() - zonedDateTime.getDayOfWeek().getValue());
    }

    public static ZonedDateTime withWeekStart(ZonedDateTime zonedDateTime) {
        return withDayOfWeek(zonedDateTime, DayOfWeek.MONDAY);
    }

    public static ZonedDateTime withDayStart(ZonedDateTime zonedDateTime) {
        return zonedDateTime.truncatedTo(ChronoUnit.DAYS);
    }

    public static ZonedDateTime getTodayStartInSystem() {
        return withDayStart(ZonedDateTime.now());
    }

    public static ZonedDateTime getTomorrowStartInSystem() {
        return getTodayStartInSystem().plusDays(1);
    }

    public static long getNowEpochSecond() {
        return Instant.now().getEpochSecond();
    }

    public static long getNowEpochMilli() {
        return Instant.now().toEpochMilli();
    }

    public static ZonedDateTime getNowInZju() {
        return ZonedDateTime.now(ZJU_ZONE_ID);
    }

    public static long secondToMilli(long second) {
        return TimeUnit.SECONDS.toMillis(second);
    }

    public static long milliToSecond(long milli) {
        return TimeUnit.MILLISECONDS.toSeconds(milli);
    }

    public static boolean isInSameDate(ZonedDateTime first, ZonedDateTime second) {
        return first.toLocalDate().equals(second.toLocalDate());
    }

    public static String formatTimestamp(ZonedDateTime zonedDateTime, Context context) {
        String pattern = context.getString(R.string.timestamp_pattern);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return zonedDateTime.format(dateTimeFormatter);
    }

    public static String formatTimestamp(Instant instant, Context context) {
        return formatTimestamp(toZonedDateTimeInSystem(instant), context);
    }

    public String formatTimestamp(long epochSecond, Context context) {
        return formatTimestamp(toZonedDateTimeInSystem(epochSecond), context);
    }

    public static String formatDuration(long seconds, Context context) {
        long days = TimeUnit.SECONDS.toDays(seconds);
        seconds -= TimeUnit.DAYS.toSeconds(days);
        long hours = TimeUnit.SECONDS.toHours(seconds);
        seconds -= TimeUnit.HOURS.toSeconds(hours);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds);
        seconds -= TimeUnit.MINUTES.toSeconds(minutes);
        if (days > 0) {
            return context.getString(R.string.duration_with_day_format, days, hours, minutes,
                    seconds);
        } else {
            return context.getString(R.string.duration_format, hours, minutes, seconds);
        }
    }

    private static String getDateTimeFormat(ZonedDateTime dateTime, LocalDate defaultDate,
                                            Context context) {
        if (dateTime.toLocalDate().equals(defaultDate)) {
            return context.getString(R.string.time_pattern);
        } else if (dateTime.getYear() == defaultDate.getYear()) {
            return context.getString(R.string.month_day_time_pattern);
        } else {
            return context.getString(R.string.date_time_pattern);
        }
    }

    public static String formatPeriod(ZonedDateTime start, ZonedDateTime end, LocalDate defaultDate,
                                      Context context) {
        ZonedDateTime startDayStart = TimeUtils.withDayStart(start);
        if (start.equals(startDayStart)
                && end.equals(startDayStart.plusDays(1))) {
            // The period is a whole day, just return the date.
            String datePattern = context.getString(R.string.month_day_pattern);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
            return start.format(dateFormatter);
        } else {
            String startFormat = getDateTimeFormat(start, defaultDate, context);
            DateTimeFormatter startFormatter = DateTimeFormatter.ofPattern(startFormat);
            // NOTE: We should use the date part of start time as default date for end time.
            String endFormat = getDateTimeFormat(end, start.toLocalDate(), context);
            DateTimeFormatter endFormatter = DateTimeFormatter.ofPattern(endFormat);
            return context.getString(R.string.period_format, start.format(startFormatter),
                    end.format(endFormatter));
        }
    }


    private TimeUtils() {}
}
