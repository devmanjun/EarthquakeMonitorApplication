package com.mvs.testapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mvs.testapplication.R;
import com.mvs.testapplication.beans.FeatureEarthquake;
import com.mvs.testapplication.utils.Utility;

import java.util.ArrayList;

import io.realm.RealmList;

/**
 * Created by User on 1/26/2017.
 */

public class EarthQuakeListAdapter extends ArrayAdapter {
    private Context mContext;
    private ArrayList<FeatureEarthquake> earthQuakeList;
    private final String TAG=EarthQuakeListAdapter.class.getSimpleName();
    private int mPosition;

    public EarthQuakeListAdapter(Context context, ArrayList<FeatureEarthquake> earthQuakeList) {
        super(context, R.layout.earth_quake_list_row);
        this.mContext = context;
        this.earthQuakeList = earthQuakeList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        mPosition=position;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.earth_quake_list_row, null);
            viewHolder.magnitude = (TextView) convertView.findViewById(R.id.tv_magnitude_value);
            viewHolder.place = (TextView) convertView.findViewById(R.id.tv_place_value);
            viewHolder.date = (TextView) convertView.findViewById(R.id.tv_date_value);
            viewHolder.coordinates = (TextView) convertView.findViewById(R.id.tv_coordinates_value);
            viewHolder.details = (TextView) convertView.findViewById(R.id.tv_details_value);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final FeatureEarthquake fQuake = earthQuakeList.get(position);
        viewHolder.magnitude.setText(fQuake.getEarthquakeProperties().getMagnitude() + "");
        viewHolder.place.setText(fQuake.getEarthquakeProperties().getPlace());
        viewHolder.date.setText(fQuake.getEarthquakeProperties().getTime());
        viewHolder.coordinates.setText(fQuake.getGeometry().getLatutitude() + "," + fQuake.getGeometry().getLongitude());
        viewHolder.coordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String data="geo:"+fQuake.getGeometry().getLongitude()+","+fQuake.getGeometry().getLatutitude()+"?z=15";
                String data2="geo:0,0?q="+fQuake.getGeometry().getLongitude() + "," + fQuake.getGeometry().getLatutitude()+"("+fQuake.getEarthquakeProperties().getPlace()+")?z=10";
                Utility.Logger.i(TAG,"coordinates "+data2);
                Uri gmmIntentUri = Uri.parse(data2);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(mContext.getPackageManager()) != null) {
                   mContext.startActivity(mapIntent);
                }
                else
                {
                    Toast.makeText(mContext,"Please install Google maps to view the location", Toast.LENGTH_LONG).show();
                }
            }
        });
        viewHolder.details.setText(fQuake.getEarthquakeProperties().getDetail());
        viewHolder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.Logger.i(TAG,"coordinates "+fQuake.getEarthquakeProperties().getDetail());
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(fQuake.getEarthquakeProperties().getDetail()));
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }



    public class ViewHolder {
        private TextView magnitude, place, date, coordinates, details;
    }


    @Override
    public int getCount() {
        return earthQuakeList.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return earthQuakeList.get(position);
    }

}
