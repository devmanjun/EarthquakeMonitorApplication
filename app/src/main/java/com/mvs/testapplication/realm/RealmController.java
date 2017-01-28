package com.mvs.testapplication.realm;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;

import com.mvs.testapplication.beans.EarthquakeProperties;
import com.mvs.testapplication.beans.FeatureEarthquake;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by User on 1/24/2017.
 */

public class RealmController {

    private static RealmController instance;
    private final Realm realm;


    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();

    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }



    public void clearAll() {

        realm.beginTransaction();
        realm.delete(FeatureEarthquake.class);
        realm.commitTransaction();
    }

    public RealmResults<FeatureEarthquake> getAllEarthQuakes() {

        return realm.where(FeatureEarthquake.class).findAll();
    }

    //query a single item with the given id
    public FeatureEarthquake getEarthQuakeDetails(String id) {

        return realm.where(FeatureEarthquake.class).equalTo("id", id).findFirst();
    }


    //check if Book.class is empty
   /* public boolean hasBooks() {

        return !realm.allObjects(Book.class).isEmpty();
    }

    //query example
    public RealmResults<Book> queryedBooks() {

        return realm.where(Book.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();

    }*/
}
