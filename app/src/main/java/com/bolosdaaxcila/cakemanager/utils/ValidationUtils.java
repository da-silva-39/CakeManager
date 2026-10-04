package com.bolosdaaxcila.cakemanager.utils;

import android.util.Patterns;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        String digits = phone.replaceAll("[^0-9]", "");
        return digits.length() >= 9 && digits.length() <= 15;
    }

    public static boolean isPositive(String value) {
        try {
            return Double.parseDouble(value.trim().replace(',', '.')) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNonNegative(String value) {
        try {
            return Double.parseDouble(value.trim().replace(',', '.')) >= 0;
        } catch (Exception e) {
            return false;
        }
    }
}
