package com.example.elena.shopeasy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Absolute on 3/2/2018.
 */

public class PrefUtils {

    private static final String PREF_CONFIRM ="confirmation_dialogues" ;
    private Activity activity;
    private static final String SHARED_PREF_NAME = "ACCOUNT_PREFS";

    private static final String PREF_ACCESS_TOKEN = "fb_access_token";
    private static final String TAG = "utils.PrefUtils";
    private static final String PREF_FIRST_NAME = "fb_first_name";
    private static final String PREF_LAST_NAME = "fb_last_name";
    private static final String PREF_EMAIL = "fb_email";
    private static final String PREF_GENDER = "fb_gender";
    private static final String PREF_PROFILE_URL = "fb_profileURL";

    private static final String SHARED_PREF_SETTINGS = "SETTINGS_PREFS";
    private static final String PREF_FONT_COLOR = "font_color";
    private static final String PREF_BACKGROUND_COLOR = "background_color";

    private static final String PREF_NOTIFY = "notify";
    private static final String PREF_TOP_COUNT = "ingredients_top_count";

    private static final String PREF_SEARCH= "search_option";

    // Constructor
    public PrefUtils(Activity activity) {
        this.activity = activity;

    }

    public void saveAccessToken(String token) {
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(SHARED_PREF_NAME,
                                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PREF_ACCESS_TOKEN, token);
        editor.apply(); // This line is IMPORTANT !!!
    }


    public String getToken() {
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(SHARED_PREF_NAME,
                        Context.MODE_PRIVATE);
        return prefs.getString(PREF_ACCESS_TOKEN, null);
    }

    public void clearToken() {
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(SHARED_PREF_NAME,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        Log.wtf(TAG,"token cleared");
        editor.apply(); // This line is IMPORTANT !!!
    }

    public void saveFacebookUserInfo(String first_name,String last_name, String email, String gender, String profileURL){
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(
                SHARED_PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_FIRST_NAME, first_name);
        editor.putString(PREF_LAST_NAME, last_name);
        editor.putString(PREF_EMAIL, email);
        editor.putString(PREF_GENDER, gender);
        editor.putString(PREF_PROFILE_URL, profileURL);
        editor.apply(); // This line is IMPORTANT !!!
        Log.wtf(TAG, "Shared Name : "+first_name+"\nLast Name : "+last_name
                +"\nEmail : "+email+"\nGender : "+gender+"\nProfile Pic : "+profileURL);
    }

    public void getFacebookUserInfo(){
        SharedPreferences prefs =activity.getApplicationContext().getSharedPreferences(
                SHARED_PREF_NAME,
                Context.MODE_PRIVATE);
        Log.wtf(TAG, "Name : "+prefs.getString("fb_name",null)+"\nEmail : "
                +prefs.getString(PREF_EMAIL,null));
    }

    public String getFacebookUsername(){
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(
                SHARED_PREF_NAME,
                Context.MODE_PRIVATE);
        String firstname = prefs.getString(PREF_FIRST_NAME,"Not");

        String lastname = prefs.getString(PREF_LAST_NAME,"Authenticated");
        Log.wtf(TAG,firstname+" , "+lastname);

        return firstname+" "+lastname;

    }

    public String getFacebookImage(){
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(
                SHARED_PREF_NAME,
                Context.MODE_PRIVATE);
        String image = prefs.getString(PREF_PROFILE_URL,"");

        Log.wtf(TAG,image+" image");

        return image;
    }

    public boolean isAuthenticated(){
        return getFacebookImage().length()!=0;
    }

    public String getEmail(){
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(
                SHARED_PREF_NAME,
                Context.MODE_PRIVATE);

        //   Log.wtf(TAG,image+" image");

        return prefs.getString(PREF_EMAIL,"");
    }

    public String getGender(){
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(
                SHARED_PREF_NAME,
                Context.MODE_PRIVATE);

        //   Log.wtf(TAG,image+" image");

        return prefs.getString(PREF_GENDER,"");
    }

    public boolean saveFontColor(int color){
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(
                SHARED_PREF_SETTINGS,
                Context.MODE_PRIVATE);

        int background = prefs.getInt(PREF_BACKGROUND_COLOR,0);
        if (background==color) return false;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_FONT_COLOR, color);

        editor.commit(); // This line is IMPORTANT !!!
        Log.wtf(TAG, "Shared Font Color : "+color);

        return true;
    }

    public int getFontColor(){
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(
                SHARED_PREF_SETTINGS,
                Context.MODE_PRIVATE);

        return prefs.getInt(PREF_FONT_COLOR,0);
    }

    public boolean saveBackgroundColor(int color){
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(
                SHARED_PREF_SETTINGS,
                Context.MODE_PRIVATE);

        int font = prefs.getInt(PREF_FONT_COLOR,0);
        if (font==color) return false;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_BACKGROUND_COLOR, color);

        editor.commit(); // This line is IMPORTANT !!!
        Log.wtf(TAG, "Shared Font Color : "+color);

        return true;
    }

    public int getBackgroundColor(){
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(
                SHARED_PREF_SETTINGS,
                Context.MODE_PRIVATE);

        return prefs.getInt(PREF_BACKGROUND_COLOR,0);
    }

    public void saveConfirm(boolean confirm){
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(
                SHARED_PREF_SETTINGS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_CONFIRM,confirm);

        editor.commit(); // This line is IMPORTANT !!!
    }

    public boolean getConfirm(){
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(
                SHARED_PREF_SETTINGS,
                Context.MODE_PRIVATE);

        return prefs.getBoolean(PREF_CONFIRM,true);
    }

    public void saveNotify(String notify){
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(
                SHARED_PREF_SETTINGS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_NOTIFY,notify);

        editor.apply(); // This line is IMPORTANT !!!
    }

    public String getNotify(){
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(
                SHARED_PREF_SETTINGS,
                Context.MODE_PRIVATE);

        return prefs.getString(PREF_NOTIFY,"Daily");
    }

    public void saveTopCount(int topCount){
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(
                SHARED_PREF_SETTINGS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_TOP_COUNT,topCount);

        editor.apply(); // This line is IMPORTANT !!!
    }

    public int getTopCount(){
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(
                SHARED_PREF_SETTINGS,
                Context.MODE_PRIVATE);

        return prefs.getInt(PREF_TOP_COUNT,9);
    }

    public void saveSearchOption(String option){
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(
                SHARED_PREF_SETTINGS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_SEARCH, option);

        editor.apply(); // This line is IMPORTANT !!!
    }

    public String getSearchOption(){
        SharedPreferences prefs = activity.getApplicationContext().getSharedPreferences(
                SHARED_PREF_SETTINGS,
                Context.MODE_PRIVATE);

        return prefs.getString(PREF_SEARCH,"By recipe name");
    }

}