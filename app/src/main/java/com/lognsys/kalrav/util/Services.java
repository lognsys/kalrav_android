package com.lognsys.kalrav.util;

import java.util.regex.Pattern;

/**
 * Created by admin on 4/11/2017.
 */

public class Services {

    private static final String NAME_PATTERN = "^[\\p{L} .'-]+$";
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    /**
     * Test Cases
     * Name: Peter Müller , result : true
     * Name: François Hollande, result : true
     * Name: Patrick O'Brian, result : true
     * Name: Silvana Koch-Mehrin, result: true
     *
     * @param str
     * @return
     */
    public static boolean isNameValid(String str) {

        Pattern pattern = Pattern.compile(NAME_PATTERN);

        if (Pattern.matches(NAME_PATTERN, str.trim()))
            return true;

        return false;
    }

    public static boolean isEmailValid(String str) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);

        if (Pattern.matches(EMAIL_PATTERN, str.trim()))
            return true;

        return false;
    }

    /**
     * Test Cases:
     * Phone number 1234567890 validation result: true
     * Phone number 9999999999 validation result: false
     *
     * @param phoneNo
     * @return
     */
    public static boolean isValidMobileNo(String phoneNo) {

        boolean isValid = true;

        if(!phoneNo.matches("\\d{10}") || !phoneNo.matches("^(\\d)(?!\\1+$)\\d*$"))
            isValid = false;

        return isValid;

    }
    public static boolean isValidZipCode(String zipcode) {

        boolean isValid = true;

        if(!zipcode.matches("\\d{10}") || !zipcode.matches("^(\\d)(?!\\1+$)\\d*$"))
            isValid = false;

        return isValid;

    }
    public static boolean isEmpty(String str) {
        return str.trim().isEmpty();
    }

}
