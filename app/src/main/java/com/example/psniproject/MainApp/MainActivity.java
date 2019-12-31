package com.example.psniproject.MainApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.psniproject.LoginScreen.AdminLoginFragment;
import com.example.psniproject.LoginScreen.LoginSectionStatePagerAdapter;
import com.example.psniproject.LoginScreen.RegisterUserFragment;
import com.example.psniproject.LoginScreen.UserLoginFragment;
import com.example.psniproject.R;

public class MainActivity extends AppCompatActivity {

    private LoginSectionStatePagerAdapter mLoginSectionStatePagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginSectionStatePagerAdapter = new LoginSectionStatePagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.containter1);
        setupViewPager(mViewPager);

        mViewPager.beginFakeDrag();

    }


    private void setupViewPager(ViewPager viewPager){
        LoginSectionStatePagerAdapter adapter = new LoginSectionStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomePageFragment(), "HomePageFragment");
        adapter.addFragment(new MyJourneyFragment(), "MyJourneyFragment");
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber){
        mViewPager.setCurrentItem(fragmentNumber);
    }
}
