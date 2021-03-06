package com.mvs.testapplication.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mvs.testapplication.constants.AppConstants;
import com.mvs.testapplication.services.EarthQuakeDownloadService;

/**
 *Will receive broadcast on boot complete
 */

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context, EarthQuakeDownloadService.class);
        intentService.putExtra("url", AppConstants.Urls.BASE_URL+ AppConstants.Urls.ALL);
        context.startService(intentService);
    }
}
