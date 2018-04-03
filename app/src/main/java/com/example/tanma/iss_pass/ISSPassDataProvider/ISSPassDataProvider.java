package com.example.tanma.iss_pass.ISSPassDataProvider;

import java.util.ArrayList;

/*****************************
 * class ISSPassDataProvider
 * purpose: obtains the Iss Pass data from ISS_Service
 * passes it on to UI to display in list view
 ****************************/
public class ISSPassDataProvider {
    private static  ISSPassDataProvider instance = new ISSPassDataProvider();
    private   ArrayList<ISS_Pass> mISSPassdata;

    private ISSPassDataProvider()
    {       mISSPassdata = new ArrayList<>();   }

    synchronized public static ISSPassDataProvider getInstance()
    {
        if(instance==null)
        {
            instance = new ISSPassDataProvider();
        }
        return instance;
    }
   public  void clearData()
   {    mISSPassdata.clear();   }

   synchronized public ArrayList<ISS_Pass> getmISSPassdata() {
        return mISSPassdata;
    }

    synchronized public  void  setmISSPassdata(ArrayList<ISS_Pass> mISSPassdata) {
        this.mISSPassdata = mISSPassdata;
    }
}
