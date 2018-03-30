package com.example.tanma.iss_pass;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ISS_Data.ISS_Pass;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    List<ISS_Pass> dataList;
    Context mContext;

    RecyclerAdapter(Context context, List<ISS_Pass> list)
    {
        dataList = list;
        mContext = context;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

       View view =  inflater.inflate(R.layout.recycler_list_item,parent,false);

       ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ISS_Pass item = dataList.get(position);

        long timestamp = (long) item.getRisetime();
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMMMM.dd GGG hh:mm aaa");
        sdf.setTimeZone(tz);


        String localTime = sdf.format(new Date(timestamp * 1000));

        String minutes = String.format(String.format("%.2f",(((item.getDuration()) % 86400) % 3600) / 60)) ;

        holder.risetime.setText(localTime);
        holder.duration.setText(minutes + " mins");


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView risetime;
        public TextView duration;


       public ViewHolder(View itemView) {
           super(itemView);

          risetime = (TextView) itemView.findViewById(R.id.RV_risetime);
          duration = (TextView) itemView.findViewById(R.id.RV_duration);

       }


   }
}
