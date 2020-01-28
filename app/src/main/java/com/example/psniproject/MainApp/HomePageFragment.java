package com.example.psniproject.MainApp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.widget.Toolbar;

import com.example.psniproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment {

    private View view;

    //private Button myJButton;

    public HomePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_page, container, false);


        Button myJButton = view.findViewById(R.id.myJButton);
        ImageButton victimTile = view.findViewById(R.id.ibVictimTile);
        ImageButton witnessTile = view.findViewById(R.id.ibWitnessTile);

        victimTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setViewPager(2);
            }
        });

        witnessTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setViewPager(3);
            }
        });

        myJButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setViewPager(1);
            }
        });


        return view;
    }

}
