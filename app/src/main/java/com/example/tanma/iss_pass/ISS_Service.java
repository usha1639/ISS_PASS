package com.example.tanma.iss_pass;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import ISS_Data.ISSPassData;
import ISS_Data.ISS_Response;

/**
 ISS_Service will query the ISS pass data and provide the data to application to update the list view

 */
public class ISS_Service extends IntentService {
    private ISSPassData issPassData = ISSPassData.getInstance();
    private static String TAG = ISS_Service.class.getCanonicalName();
    private ISS_Response miss_response;
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_GET_ISS_PASS = "com.example.tanma.iss_pass.action.GET";
    private static final  String ACTION_GET_ISS_PASS_CUSTOM = "com.example.tanma.iss.custom.GET";

    // TODO: Rename parameters

    public ISS_Service() {
        super("ISS_Service");
      //  Log.d(TAG, "ISS_Service: constructed");

        miss_response = new ISS_Response();
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_ISS_PASS.equals(action)) {
                handleActionGetISSPass();

            }
            else if (ACTION_GET_ISS_PASS_CUSTOM.equals(action))
            {
                Bundle b = intent.getExtras();
                Log.d(TAG, "onHandleIntent: "+b.getString("url"));

                handleIntentCustonGetPass(b.getString("url"));


            }
        }
    }

    protected  void handleIntentCustonGetPass(String url)
    {
            miss_response.setUrl(true,url);
            if(miss_response.getHttpClientFetch())
            {
                handleErrors("",miss_response.getError());
            }
        Intent intent = new Intent();
        intent.setAction(CONSTANTS.ACTION_DATA_UPDATE);
        sendBroadcast(intent);
    }

    /**
     * action ACTION_GET_ISS_PASS will get the Pass information for current location of the device
     * and inform UI to update the views
     *
     */
    private void handleActionGetISSPass() {
           miss_response.setUrl(false,"");
            miss_response.getIssPassData();
            Intent intent = new Intent();
            intent.setAction(CONSTANTS.ACTION_DATA_UPDATE);
            sendBroadcast(intent);

    }

    private void handleErrors(String id, String reason)

    {
        Intent intent = new Intent();
        intent.setAction(CONSTANTS.ACTION_FETCH_ERROR);
        Bundle bundle = new Bundle();

        intent.putExtra("id",id);
        intent.putExtra("message", reason);

        sendBroadcast(intent);


    }





}
