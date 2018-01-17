package com.example.makati.testswipe;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Makati on 1/14/2018.
 */
//pass the context
public class GPSTracker implements LocationListener {
    private static int MY_PERMISSIONS_REQUEST_GET_LOC = 1;
    Context context;

    public GPSTracker(Context c) {
        context = c;
    }

    //a different method for getting location
    public Location getLocation() {
        //is the permission allowed?
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e("Error","permission denied");
            return null;
        }


        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Toast.makeText(context, "Got location.", Toast.LENGTH_SHORT).show();
            return l;
        }else{
            Toast.makeText(context, "Please enable your GPS.", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

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
}
