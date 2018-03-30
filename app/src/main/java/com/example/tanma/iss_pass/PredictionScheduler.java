package com.example.tanma.iss_pass;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ISS_Data.ISSPassData;
import ISS_Data.ISS_Pass;

/**
 * Created by tanma on 3/27/2018.
 *
 *
 */

public class PredictionScheduler extends BroadcastReceiver {
    private static String TAG = PredictionScheduler.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        ISSPassData issPassData = ISSPassData.getInstance();


        Log.d(TAG, "onReceive: alarm msgreceived");
       Intent intnt = new Intent(context,ISS_Service.class);
        intnt.setAction("com.example.tanma.iss_pass.action.GET");

       context.startService(intnt);






    }
}
