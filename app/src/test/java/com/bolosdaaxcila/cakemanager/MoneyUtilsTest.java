package com.bolosdaaxcila.cakemanager;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.bolosdaaxcila.cakemanager.utils.MoneyUtils;
import com.bolosdaaxcila.cakemanager.utils.PasswordUtils;

public class MoneyUtilsTest {

    @Test
    public void subtotal_multipliesPriceAndQuantity() {
        assertEquals("2000.00", MoneyUtils.subtotal(1000.0, 2).toPlainString());
    }

    @Test
    public void total_sumsSubtotals() {
        assertEquals("2500.00",
                MoneyUtils.total(MoneyUtils.subtotal(1000.0, 2), MoneyUtils.subtotal(100.0, 5)).toPlainString());
    }

    @Test
    public void passwordHash_isDeterministicAndMatches() {
        String hash = PasswordUtils.hash("segredo123");
        org.junit.Assert.assertTrue(PasswordUtils.matches("segredo123", hash));
        org.junit.Assert.assertFalse(PasswordUtils.matches("outro", hash));
    }
}
