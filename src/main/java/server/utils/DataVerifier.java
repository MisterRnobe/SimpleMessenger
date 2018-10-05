package server.utils;

import common.Errors;
import server.database.DatabaseConnectorOld;
import server.core.HandleError;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataVerifier {
    public static void checkLogin(String login)
    {
        if (login.length() > 15)
            throw new HandleError(Errors.LONG_LOGIN);
        if (login.length() < 5)
            throw new HandleError(Errors.SHORT_LOGIN);
        if (!check(login, "^[A-z0-9_-]{5,15}$"))
            throw new HandleError(Errors.WRONG_LOGIN_SYMBOLS);
        if (DatabaseConnectorOld.getInstance().checkUserExistence("login", login))
            throw new HandleError(Errors.LOGIN_EXISTS);

    }
    public static void checkPassword(String password)
    {
        if (password.length() > 20)
            throw new HandleError(Errors.LONG_PASSWORD);
        if (password.length() < 6)
            throw new HandleError(Errors.SHORT_PASSWORD);
        if (!check(password, "^[A-z0-9_#@!@?+=.-]{6,20}$"))
            throw new HandleError(Errors.WRONG_PASSWORD_SYMBOLS);
    }
    public static void checkName(String name)
    {
        if (name.length() > 50)
            throw new HandleError(Errors.LONG_NAME);
        if (!check(name, "^[A-z0-9А-я ]{0,20}$"))
            throw new HandleError(Errors.WRONG_NAME_SYMBOLS);
    }
    public static void checkMail(String email)
    {
        if (!check(email, "^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$"))
            throw new HandleError(Errors.WRONG_MAIL);
        if (DatabaseConnectorOld.getInstance().checkUserExistence("email", email))
            throw new HandleError(Errors.MAIL_EXISTS);
    }
    public static void checkInfo(String info)
    {
        if (info != null && info.length() > 150)
            throw new HandleError(Errors.LONG_INFO);
    }
    private static boolean check(String str, String regex)
    {
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
}
