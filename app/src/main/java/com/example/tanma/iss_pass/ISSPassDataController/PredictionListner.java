package com.example.tanma.iss_pass.ISSPassDataController;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/*******************************
 * class : PredictionListner
 * func :  Listens to next data fetch remainder (scheduled by class DataFetchScheduler)
 *         request to ISS_Service for new fetch
 ************************************/
public class PredictionListner extends BroadcastReceiver {
    private static String TAG = PredictionListner.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
       Log.d(TAG, "onReceive: alarm msgreceived");
       Intent intent_getdata = new Intent(context,ISS_Service.class);
       intent_getdata.setAction("com.example.tanma.iss_pass.action.GET");
       context.startService(intent_getdata);
    }

}
