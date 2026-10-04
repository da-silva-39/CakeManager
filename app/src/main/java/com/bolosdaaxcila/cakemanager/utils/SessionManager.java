package com.bolosdaaxcila.cakemanager.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "cakemanager_session";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveSession(long userId, String name) {
        prefs.edit().putLong(KEY_USER_ID, userId).putString(KEY_USER_NAME, name).apply();
    }

    public long getUserId() {
        return prefs.getLong(KEY_USER_ID, -1);
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "");
    }

    public boolean isLoggedIn() {
        return getUserId() != -1;
    }

    public void logout() {
        prefs.edit().clear().apply();
    }
}
