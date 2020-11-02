package com.pashin.compass;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class AppLocationListener implements LocationListener {

    private LocationListenerInterface locationListenerInterface;

    @Override
    public void onLocationChanged(Location location) {
        locationListenerInterface.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void setLocationListenerInterface(LocationListenerInterface locationListenerInterface) {
        this.locationListenerInterface = locationListenerInterface;
    }
}
