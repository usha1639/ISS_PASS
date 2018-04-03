package com.example.tanma.iss_pass.ISSPassDataController;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.tanma.iss_pass.Utils.Constants;
import com.example.tanma.iss_pass.ISSPassDataProvider.ISSPassDataProvider;
import com.example.tanma.iss_pass.ISSPassDataProvider.ISS_Pass;

/******************************
 Location updater keeps track of change in location on 2 constraints
 in every - 5 minutes change in distance  ~ 2 miles
 ****************************************************/

public class LocationUpdater implements LocationListener {
    private static String TAG = LocationUpdater.class.getName();
    Context mcontext;
    private Location mlocation;
    LocationManager mlocationManager;

    public LocationUpdater(Context context) {
        this.mcontext = context;
        mlocationManager = (LocationManager) mcontext.getSystemService(Context.LOCATION_SERVICE);
        initiateLocationupdates();
    }

    public void initiateLocationupdates() {
        if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            {
                Log.d(TAG, "initiateLocationupdates: location not enabled so returning ");
                return;
            }
        }
        if (mlocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true)
            Log.d(TAG, "initiateLocationupdates: Requesting location updates");
            mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 3220, this);


    }

    // Informs the UI about the location change
    @Override
    public void onLocationChanged(Location location) {
            mlocation = location;
        Log.d(TAG, "onLocationChanged: Entered Latitude :" + location.getLatitude() + " :: longitude : " + location.getLongitude());
            if(ISS_Pass.getMlatestCurrentLocation()!=location) {
                ISS_Pass.setMlatestCurrentLocation(location);
                ISS_Pass.setN(10);
                Intent intent = new Intent();
                intent.setAction(Constants.ACTION_LOCATION_UPDATE);
                mcontext.sendBroadcast(intent);
            }
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

    public void unregisterUpdtes()
    {
        mlocationManager.removeUpdates(this);
    }
}
