package com.example.tutor.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Main {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        BCryptPasswordEncoder passwordEncoder2 = new BCryptPasswordEncoder();

        String encodedPasswordBCrypt = passwordEncoder.encode("UHMhuXdEPm7Hqz");
        String encodedPasswordBCrypt2 = passwordEncoder.encode("UHMhuXdEPm7Hqz");
        boolean result = encodedPasswordBCrypt.equals(encodedPasswordBCrypt2);
        boolean decodePasswordBCrypt = passwordEncoder2.matches("UHMhuXdEPm7Hqz", encodedPasswordBCrypt);

        System.out.println("encodedPasswordBCrypt = " + encodedPasswordBCrypt); // encode result
        System.out.println("encodedPasswordBCrypt2 = " + encodedPasswordBCrypt2); // encode result
        System.out.println("decodePasswordBCrypt = " + decodePasswordBCrypt); // true or false
        System.out.println("result = " + result); // true or false
    }
}
