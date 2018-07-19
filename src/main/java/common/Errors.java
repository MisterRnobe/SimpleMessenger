package common;

import common.objects.Body;

public class Errors {
    //RegistrationHandler
    //Errors related to login
    public static final int SHORT_LOGIN = 0;
    public static final int LONG_LOGIN = 1;
    public static final int LOGIN_EXISTS = 2;
    public static final int WRONG_LOGIN_SYMBOLS = 3;

    //Errors related to password
    public static final int SHORT_PASSWORD = 10;
    public static final int LONG_PASSWORD = 11;
    public static final int WRONG_PASSWORD_SYMBOLS = 12;

    //Errors related to name
    public static final int LONG_NAME = 20;
    public static final int WRONG_NAME_SYMBOLS = 21;

    // --/-- to email
    public static final int WRONG_MAIL = 30;
    public static final int MAIL_EXISTS = 31;

    // --/-- to info
    public static final int LONG_INFO = 40;
    //End registration


    //AuthorizationHandler
    public static final int NOT_EXIST = 50;
    public static final int BAD_PASSWORD = 51;


    //Common
    public static final int WRONG_REQUEST_PARAMETERS = 1000;
    public static final int INTERNAL_ERROR = 1001;
    public static final int EMPTY_BODY = 1002;


}
