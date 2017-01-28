package com.mvs.testapplication.beans;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by User on 1/23/2017.
 */

public class FeatureEarthquake extends RealmObject implements Parcelable{


    @PrimaryKey
    private String id;
    private EarthquakeProperties earthquakeProperties;
    private Geometry geometry;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EarthquakeProperties getEarthquakeProperties() {
        return earthquakeProperties;
    }

    public void setEarthquakeProperties(EarthquakeProperties earthquakeProperties) {
        this.earthquakeProperties = earthquakeProperties;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
