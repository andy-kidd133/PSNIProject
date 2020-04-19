package com.example.psniproject.LoginScreen;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.widget.Toast;

import com.example.psniproject.LoginScreen.Notifications.Constants;
import com.example.psniproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class LoginActivity extends AppCompatActivity {

    private MyFragmentPagerAdapter mMyFragmentPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_screen);


        mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.containter);
        setupViewPager(mViewPager);

        mViewPager.beginFakeDrag();

       /* FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("tag", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();


                        // Log and toast
                        Log.d("FCMToken", token);
                        //Toast.makeText(LoginActivity.this, token, Toast.LENGTH_LONG).show();
                    }
                });*/

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel mNotificationChannel =
                new NotificationChannel(Constants.CHANNEL_ID,
                        Constants.CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH);

        mNotificationChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
        mNotificationChannel.enableLights(true);
        mNotificationChannel.setLightColor(Color.CYAN);
        mNotificationChannel.enableVibration(true);
        mNotificationChannel.setVibrationPattern(new long [] {100,100,200,200,100,100,500});

        mNotificationManager.createNotificationChannel(mNotificationChannel);

    }


    private void setupViewPager(ViewPager viewPager){
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UserLoginFragment(), "UserLoginFragment");          //0
        adapter.addFragment(new AdminLoginFragment(), "AdminLoginFragment");        //1
        adapter.addFragment(new RegisterUserFragment(), "RegisterUserFragment");    //2
        //adapter.addFragment(new HomePageFragment(), "HomePageFragment");
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber){
        mViewPager.setCurrentItem(fragmentNumber);
    }



}
