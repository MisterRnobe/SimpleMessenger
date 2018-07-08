package server.core;

import common.Errors;
import server.DatabaseConnector;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration {
    public static Map<String, String> register(Map<String, String> map)
    {
        Map<String, String> response = new TreeMap<>();
        response.put("type", "registration");
        try {
            checkLogin(map.get("login"));
            checkPassword(map.get("password"));
            checkName(map.get("name"));
            checkMail(map.get("email"));
            checkInfo(map.get("info"));

            DatabaseConnector.getInstance().addUser(map);
            response.put("status", "OK");

        }
        catch (RequestError e)
        {
            response.put("status","error");
            response.put("code", Integer.toString(e.getCode()));
        }
        return response;
    }
    private static void checkLogin(String login)
    {
        if (login.length() > 15)
            throw new RequestError(Errors.LONG_LOGIN);
        if (login.length() < 5)
            throw new RequestError(Errors.SHORT_LOGIN);
        if (!check(login, "^[A-z0-9_-]{5,15}$"))
            throw new RequestError(Errors.WRONG_LOGIN_SYMBOLS);
        if (DatabaseConnector.getInstance().checkUserExistence("login", login))
            throw new RequestError(Errors.LOGIN_EXISTS);

    }
    private static void checkPassword(String password)
    {
        if (password.length() > 20)
            throw new RequestError(Errors.LONG_PASSWORD);
        if (password.length() < 6)
            throw new RequestError(Errors.SHORT_PASSWORD);
        if (!check(password, "^[A-z0-9_#@!@?+=.-]{6,20}$"))
            throw new RequestError(Errors.WRONG_PASSWORD_SYMBOLS);
    }
    private static void checkName(String name)
    {
        if (name.length() > 50)
            throw new RequestError(Errors.LONG_NAME);
        if (!check(name, "^[A-z0-9А-я ]{0,20}$"))
            throw new RequestError(Errors.WRONG_NAME_SYMBOLS);
    }
    private static void checkMail(String email)
    {
        if (!check(email, "^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$"))
            throw new RequestError(Errors.WRONG_MAIL);
        if (DatabaseConnector.getInstance().checkUserExistence("email", email))
            throw new RequestError(Errors.MAIL_EXISTS);
    }
    private static void checkInfo(String info)
    {
        if (info.length() > 150)
            throw new RequestError(Errors.LONG_INFO);
    }
    private static boolean check(String str, String regex)
    {
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
}
