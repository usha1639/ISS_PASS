package ISS_Data;

import android.location.Location;

import java.sql.Time;

/**
 * Created by tanma on 3/27/2018.
 */

public class ISS_Pass {
    private static Location mlatestCurrentLocation;
    private static Time nextPredictionTime ;
    private static int n =10;
    private double risetime;
    private double duration;
    private static String ISS_PASS_Error = "" ;

    public static int getN() {
        return n;
    }

    public static void setN(int n) {
        ISS_Pass.n = n;
    }




    public void setRisetime(double risetime) {
        this.risetime = risetime;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public static Location getMlatestCurrentLocation() {
        return mlatestCurrentLocation;
    }

    public static void setMlatestCurrentLocation(Location mlatestCurrentLocation) {
        ISS_Pass.mlatestCurrentLocation = mlatestCurrentLocation;
    }

    public static Time getNextPredictionTime() {
        return nextPredictionTime;
    }

    public static void setNextPredictionTime(Time nextPredictionTime) {
        ISS_Pass.nextPredictionTime = nextPredictionTime;
    }

    public double getRisetime() {
        return risetime;
    }

    public double getDuration() {
        return duration;
    }

    public String getISS_PASS_Error() {
        return ISS_PASS_Error;
    }

    public void setISS_PASS_Error(String ISS_PASS_Error) {
        this.ISS_PASS_Error = ISS_PASS_Error;
    }
}
