package com.quixom.jewelrap.Global;

import org.json.JSONObject;

public class Global_variable {

    public static final String GOOGLE_PROJECT_ID = "408379477416";
    //     public static String base_url = "http://192.168.5.17:8000";
    public static final String MESSAGE_KEY = "message";
    public static final String INQUIRY_ID = "inquiry_id";
    public static final String BADGE = "badge";
    public static final String NEWS = "news_id";
    public static int COUNT = 0;


    //public static String base_url = "http://jewelrap.com";

//    public static String base_url = "http://developer.jewelrap.com";

   // public static String base_url = "http://jewelrap.com";

    public static String base_url = "http://jewelrap.affixwebsolution.com";

    public static String login_api = base_url + "/user/login/";
    public static String signup_api = base_url + "/user/signup/";
    public static String chagepassword_api = base_url + "/user/change_password/";
    public static String get_categories_api = base_url + "/api/v1/category/";
    public static String get_categories_api_new = base_url + "/api/v1/inventory/";
    public static String inquiry_api = base_url + "/product/inquiry/";
    public static String news_api = base_url + "/api/v1/news/";
    public static String inquiry_details_api = base_url + "/api/v1/inquiry/";
    public static String current_demand_api = inquiry_details_api + "?current_demand=yes";
    public static String myinquiry_details_api = base_url + "/api/v1/myinquiry/";
    public static String feedback_api = base_url + "/product/feedback/";
    public static String logout_api = base_url + "/user/logout/";
    public static String profile_api = base_url + "/user/editprofile/";
    public static String get_profile_api = base_url + "/api/v1/user/";
    public static String badge_api = base_url + "/user/userbadge/";
    public static String search_api = base_url + "/api/v1/search/";
    public static String state_api = base_url + "/api/v1/state/";
    public static String city_api = base_url + "/api/v1/city/?state";
    public static String contact_us_api = base_url + "/api/v1/contact/";
    public static String new_delete_api = base_url + "/product/deactive_news/";

    public static String change_profile_api = base_url + "/user/change_profileimg/";
    public static String ld_categories_api = base_url + "/api/v1/ld_category/2/";
    public static String ld_search_api = base_url + "/api/v1/ld_search/";
    public static String invetory_api = base_url + "/api/v1/inventory_details/?limit=1000";
    public static String invetory_api_details = base_url + "/api/v1/inventory_details/";
    public static int maxread_size = 1024;
    public static String LOG_TAG = "JEWELRAP";
    public static String google_key = "AIzaSyCPrliyInSk_3q5VFaf4ltTM7FsoCQQ324";
    public static JSONObject searchResponse;
    public static boolean logEnabled = false;
    public static int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 112;
}
