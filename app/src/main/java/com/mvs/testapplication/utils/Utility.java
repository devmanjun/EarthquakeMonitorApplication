package com.mvs.testapplication.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;

import com.mvs.testapplication.broadcastreceiver.AlarmReceiver;
import com.mvs.testapplication.constants.AppConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by User on 1/24/2017.
 */

public class Utility {

    public static final String APP_LOG_TAG="mvs ";

    public static class Logger
    {
        public static void i(String TAG, String message)
        {
            Log.i(APP_LOG_TAG+TAG,message);
        }
        public static void v(String TAG, String message)
        {
            Log.v(APP_LOG_TAG+TAG,message);
        }
        public static void d(String TAG, String message)
        {
            Log.d(APP_LOG_TAG+TAG,message);
        }
    }
    public static String getDate(long milliSeconds)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(AppConstants.DateConstants.SIMPLE_DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    public static float getFloatFromPreferences(Context context,String key)
    {
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getFloat(key, (float) 5.5);
    }

    public static void putFloatToPreferences(Context context,String key,float value)
    {
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putFloat(key,value);
        editor.commit();
    }

    public static int getIntFromPreferences(Context context,String key)
    {
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key,5);
    }

    public static void putIntToPreferences(Context context,String key,int value)
    {
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt(key,value);
        editor.commit();
    }

    public static void scheduleAlarm(Context context) {
        int timeInMinutes=getIntFromPreferences(context,AppConstants.BundleArguments.DATA_FETCH_INTERVAL);
        Intent intent = new Intent(context, AlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,1000*timeInMinutes, pIntent);
    }

    public static void cancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        if(pIntent!=null)
            alarm.cancel(pIntent);
    }
}
