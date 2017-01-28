package com.mvs.testapplication;

import android.app.Application;

import com.mvs.testapplication.utils.Utility;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 *
 */

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utility.Logger.i(Utility.APP_LOG_TAG,"Application Classs On create");
        Realm.init(this);
        RealmConfiguration realmConfiguration=new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
