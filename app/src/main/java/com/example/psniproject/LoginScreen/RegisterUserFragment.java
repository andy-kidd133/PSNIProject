package com.example.psniproject.LoginScreen;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.psniproject.MainApp.MainActivity;
import com.example.psniproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RegisterUserFragment extends Fragment {

    private TextInputLayout tiFirstName, tiSurname, tiEmail, tiPassword, tiPasswordCon, tiPhoneNum, tiAddress, tiCity, tiCounty, tiPostcode, tiDOB;
    private FirebaseAuth firebaseAuth;
    private View view;
    private String fName, sName, eMail, pWord1, pWord2, pNum, address, city, county, postcode, DOB;

    public RegisterUserFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_register_user, container, false);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        Button backButton = view.findViewById(R.id.backBtn);
        Button regButton = view.findViewById(R.id.regBtn);

        //registering user
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateRegistration() && checkPasswordMatch(pWord1, pWord2)) {

                    String user_email = tiEmail.getEditText().getText().toString().trim();
                    String user_password = tiPassword.getEditText().getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                //firebaseAuth.signOut();
                                sendUserData();
                                Toast.makeText(getActivity(), "Registration Successful & upload complete.", Toast.LENGTH_SHORT).show();
                                resetEditTexts();
                                ((LoginActivity)getActivity()).setViewPager(0);
                            }else {
                                Toast.makeText(getActivity(), "Registration Failed.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });

        //back to admin login screen
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity)getActivity()).setViewPager(1);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    //String fName, sName, eMail, pWord,pNum, address, city, county, postcode, DOB;

    private void setupUIViews() {
        tiFirstName = view.findViewById(R.id.etFirstName);
        tiSurname = view.findViewById(R.id.etSurname);
        tiEmail = view.findViewById(R.id.etEmail);
        tiPassword = view.findViewById(R.id.etPassword);
        tiPasswordCon = view.findViewById(R.id.etPasswordConfirm);
        tiPhoneNum = view.findViewById(R.id.etPhoneNum);
        tiAddress = view.findViewById(R.id.etAddress);
        tiCity = view.findViewById(R.id.etCity);
        tiCounty = view.findViewById(R.id.etCounty);
        tiPostcode = view.findViewById(R.id.etPostcode);
        tiDOB = view.findViewById(R.id.etDOB);
    }

    private Boolean validateRegistration() {
        Boolean result = false;

        fName = tiFirstName.getEditText().getText().toString();
        sName = tiSurname.getEditText().getText().toString();
        eMail = tiEmail.getEditText().getText().toString();
        pWord1 = tiPassword.getEditText().getText().toString();
        pWord2 = tiPasswordCon.getEditText().getText().toString();
        eMail = tiEmail.getEditText().getText().toString();
        pNum = tiPhoneNum.getEditText().getText().toString();
        address = tiAddress.getEditText().getText().toString();
        city = tiCity.getEditText().getText().toString();
        county = tiCounty.getEditText().getText().toString();
        postcode = tiPostcode.getEditText().getText().toString();
        DOB = tiDOB.getEditText().getText().toString();

        //checkPasswordMatch(pWord1, pWord2);

        if(fName.isEmpty() || sName.isEmpty() || eMail.isEmpty() || pWord1.isEmpty()) {
            Toast.makeText(getActivity(), "Please complete all fields.", Toast.LENGTH_SHORT).show();
        }
        else {
            result = true;
        }

        return result;
    }


    private void sendUserData() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile userProfile = new UserProfile(fName, sName, eMail, pNum, address, city, county, postcode, DOB);
        myRef.setValue(userProfile);

    }

    //List<View> edit_texts;

    public void resetEditTexts() {

        tiFirstName.getEditText().getText().clear();
        tiSurname.getEditText().getText().clear();
        tiEmail.getEditText().getText().clear();
        tiPassword.getEditText().getText().clear();
        tiPasswordCon.getEditText().getText().clear();
        tiPhoneNum.getEditText().getText().clear();
        tiAddress.getEditText().getText().clear();
        tiCity.getEditText().getText().clear();
        tiPostcode.getEditText().getText().clear();
        tiCounty.getEditText().getText().clear();
        tiDOB.getEditText().getText().clear();

    }

    public boolean checkPasswordMatch(String password1, String password2) {

        boolean result = false;

        if (!password1.equals(password2)) {
            Toast.makeText(getActivity(), "Passwords do not match.", Toast.LENGTH_LONG).show();
        } else {
            result = true;
        }
        return result;
    }
}
