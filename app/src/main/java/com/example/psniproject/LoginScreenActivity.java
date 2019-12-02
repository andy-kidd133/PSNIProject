package com.example.psniproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginScreenActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText username, password;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login_screen);
        Button adminButton = findViewById(R.id.adminBtn);
        loginButton = findViewById(R.id.loginBtn);
        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginScreenActivity.this, AdminLoginScreenActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateCredentials(username.getText().toString(), password.getText().toString());
            }
        });
    }

    private void validateCredentials(String userEmail, String userPassword) {

        progressDialog.setMessage("Signing In..");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginScreenActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginScreenActivity.this, MainActivity.class));
                }else {
                    counter--;
                    Toast.makeText(LoginScreenActivity.this, "You have: " + String.valueOf(counter) + " attempts remaining", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(LoginScreenActivity.this, "Username or Password incorrect.", Toast.LENGTH_SHORT).show();
                    if(counter==0) {
                        loginButton.setEnabled(false);
                    }
                }
            }
        });
    }
}
