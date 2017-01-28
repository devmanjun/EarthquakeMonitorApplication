package com.mvs.testapplication.constants;

/**
 * Created by User on 1/23/2017.
 */

public class AppConstants {

    /**
     * The URLs used to fetch data from server
     */
    public static class Urls {
        public static final String BASE_URL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/";
        public static final String SIGNIFICANT_DAY = "significant_day.geojson";
        public static final String ABOVE_4_5 = "4.5_day.geojson";
        public static final String ABOVE_2_5 = "2.5_day.geojson";
        public static final String ABOVE_1 = "1.0_day.geojson";
        public static final String ALL = "all_day.geojson";
        public static final String URL="url";
    }

    /**
     * The JSON keys which will vbe part of the outer JSON structure
     */
    public static class JsonKeysBase {
        public static final String TYPE = "type";
        public static final String META_DATA = "metadata";
        public static final String FEATURES = "features";
        public static final String BBOX = "bbox";
        public static final String ID = "id";
        public static final String GEOMETRY = "geometry";
        public static final String PROPERTIES = "properties";
    }

    /**
     * The JSON keys which will vbe part of the inner JSON structure
     */
    public static class JsonKeys {
        public static final String METADATA_GENERATED = "generated";
        public static final String METADATA_URL = "url";
        public static final String METADATA_TITLE = "title";
        public static final String METADATA_STATUS = "status";
        public static final String METADATA_COUNT = "count";

        public static final String MAGNITUDE = "mag";
        public static final String PLACE = "place";
        public static final String TIME = "time";
        public static final String UPDATED = "updated";
        public static final String TZ = "tz";
        public static final String URL_DETAIL = "url";
        public static final String DETAIL = "detail";
        public static final String FELT = "felt";
        public static final String CDI = "cdi";
        public static final String MMI = "mmi";
        public static final String ALERT = "alert";
        public static final String STATUS = "status";
        public static final String TSUNAMI = "tsunami";
        public static final String SIG = "sig";
        public static final String NET = "net";
        public static final String CODE = "code";
        public static final String IDS = "ids";
        public static final String SOURCES = "sources";
        public static final String TYPES = "types";
        public static final String NST = "nst";
        public static final String DMIN = "dmin";
        public static final String RMS = "rms";
        public static final String GAP = "gap";
        public static final String MAGTYPE = "magType";
        public static final String TYPE = "type";
        public static final String COORDINATES = "coordinates";
        public static final String LATITUDE = "0";
        public static final String LONGITUDE = "1";
        public static final String RADIUS = "2";
        public static final String UNIQUE_ID = "id";
        public static final String POINT = "point";
    }

    public static class DateConstants
    {
        public static final String SIMPLE_DATE_FORMAT="dd-MMMM-yyyy hh:mm:ss";
    }
    public static class EarthQuakeMagnitudes
    {
        public static final double SIGNIFICANT=5.5;
        public static final double ALL=0;
        public static final double ONE_PLUS=1;
        public static final double TWO_PLUS=2.5;
        public static final double FOUR_PLUS=4.5;
    }

    public static class BundleArguments
    {
        public static final String FEATURE_LIST="earthquakeList";
        public static final String THRESHHOLD_VALUE="threshhold";
        public static final String DATA_FETCH_INTERVAL="dataFetchInterval";
    }


}
