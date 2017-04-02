package com.example.iapp.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.iapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rahul on 02/04/17.
 */


public class OccassionFragment extends Fragment implements View.OnClickListener{

    @Bind(R.id.fab)
    FloatingActionButton fab;

    public OccassionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_occassion, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.fab:
                break;
        }
    }
}