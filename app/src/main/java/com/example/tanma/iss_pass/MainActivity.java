package com.example.tanma.iss_pass;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import ISS_Data.ISSPassData;
import ISS_Data.ISS_Pass;




/*
* On first launch, application will fetch current location and get the ISS Pass data and display,
* If user wants to get details for custom location, use get prediction after entering the custom locations
*
* using my location user can go back to device location information
*
* Delay due to docking re-boost, and debris avoidance :
* Data in the list view is refreshed  from 2 ends,
* 1. Location updator updates when device changes  distance of ~ 2miles, or in every 5mins
*
* 2. schedule the alram for half way(time) from next ISS_Pass rise time,
*   
*
 */
public class MainActivity extends AppCompatActivity implements ISSPassFragment.OnListFragmentInteractionListener , RecyclerFragment.OnFragmentInteractionListener{



    private static String TAG = MainActivity.class.getName();
    private ISSPassData missPassData = ISSPassData.getInstance();
    private LocationUpdater mlocationUpdater;
    private EditText mETlongtude;
    private EditText mETlatitude;
    private EditText mETaltitude;
    private TextView mErrorInfo;
    private Button mGetPrediction;
    private Button mmyLocation;

    private Fragment mfragment;


    private EditText mPass;

    private static final int  JOB_ID = 100;
    private JobScheduler js;
    private JobInfo jobInfo;




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

