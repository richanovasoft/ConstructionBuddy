package com.consturctionbuddy.Utility;


public class Constant {

    public static final int MIN_PASSWORD_LENGTH = 5;

    public static final int APPLICATION_BACK_EXIT_COUNT = 1;

    public static final int APPLICATION_BACK_COUNT = 0;

    public static final int GP_SIGN_IN = 9001;
    public static final int SPLASH_TIME_OUT = 3 * 1000;

    public static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    public static final int MAX_PRODUCT_IMAGE_DISPLAY = 4;

    public static final String INTENT_IMAGE_SELECTED_INDEX_KEY = "image_selected_index";
    public static final String INTENT_IMAGE_LIST_INDEX_KEY = "image_list";
    public static final int MY_PERMISSIONS_REQUEST_FOR_EXTERNAL_STORAGE = 2000;
    public static final int GALLERY_CAPTURE_IMAGE_REQUEST_CODE = 12452;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1337;


    //======================Shared Preference ======================
    public static final String PREF_USER_LOGGED_IN = "user_login";
    public static final String PREF_USER_ID = "user_id";
    public static final String PREF_TOKEN_ID = "token_id";
    public static final String PREF_USER_INFO = "user_info";

    public static final String PREF_FIRST_LAUNCH = "skipIntro";
    public static final String PREF_SKIP_USER_ACCESS = "skipAccess";

    public static final String NOTIFICATION_COUNTER_VALUE_KEY = "notify_counter";

    public static final String PREF_USER_INFO_FIREBASE_USER_ID = "firebase_uid";

    public static final String SHARED_PREF_FIREBASE_KEY = "ah_firebase";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String TOPIC_GLOBAL = "global";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String NOTIFICATION_TYPE_KEY = "type";
    public static final String NOTIFICATION_ID_KEY = "id";
    public static final String NOTIFICATION_TITLE_KEY = "title";
    public static final String NOTIFICATION_MSG_KEY = "body";
    public static final String NOTIFICATION_IMAGE_URL_KEY = "image";
    public static final String INTENT_NOTIFICATION_TYPE = "type";
    public static final String INTENT_NOTIFICATION_ID = "id";
    public static final String INTENT_NOTIFICATION_TITLE = "title";
    public static final String INTENT_NOTIFICATION_MSG = "message";
    public static final String INTENT_NOTIFICATION_IMAGE_URL = "image";

    //FaceBook
    public static final String FB_PERMISSION_PROFILE = "public_profile";
    public static final String FB_PERMISSION_EMAIL = "email";
    public static final String FB_PERMISSION_BIRTHDAY = "user_birthday";
    public static final String FB_PERMISSION_LOCATION = "user_location";
    public static final String FB_PERMISSION_PUBLISH_ACTIONS = "publish_actions";

    public static final String FACEBOOK_PERMISSION_STR = FB_PERMISSION_PROFILE + "," +
            FB_PERMISSION_EMAIL + "," +
            FB_PERMISSION_BIRTHDAY + "," +
            FB_PERMISSION_PUBLISH_ACTIONS + "," +
            FB_PERMISSION_LOCATION;

    public static final String FB_FIELD_ID = "id";
    public static final String FB_FIELD_NAME = "name";
    public static final String FB_FIELD_FIRST_NAME = "first_name";
    public static final String FB_FIELD_LAST_NAME = "last_name";
    public static final String FB_FIELD_EMAIL = "email";
    public static final String FB_FIELD_GENDER = "gender";
    public static final String FB_FIELD_BIRTHDAY = "birthday";
    public static final String FB_FIELD_LOCATION = "location";
    public static final String FB_FIELD_COVER = "cover";
    public static final String FB_FIELD_PICTURE_TYPE = "picture.type(large)";
    public static final String FB_FIELD_PHONE = "phone";

    public static final String FB_FIELDS_STR = FB_FIELD_ID + "," +
            FB_FIELD_NAME + "," +
            FB_FIELD_FIRST_NAME + "," +
            FB_FIELD_LAST_NAME + "," +
            FB_FIELD_EMAIL + "," +
            FB_FIELD_GENDER + "," +
            FB_FIELD_BIRTHDAY + "," +
            FB_FIELD_LOCATION + "," +
            FB_FIELD_COVER + "," +
            FB_FIELD_PICTURE_TYPE;


    public static String[] name = {"Dashboard",
            "My Account",
            "Staff Management",
            "Site Image Management",
            "Chat management",
            "Logout"
    };
    public static String[] subName = {"google", "Motorola", "Samsung", "Lenevo"};


    public static final String LOGIN_USERNAME_KEY = "email";
    public static final String LOGIN_PASSWORD_KEY = "password";


    private static final String API_BASE_URL_DEV = "http://ec2-34-235-127-11.compute-1.amazonaws.com:5555";
    private static final String API_BASE_URL = API_BASE_URL_DEV;


    private static final String API_LOGIN_METHOD = "/loginApi.php";
    public static final String API_LOGIN = API_BASE_URL + API_LOGIN_METHOD;


    private static final String FORGOT_PASSWORD = "/forgetPassApi.php";
    public static final String API_FORGOT_PASSWORD = API_BASE_URL + FORGOT_PASSWORD;


    private static final String CHANGE_PASSWORD = "/changePassApi.php";
    public static final String API_CHANGE_PASSWORD = API_BASE_URL + CHANGE_PASSWORD;


    private static final String HOME_NOTIFICATION = "/updateNotificationApi.php";
    public static final String API_HOME_NOTIFICATION = API_BASE_URL + HOME_NOTIFICATION;

    private static final String SERVICE_REQUEST = "/ServiceFetchApi.php";
    public static final String API_SERVICE_REQUEST = API_BASE_URL + SERVICE_REQUEST;


    private static final String PAYMENT_API = "/paymentApi.php";
    public static final String API_PAYMENT_API = API_BASE_URL + PAYMENT_API;


    private static final String ADD_SERVICE = "/LeaveRequestApi";
    public static final String APPLY_LEAVES_API = API_BASE_URL + ADD_SERVICE;


    private static final String ADD_MEAL = "/updateMealApi.php";
    public static final String API_ADD_MEAL = API_BASE_URL + ADD_MEAL;


    private static final String UPDATE_PROFILE = "/userUpdateApi.php";
    public static final String API_UPDATE_PROFILE = API_BASE_URL + UPDATE_PROFILE;


    private static final String UPDATE_PROFILE_RELATION = "/emergencyUpdateApi.php";
    public static final String API_UPDATE_PROFILE_RELATION = API_BASE_URL + UPDATE_PROFILE_RELATION;


    private static final String EVENT_LIST = "/LeaveListApi";
    public static final String API_EVENT_LIST = API_BASE_URL + EVENT_LIST;

}
