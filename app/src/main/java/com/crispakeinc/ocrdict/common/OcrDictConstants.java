package com.crispakeinc.ocrdict.common;


import android.graphics.Color;

public class OcrDictConstants {
    /*------------------------------------------------------------ Web services related begin -----------------------------------------------------------------------*/
    public static final int MAP_ZOOM_LEVEL_15 = 15;
    public static final Integer POINT_OFFSET_AGAINST_LINT = 25;
    public static final String OCR_DICT_PREFERRENCES = "OCR_DICT_PREFERRENCES";




    public static Class answerDetailWithLocationsActivity;
    public static Class askQuestionActivity;
    public static Class publishAnswerActivity;
    public static Class questionLocationActivity;
    public static Class signInActivity;
    public static Class mapFragmentForHome;

    public static final String WHERESK_BROADCAST_RECEIVER_INTENT = "WHERESK_BROADCAST_RECEIVER_INTENT";
    public static final String KEY_TYPE_BROADCAST_RECEIVER = "KEY_TYPE_BROADCAST_RECEIVER";
    public static final String WHERESK_PACKAGE_NAME = "com.crispcake.wheresk.android";
    public static final String WHERESK_SHARED_PREFERRENCES = "WHERESK_SHARED_PREFERRENCES";
    public static final String SHARED_PREFERRENCE_FIRST_LAUNCH_INDICATOR = "SHARED_PREFERRENCE_FIRST_LAUNCH_INDICATOR";
    public static final String MOBILE_INFO_DELIMITER = ",,,,,,.....";
    public static final int SMALL_SCREEN_WIDTH = 350;
    public static final String SHARED_PREFERRENCE_DEBUG_ENABLED = "SHARED_PREFERRENCE_DEBUG_ENABLED";
    public static final Boolean DEFAULT_DEBUG_ENABLED = false;
    public static final int TIME_OUT_FOR_SERVER_CONNECTION = 50000;
    public static final int SHORT_TIME_OUT_FOR_SERVER_READ_DATA = 30000;
    public static final int LONG_TIME_OUT_FOR_SERVER_READ_DATA = 50000;
    public static final String GCM_RECEIVED_MESSAGE_TYPE = "GCM_RECEIVED_MESSAGE_TYPE"; //The value should be consistent with server
    public static final String KEY_LOCATION_TIME_OUT_INDICATOR = "KEY_LOCATION_TIME_OUT_INDICATOR";
    public static final float[] MAP_MARKER_ANCHOR = {0.5f, 1.0f};
    public static final float[] MAP_MARKER_CENTER_ANCHOR = {0.5f, 0.5f};
    public static final long LOCATION_TIMEOUT_CHECK_INTERVAL = 2000l;
    public static final long GET_LOCATION_TIMEOUT = 40000l;
    public static final String GET_MY_LOCATION_DATA = "GET_MY_LOCATION_DATA";
    public static final String IS_SHORTCUT_ICON_CREATED = "IS_SHORTCUT_ICON_CREATED";
    public static final String FIRST_TIME_REFRESH_GROUP_LOCATION_ACTIVITY = "FIRST_TIME_REFRESH_GROUP_LOCATION_ACTIVITY";
    public static final int METERS_BETW_CUR_LOC_AND_DIST_NUMBER = 500;
    public static final double MIN_DIST_DIS_VALUE = 20;
    public static final int MIN_DIST_DISP_CONTACT_NAME = 50;

    /*Log Related*/
    public static final String LOG_WHERESK_ANDROID_TAG = "LOG_WHERESK_ANDROID_TAG";

