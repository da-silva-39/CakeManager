package com.bolosdaaxcila.cakemanager;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.bolosdaaxcila.cakemanager.utils.ValidationUtils;

public class ValidationUtilsTest {

    @Test
    public void phone_validatesDigitCount() {
        assertTrue(ValidationUtils.isValidPhone("+258 84 123 4567"));
        assertFalse(ValidationUtils.isValidPhone("123"));
        assertFalse(ValidationUtils.isValidPhone(null));
    }

    @Test
    public void positive_checksNumbers() {
        assertTrue(ValidationUtils.isPositive("10.5"));
        assertTrue(ValidationUtils.isPositive("10,5"));
        assertFalse(ValidationUtils.isPositive("0"));
        assertFalse(ValidationUtils.isPositive("abc"));
    }
}
