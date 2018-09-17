package com.example.cupidlove.utils;

import android.support.v4.app.Fragment;

import com.pixplicity.easyprefs.library.Prefs;
import com.example.cupidlove.fragment.ChatListFragment;
import com.example.cupidlove.model.UserFilter;

import java.util.ArrayList;

/**
 * Created by Bhumi Shah on 11/21/2017.
 */

public class Constant {

    public static ChatListFragment chatListFragment;

    public static final int TIMEOUT = 5 * 60 * 1000;

    public static final String MyPREFERENCES = "com.example.cupidlove"; // Set Your Package Here

    public static final String PACKAGE_NAME = "com.example.cupidlove";// Set Your Package Here

    public static final String EMAIL = "";

    public static final int PORT =5222;

    public static String NAME_POSTFIX = "";

    public static String XMPP_USERNAME_POSTFIX = "";

    public static  String XMPP_PASSWORD = "";

    public static  String XMPP_HOST = "";

    public static  String GOOGLE_PLACE_API_KEY = "";

    public static String LANGUAGE = "";

    public static boolean IS_FULL_BUTTON_SHOW = true;

    public static String FRIEND_ID = "";

    public static ArrayList<String> selectedImages = new ArrayList<>();

    public static Fragment CHATLISTFRAGMENT = null;

    public static UserFilter.Body USER_FILTER_BODY = new UserFilter().getBodyObject();

    public static String FRIEND_USER_ID = "";
    public static String FRIEND_FIRST_NAME = "";
    public static String FRIEND_LAST_NAME = "";
    public static String EJUSERID = "";
    public static String FRIEND_PROFILE_PICTURE = "";

    public static boolean IsOnline = false;

    public static int isRtl = 0;

    public static int ad_After_Card = 5;

    public static boolean ISConnectionDone = true;

    public static float LAT;

    public static float LNG;

    public static int ISTOPCLICK = 0;

    public static int UNREADCHAt = Prefs.getInt(RequestParamUtils.UNREADCHAT, 0);

    public static CustomProgressDialog XMPPIALOG;

    public static double currentLatitude;
    public static double currentLongitude;

}
