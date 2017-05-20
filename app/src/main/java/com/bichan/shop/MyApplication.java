package com.bichan.shop;

import android.app.Application;
import android.content.ContextWrapper;

import com.pixplicity.easyprefs.library.Prefs;

/**
 * Created by cuong on 5/17/2017.
 */

public class MyApplication extends Application {
    public static final String KEY_SEARCH_HISTORY = "KEY_SEARCH_HISTORY";
    private String userToken = "1qxpogwbnf";
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the Prefs class
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
