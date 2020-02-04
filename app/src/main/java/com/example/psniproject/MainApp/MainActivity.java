package com.example.psniproject.MainApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
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

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        headerName = (TextView)header.findViewById(R.id.drawer_name);
        headerEmail = (TextView)header.findViewById(R.id.drawer_email);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //setUpNavigationDrawer();

        mViewPager.beginFakeDrag();


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


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                headerName.setText(userProfile.getfName());
                headerEmail.setText(userProfile.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    private void setupViewPager(ViewPager viewPager){
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomePageFragment(), "HomePageFragment");        //0
        adapter.addFragment(new MyJourneyFragment(), "MyJourneyFragment");      //1
        adapter.addFragment(new VictimFragment(), "VictimFragment");            //2
        adapter.addFragment(new WitnessFragment(), "WitnessFragment");          //3
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber){
        mViewPager.setCurrentItem(fragmentNumber);
    }


    //set up the navigation drawer, pulling users, name and first name from FirebaseDB using userProfile
    //reference and DatabaseReference

    /*private void setUpNavigationDrawer() {

        //set up toolbar


        //**************************************************
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                // Create the AccountHeader
                AccountHeader headerResult = new AccountHeaderBuilder()
                        .withActivity(MainActivity.this)
                        .withHeaderBackground(R.drawable.header)
                        .addProfiles(
                                new ProfileDrawerItem().withName(userProfile.getfName()).withEmail(userProfile.getEmail()).withIcon(getResources().getDrawable(R.drawable.profile))
                        )
                        .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                            @Override
                            public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                                return false;
                            }
                        })
                        .build();

                new DrawerBuilder().withActivity(MainActivity.this).build();

                //if you want to update the items at a later time it is recommended to keep it in a variable
                PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Home");
                PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("2nd page");

                //create the drawer and remember the `Drawer` result object
                Drawer result = new DrawerBuilder()
                        .withActivity(MainActivity.this)
                        .withAccountHeader(headerResult)
                        .withToolbar(toolbar)
                        .addDrawerItems(
                                item1,
                                //new DividerDrawerItem(),
                                item2
                                //new SecondaryDrawerItem().withName(R.string.drawer_item_settings)
                        )
                        .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                // do something with the clicked item :D
                                switch(position) {
                                    case 1: break;
                                    case 2: break;
                                }

                                return true;
                            }
                        })
                        .build();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getActivity(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }*/


}




