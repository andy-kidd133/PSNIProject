package com.example.psniproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminRegisterUserActivity extends AppCompatActivity {

    private EditText firstName, surname, userName, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register_user);
        setupUIViews();

        Button backButton = findViewById(R.id.backBtn);
        Button regButton = findViewById(R.id.regBtn);

        //registering user
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateRegistration()) {
                    //register to Firebase
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
