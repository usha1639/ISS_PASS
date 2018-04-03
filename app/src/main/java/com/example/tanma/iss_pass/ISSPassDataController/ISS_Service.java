package com.example.tanma.iss_pass.ISSPassDataController;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.example.tanma.iss_pass.Utils.Constants;
import com.example.tanma.iss_pass.ISSPassDataProvider.ISSPassDataProvider;
import com.example.tanma.iss_pass.ISSPassDataProvider.ISS_Response;

/***********************
* ISS_Service will query the ISS pass data and provide the data to application to update the list view
*************************** */
public class ISS_Service extends IntentService {
    private ISSPassDataProvider issPassData = ISSPassDataProvider.getInstance();
    private static String TAG = ISS_Service.class.getCanonicalName();
    private ISS_Response miss_response;

    public ISS_Service() {
        super("ISS_Service");
        miss_response = new ISS_Response();
    }

    /*************************************
    * Func : onHandleIntent(Intent intent)
    * handler for ISS_Service, respond to 2 ACTIONS
    * 1. ACTION_GET_ISS_PASS
    * 2. ACTION_GET_ISS_PASS_CUSTOM
    * depending on the action received, handler is setting the url for datafetch, and requests data
     **************************************/
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Constants.ACTION_GET_ISS_PASS.equals(action)) {
                miss_response.setUrl(false,"");
             } else if (Constants.ACTION_GET_ISS_PASS_CUSTOM.equals(action))
             {
                Bundle b = intent.getExtras();
                Log.d(TAG, "onHandleIntent: "+b.getString("url"));
                miss_response.setUrl(true,b.getString("url"));
             }else
                return;

            handleActionGetISSPass();
        }
    }


    /************************************
     * action ACTION_GET_ISS_PASS will get the Pass information for current location of the device
     * and inform UI to update the views
     *************************************/
    private void handleActionGetISSPass() {
       if(miss_response.getHttpClientFetch())
        {
            handleErrors("",miss_response.getError());
        }
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_DATA_UPDATE);
        sendBroadcast(intent);
    }

    /******************************************
    * Func : handleErrors(String id, String reason)
    * if data fetch fails, this function
    * will pass the error information to UI thread
     ********************************************/
    private void handleErrors(String id, String reason)
    {
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_FETCH_ERROR);
        Bundle bundle = new Bundle();
        intent.putExtra("id",id);
        intent.putExtra("message", reason);
        sendBroadcast(intent);
    }
}
