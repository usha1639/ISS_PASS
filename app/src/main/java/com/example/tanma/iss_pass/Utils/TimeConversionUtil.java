package com.example.tanma.iss_pass.Utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeConversionUtil {

    public TimeConversionUtil()
    {}

    public String UTCtoFormat(long timestamp)
    {
        String time = "";
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMMMM.dd GGG hh:mm aaa");
        sdf.setTimeZone(tz);
        time = sdf.format(new Date(timestamp * 1000));
        return time;
    }

    public String durationToMinutes(double duration)
    {
        String minutes = String.format(String.format("%.2f",(((duration) % 86400) % 3600) / 60)) ;
        return minutes;
    }

    public long getTimeDifference( long laterTimeStamp)
    {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        Date date1 = null;
        Date date2 = null;
        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        format.setTimeZone(tz);
        date1 = new Date();
        date2 = new Date(laterTimeStamp*1000);
        long diff = date2.getTime() - date1.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        Log.d("TimeUtil", "getTimeDifference: "+TimeUnit.MILLISECONDS.toHours(diff));
        return seconds;
    }
}
