package com.battmobile.battmobiletechnician.utility;

import android.text.TextUtils;
import android.util.Patterns;


public class FormUtils {
    public static boolean checkEmail(String target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean checkPassword(String target) {
        return !TextUtils.isEmpty(target);
    }

    public static boolean checkPhone(String target) {
       // return target.length() == 10;
        return target.length() >= 1;
    }

    public static boolean checkPhoneText(String text) {
        return text.matches("[0-9]+");
    }

    public static boolean checkCharacter(String text) {
        return text.matches("[A-Za-z]+");
    }

    public static boolean checkTarget(String target) {
        return TextUtils.isEmpty(target);
    }

    public static boolean checkMinPasswordLength(String target) {
        return (target.length() >= 6);
    }

    public static boolean checkMaxPasswordLength(String target) {
        return (target.length() < 15);
    }

}
