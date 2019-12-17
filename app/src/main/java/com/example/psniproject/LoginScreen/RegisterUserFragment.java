package com.example.psniproject.LoginScreen;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.psniproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterUserFragment extends Fragment {

    private EditText firstName, surname, userName, email, password;
    private FirebaseAuth firebaseAuth;
    private View view;

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
                if(validateRegistration()) {
                    //Upload data to "VictimSupportDB"
                    String user_email = email.getText().toString().trim();
                    String user_password = password.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Registration Successful.", Toast.LENGTH_SHORT).show();
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
                ((LoginScreenActivity)getActivity()).setViewPager(1);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void setupUIViews() {
        firstName = view.findViewById(R.id.etFirstName);
        surname = view.findViewById(R.id.etSurname);
        userName = view.findViewById(R.id.etUsername);
        email = view.findViewById(R.id.etEmail);
        password = view.findViewById(R.id.etPassword);
    }

    private Boolean validateRegistration() {
        Boolean result = false;

        String fName = firstName.getText().toString();
        String sName = surname.getText().toString();
        String uName = userName.getText().toString();
        String eMail = email.getText().toString();
        String pWord = password.getText().toString();

        if(fName.isEmpty() || sName.isEmpty() || uName.isEmpty() || eMail.isEmpty() || pWord.isEmpty()) {
            Toast.makeText(getActivity(), "Please complete all fields.", Toast.LENGTH_SHORT).show();
        }else {
            result = true;
        }

        return result;
    }

}
