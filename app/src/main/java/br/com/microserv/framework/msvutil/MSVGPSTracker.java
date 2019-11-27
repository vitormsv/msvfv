package br.com.microserv.framework.msvutil;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
//import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by notemsv01 on 15/05/2017.
 */

public class MSVGPSTracker extends Service implements LocationListener {

    private Context context = null;

    Location location = null;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    double latitude = 0;
    double longitude = 0;

    protected LocationManager locationManager = null;


    public MSVGPSTracker(Context context) {

        this.context = context;
        getLocation();

    }


    public Location getLocation() {

        try {

            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isNetworkEnabled) {

                canGetLocation = true;

                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        60000,
                        10,
                        this
                );

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }

            }

            if (isGPSEnabled) {

                canGetLocation = true;

                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        60000,
                        10,
                        this
                );

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }

            }

        } catch (SecurityException e) {
            e.printStackTrace();
        } finally {

        }


        return location;

    }


    public double getLatitude() {

        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;

    }


    public double getLongitude() {

        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;

    }


    public boolean canGetLocation() {

        return this.canGetLocation;

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
