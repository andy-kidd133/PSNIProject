package com.example.psniproject.LoginScreen;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
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

    private TextInputLayout firstName, surname, email, password;
    private FirebaseAuth firebaseAuth;
    private View view;
    String fName, sName, eMail, pWord;

    public RegisterUserFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_register_user, container, false);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        Button backButton = view.findViewById(R.id.backBtn);
        //Button regButton = view.findViewById(R.id.regBtn);

        //registering user
        /*regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateRegistration()) {

                    String user_email = email.getEditText().getText().toString().trim();
                    String user_password = password.getEditText().getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                //Upload data to "VictimSupportDB"
                                //firebaseAuth.signOut();
                                sendUserData();
                                Toast.makeText(getActivity(), "Registration Successful & upload complete.", Toast.LENGTH_SHORT).show();
                                resetEditTexts();
                            }else {
                                Toast.makeText(getActivity(), "Registration Failed.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });*/

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

    private void setupUIViews() {
        firstName = view.findViewById(R.id.etFirstName);
        surname = view.findViewById(R.id.etSurname);
        email = view.findViewById(R.id.etEmail);
        password = view.findViewById(R.id.etPassword);
    }

    private Boolean validateRegistration() {
        Boolean result = false;

        fName = firstName.getEditText().getText().toString();
        sName = surname.getEditText().getText().toString();
        eMail = email.getEditText().getText().toString();
        pWord = password.getEditText().getText().toString();

        if(fName.isEmpty() || sName.isEmpty() || eMail.isEmpty() || pWord.isEmpty()) {
            Toast.makeText(getActivity(), "Please complete all fields.", Toast.LENGTH_SHORT).show();
        }else {
            result = true;
        }

        return result;
    }


    private void sendUserData() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile userProfile = new UserProfile(fName, sName, eMail);
        myRef.setValue(userProfile);

    }

    //List<View> edit_texts;

    public void resetEditTexts() {

        firstName.getEditText().getText().clear();
        surname.getEditText().getText().clear();
        email.getEditText().getText().clear();
        password.getEditText().getText().clear();
    }
}
