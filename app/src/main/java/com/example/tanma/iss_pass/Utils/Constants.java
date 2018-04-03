package com.example.tanma.iss_pass.Utils;

/******************************
 * class Constants
 * purpose : Holds constant string needed across the application
 ***************************/
public class Constants {
    public static String latitude ="latitude";
    public static String longitude ="longitude";
    public static String altitude = "altitude";
    public static String n = "altitude";
    public static String datetime ="datetime";
    public static String risetime ="risetime";
    public static String duration = "duration";
    public static String ACTION_DATA_UPDATE = "com.example.androidintentservice.DATA_UPDATED";
    public static String ACTION_LOCATION_UPDATE = "com.example.androidintentservice.LOCATION_UPDATED";
    public static String ACTION_FETCH_ERROR = "com.example.androidintentservice.FETCH_ERROR";
    public static final String ACTION_GET_ISS_PASS = "com.example.tanma.iss_pass.action.GET";
    public static final  String ACTION_GET_ISS_PASS_CUSTOM = "com.example.tanma.iss.custom.GET";
    public static final String url = "http://api.open-notify.org/iss-pass.json?lat=";
}
