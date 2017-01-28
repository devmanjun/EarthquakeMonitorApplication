package com.mvs.testapplication.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mvs.testapplication.constants.AppConstants;
import com.mvs.testapplication.services.EarthQuakeDownloadService;
import com.mvs.testapplication.utils.Utility;

/**
 * Created by User on 1/27/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    private final String TAG=AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Utility.Logger.i(TAG,"onReceive");
        Intent intentService = new Intent(context, EarthQuakeDownloadService.class);
        intentService.putExtra("url", AppConstants.Urls.BASE_URL+ AppConstants.Urls.ALL);
        context.startService(intentService);
    }
}
