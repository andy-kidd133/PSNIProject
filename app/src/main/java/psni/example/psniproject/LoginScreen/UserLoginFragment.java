package psni.example.psniproject.LoginScreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import psni.example.psniproject.LoginScreen.Models.Courthouse;
import psni.example.psniproject.LoginScreen.Models.OfficerProfile;
import psni.example.psniproject.LoginScreen.Models.VictimProfile;
import psni.example.psniproject.MainApp.MainActivity;
import psni.example.psniproject.MainApp.MyJourneyFragment;
import psni.example.psniproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;


public class UserLoginFragment extends Fragment {

    private Button loginButton;
    private TextInputLayout username, password;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private int counter = 5;
    private static int profileCountOfficer, profileCountVictim;
    public static boolean alreadyExecuted = false;
    public static boolean officerArrayCreated = false;

    public static ArrayList<String> victimIDs = new ArrayList<>();
    public static ArrayList<String> officerIDs = new ArrayList<>();

    public static ArrayList<VictimProfile> victimProfiles = new ArrayList<>();
    public static ArrayList<OfficerProfile> officerProfiles = new ArrayList<>();

    public static ArrayList<String> officerNames = new ArrayList<>();

    public UserLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_login, container, false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.background));

        //add courthouses to ArrayList only once
        if (!alreadyExecuted) {
            MyJourneyFragment.setCourthouseData();
            MyJourneyFragment.courthouses = removeDuplicates(MyJourneyFragment.courthouses);
            alreadyExecuted = true;
        }

        loginButton = view.findViewById(R.id.loginBtn);
        username = view.findViewById(R.id.etUsername);
        password = view.findViewById(R.id.etPassword);
        Button adminButton = view.findViewById(R.id.adminBtn);
        Button skipButton = view.findViewById(R.id.skip);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        officerNames.clear();
        getVictimIDsandProfiles();
        getOfficerIDsandProfiles();

        progressDialog = new ProgressDialog(getActivity());

        FirebaseUser user = firebaseAuth.getCurrentUser();

        /*if (user != null) {
            startActivity(new Intent(getActivity(), MainActivity.class));
        }*/

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity) getActivity()).setViewPager(1);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateCredentials(username.getEditText().getText().toString(), password.getEditText().getText().toString());
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });


        return view;
    }


    private void validateCredentials(String userEmail, String userPassword) {

        progressDialog.setMessage("Signing In..");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    pushDeviceToken();
                    Toast.makeText(getActivity(), "Login Successful.", Toast.LENGTH_SHORT).show();
                    resetEditTexts();
                    startActivity(new Intent(getActivity(), MainActivity.class));

                } else {
                    counter--;
                    Toast.makeText(getActivity(), "You have: " + counter + " attempts remaining", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Username or Password incorrect.", Toast.LENGTH_SHORT).show();
                    if (counter == 0) {
                        loginButton.setEnabled(false);
                    }
                }
            }
        });
    }

    private void resetEditTexts() {

        username.getEditText().getText().clear();
        password.getEditText().getText().clear();

    }

    private void pushDeviceToken() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("tag", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Tokens");
                        ref.child(firebaseAuth.getCurrentUser().getUid()).setValue(token);

                        //DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(firebaseAuth.getUid());
                        //myRef.child("Token").setValue(token);

                        // Log and toast
                        Log.d("FCMToken", token);
                        //Toast.makeText(LoginActivity.this, token, Toast.LENGTH_LONG).show();
                    }
                });

    }


    //method to remove duplicate courthouses if Activity is restarted and
    //courthouses are recreated

    public static ArrayList<Courthouse> removeDuplicates(ArrayList<Courthouse> list) {

        ArrayList<Courthouse> newList = new ArrayList<>();

        for (Courthouse courthouse : list) {
            if (!newList.contains(courthouse)) {
                newList.add(courthouse);
            }
        }
        list = newList;
        return list;
    }

    public static ArrayList<String> removeStringDuplicates(ArrayList<String> list) {

        ArrayList<String> newList = new ArrayList<>();

        for (String string : list) {
            if (!newList.contains(string)) {
                newList.add(string);
            }
        }
        list = newList;
        return list;
    }


    /*public void getVictimIDs() {

        System.out.println("Before attaching listener");
        databaseReference.child("victims").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    System.out.println("Getting victim profile and adding ID");
                    victimIDs.add(snapshot.getKey());
                    victimIDs = removeStringDuplicates(victimIDs);
                    Log.e("VictimIDS.....", victimIDs.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed");
            }
        });
    }*/

    public void getVictimIDsandProfiles() {

        System.out.println("Before attaching listener");
        databaseReference.child("victims").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    System.out.println("Adding ID to VictimArray");
                    victimIDs.add(snapshot.getKey());

                    VictimProfile victimProfile = snapshot.getValue(VictimProfile.class);

                    //logic to stop duplicating VictimProfileDataSnapshots
                    if (profileCountVictim < dataSnapshot.getChildrenCount()) {
                        victimProfiles.add(victimProfile);
                        profileCountVictim++;
                        System.out.println("Profile Count....." + profileCountVictim);
                    }
                    System.out.println("Number of victims to be retrieved: " + dataSnapshot.getChildrenCount());

                    victimIDs = removeStringDuplicates(victimIDs);
                    Log.e("VictimIDS.....", victimIDs.toString());
                    Log.e("Victim profiles.....", victimProfiles.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed");
            }
        });
    }


    public void getOfficerIDsandProfiles() {

        System.out.println("Before attaching listener");
        databaseReference.child("officers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    System.out.println("Adding ID to OfficerArray");
                    officerIDs.add(snapshot.getKey());

                    OfficerProfile officerProfile = snapshot.getValue(OfficerProfile.class);
                    officerNames.add(officerProfile.getmFirstName() + " " + officerProfile.getmSurname());

                    //logic to stop duplicating OfficerProfileDataSnapshots into Officer array for spinner
                    if (profileCountOfficer < dataSnapshot.getChildrenCount()) {
                        officerProfiles.add(officerProfile);
                        profileCountOfficer++;
                        System.out.println("Profile Count....." + profileCountOfficer);
                    }
                    System.out.println("Number of officers to be retrieved: " + dataSnapshot.getChildrenCount());

                    officerNames = removeStringDuplicates(officerNames);
                    officerIDs = removeStringDuplicates(officerIDs);
                    Log.e("OfficerIDS.....", officerIDs.toString());
                    Log.e("Officer profiles.....", officerProfiles.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed");
            }
        });

    }
}
