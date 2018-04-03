package com.example.tanma.iss_pass.View;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.tanma.iss_pass.ISSPassDataController.DataFetchScheduler;
import com.example.tanma.iss_pass.Utils.Constants;
import com.example.tanma.iss_pass.ISSPassDataProvider.ISSPassDataProvider;
import com.example.tanma.iss_pass.ISSPassDataProvider.ISS_Pass;
import com.example.tanma.iss_pass.ISSPassDataController.ISS_Service;
import com.example.tanma.iss_pass.ISSPassDataController.LocationUpdater;
import com.example.tanma.iss_pass.R;

/*
* On first launch, application will fetch current location and get the ISS Pass data and display,
* If user wants to get details for custom location, use get prediction after entering the custom locations
* using my location user can go back to device location information
* Delay due to docking re-boost, and debris avoidance :
* Data in the list view is refreshed  from 2 ends,
* 1. Location updator updates when device changes  distance of ~ 2miles, or in every 5mins
* 2. schedule the alram for half way(time) from next ISS_Pass rise time,
 */

public class MainActivity extends AppCompatActivity implements RecyclerFragment.OnFragmentInteractionListener {
    private static String TAG = MainActivity.class.getName();
    private ISSPassDataProvider m_issPassData = ISSPassDataProvider.getInstance();
    private LocationUpdater mlocationUpdater;
    private DataFetchScheduler dataFetchScheduler;
    private EditText mETlongtude;
    private EditText mETlatitude;
    private EditText mETaltitude;
    private TextView mErrorInfo;
    private Button mGetPrediction;
    private Button mmyLocation;
    private Fragment mfragment;
    private EditText mPass;
    public ISS_PassListner broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mETlatitude = (EditText) findViewById(R.id.ET_latitude);
        mETlongtude = (EditText) findViewById(R.id.longitude);
        mETaltitude = (EditText) findViewById(R.id.altitude);
        mPass = (EditText) findViewById(R.id.N);
        mErrorInfo = (TextView) findViewById(R.id.errorInfo);
        mGetPrediction = (Button) findViewById(R.id.get_prediction);
        mmyLocation = (Button) findViewById(R.id.myLocation);
        broadcastReceiver = new ISS_PassListner();
        dataFetchScheduler = new DataFetchScheduler(this);
        if(savedInstanceState!=null)
        {
            ISS_Pass.getMlatestCurrentLocation().setAltitude(savedInstanceState.getDouble("alt"));
            ISS_Pass.getMlatestCurrentLocation().setLatitude(savedInstanceState.getDouble("lat"));
            ISS_Pass.getMlatestCurrentLocation().setLongitude(savedInstanceState.getDouble("lng"));
            ISS_Pass.setN(savedInstanceState.getInt("N"));
            requestLoadData();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermission();
    }

   private void requestLoadData()
    {
        Intent intent = new Intent(this, ISS_Service.class);
        intent.setAction(Constants.ACTION_GET_ISS_PASS);
        startService(intent);
    }