    public static final String SERVER_WS_URL = "/webservice";
    public static final String GET_HOME_PAGE_LIST_RESPONSE_URL = SERVER_WS_URL + "/question/getQuestionsByLocation";
    public static final String GET_FOLLOWED_QUESTION_PAGE_LIST_RESPONSE_URL = SERVER_WS_URL + "/question/getFollowedQuestions";
    public static final String GET_USER_QUESTIONS_URL = SERVER_WS_URL + "/question/getUserQuestions";
    public static final String GET_USER_ANSWERED_QUESTIONS_URL = SERVER_WS_URL + "/question/getUserAnsweredQuestions";
    public static final String GET_LIST_OF_ANSWER_RESPONSE_URL = SERVER_WS_URL + "/question/getAnswersByQuestionId";
    public static final String FOLLOW_QUESTION_URL = SERVER_WS_URL + "/question/followQuestion";
    public static final String UNFOLLOW_QUESTION_URL = SERVER_WS_URL + "/question/unfollowQuestion";
    public static final String RATE_ANSWER_URL = SERVER_WS_URL + "/answer/rateAnswer";
    public static final String GET_ANSWER_DETAIL_RESPONSE_URL = SERVER_WS_URL + "/answer/getAnswerDetailByAnswerId";
    public static final String THANK_ANSWER_URL = SERVER_WS_URL + "/answer/thanks";
    public static final String GET_ANSWER_COMMENT_LIST_URL = SERVER_WS_URL + "/answer/getAnswerCommentList";
    public static final String INSERT_ANSWER_COMMENT_URL = SERVER_WS_URL + "/answer/insertAnswerComment";
    public static final String INSERT_MODIFY_QUESTION_URL = SERVER_WS_URL + "/question/insertModifyQuestion";
    public static final String PUBLISH_ANSWER_URL = SERVER_WS_URL + "/answer/publishAnswer";
    public static final String REGISTER_URL = SERVER_WS_URL + "/user/register";
    public static final String GET_USER_URL = SERVER_WS_URL + "/user/getUserById";
    public static final String GET_USER_PROFILE_URL = SERVER_WS_URL + "/user/getUserProfile";
    public static final String XG_SERVER_PUSH_REGISTRATION_URL = SERVER_WS_URL + "/xgpush/register";
    public static final String GET_USER_INFO_LIST_FOR_ANSWER_DETAIL_PAGE_URL = SERVER_WS_URL + "/answer/getUserInfoListForAnswerDetailPage";
    public static final String GET_USER_INFO_LIST_FOR_PROFILE_PAGE_URL = SERVER_WS_URL + "/user/getUserInfoListForProfilePage";
    public static final String GET_USER_INFO_LIST_FOR_QUESTION_FOLLOWERS_PAGE_URL = SERVER_WS_URL + "/question/getUserInfoListForQuestionFollowersPage";
    public static final String GET_QUESTIONS_BY_GEO_BOUNDS = SERVER_WS_URL + "/question/getQuestionsByGeoBounds";
    public static final String DELETE_ANSWER_URL = SERVER_WS_URL + "/answer/deleteAnswer";

    public static final Integer NUMBER_OF_PAGE_OFFSET = 25;
    public static final String JPG_FILE_SUFFIX = ".jpg";

    public static final String PAGE_JSON_STRING_HOME = "PAGE_JSON_STRING_HOME";
    public static final String PAGE_JSON_STRING_FOLLOWED_QUESTION = "PAGE_JSON_STRING_FOLLOWED_QUESTION";
    public static final String PAGE_JSON_STRING_USER_QUESTIONS_PREFIX = "PAGE_JSON_STRING_USER_QUESTIONS_PREFIX";
    public static final String PAGE_JSON_STRING_USER_ANSWERED_QUESTIONS_PREFIX = "PAGE_JSON_STRING_USER_ANSWERED_QUESTIONS_PREFIX";
    public static final String PAGE_JSON_STRING_QUESTION_PREFIX = "PAGE_JSON_STRING_QUESTION_PREFIX";
    public static final String PAGE_JSON_STRING_ANSWER_PREFIX = "PAGE_JSON_STRING_ANSWER_PREFIX";
    public static final String PAGE_JSON_STRING_ANSWER_COMMENTS_PREFIX = "PAGE_JSON_STRING_ANSWER_COMMENTS_PREFIX";
    public static final String PAGE_JSON_STRING_ANSWER_THANK_IN_ANSWER_PREFIX = "PAGE_JSON_STRING_ANSWER_THANK_IN_ANSWER_PREFIX";
    public static final String PAGE_JSON_STRING_ANSWER_THANK_IN_PROFILE_PREFIX = "PAGE_JSON_STRING_ANSWER_THANK_IN_PROFILE_PREFIX";
    public static final String PAGE_JSON_STRING_QUESTION_FOLLOWERS_PREFIX = "PAGE_JSON_STRING_QUESTION_FOLLOWERS_PREFIX";

    public static final String KEY_QUESTION_ID = "KEY_QUESTION_ID";
    public static final String KEY_QUESTION_TEXT = "KEY_QUESTION_TEXT";
    public static final String KEY_QUESTION_DISTANCE_STRING = "KEY_QUESTION_DISTANCE_STRING";
    public static final String KEY_ANSWER_ID = "KEY_ANSWER_ID";
    public static final String KEY_NEED_TO_REFRESH = "KEY_NEED_TO_REFRESH";
    public static final String KEY_LAT = "KEY_LAT";
    public static final String KEY_LNG = "KEY_LNG";
    public static final String KEY_USER_NAME = "KEY_USER_NAME";
    public static final String KEY_USER_PHOTO_ICON = "KEY_USER_PHOTO_ICON";
    public static final String KEY_ANSWER_DETAIL_RESPONSE = "KEY_ANSWER_DETAIL_RESPONSE";
    public static final String KEY_ANSWER_HAS_LOCATION = "KEY_ANSWER_HAS_LOCATION";
    public static final String KEY_QUESTION_CREATED_DATE_AGO = "KEY_QUESTION_CREATED_DATE_AGO";

