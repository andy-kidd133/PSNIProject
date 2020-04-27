package psni.example.psniproject.MainApp.Messages;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import psni.example.psniproject.LoginScreen.Models.OfficerProfile;
import psni.example.psniproject.LoginScreen.Models.VictimProfile;
import psni.example.psniproject.LoginScreen.Models.UserType;
import psni.example.psniproject.LoginScreen.UserLoginFragment;
import psni.example.psniproject.MainApp.MainActivity;
import psni.example.psniproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    private View view;

    public UsersFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private List<OfficerProfile> officerProfiles;
    private ArrayList<VictimProfile> victimProfiles;

    private UserAdapter userAdapter;
    private FirebaseAuth firebaseAuth;
    private TextView loginMessage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_users, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        loginMessage = view.findViewById(R.id.loginMessage);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        officerProfiles = new ArrayList<>();
        victimProfiles = new ArrayList<>();

        if(MainActivity.userType == UserType.VICTIM) {
            readOfficer();
        }
        else if(MainActivity.userType == UserType.OFFICER) {
            readVictims();
        }

        return view;
    }

    private void readOfficer() {

        if(firebaseAuth.getCurrentUser() == null) {
            loginMessage.setText("Please log in to use this feature");
            loginMessage.setTextSize(20);
            loginMessage.setVisibility(View.VISIBLE);
        }
        else {
            DatabaseReference reference = FirebaseDatabase.getInstance()
                    .getReference("victims/" + firebaseAuth.getUid());

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    officerProfiles.clear();
                    final VictimProfile victimProfile = dataSnapshot.getValue(VictimProfile.class);
                    officerProfiles.add(victimProfile.getOfficerProfile());
                    userAdapter = new UserAdapter(getContext(), officerProfiles, null);
                    recyclerView.setAdapter(userAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void readVictims() {

        if(firebaseAuth.getCurrentUser() == null) {
            loginMessage.setText("Please log in to use this feature");
            loginMessage.setTextSize(20);
            loginMessage.setVisibility(View.VISIBLE);
        }
        else {
            DatabaseReference reference = FirebaseDatabase.getInstance()
                    .getReference("officers/" + firebaseAuth.getUid());

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    victimProfiles.clear();

                    ArrayList<String> victimIDs = new ArrayList<>();

                    victimIDs.clear();

                    final OfficerProfile officerProfile = dataSnapshot.getValue(OfficerProfile.class);
                    victimIDs = officerProfile.getVictimIds();
                    System.out.println("victimIDs that have been retrieved for matching..." + victimIDs);
                    //Collections.sort(victimIDs);
                    System.out.println("victimIDs SORTED that have been treieved for officer..." + victimIDs);

                    for (String ID : victimIDs) {
                        if(ID.length() < 10) {
                            //do nothing
                        }else {
                            victimProfiles.add(matchVictimwithVictimID(ID, UserLoginFragment.victimProfiles));
                        }
                    }
                    userAdapter = new UserAdapter(getContext(), null, victimProfiles);
                    recyclerView.setAdapter(userAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private VictimProfile matchVictimwithVictimID(String ID,
                            ArrayList<VictimProfile> victimProfiles) {

        VictimProfile victimProfile = new VictimProfile();

        for(int i =0; i<victimProfiles.size(); i++) {
            String idToTest = victimProfiles.get(i).getuID();
            if(ID.equals(idToTest)) {
                victimProfile = victimProfiles.get(i);
            }
        }
        return victimProfile;
    }

}
