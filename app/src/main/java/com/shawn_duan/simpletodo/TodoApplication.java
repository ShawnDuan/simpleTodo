package com.shawn_duan.simpletodo;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by sduan on 9/28/16.
 */

public class TodoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
