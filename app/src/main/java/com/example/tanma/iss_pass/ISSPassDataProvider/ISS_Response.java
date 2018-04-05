package com.example.tanma.iss_pass.ISSPassDataProvider;

import android.util.Log;

import com.example.tanma.iss_pass.Utils.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.client.HttpClient;

/**
 * Created by usha on 3/27/2018.
 */

public class ISS_Response {
    private final static String TAG=ISS_Response.class.getName();
    private String murl;
    public String error;
    private ISSPassDataProvider issPassData = ISSPassDataProvider.getInstance();


    public ISS_Response()
    {   murl="";
        error = "";
    }

    public String getError() {
        return error;
    }

    public void setUrl(boolean custom, String customUrl)
    {
        if(!custom) {
            murl = new String(Constants.url + ISS_Pass.getMlatestCurrentLocation().getLatitude() + "&lon=" + ISS_Pass.getMlatestCurrentLocation().getLongitude() + "&alt=" + "&n=" + ISS_Pass.getN());

            if (ISS_Pass.getMlatestCurrentLocation().getAltitude() > 0){
                murl =new String ( Constants.url+ ISS_Pass.getMlatestCurrentLocation().getLatitude() + "&lon=" + ISS_Pass.getMlatestCurrentLocation().getLongitude() + "&alt=" + ISS_Pass.getMlatestCurrentLocation().getAltitude() + "&n=" + ISS_Pass.getN());
                Log.d(TAG, "setUrl: " + ISS_Pass.getN());
            }
        }else{
         murl = new String(customUrl);
        }
    }

    private  String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /*
     * parse the new data in success case,
     * communicate to error to ui in failure case
     */

    public boolean getHttpClientFetch()
    {
        InputStream inputStream;
        String response;
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpResponse httpResponse = httpclient.execute(new HttpGet(murl));
            inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null)
            {
                response = getStringFromInputStream(inputStream);
                JSONObject jresponse = new JSONObject(response);

                if(jresponse.getString("message").equals("failure"))
                {
                    error = new String(jresponse.getString("reason"));
                    return true;
                }

            JSONArray jresponsearray = jresponse.getJSONArray("response");
            JSONObject req = jresponse.getJSONObject("request");
            ArrayList<ISS_Pass> data = new ArrayList<>();
            int length = jresponsearray.length();

            Log.d(TAG, "getHttpClientFetch: length" + jresponsearray.length());
            JSONObject obj = jresponsearray.getJSONObject(0);

            if (issPassData.getmISSPassdata().size()==length && issPassData.getmISSPassdata().get(0).getRisetime() == obj.getInt(Constants.risetime)) {
                return false;}

                Log.d(TAG, "getHttpClientFetch: length" + jresponsearray.length());

                for (int index = 0; index < length; index++) {
                JSONObject object = jresponsearray.getJSONObject(index);
                ISS_Pass item = new ISS_Pass();
                item.setDuration(object.getInt(Constants.duration));
                item.setRisetime(object.getInt(Constants.risetime));
                data.add(item);
            }
            issPassData.clearData();
            issPassData.setmISSPassdata(data);
        }else
          return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    }
