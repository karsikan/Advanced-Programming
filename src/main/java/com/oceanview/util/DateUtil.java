package com.oceanview.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Date utility functions for the reservation system.
 */
public class DateUtil {

    public static long calculateNights(String checkIn, String checkOut) {
        try {
            LocalDate in = LocalDate.parse(checkIn.trim());
            LocalDate out = LocalDate.parse(checkOut.trim());
            return ChronoUnit.DAYS.between(in, out);
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean isCheckOutAfterCheckIn(String checkIn, String checkOut) {
        try {
            LocalDate in = LocalDate.parse(checkIn.trim());
            LocalDate out = LocalDate.parse(checkOut.trim());
            return out.isAfter(in);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDateInFuture(String date) {
        try {
            return LocalDate.parse(date.trim()).isAfter(LocalDate.now().minusDays(1));
        } catch (Exception e) {
            return false;
        }
    }
}
