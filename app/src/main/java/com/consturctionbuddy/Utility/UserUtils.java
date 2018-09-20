package com.consturctionbuddy.Utility;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.consturctionbuddy.Bean.LoginResponce;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class UserUtils {

    public static final String FONT_OPEN_SANS_REGULAR = "OpenSans_Regular.ttf";
    public static final String FONT_OPEN_SANS_BOLD = "OpenSans_Bold.ttf";
    private Map<String, Typeface> mFontTypeList;
    public static UserUtils smUserUtils;

    public static UserUtils getInstance() {
        if (smUserUtils == null) {
            smUserUtils = new UserUtils();
        }
        return smUserUtils;
    }

    private void initFontList() {
        if (mFontTypeList == null) {
            mFontTypeList = new HashMap<>();
        }
    }

    public Typeface getTypeFaceForType(Context aContext, String aFontName) {
        initFontList();
        Typeface typeFace = Typeface.DEFAULT;
        if (aFontName != null) {
            Typeface selectedTypeFace = mFontTypeList.get(aFontName);
            if (selectedTypeFace == null) {
                AssetManager am = aContext.getApplicationContext().getAssets();
                Typeface curTypeFace = Typeface.createFromAsset(am,
                        String.format(Locale.US, "fonts/%s", aFontName));
                if (curTypeFace != null) {
                    mFontTypeList.put(aFontName, curTypeFace);
                    typeFace = curTypeFace;
                }
            } else {
                typeFace = selectedTypeFace;
            }
        }
        return typeFace;
    }


    public void setUserId(Context aContext, String aUser) {
        StorageUtils.putPref(aContext, Constant.PREF_USER_ID, aUser);
    }


    public void setFirebaseUID(Context aContext, String aUser) {
        StorageUtils.putPref(aContext, Constant.PREF_USER_INFO_FIREBASE_USER_ID, aUser);
    }

    public String getFireBaseUID(Context aContext) {
        return StorageUtils.getPrefStr(aContext, Constant.PREF_USER_INFO_FIREBASE_USER_ID);
    }


    public void setUserLoggedIn(Context aContext, boolean aLoggedIn) {
        StorageUtils.putPref(aContext, Constant.PREF_USER_LOGGED_IN, aLoggedIn);
    }

    public boolean isUserLoggedIn(Context aContext) {
        return StorageUtils.getPrefForBool(aContext, Constant.PREF_USER_LOGGED_IN);
    }

    public String getUserID(Context aContext) {
        return StorageUtils.getPrefStr(aContext, Constant.PREF_USER_ID);
    }


    public void clearAll(Context aContext) {
        StorageUtils.clearPref(aContext, Constant.PREF_USER_LOGGED_IN);
        StorageUtils.clearPref(aContext, Constant.PREF_USER_INFO);
    }

    public LoginResponce getUserInfo(Context aContext) {
        LoginResponce userInfo = null;
        String jsonStr = StorageUtils.getPrefStr(aContext, Constant.PREF_USER_INFO);
        if (jsonStr != null && jsonStr.length() > 1) {
            Gson gson = new Gson();
            userInfo = gson.fromJson(jsonStr, LoginResponce.class);
        }
        return userInfo;
    }

    public void saveUserInfo(Context aContext, LoginResponce aUserInfo) {
        Gson gson = new Gson();
        String jsonInString = gson.toJson(aUserInfo);
        if (jsonInString != null) {
            StorageUtils.putPref(aContext, Constant.PREF_USER_INFO, jsonInString);
        }
    }

}



