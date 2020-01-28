package com.example.psniproject.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.psniproject.LoginScreen.LoginActivity;
import com.example.psniproject.R;

public class SplashScreenActivity extends AppCompatActivity {

    private int SLEEP_TIMER = 4;
    ImageView vsLogo;
    Animation fromTop;
    Animation fadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(getResources().getColor(R.color.background));

        setContentView(R.layout.activity_splash_screen);
        //getSupportActionBar().hide();
        SplashLauncher splashLauncher = new SplashLauncher();
        splashLauncher.start();
        splashLauncher.logoLauncher();
    }

    private class SplashLauncher extends Thread {

        public void run() {
            try {
                sleep(1000 * SLEEP_TIMER);

            }catch(InterruptedException e){
                e.printStackTrace();
            }

            //CHANGE TO LOGIN ACTIVITY

            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
            SplashScreenActivity.this.finish();
        }

        private void logoLauncher() {
            vsLogo = findViewById(R.id.vsLogo);
            fadeIn = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.fadein);
            vsLogo.setAnimation(fadeIn);
        }
    }
}
