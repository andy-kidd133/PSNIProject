package com.example.psniproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginScreenActivity extends AppCompatActivity {

    private Button backButton;
    private Button loginButton;
    private EditText username;
    private EditText password;
    private int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_admin_login_screen);
        backButton = findViewById(R.id.backBtn);
        loginButton = findViewById(R.id.loginBtn);
        username = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLoginScreenActivity.this, LoginScreenActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateCredentials(username.getText().toString(), password.getText().toString());
            }
        });
    }

    private void validateCredentials(String adminUsername, String adminPassword) {
        if((adminUsername.equals("Admin")) && (adminPassword.equals("admin1234"))) {
            Intent intent = new Intent(AdminLoginScreenActivity.this, AdminRegisterUserActivity.class);
            startActivity(intent);
        }else {
            counter --;
            Toast.makeText(this, "You have: " + String.valueOf(counter) + " attempts remaining", Toast.LENGTH_SHORT).show();
            if (counter == 0) {
                loginButton.setEnabled(false);
                Toast.makeText(this, "Too many incorrect attempts. Account Locked.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
