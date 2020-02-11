package com.battmobile.battmobiletechnician.utility;

/*
 * Created by rahulkhandelwal on 15/06/17.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.battmobile.battmobiletechnician.login.LoginActivity;


public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "BattmobileTechnician";
    private static final String IS_LOGIN = "IsLoggedIn";

    private String TECHNICIAN_ID = "technician_id";
    private String USER_NAME = "username";
    private String EMAIL = "email";
    private String FIRST_NAME = "first_name";
    private String LAST_NAME = "last_name";
    private String PHONE = "phone";
    private String USER_TYPE = "usertype";
    private String PROFILE_IMAGE = "profile_image";
    private String FIREBASE_TOKEN = "firbase_token";


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void loginUser(String warehouse_id, String username, String email, String first_name, String last_name,
                          String phone, String usertype, String profile_image) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(TECHNICIAN_ID, warehouse_id);
        editor.putString(USER_NAME, username);
        editor.putString(EMAIL, email);
        editor.putString(FIRST_NAME, first_name);
        editor.putString(LAST_NAME, last_name);
        editor.putString(PHONE, phone);
        editor.putString(USER_TYPE, usertype);
        editor.putString(PROFILE_IMAGE, profile_image);
        editor.commit();
    }

    public String getFIREBASE_TOKEN() {
        return pref.getString(FIREBASE_TOKEN, null);
    }

    public void setFIREBASE_TOKEN(String firebase_token) {
        editor.putString(FIREBASE_TOKEN, firebase_token);
        editor.commit();
    }

    public void setFIRST_NAME(String first_name) {
        editor.putString(FIRST_NAME, first_name);
        editor.commit();
    }

    public void setLAST_NAME(String last_name) {
        editor.putString(LAST_NAME, last_name);
        editor.commit();
    }

    public void setPHONE(String phone) {
        editor.putString(PHONE, phone);
        editor.commit();
    }

    public void setPROFILE_IMAGE(String profile_image) {
        editor.putString(PROFILE_IMAGE, profile_image);
        editor.commit();
    }

    public String getTECHNICIAN_ID() {
        return pref.getString(TECHNICIAN_ID, "");
    }

    public String getUSER_NAME() {
        return pref.getString(USER_NAME, "");
    }

    public String getEMAIL() {
        return pref.getString(EMAIL, "");
    }

    public String getFIRST_NAME() {
        return pref.getString(FIRST_NAME, "");
    }

    public String getLAST_NAME() {
        return pref.getString(LAST_NAME, "");
    }

    public String getPHONE() {
        return pref.getString(PHONE, "");
    }

    public String getUSER_TYPE() {
        return pref.getString(USER_TYPE, "");
    }

    public String getPROFILE_IMAGE() {
        return pref.getString(PROFILE_IMAGE, null);
    }

    public void logoutUser() {
        editor.clear();
        editor.apply();
        Intent intent = new Intent(_context, LoginActivity.class);
        intent.putExtra("from", "SplashActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}