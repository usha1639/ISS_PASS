package com.example.tanma.iss_pass.View;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.example.tanma.iss_pass.ISSPassDataProvider.ISSPassDataProvider;
import com.example.tanma.iss_pass.ISSPassDataProvider.ISS_Pass;
import com.example.tanma.iss_pass.R;


/**********************
 * class RecyclerFragment
 * purpose : displays  Iss_pass data in a list view
 * refreshes it when the data information is changed
 * **********************************/
public class RecyclerFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    ArrayList<ISS_Pass> dataList;
    ISSPassDataProvider issPassData;
    private OnFragmentInteractionListener mListener;

    public RecyclerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_recycler, container, false);
        issPassData = ISSPassDataProvider.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        dataList = issPassData.getmISSPassdata();
        recyclerAdapter = new RecyclerAdapter(view.getContext(),dataList );
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