    public static final int MAP_ZOOM_LEVEL = 19;

    public static final String WS_PARAM_USER_TOKEN = "userToken";
    public static final String WS_PARAM_ANSWER_ID = "answerId";
    public static final String WS_PARAM_TARGET_USER_TOKEN = "targetUserToken";
    public static final String WS_PARAM_COMMENT = "comment";
    public static final String WS_PARAM_QUESTION_TEXT = "questionText";
    public static final String WS_PARAM_QUESTION_ID = "questionId";
    public static final String WS_PARAM_ANSWER_DETAIL_REQUEST_JSON = "answerDetailRequestJson";
    public static final String WS_PARAM_LAT = "lat";
    public static final String WS_PARAM_LNG = "lng";
    public static final String WS_PARAM_ZOOM_LEVEL = "zoomLevel";

    public static final String WS_SIGNIN_ID_TOKEN = "signInIdToken";
    public static final String WS_USER_DISPLAY_NAME = "userDisplayName";
    public static final String WS_USER_PHOTO_URL = "userPhotoUrl";
    public static final String WS_USER_EMAIL = "userEmail";
    public static final String WS_ENUM_REG_TYPE = "enumRegType";

    public static final Integer MAX_PAGE_LOAD_OFFSET = 20000;

    public static final String KEY_INCLUDE_QUESTION_MARKER = "KEY_INCLUDE_QUESTION_MARKER";
    public static final int MY_LOCATION_CLUSTER_GROUP_ID = 100;
    public static final String LOGGED_IN_TOKEN = "LOGGED_IN_TOKEN";
    public static final String LOGGED_IN_USER_ID_ON_SERVER = "LOGGED_IN_USER_ID_ON_SERVER";

    public static final int MAP_CAMERA_ZOOM_SPEED_MS = 700;
    public static final String XG_PUSH_SERVICE_TOKEN = "XG_PUSH_SERVICE_TOKEN";

    public static final String GCM_MESSAGE_FOLLOWED_QUESTION_ANSWERED = "GCM_MESSAGE_FOLLOWED_QUESTION_ANSWERED";
    public static final String GCM_MESSAGE_THANK_USER_ANSWER = "GCM_MESSAGE_THANK_USER_ANSWER";
    public static final String GCM_MESSAGE_ANSWER_COMMENT = "GCM_MESSAGE_ANSWER_COMMENT";

    public static final String KEY_FOLLOWED_QUESTION_INDICATOR = "KEY_FOLLOWED_QUESTION_INDICATOR";
    public static final java.lang.String KEY_USER_ID = "KEY_USER_ID";


    public static final String GCM_ANSWER_HAS_LOCATION = "GCM_ANSWER_HAS_LOCATION";
    public static final String GCM_USER_DISPLAY_NAME = "GCM_USER_DISPLAY_NAME";
    public static final String GCM_ANSWER_ID = "GCM_ANSWER_ID";
    public static final String GCM_ANSWER_SUMMARY = "GCM_ANSWER_SUMMARY";
    public static final String GCM_QUESTION_ID = "GCM_QUESTION_ID";
    public static final String GCM_QUESTION_TEXT = "GCM_QUESTION_TEXT";
    public static final String GCM_QUESTION_LAT = "GCM_QUESTION_LAT";
    public static final String GCM_QUESTION_LNG = "GCM_QUESTION_LNG";
    public static final String GCM_ANSWER_COMMENT = "GCM_ANSWER_COMMENT";

    public static final int BLUE_LINE = Color.argb(200, 51, 181, 229);
    public static final int BLUE_FILL = Color.argb(20, 51, 181, 229);

    public static final int MAX_MARKER_NUM = 1000;

    public static final String NOTIFICATION_WHEN_NEW_ANSWER_PROVIDED_ENABLE_KEY = "NOTIFICATION_WHEN_NEW_ANSWER_PROVIDED_ENABLE_KEY";
    public static final Boolean NOTIFICATION_WHEN_NEW_ANSWER_PROVIDED_ENABLE_KEY_DEFAULT_VALUE = true;
    public static final String NOTIFICATION_WHEN_RECEIVE_THANK_ENABLE_KEY = "NOTIFICATION_WHEN_RECEIVE_THANK_ENABLE_KEY";
    public static final Boolean NOTIFICATION_WHEN_RECEIVE_THANK_ENABLE_KEY_DEFAULT_VALUE = true;
    public static final String NOTIFICATION_WHEN_RECEIVE_COMMENTS_ENABLE_KEY = "NOTIFICATION_WHEN_RECEIVE_COMMENTS_ENABLE_KEY";
    public static final Boolean NOTIFICATION_WHEN_RECEIVE_COMMENTS_ENABLE_KEY_DEFAULT_VALUE = false;
}
