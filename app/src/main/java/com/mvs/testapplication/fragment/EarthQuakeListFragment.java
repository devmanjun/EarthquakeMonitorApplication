package com.mvs.testapplication.fragment;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mvs.testapplication.R;
import com.mvs.testapplication.adapter.EarthQuakeListAdapter;
import com.mvs.testapplication.beans.FeatureEarthquake;
import com.mvs.testapplication.constants.AppConstants;
import com.mvs.testapplication.utils.Utility;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;



/**
 * Created by User on 1/25/2017.
 */

public class EarthQuakeListFragment extends BaseFragment {

    private double earthQuakeType;
    private final String TAG=EarthQuakeListFragment.class.getSimpleName();
    private ArrayList<FeatureEarthquake> results;
    private ListView eQuakeListView;
    private EarthQuakeListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.Logger.d(TAG,"onCreate");
        if(savedInstanceState!=null)
        {
            earthQuakeType=savedInstanceState.getDouble(AppConstants.JsonKeys.MAGNITUDE,0);
            setEarthQuakeData(earthQuakeType);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Utility.Logger.d(TAG,"onResume");
        setEarthQuakeData(earthQuakeType);
        if(listAdapter!=null)
        listAdapter.notifyDataSetChanged();

//        getSupportActionBar().setSubtitle("Magnitude greater than "+earthQuakeType);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Utility.Logger.d(TAG,"onActivityCreated");
//        getActivity().getActionBar().setTitle(getString(R.string.earthquake_details));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Utility.Logger.d(TAG,"onCreateView");
        View v=inflater.inflate(R.layout.layout_earthauake_list_fragment,container,false);
//        getActivity().getActionBar().setTitle(getActivity().getString(R.string.earthquake_details));
//        getActivity().getActionBar().setSubtitle("Earthquakes above "+earthQuakeType+ " magnitude");
        eQuakeListView= (ListView) v.findViewById(R.id.lv_earthQuakeList);
        listAdapter = new EarthQuakeListAdapter(getActivity(),results);
        if(listAdapter.getCount()<=0)
        showAlert();
        eQuakeListView.setItemsCanFocus(true);
        eQuakeListView.setAdapter(listAdapter);
        Utility.Logger.d(TAG,"Adapter Count " +listAdapter.getCount());
        return v;
    }

    private void showAlert() {
        Toast.makeText(getActivity(), "Data not available. Try after some time.", Toast.LENGTH_SHORT).show();
        getFragmentManager().popBackStack();
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        Utility.Logger.d(TAG,"setArguments");
        earthQuakeType=args.getDouble(AppConstants.JsonKeys.MAGNITUDE);
        setEarthQuakeData(earthQuakeType);


    }

    private void setEarthQuakeData(double magnitude) {
        Utility.Logger.d(TAG,"setEarthQuakeData");
        Realm realm=Realm.getDefaultInstance();
        RealmQuery<FeatureEarthquake> query=realm.where(FeatureEarthquake.class);
        results = new ArrayList<FeatureEarthquake>(query.greaterThan("earthquakeProperties.magnitude",magnitude).findAll());
        Utility.Logger.i(TAG,"No of EarthQuakes with Magnitude "+magnitude+" : " +results.size());
        realm.close();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(AppConstants.JsonKeys.MAGNITUDE,earthQuakeType);
    }

}
