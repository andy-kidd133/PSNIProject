package psni.example.psniproject.LoginScreen;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import psni.example.psniproject.LoginScreen.Notifications.Constants;
import psni.example.psniproject.R;


public class LoginActivity extends AppCompatActivity {

    private MyFragmentPagerAdapter mMyFragmentPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_screen);
        mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.containter);
        setupViewPager(mViewPager);
        mViewPager.beginFakeDrag();

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


    /**
     * Fragments created and added to ArrayList ready to be inflated
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager){
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UserLoginFragment(), "UserLoginFragment");          //0
        adapter.addFragment(new AdminLoginFragment(), "AdminLoginFragment");        //1
        adapter.addFragment(new RegisterUserFragment(), "RegisterUserFragment");    //2
        //adapter.addFragment(new HomePageFragment(), "HomePageFragment");
        viewPager.setAdapter(adapter);
    }

    /**
     * @param fragmentNumber
     */
    public void setViewPager(int fragmentNumber){
        mViewPager.setCurrentItem(fragmentNumber);
    }



}
