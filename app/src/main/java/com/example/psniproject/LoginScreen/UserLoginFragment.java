package com.example.psniproject.LoginScreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.psniproject.MainApp.MainActivity;
import com.example.psniproject.MainApp.MyJourneyFragment;
import com.example.psniproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class UserLoginFragment extends Fragment {

    private Button loginButton;
    private TextInputLayout username, password;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private int counter = 5;
    public boolean alreadyExecuted = false;

    public UserLoginFragment() {
        // Required empty public constructor
    }

    //method to remove duplicate courthouses if Activity is restarted and
    //courthouses are recreated

    private static ArrayList<Courthouse> removeDuplicates(ArrayList<Courthouse> list) {

        ArrayList<Courthouse> newList = new ArrayList<>();

        for (Courthouse courthouse : list) {
            if(!newList.contains(courthouse)) {
                newList.add(courthouse);
            }
        }
        list = newList;
        return list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_user_login, container, false);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.background));

        //add courthouses to ArrayList only once
        if(!alreadyExecuted) {
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
        progressDialog = new ProgressDialog(getActivity());

        FirebaseUser user = firebaseAuth.getCurrentUser();

        /*if (user != null) {
            startActivity(new Intent(getActivity(), MainActivity.class));
        }*/

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity)getActivity()).setViewPager(1);
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
                if(task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Login Successful.", Toast.LENGTH_SHORT).show();
                    resetEditTexts();
                    startActivity(new Intent(getActivity(), MainActivity.class));

                }else {
                    counter--;
                    Toast.makeText(getActivity(), "You have: " + counter + " attempts remaining", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Username or Password incorrect.", Toast.LENGTH_SHORT).show();
                    if(counter==0) {
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

}
