package com.example.tanma.iss_pass.ISSPassDataController;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.example.tanma.iss_pass.ISSPassDataProvider.ISSPassDataProvider;
import com.example.tanma.iss_pass.Utils.TimeConversionUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import static android.content.Context.ALARM_SERVICE;


/****************************
*class : DataFetchScheduler
* purpose : schedule a request for ISS_Pass data fetch half the way to next risetime.
****************************/
public class DataFetchScheduler {
    private final static String TAG = DataFetchScheduler.class.getName();
    ISSPassDataProvider issPassDataProvider = ISSPassDataProvider.getInstance();
    TimeConversionUtil mtimeConversionUtil;
    private Context mContext;
    public  DataFetchScheduler(Context context)
    {
        mContext = context;
        mtimeConversionUtil = new TimeConversionUtil();
    }

    /*Func: setNextIssPassDataFetch()
    * Determines when to schedule the next data fetch,
    * cancels any requests pending for data fetch and set a new request at determined time.*/
    public void setNextIssPassDataFetch() {

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        long seconds = getnextRequestTime();
        Log.d(TAG, "setNextIssPassDataFetch: " + TimeUnit.SECONDS.toMinutes(seconds));
        Calendar cal = Calendar.getInstance();
        if (seconds < 60) {
            seconds = 5;
        } else
            cancelAlarms();
        cal.set(Calendar.SECOND, (int) seconds);

        Intent intent = new Intent(mContext, PredictionListner.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 1, intent, 0);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Log.d(TAG, "schduleNextfetch: below kitkal");
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    cal.getTimeInMillis(), pendingIntent);

        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            seconds * 1000, pendingIntent);
            Log.d(TAG, "schduleNextfetch: above kitkat for" + seconds);
        }
    }

    /************************
     * *Func : cancelAlarms()
     * camcels all the pending requests for ISS Pass data fetch
     ***********************/
    public void cancelAlarms()
    {
        AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext,PredictionListner.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast( mContext,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        Log.d(TAG, "cancelAlarms: Completed");
    }

    /*************************
    *Func : getnextRequestTime()
    * get the how much time for next satalite Pass risetime from current system time,
    * halve it and return the result
     *****************************/
    public  long getnextRequestTime() {
        long latertimestamp= (long) issPassDataProvider.getmISSPassdata().get(0).getRisetime();
        return mtimeConversionUtil.getTimeDifference(latertimestamp)/2;
    }

}
