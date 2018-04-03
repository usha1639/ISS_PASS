package com.example.tanma.iss_pass.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import com.example.tanma.iss_pass.ISSPassDataProvider.ISS_Pass;
import com.example.tanma.iss_pass.R;
import com.example.tanma.iss_pass.Utils.TimeConversionUtil;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    List<ISS_Pass> dataList;
    Context mContext;
    TimeConversionUtil mtimeConversionUtil;

    RecyclerAdapter(Context context, List<ISS_Pass> list)
    {
        dataList = list;
        mContext = context;
        mtimeConversionUtil = new TimeConversionUtil();
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
        holder.risetime.setText(mtimeConversionUtil.UTCtoFormat(timestamp));
        holder.duration.setText(mtimeConversionUtil.durationToMinutes(item.getDuration()) + " mins");
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
