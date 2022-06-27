package util;

public class Constants {
    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/ABSApp";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;


    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/login";
    public final static String ChargeAccount = FULL_SERVER_PATH + "/Transaction";

}
