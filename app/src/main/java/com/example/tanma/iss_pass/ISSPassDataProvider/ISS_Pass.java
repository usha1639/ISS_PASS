package com.example.tanma.iss_pass.ISSPassDataProvider;

import android.location.Location;

/**
 * Created by tanma on 3/27/2018.
 */

public class ISS_Pass {
    private static Location mlatestCurrentLocation;
    private static int n =10;
    private double risetime;
    private double duration;

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

    public double getRisetime() {
        return risetime;
    }
    public double getDuration() {
        return duration;
    }
   }
