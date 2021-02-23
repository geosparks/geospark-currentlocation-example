package com.geospark.example;

import android.app.Activity;
import android.content.Context;

import com.geospark.lib.GeoSpark;

public class GeoSparkHelper {

    private static GeoSparkHelper mGeoSparkHelper;
    private final Context mContext;

    private GeoSparkHelper(Context context) {
        mContext = context.getApplicationContext();
    }

    public synchronized static void initialize(Context context) {
        if (context == null)
            throw new NullPointerException("Provided context is null");
        else if (mGeoSparkHelper == null) {
            mGeoSparkHelper = new GeoSparkHelper(context);
        }
    }

    public static GeoSparkHelper getInstance() {
        if (mGeoSparkHelper == null)
            throw new NullPointerException("Please call initialize() before getting the instance.");
        return mGeoSparkHelper;
    }

    public void initialize() {
        GeoSpark.initialize(mContext, "YOUR-PUBLISHABLE-KEY");
    }

    public boolean checkLocationPermission() {
        return GeoSpark.checkLocationPermission();
    }

    public void requestLocationPermission(Activity activity) {
        GeoSpark.requestLocationPermission(activity);
    }

    public boolean checkLocationServices() {
        return GeoSpark.checkLocationServices();
    }

    public void requestLocationServices(Activity activity) {
        GeoSpark.requestLocationServices(activity);
    }

    public void getCurrentLocation() {
        GeoSpark.getCurrentLocation(100);
    }
}
