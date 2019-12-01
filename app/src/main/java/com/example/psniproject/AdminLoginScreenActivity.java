package com.example.psniproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginScreenActivity extends AppCompatActivity {

    Button backButton;
    Button loginButton;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_admin_login_screen);
        backButton = findViewById(R.id.backBtn);
        loginButton = findViewById(R.id.loginBtn);
        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLoginScreenActivity.this, LoginScreenActivity.class));
            }
        });

    }

}
