package com.example.psniproject.MainApp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.psniproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VictimFragment extends Fragment {

    private View view;


    public VictimFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_victim, container, false);

        return view;
    }

}
