package com.example.psniproject.LoginScreen;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import com.example.psniproject.R;


public class LoginScreenActivity extends AppCompatActivity {

    private LoginSectionStatePagerAdapter mLoginSectionStatePagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_screen);

        getSupportActionBar().hide();

        mLoginSectionStatePagerAdapter = new LoginSectionStatePagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.containter);
        setupViewPager(mViewPager);
    }


    private void setupViewPager(ViewPager viewPager){
        LoginSectionStatePagerAdapter adapter = new LoginSectionStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UserLoginFragment(), "UserLoginFragment");
        adapter.addFragment(new AdminLoginFragment(), "AdminLoginFragment");
        adapter.addFragment(new RegisterUserFragment(), "RegisterUserFragment");
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber){
        mViewPager.setCurrentItem(fragmentNumber);
    }

}
