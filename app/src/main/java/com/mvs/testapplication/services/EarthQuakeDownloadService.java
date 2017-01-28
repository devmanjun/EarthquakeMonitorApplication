package com.mvs.testapplication.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.mvs.testapplication.R;
import com.mvs.testapplication.beans.EarthquakeProperties;
import com.mvs.testapplication.beans.FeatureEarthquake;
import com.mvs.testapplication.beans.Geometry;
import com.mvs.testapplication.constants.AppConstants;
import com.mvs.testapplication.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by User on 1/24/2017.
 */

public class EarthQuakeDownloadService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    private static final int MY_NOTIFICATION_ID=1;
    private NotificationManager notificationManager;
    private Notification myNotification;

    private static final String TAG = EarthQuakeDownloadService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public EarthQuakeDownloadService() {
        super(EarthQuakeDownloadService.class.getName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        ResultReceiver receiver=null;
        Utility.Logger.d(TAG, "Service Started!");
        if(intent.hasExtra("receiver"))
        receiver = intent.getParcelableExtra("receiver");
        String url = intent.getStringExtra("url");

        Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(url)) {
            /* Update UI: Download Service is Running */
            if(receiver!=null)
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);
            try {
                String result = downloadData(url);
                String results[] = {result};
                /* Sending result back to activity */
                if (null != results && results.length > 0) {
                    bundle.putStringArray("result", results);
                    if(receiver!=null)
                    receiver.send(STATUS_FINISHED, bundle);
                }
            } catch (Exception e) {
                /* Sending error message back to activity */
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                if(receiver!=null)
                receiver.send(STATUS_ERROR, bundle);
            }
        }
        Utility.Logger.d(TAG, "Service Stopping!");
        this.stopSelf();
    }

    private String downloadData(String requestUrl) throws IOException, DownloadException, JSONException {
        InputStream inputStream = null;

        HttpURLConnection urlConnection = null;

        /* forming th java.net.URL object */
        URL url = new URL(requestUrl);

        urlConnection = (HttpURLConnection) url.openConnection();

        /* optional request header */
        urlConnection.setRequestProperty("Content-Type", "application/json");

        /* optional request header */
        urlConnection.setRequestProperty("Accept", "application/json");

        /* for Get request */
        urlConnection.setRequestMethod("GET");

        int statusCode = urlConnection.getResponseCode();

        /* 200 represents HTTP OK */
        if (statusCode == 200) {
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            String response = convertInputStreamToString(inputStream);
            insertDataToDatabase(response);
            return response;
        } else {
            throw new DownloadException("Failed to fetch data!!");
        }
    }

    private void insertDataToDatabase(String response) throws JSONException {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<FeatureEarthquake> results;
        int oldSize=0,newSize=0;
        double userMagnitude=Utility.getFloatFromPreferences(getApplicationContext(),AppConstants.BundleArguments.THRESHHOLD_VALUE);
        JSONObject earthQuakeJson = new JSONObject(response);
        JSONArray earthQuakeArray = earthQuakeJson.optJSONArray(AppConstants.JsonKeysBase.FEATURES);
        final RealmList<FeatureEarthquake> earthquakeArrayList =  getListFromJson(earthQuakeArray);
        Utility.Logger.i(TAG, "List size ->" + earthquakeArrayList.size());
        RealmQuery<FeatureEarthquake> query=realm.where(FeatureEarthquake.class);
        results = query.greaterThan("earthquakeProperties.magnitude",userMagnitude).findAll();
        oldSize=results.size();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(earthquakeArrayList);

            }
        });


        results=query.greaterThan("earthquakeProperties.magnitude",userMagnitude).findAll();
        if(newSize!=oldSize)
        generateNotification(results.size(),userMagnitude);
        realm.close();
        Utility.Logger.i(TAG, "Table size ->" + results.size());



    }

    private RealmList<FeatureEarthquake> getListFromJson(JSONArray earthQuakeArray) throws JSONException {
        RealmList<FeatureEarthquake> eList = new RealmList<>();
        for (int i = 0; i < earthQuakeArray.length(); i++) {
            JSONObject jsonEarthQuake = earthQuakeArray.getJSONObject(i);
            JSONObject geometryJson = jsonEarthQuake.optJSONObject(AppConstants.JsonKeysBase.GEOMETRY);
            JSONObject propertiesJson = jsonEarthQuake.optJSONObject(AppConstants.JsonKeysBase.PROPERTIES);
            FeatureEarthquake eQuake = new FeatureEarthquake();
            Geometry geometry = new Geometry();
            EarthquakeProperties properties = new EarthquakeProperties();
            String id = jsonEarthQuake.optString(AppConstants.JsonKeysBase.ID, "");
            if (TextUtils.isEmpty(id))
                continue;
            eQuake.setId(id);
            geometry.setLatutitude((Double) geometryJson.optJSONArray(AppConstants.JsonKeys.COORDINATES).get(0));
            geometry.setLongitude((Double) geometryJson.optJSONArray(AppConstants.JsonKeys.COORDINATES).get(1));
//            geometry.setRadius((int) geometryJson.optJSONArray(AppConstants.JsonKeys.COORDINATES).get(2));
            eQuake.setGeometry(geometry);

            properties.setMagnitide(propertiesJson.optDouble(AppConstants.JsonKeys.MAGNITUDE, -1));
            properties.setDetail(propertiesJson.getString(AppConstants.JsonKeys.URL_DETAIL));
            properties.setTime(Utility.getDate(propertiesJson.getLong(AppConstants.JsonKeys.TIME)));
            properties.setPlace(propertiesJson.optString(AppConstants.JsonKeys.PLACE));
            eQuake.setEarthquakeProperties(properties);
            eList.add(eQuake);
        }
        return eList;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }

        return result;
    }


    public class DownloadException extends Exception {

        public DownloadException(String message) {
            super(message);
        }

        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private void generateNotification(int size,double magnitude)
    {

        String notificationText = "Magnitude above "+magnitude;
        myNotification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Earthquake Monitor")
                .setContentText(notificationText)
                .setTicker("Notification!")
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
    }

}
