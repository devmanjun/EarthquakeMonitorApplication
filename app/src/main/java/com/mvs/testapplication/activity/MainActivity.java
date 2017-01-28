package com.mvs.testapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mvs.testapplication.R;
import com.mvs.testapplication.constants.AppConstants;
import com.mvs.testapplication.fragment.DashBoardFragment;
import com.mvs.testapplication.fragment.EarthQuakeListFragment;
import com.mvs.testapplication.receiver.DownloadResultReceiver;
import com.mvs.testapplication.utils.Utility;

public class MainActivity extends AppCompatActivity implements DashBoardFragment.DataListener{

    private DownloadResultReceiver mReceiver;
    private final String TAG=MainActivity.class.getSimpleName();
    private double earthQuakeMagnitude;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.Logger.i(TAG,"OnCreate");
        setContentView(R.layout.layout_activity);
        Utility.scheduleAlarm(getApplicationContext());
        if(savedInstanceState==null) {
            fragmentTransaction = getFragmentTransaction();
            DashBoardFragment dashBoard = new DashBoardFragment();
            getSupportActionBar().setTitle(getString(R.string.earthquake_types));
            dashBoard.setDataListener(this);
            fragmentTransaction.add(R.id.ll_container, dashBoard, DashBoardFragment.class.getSimpleName());
            fragmentTransaction.commit();
        }else
        {
            DashBoardFragment dashFragment= (DashBoardFragment) getSupportFragmentManager().findFragmentByTag(DashBoardFragment.class.getSimpleName());
            if(dashFragment!=null)
                dashFragment.setDataListener(this);
            earthQuakeMagnitude=savedInstanceState.getDouble(AppConstants.JsonKeys.MAGNITUDE);
        }

    }






    @Override
    public void getData(double magnitude) {
       showEarthQuakeDetails(magnitude);
    }

    private void showEarthQuakeDetails(double magnitude) {
        earthQuakeMagnitude=magnitude;
        fragmentTransaction=getFragmentTransaction();
        EarthQuakeListFragment earthQuakeListFragment=new EarthQuakeListFragment();
        earthQuakeListFragment.setArguments(getArgumentBundle(magnitude));
        fragmentTransaction.replace(R.id.ll_container,earthQuakeListFragment,EarthQuakeListFragment.class.getSimpleName());

        fragmentTransaction.addToBackStack(DashBoardFragment.class.getSimpleName());
        fragmentTransaction.commit();

    }

    private Bundle getArgumentBundle(double magnitude) {
        Bundle bundle=new Bundle();
        bundle.putDouble(AppConstants.JsonKeys.MAGNITUDE,magnitude);
        return bundle;
    }

    private FragmentTransaction getFragmentTransaction()
    {
        FragmentManager fragmentManager=getSupportFragmentManager();
        return fragmentManager.beginTransaction();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getTitle().toString().equalsIgnoreCase(getString(R.string.settings)))
        {
            Intent settingsIntent=new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putDouble(AppConstants.JsonKeys.MAGNITUDE,earthQuakeMagnitude);
    }
}