    /*******************************
    function : getPrediction
    fetches the custom data entered in the lon,lat, alt, N fields and gets
    ISS Pass data for that location
    ********************************/
    protected void getPrediction(View view)
    {
        hideSoftKeyboard(view);
        mErrorInfo.setText("");
        if(mETaltitude.getText().toString().matches("")||mETlatitude.getText().toString().matches("")||mETlongtude.getText().toString().matches("")||mPass.getText().toString().matches("")) {
            mErrorInfo.setText("All fields should be filled");
            return;
        }
        String url = Constants.url+Float.valueOf( mETlatitude.getText().toString())+"&lon="+Float.valueOf( mETlongtude.getText().toString())+"&alt="+Float.valueOf( mETaltitude.getText().toString())+"&n="+Integer.valueOf( mPass.getText().toString());
        Log.d(TAG, "getPrediction: url" + url);
        Intent intent = new Intent(this, ISS_Service.class);
        intent.setAction(Constants.ACTION_GET_ISS_PASS_CUSTOM);
        intent.putExtra("url",url);
        startService(intent);
        mmyLocation.setEnabled(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Bundle b = new Bundle();
        b.putDouble("lat",ISS_Pass.getMlatestCurrentLocation().getLatitude());
        b.putDouble("alt",ISS_Pass.getMlatestCurrentLocation().getAltitude());
        b.putDouble("lng",ISS_Pass.getMlatestCurrentLocation().getLongitude());
        b.putInt("n",ISS_Pass.getN());
    }

    /*
    * function: getMyLocation()
    * when  custom location desults are being displayed,
    * this function on My Location button press will load the current device location
    * and reload the ISS Pass results for device location
     */
    public void getMyLocation(View view)
    {
        hideSoftKeyboard(view);
        mErrorInfo.setText("");
        ISS_Pass.setN(Integer.valueOf(mPass.getText().toString()));
        mETlongtude.setText(String.valueOf(ISS_Pass.getMlatestCurrentLocation().getLongitude()));
        mETlatitude.setText(String.valueOf(ISS_Pass.getMlatestCurrentLocation().getLatitude()));
        mETaltitude.setText(String.valueOf(ISS_Pass.getMlatestCurrentLocation().getAltitude()));
        mPass.setText(String.valueOf(ISS_Pass.getN()));
        requestLoadData();
    }

    private void hideSoftKeyboard(View view)
    {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void requestPermission()
    {
       ActivityCompat.requestPermissions(this, new String[]
                {
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 2);

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mlocationUpdater = new LocationUpdater(this);
        IntentFilter issPassUpdate = new IntentFilter(Constants.ACTION_DATA_UPDATE);
        issPassUpdate.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(broadcastReceiver,issPassUpdate);

        IntentFilter locationUpdate = new IntentFilter(Constants.ACTION_LOCATION_UPDATE);
        locationUpdate.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(broadcastReceiver,locationUpdate);

        IntentFilter errorupdate = new IntentFilter(Constants.ACTION_FETCH_ERROR);
        errorupdate.addAction((Intent.CATEGORY_DEFAULT));
        registerReceiver(broadcastReceiver,errorupdate );


    }

    @Override
    protected void onStop() {
        super.onStop();
        mlocationUpdater.unregisterUpdtes();
        unregisterReceiver(broadcastReceiver);
        broadcastReceiver =null;
   }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(TAG, "onFragmentInteraction: Entered");
    }

    public class ISS_PassListner extends BroadcastReceiver {
        public ISS_PassListner(){ super(); }

        @Override
        public void onReceive(Context context, Intent intent) {

          if (intent.getAction()== Constants.ACTION_DATA_UPDATE){
              Log.d(TAG, "onReceive: data updated");
              mPass.setText(String.valueOf(m_issPassData.getmISSPassdata().size()));

              FragmentManager fm = getSupportFragmentManager();
              FragmentTransaction ft = fm.beginTransaction();
              if(mfragment==null){

                    mfragment = new RecyclerFragment();
                    ft.add(R.id.fragmentContainer, mfragment).commit();
              } else{
                  ft.detach(mfragment);
                  ft.attach(mfragment);
                  ft.commit();
              }
              mGetPrediction.setEnabled(true);
              scheduleNextPrediction();
          } else if(intent.getAction()== Constants.ACTION_LOCATION_UPDATE)
          {
              Log.d(TAG, "onReceive: location update entered");
              mETaltitude.setText(String.format("%.2f",ISS_Pass.getMlatestCurrentLocation().getLatitude()));
              mETlatitude.setText(String.format("%.2f",ISS_Pass.getMlatestCurrentLocation().getAltitude()));
              mETlongtude.setText(String.format("%.2f",ISS_Pass.getMlatestCurrentLocation().getLongitude()));
              requestLoadData();

          }else if (intent.getAction()== Constants.ACTION_FETCH_ERROR)
          {
              Bundle bundle = intent.getExtras();
              mErrorInfo.setText(bundle.getString("message"));
          }
        }


        public  void scheduleNextPrediction()
        {
            if(m_issPassData==null){
                return;
            }
            dataFetchScheduler.setNextIssPassDataFetch();
        }
        }
}