        if(savedInstanceState!=null)
        {
            ISS_Pass.getMlatestCurrentLocation().setAltitude(savedInstanceState.getDouble("alt"));
            ISS_Pass.getMlatestCurrentLocation().setLatitude(savedInstanceState.getDouble("lat"));
            ISS_Pass.getMlatestCurrentLocation().setLongitude(savedInstanceState.getDouble("lng"));
            ISS_Pass.setN(savedInstanceState.getInt("N"));
            loadData();

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



   private void loadData()
    {
        Intent intent = new Intent(this, ISS_Service.class);
        intent.setAction("com.example.tanma.iss_pass.action.GET");

        startService(intent);
    }

    /*
            function : getPrediction
            fetches the custom data entered in the lon,lat, alt, N fields and gets
            ISS Pass data for that location
     */
    protected void getPrediction(View view)
    {
        hideSoftKeyboard(view);

        Log.d(TAG, "getPrediction: Entered"+mETaltitude.getText().toString());
        mErrorInfo.setText("");
       // String url = "http://api.open-notify.org/iss-pass.json?lat="+Float.valueOf(mETlatitude.getText().toString())+"&lon="+Float.valueOf(mETlongtude.getText().toString())+"&alt="+Float.valueOf(mETaltitude.getText().toString())+"&n="+Float.valueOf(n.getText().toString());


        if(mETaltitude.getText().toString().matches("")||mETlatitude.getText().toString().matches("")||mETlongtude.getText().toString().matches("")||mPass.getText().toString().matches("")) {
            mErrorInfo.setText("All fields should be filled");
            return;
        }
        String url = "http://api.open-notify.org/iss-pass.json?lat="+Float.valueOf( mETlatitude.getText().toString())+"&lon="+Float.valueOf( mETlongtude.getText().toString())+"&alt="+Float.valueOf( mETaltitude.getText().toString())+"&n="+Integer.valueOf( mPass.getText().toString());
        Log.d(TAG, "getPrediction: url" + url);




        Intent intent = new Intent(this, ISS_Service.class);
        intent.setAction("com.example.tanma.iss.custom.GET");
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
    * function: getMyLocation
    * when  custom location desults are being displayed,
    * this function My Location button will load the current device location
    * and reload the device location ISS Pass results
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

        loadData();

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


        IntentFilter issPassUpdate = new IntentFilter(CONSTANTS.ACTION_DATA_UPDATE);
        issPassUpdate.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(broadcastReceiver,issPassUpdate);

        IntentFilter locationUpdate = new IntentFilter(CONSTANTS.ACTION_LOCATION_UPDATE);
        locationUpdate.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(broadcastReceiver,locationUpdate);

        IntentFilter errorupdate = new IntentFilter(CONSTANTS.ACTION_FETCH_ERROR);
        errorupdate.addAction((Intent.CATEGORY_DEFAULT));
        registerReceiver(broadcastReceiver,errorupdate );


    }

    private void hideSoftKeybord()
    {

    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
        broadcastReceiver =null;


    }

    @Override
    public void onListFragmentInteraction(ISS_Pass item) {
        Log.d(TAG, "onListFragmentInteraction: Entered");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(TAG, "onFragmentInteraction: Entered");
    }


    public class ISS_PassListner extends BroadcastReceiver {
        public ISS_PassListner()
        {
            super();
        }
        @Override
        public void onReceive(Context context, Intent intent) {

          if (intent.getAction()==CONSTANTS.ACTION_DATA_UPDATE){
                Log.d(TAG, "onReceive: data updated");
              mPass.setText(String.valueOf(missPassData.getmISSPassdata().size()));

              FragmentManager fm = getSupportFragmentManager();
              FragmentTransaction ft = fm.beginTransaction();

                if(mfragment==null){
                    mfragment = new RecyclerFragment();
                    ft.add(R.id.fragmentContainer, mfragment).commit();
              }
              else
              {
                  ft.detach(mfragment);
                  ft.attach(mfragment);
                  ft.commit();
              }
                mGetPrediction.setEnabled(true);
                scheduleNextPrediction();


          }
          else if(intent.getAction()==CONSTANTS.ACTION_LOCATION_UPDATE)
          {
              Log.d(TAG, "onReceive: location update entered");
              mETaltitude.setText(String.format("%.2f",ISS_Pass.getMlatestCurrentLocation().getLatitude()));
              mETlatitude.setText(String.format("%.2f",ISS_Pass.getMlatestCurrentLocation().getAltitude()));
              mETlongtude.setText(String.format("%.2f",ISS_Pass.getMlatestCurrentLocation().getLongitude()));


              Intent fetchIntent = new Intent(getApplicationContext(), ISS_Service.class);
              fetchIntent.setAction("com.example.tanma.iss_pass.action.GET");
              startService(fetchIntent);

          }else if (intent.getAction()==CONSTANTS.ACTION_FETCH_ERROR)
          {
              Bundle bundle = intent.getExtras();


              mErrorInfo.setText(bundle.getString("message"));
          }



        }
        public  void scheduleNextPrediction()
        {
            if(missPassData==null){
                return;
            }
            setAlarm(getnextAlarm());




        }

        public void setAlarm(long seconds)
        {
            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            Calendar cal  = Calendar.getInstance();
            if(seconds<60)
            {
                seconds=5;
            }
            else
             cancelAlarms();

             cal.set(Calendar.SECOND, (int) seconds);



           Intent intent = new Intent(getApplicationContext(),PredictionScheduler.class);

           PendingIntent pendingIntent = PendingIntent.getBroadcast( getApplicationContext(),1,intent,0);

            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            {
                Log.d(TAG, "schduleNextfetch: below kitkal");
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        cal.getTimeInMillis(), pendingIntent);
                Toast.makeText(getBaseContext(),
                        "call alarmManager.set()",
                        Toast.LENGTH_LONG).show();
            }else{
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() +
                                seconds * 1000,pendingIntent);
                Log.d(TAG, "schduleNextfetch: above kitkat for" + seconds);
            /*alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    cal.getTimeInMillis(), pendingIntent);*/
               /* Toast.makeText(getBaseContext(),
                        "call alarmManager.setExact()"+seconds,
                        Toast.LENGTH_LONG).show();*/
            }
        }

        public  long getnextAlarm() {
            Calendar cal = Calendar.getInstance();
            TimeZone tz = cal.getTimeZone();
            SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
             format.setTimeZone(tz);

            Date date1 = null;
            Date date2 = null;

            date1 = new Date();

            Date gmt = new Date(format.format(new Date()));
            long timestamp= (long) missPassData.getmISSPassdata().get(0).getRisetime();
             date2 = new Date(timestamp*1000);


            long diff = date2.getTime() - date1.getTime();

           long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
           long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
           long hours = TimeUnit.MICROSECONDS.toHours(diff);


            return seconds/2;
        }
        public void cancelAlarms()
        {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getApplicationContext(),PredictionScheduler.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast( getApplicationContext(),1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
            Log.d(TAG, "cancelAlarms: Completed");

        }
    }

}
