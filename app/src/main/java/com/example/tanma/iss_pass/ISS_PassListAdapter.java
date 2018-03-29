package com.example.tanma.iss_pass;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ISS_Data.ISSPassData;
import ISS_Data.ISS_Pass;

public class ISS_PassListAdapter extends RecyclerView.Adapter<ISS_PassListAdapter.viewHolder> {
    private static ISSPassData issPassData = ISSPassData.getInstance();
    private static String TAG = ISS_PassListAdapter.class.getName();
    private   List<ISS_Pass> listData;
    Context context;

    public void setList(List<ISS_Pass> l)
    {
        listData = l;
    }
    public ISS_PassListAdapter(List<ISS_Pass> data, Context context)

    {
        this.listData = issPassData.getmISSPassdata();
        this.context = context;

        for(int index =0 ; index < listData.size();index++)
            Log.d(TAG, "ISS_PassListAdapter: risetime : " + listData.get(index).getRisetime() + " duration : " + listData.get(index).getDuration() );
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_item2, parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: listdata values" + listData);
        holder.mItem = listData.get(position);
        holder.textViewDuration.setText(String.valueOf(listData.get(position).getDuration()));
        holder.textViewRisetime.setText(String.valueOf(listData.get(position).getRisetime()));

    }




    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        public final TextView textViewRisetime;
        public final TextView textViewDuration;
        public  ISS_Pass mItem;
        public final View mView;

        public viewHolder(View itemView) {
            super(itemView);
            mView  = itemView;
            textViewRisetime = (TextView) itemView.findViewById(R.id.risetime);
            textViewDuration = (TextView) itemView.findViewById(R.id.duration);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewDuration.getText() + "'";
        }

    }
}
