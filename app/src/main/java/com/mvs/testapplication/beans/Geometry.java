package com.mvs.testapplication.beans;

import io.realm.RealmObject;

/**
 *
 */

public class Geometry extends RealmObject{

    private double latutitude;
    private double longitude;
    private  int radius;

    public double getLatutitude() {
        return latutitude;
    }

    public void setLatutitude(double latutitude) {
        this.latutitude = latutitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
