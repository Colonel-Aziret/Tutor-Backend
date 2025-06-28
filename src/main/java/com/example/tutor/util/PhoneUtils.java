package com.example.tutor.util;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneUtils {
    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    public static String extractCountryCode(String fullPhone) {
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(fullPhone, null);
            return "+" + number.getCountryCode();
        } catch (Exception e) {
            return null;
        }
    }

    public static String extractNationalNumber(String fullPhone) {
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(fullPhone, null);
            return String.valueOf(number.getNationalNumber());
        } catch (Exception e) {
            return fullPhone;
        }
    }
}
