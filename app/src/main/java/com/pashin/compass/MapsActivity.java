package com.pashin.compass;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListenerInterface {

    private GoogleMap map;
    private LocationManager locationManager;
    private Location lastLocation;
    private AppLocationListener appLocationListener;
    private boolean granted;

    private List<LatLng> routePointsList;
    View mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        granted = false;
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        appLocationListener = new AppLocationListener();
        appLocationListener.setLocationListenerInterface(this);

        routePointsList = new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapView = mapFragment.getView();
            mapFragment.getMapAsync(this);
        }
    }

    public void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, 1);
        } else {
            granted = true;
            update();
        }
    }

    @SuppressLint("MissingPermission")
    public void update() {
        if (granted) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, appLocationListener);
        }
        else {
            checkPermissions();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        checkPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length == 2 && grantResults[0] == RESULT_OK && grantResults[1] == RESULT_OK)
                granted = true;
        }
        update();
    }

    private void draw() {
        final PolylineOptions polylineOptions = new PolylineOptions();
        for (LatLng point : routePointsList) {
            polylineOptions.add(point);
        }

        polylineOptions.width(5).color(Color.parseColor("#C1B05C"));

        map.addPolyline(polylineOptions);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(routePointsList.get(routePointsList.size() - 1), 15));
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.hasSpeed() && lastLocation != null) {
            routePointsList.add(new LatLng(location.getLatitude(), location.getLongitude()));
            draw();
        }
        lastLocation = location;
    }

    public void goCompass(View view) {
        Intent intent = new Intent(MapsActivity.this, MainActivity.class);
        startActivity(intent);
    }
}