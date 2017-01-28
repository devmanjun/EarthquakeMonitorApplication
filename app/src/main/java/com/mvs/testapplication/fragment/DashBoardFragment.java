package com.mvs.testapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mvs.testapplication.R;
import com.mvs.testapplication.beans.EarthquakeProperties;
import com.mvs.testapplication.beans.FeatureEarthquake;
import com.mvs.testapplication.constants.AppConstants;
import com.mvs.testapplication.receiver.DownloadResultReceiver;
import com.mvs.testapplication.services.EarthQuakeDownloadService;
import com.mvs.testapplication.utils.Utility;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 *Show the dasboard to the user
 */

public class DashBoardFragment extends BaseFragment implements DownloadResultReceiver.Receiver, View.OnClickListener {
    private DownloadResultReceiver mReceiver;
    private final String TAG = DashBoardFragment.class.getSimpleName();
    private final double SIGNIFICANT_EARTHQUAKE_MAGNITUDE=5.5;
    private Context mContext;
    private TextView tvAll,tvSig,tvOne,tvTwo,tvFour;
    private DataListener dataListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, mContext, EarthQuakeDownloadService.class);

//        getActivity().getActionBar().setTitle(getActivity().getString(R.string.earthquake_types));
        /* Send optional extras to Download IntentService */
        intent.putExtra("url", AppConstants.Urls.BASE_URL+AppConstants.Urls.ALL);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("requestId", 101);

//        mContext.startService(intent);
        initViews();


    }



    private void initViews() {
        tvAll= (TextView) getView().findViewById(R.id.tv_all);
        tvAll.setOnClickListener(this);
        tvFour= (TextView) getView().findViewById(R.id.tv_4plus);
        tvFour.setOnClickListener(this);
        tvOne= (TextView) getView().findViewById(R.id.tv_1plus);
        tvOne.setOnClickListener(this);
        tvTwo= (TextView) getView().findViewById(R.id.tv_2plus);
        tvTwo.setOnClickListener(this);
        tvSig= (TextView) getView().findViewById(R.id.tv_significant);
        tvSig.setOnClickListener(this);
    }

    private void getEarthQuakeData(double magnitude) {
       Realm realm=Realm.getDefaultInstance();
        RealmQuery<FeatureEarthquake> query=realm.where(FeatureEarthquake.class);

        RealmResults<FeatureEarthquake> results=query.greaterThan("earthquakeProperties.magnitude",magnitude).findAll();
        Utility.Logger.i(TAG,"magnitude earthquaks number"+results.size());
        dataListener.getData(magnitude);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.layout_dashboard, container, false);
        v.setOnClickListener(this);
        return v;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case EarthQuakeDownloadService.STATUS_RUNNING:

                break;
            case EarthQuakeDownloadService.STATUS_FINISHED:
                /* Hide progress & extract result from bundle */

                String[] results = resultData.getStringArray("result");

                /* Update ListView with result */
                Utility.Logger.i(TAG, results[0]);

                break;
            case EarthQuakeDownloadService.STATUS_ERROR:
                /* Handle the error */
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(mContext, error, Toast.LENGTH_LONG).show();
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_significant:
                getEarthQuakeData(AppConstants.EarthQuakeMagnitudes.SIGNIFICANT);
                break;
            case R.id.tv_all:
                getEarthQuakeData(AppConstants.EarthQuakeMagnitudes.ALL);
                break;
            case R.id.tv_4plus:
                getEarthQuakeData(AppConstants.EarthQuakeMagnitudes.FOUR_PLUS);
                break;
            case R.id.tv_2plus:
                getEarthQuakeData(AppConstants.EarthQuakeMagnitudes.TWO_PLUS);
                break;
            case R.id.tv_1plus:
                getEarthQuakeData(AppConstants.EarthQuakeMagnitudes.ONE_PLUS);
                break;
            default:
                break;
        }

    }
    public interface DataListener
    {
        public void getData(double magnitude);
    }
    public void setDataListener(DataListener dataListener)
    {
        this.dataListener=dataListener;
    }
}
