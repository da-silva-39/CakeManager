package com.bolosdaaxcila.cakemanager.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MoneyUtils {

    private MoneyUtils() {
    }

    public static BigDecimal subtotal(double unitPrice, int quantity) {
        return BigDecimal.valueOf(unitPrice)
                .multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal total(BigDecimal... subtotals) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal s : subtotals) {
            sum = sum.add(s);
        }
        return sum.setScale(2, RoundingMode.HALF_UP);
    }

    public static String format(double value) {
        return String.format(java.util.Locale.getDefault(), "%.2f MZN", value);
    }
}
