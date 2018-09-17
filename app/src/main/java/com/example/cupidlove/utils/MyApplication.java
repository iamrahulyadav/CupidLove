package com.example.cupidlove.utils;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.pixplicity.easyprefs.library.Prefs;
import com.example.cupidlove.helper.DatabaseHelper;

/**
 * Created by Kaushal on 17-01-2018.
 */

public class MyApplication extends Application {
    // TODO : Variable Declaration
    public static DatabaseHelper mydb;
    public static boolean activityVisible; //
    public static AppCompatActivity activity;

    @Override
    public void onCreate() {
        MultiDex.install(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate();
        mydb = new DatabaseHelper(this);
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(Constant.MyPREFERENCES)
                .setUseDefaultSharedPreference(true)
                .build();

    }
    public static boolean isActivityVisible() {
        return activityVisible; // return true or false
    }

    public static void activityResumed() {
        activityVisible = true;// this will set true when activity resumed

    }





    public static void activityPaused() {
        activityVisible = false;// this will set false when activity paused

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
