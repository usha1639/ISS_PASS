package com.example.tanma.iss_pass;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tanma.iss_pass.ISSPassFragment.OnListFragmentInteractionListener;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ISS_Data.ISS_Pass;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ISS_Pass} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<ISS_Pass> mValues;
    private final OnListFragmentInteractionListener mListener;


    public MyItemRecyclerViewAdapter(List<ISS_Pass> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        long timestamp = (long) mValues.get(position).getRisetime();
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(tz);


        String localTime = sdf.format(new Date(timestamp * 1000));

        String minutes = String.format(String.format("%.2f",(((mValues.get(position).getDuration()) % 86400) % 3600) / 60)) ;
        holder.mTimeView.setText(localTime);
        holder.mDurationView.setText(minutes);
        holder.itemView.setActivated(true);


    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTimeView;
        public final TextView mDurationView;
        public ISS_Pass mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
          mTimeView = (TextView) view.findViewById(R.id.risetime);
          mDurationView = (TextView) view.findViewById(R.id.duration);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}
