package com.android.indie.school.mylocalizationdemo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by herisulistiyanto on 12/13/16.
 */

public class MyApp extends Application implements ConstantPref {

    private static MyApp instance;

    @Override
    public void onCreate() {
        instance = this;
        updateLanguage(this);
        super.onCreate();
    }

    @SuppressWarnings("deprecation")
    public void updateLanguage(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String lang = sharedPreferences.getString(LANG_KEY, "en");

        Configuration config = new Configuration();
        if (!TextUtils.isEmpty(lang)) {
            Locale locale = new Locale(lang);
            config.setLocale(locale);
        } else {
            config.setLocale(Locale.getDefault());
        }
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static MyApp getInstance() {
        return instance;
    }
}
