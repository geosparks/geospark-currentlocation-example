package com.geospark.example;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.geospark.lib.GeoSpark;
import com.geospark.lib.models.GeoSparkLocation;
import com.geospark.lib.service.GeoSparkReceiver;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CurrentLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private MapFragment mMapFragment;
    private GoogleMap mMap;
    private LocationReceiver mLocationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onResume() {
        if (mMapFragment != null) {
            mMapFragment.onResume();
        }
        super.onResume();
        if (mLocationReceiver == null) {
            mLocationReceiver = new LocationReceiver();
            registerReceiver(mLocationReceiver, new IntentFilter("com.geospark.android.RECEIVED"));
        }
        locationPermission();
    }

    @Override
    public void onPause() {
        if (mMapFragment != null) {
            mMapFragment.onPause();
        }
        super.onPause();
        if (mLocationReceiver != null) {
            unregisterReceiver(mLocationReceiver);
            mLocationReceiver = null;
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapFragment != null) {
            mMapFragment.onLowMemory();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
    }

    private void locationPermission() {
        if (!GeoSparkHelper.getInstance().checkLocationPermission()) {
            GeoSparkHelper.getInstance().requestLocationPermission(this);
        } else if (!GeoSparkHelper.getInstance().checkLocationServices()) {
            GeoSparkHelper.getInstance().requestLocationServices(this);
        } else {
            GeoSparkHelper.getInstance().getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case GeoSpark.REQUEST_CODE_LOCATION_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermission();
                } else {
                    Toast.makeText(getApplicationContext(), "Location permission required", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GeoSpark.REQUEST_CODE_LOCATION_ENABLED) {
            locationPermission();
        }
    }

    public class LocationReceiver extends GeoSparkReceiver {

        @Override
        public void onLocationUpdated(Context context, GeoSparkLocation geosparkLocation) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions()
                    .title("Lat: " + geosparkLocation.getLocation().getLatitude() + " Lng: " + geosparkLocation.getLocation().getLongitude() + " Accuracy: " + geosparkLocation.getLocation().getAccuracy())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .position(new LatLng(geosparkLocation.getLocation().getLatitude(), geosparkLocation.getLocation().getLongitude())));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(geosparkLocation.getLocation().getLatitude(), geosparkLocation.getLocation().getLongitude()), 17);
            mMap.moveCamera(cameraUpdate);
        }
    }
}
