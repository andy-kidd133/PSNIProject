package psni.example.psniproject.MainApp;


import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import psni.example.psniproject.LoginScreen.UserLoginFragment;
import psni.example.psniproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment {

    private View view;
    private CardView cvVS, cvPPANI, cvNIDirect, cvMessages;
    private ImageView myJourneyButton;
    private TextView tvContactUs;


    public HomePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_page, container, false);

        //ImageButton victimSupport = view.findViewById(R.id.ibVictimSupport);
        //ImageButton pPANI = view.findViewById(R.id.ibPPANI);
        //TextView myJourneyLink = view.findViewById(R.id.myJourneyLink);

        cvVS = view.findViewById(R.id.cardViewVS);
        cvPPANI = view.findViewById(R.id.cardViewPPANI);
        cvNIDirect = view.findViewById(R.id.cardViewNID);
        cvMessages = view.findViewById(R.id.cardViewMessages);
        myJourneyButton = view.findViewById(R.id.myJourneyButton);
        tvContactUs= view.findViewById(R.id.tvContactUs);

        cvVS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("LOADING VS page....");
                ((MainActivity)getActivity()).setViewPager(2);
            }
        });

        cvPPANI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("LOADING PPANI page....");
                ((MainActivity)getActivity()).setViewPager(3);
            }
        });

        cvNIDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("LOADING NIDirect page....");
                ((MainActivity)getActivity()).setViewPager(4);
            }
        });

        cvMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("LOADING messages page....");
                ((MainActivity)getActivity()).setViewPager(5);
            }
        });

        myJourneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("LOADING journey page....");
                ((MainActivity)getActivity()).setViewPager(1);
            }
        });

        tvContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("LOADING contact us page....");
                ((MainActivity)getActivity()).setViewPager(6);
            }
        });

        if(!UserLoginFragment.alreadyExecuted) {
            MyJourneyFragment.setCourthouseData();
            MyJourneyFragment.courthouses = UserLoginFragment.removeDuplicates(MyJourneyFragment.courthouses);
            UserLoginFragment.alreadyExecuted = true;
        }


        return view;
    }

}
