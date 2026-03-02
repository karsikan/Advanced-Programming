package com.oceanview.util;

import java.util.regex.Pattern;

/**
 * Input validation utilities.
 */
public class Validator {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidDate(String date) {
        if (date == null || !DATE_PATTERN.matcher(date.trim()).matches())
            return false;
        try {
            java.time.LocalDate.parse(date.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isPositiveNumber(String value) {
        try {
            return Double.parseDouble(value) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidReservationId(String id) {
        return id != null && id.trim().toUpperCase().startsWith("OVR-");
    }
}
