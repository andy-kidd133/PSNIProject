package com.example.psniproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminRegisterUserActivity extends AppCompatActivity {

    private EditText firstName, surname, userName, email, password;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register_user);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        Button backButton = findViewById(R.id.backBtn);
        Button regButton = findViewById(R.id.regBtn);

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
                                Toast.makeText(AdminRegisterUserActivity.this, "Registration Successful.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(AdminRegisterUserActivity.this, "Registration Failed.", Toast.LENGTH_SHORT).show();

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
                startActivity(new Intent(AdminRegisterUserActivity.this, AdminLoginScreenActivity.class));
            }
        });
    }

    private void setupUIViews() {
        firstName = findViewById(R.id.etFirstName);
        surname = findViewById(R.id.etSurname);
        userName = findViewById(R.id.etUsername);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
    }

    private Boolean validateRegistration() {
        Boolean result = false;

        String fName = firstName.getText().toString();
        String sName = surname.getText().toString();
        String uName = userName.getText().toString();
        String eMail = email.getText().toString();
        String pWord = password.getText().toString();

        if(fName.isEmpty() || sName.isEmpty() || uName.isEmpty() || eMail.isEmpty() || pWord.isEmpty()) {
            Toast.makeText(this, "Please complete all fields.", Toast.LENGTH_SHORT).show();
        }else {
            result = true;
        }

        return result;
    }

}
