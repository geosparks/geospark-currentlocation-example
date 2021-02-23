package com.geospark.example;

import android.app.Application;

import com.geospark.lib.GeoSpark;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GeoSparkHelper.initialize(this);
        GeoSparkHelper.getInstance().initialize();
    }
}
