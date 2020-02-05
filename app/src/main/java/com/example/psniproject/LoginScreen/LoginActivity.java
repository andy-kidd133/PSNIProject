package com.example.psniproject.LoginScreen;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import com.example.psniproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity {

    private MyFragmentPagerAdapter mMyFragmentPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_screen);
        //getWindow().setStatusBarColor(getResources().getColor(R.color.background));

        //getSupportActionBar().hide();

        mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.containter);
        setupViewPager(mViewPager);

        mViewPager.beginFakeDrag();
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
