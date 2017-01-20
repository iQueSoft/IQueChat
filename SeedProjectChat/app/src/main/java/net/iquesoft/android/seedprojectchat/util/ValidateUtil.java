package net.iquesoft.android.seedprojectchat.util;

public class ValidateUtil {
    public boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }
}
