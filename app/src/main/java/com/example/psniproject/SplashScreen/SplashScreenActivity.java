package com.example.psniproject.SplashScreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.psniproject.LoginScreen.LoginActivity;
import com.example.psniproject.LoginScreen.UserProfile;
import com.example.psniproject.MainApp.MainActivity;
import com.example.psniproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreenActivity extends AppCompatActivity {

    private int SLEEP_TIMER = 4;
    ImageView vsLogo;
    Animation fadeIn;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(getResources().getColor(R.color.background));

        setContentView(R.layout.activity_splash_screen);
        //getSupportActionBar().hide();
        SplashLauncher splashLauncher = new SplashLauncher();
        splashLauncher.start();
        splashLauncher.logoLauncher();
        firebaseAuth.signOut();

    }

    private class SplashLauncher extends Thread {

        public void run() {
            try {
                sleep(1000 * SLEEP_TIMER);

            }catch(InterruptedException e){
                e.printStackTrace();
            }

            /*Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
            SplashScreenActivity.this.finish();*/

            //CHANGE TO LOGIN ACTIVITY

            if (firebaseAuth.getCurrentUser() == null) {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                SplashScreenActivity.this.finish();
            }
            else {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                        Toast.makeText(SplashScreenActivity.this, "Welcome back " + userProfile.getfName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(SplashScreenActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                    }
                });
                SplashScreenActivity.this.finish();
            }

        }

        private void logoLauncher() {
            vsLogo = findViewById(R.id.vsLogo);
            fadeIn = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.fadein);
            vsLogo.setAnimation(fadeIn);
        }
    }
}
