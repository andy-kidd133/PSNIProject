package com.example.psniproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class LoginScreenActivity extends AppCompatActivity {

    Button adminButton;
    Button loginButton;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login_screen);
        adminButton = findViewById(R.id.adminBtn);
        loginButton = findViewById(R.id.loginBtn);
        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);
        username.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginScreenActivity.this, AdminLoginScreenActivity.class));
            }
        });
    }
}
