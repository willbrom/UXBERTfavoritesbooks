package com.example.uxbertfavoritesbooks;

import android.app.Application;

import com.example.uxbertfavoritesbooks.utils.MyNotification;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyNotification.createNotificationChannel(this);
    }
}
