package com.example.psniproject.MainApp;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psniproject.LoginScreen.LoginActivity;
import com.example.psniproject.LoginScreen.MyFragmentPagerAdapter;
import com.example.psniproject.LoginScreen.UserLoginFragment;
import com.example.psniproject.LoginScreen.UserProfile;
import com.example.psniproject.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MyFragmentPagerAdapter mMyFragmentPagerAdapter;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private TextView headerName, headerEmail;
    private String uid;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //use view pager to inflate #0 - HomePageFragment
        mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container1);
        setupViewPager(mViewPager);

        setupMainPage();

        mViewPager.beginFakeDrag();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //when no user is signed in/present, there is a RunTimeException as null is being returned for
        //DB reference Uid. Need to create

        if (firebaseAuth.getCurrentUser() == null) {

            headerName.setText("Please sign in");
            headerEmail.setText("");

            //make logout menu item invisible

        } else {

            DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                    headerName.setText(userProfile.getfName());
                    headerEmail.setText(userProfile.getEmail());

                    //make login menu item invisible
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupMainPage() {

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        headerName = (TextView) header.findViewById(R.id.drawer_name);
        headerEmail = (TextView) header.findViewById(R.id.drawer_email);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Victim Support");

        //set up navDrawer using drawer_menu.xml & nav_header.xml
        drawer = findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.material_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                this.setViewPager(0);
                break;
            case R.id.nav_journey:
                this.setViewPager(1);
                break;
            case R.id.nav_victim:
                this.setViewPager(2);
                break;
            case R.id.nav_witness:
                this.setViewPager(3);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        firebaseAuth.signOut();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomePageFragment(), "HomePageFragment");        //0
        adapter.addFragment(new MyJourneyFragment(), "MyJourneyFragment");      //1
        adapter.addFragment(new VictimFragment(), "VictimFragment");            //2
        adapter.addFragment(new WitnessFragment(), "WitnessFragment");          //3
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber) {
        mViewPager.setCurrentItem(fragmentNumber);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem logout = menu.findItem(R.id.logoutOption);
        MenuItem login = menu.findItem(R.id.loginOption);
        if(firebaseAuth.getCurrentUser() == null)
        {
            logout.setVisible(false);
            login.setVisible(true);
        }
        else
        {
            logout.setVisible(true);
            login.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logoutOption:
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return true;
            case R.id.loginOption:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            case R.id.refreshOption:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}



