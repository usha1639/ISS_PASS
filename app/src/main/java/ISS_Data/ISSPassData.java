package ISS_Data;

import java.util.ArrayList;

/**
 * Created by tanma on 3/27/2018.
 */

public class ISSPassData {
    private static  ISSPassData instance = new ISSPassData();
    private   ArrayList<ISS_Pass> mISSPassdata;

    private ISSPassData()
    {
        mISSPassdata = new ArrayList<>();

    }

    synchronized public static ISSPassData getInstance()
    {
        if(instance==null)
        {
            instance = new ISSPassData();
        }
        return instance;
    }
   public  void clearData()
   {
     mISSPassdata.clear();
   }

   synchronized public ArrayList<ISS_Pass> getmISSPassdata() {
        return mISSPassdata;
    }

    synchronized public  void  setmISSPassdata(ArrayList<ISS_Pass> mISSPassdata) {
        this.mISSPassdata = mISSPassdata;
    }


}
