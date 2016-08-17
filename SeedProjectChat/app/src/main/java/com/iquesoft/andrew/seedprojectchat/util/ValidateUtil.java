package com.iquesoft.andrew.seedprojectchat.util;

/**
 * Created by Andrew on 17.08.2016.
 */

public class ValidateUtil {
    public boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
