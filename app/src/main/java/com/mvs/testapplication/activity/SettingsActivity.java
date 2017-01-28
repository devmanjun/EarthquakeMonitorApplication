package com.mvs.testapplication.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mvs.testapplication.R;
import com.mvs.testapplication.broadcastreceiver.AlarmReceiver;
import com.mvs.testapplication.constants.AppConstants;
import com.mvs.testapplication.utils.Utility;

public class SettingsActivity extends AppCompatActivity {

    private EditText threshHoldEdittext, dataFetchEditText;
    private boolean isvaliddataFetch,isvalidThreshhold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        intiViews();
    }

    private void intiViews() {
        threshHoldEdittext = (EditText) findViewById(R.id.et_settings_threshhold_value);
        threshHoldEdittext.setText(Utility.getFloatFromPreferences(this, AppConstants.BundleArguments.THRESHHOLD_VALUE) + "");
        dataFetchEditText = (EditText) findViewById(R.id.et_settings_data_fetch_interval_value);
        dataFetchEditText.setText(Utility.getIntFromPreferences(this, AppConstants.BundleArguments.DATA_FETCH_INTERVAL) + "");
        threshHoldEdittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                vaidateThreshHold(threshHoldEdittext.getText().toString().trim());
            }
        });
        dataFetchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                vaidateDataFetch(dataFetchEditText.getText().toString().trim());
            }
        });
       /* threshHoldEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                vaidateThreshHold(s.toString());

            }
        });*/
     /*   dataFetchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vaidateDataFetch(s.toString());

            }
        });*/
    }

    private boolean vaidateThreshHold(String s) {
        if (!TextUtils.isEmpty(s)) {
            float value = Float.parseFloat(s);
            if (value <= 0) {
                threshHoldEdittext.setText("");
                threshHoldEdittext.setHint("Enter a value greater than 0");
                return false;
            } else
                Utility.putFloatToPreferences(this, AppConstants.BundleArguments.THRESHHOLD_VALUE, Float.parseFloat(s));
                return true;
        } else {
            threshHoldEdittext.setHint("Enter some value");
            return false;
        }

    }

    private boolean vaidateDataFetch(String s) {
        if (TextUtils.isEmpty(s)) {
            dataFetchEditText.setHint("Enter some value");
            return false;
        } else if (Integer.parseInt(s)<=0) {
            dataFetchEditText.setHint("Enter a value greater than 0");
            return false;
        } else {
            Utility.putIntToPreferences(this, AppConstants.BundleArguments.DATA_FETCH_INTERVAL, Integer.parseInt(s));
            Utility.cancelAlarm(getApplicationContext());
            Utility.scheduleAlarm(getApplicationContext());
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        isvalidThreshhold=vaidateThreshHold(threshHoldEdittext.getText().toString().trim());
        isvaliddataFetch=vaidateDataFetch(dataFetchEditText.getText().toString().trim());
        if(isvalidThreshhold&&isvaliddataFetch)
            super.onBackPressed();
        else
            Toast.makeText(this,"Enter proper values for both fields",Toast.LENGTH_SHORT).show();

    }
}
