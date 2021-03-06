package psni.example.psniproject.MainApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import psni.example.psniproject.LoginScreen.LoginActivity;
import psni.example.psniproject.LoginScreen.MyFragmentPagerAdapter;
import psni.example.psniproject.LoginScreen.Models.OfficerProfile;
import psni.example.psniproject.LoginScreen.Models.VictimProfile;
import psni.example.psniproject.LoginScreen.Models.UserType;
import psni.example.psniproject.MainApp.Messages.MessagesFragment;
import psni.example.psniproject.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import psni.example.psniproject.LoginScreen.UserLoginFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MyFragmentPagerAdapter mMyFragmentPagerAdapter;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private TextView headerName, headerEmail;
    private NavigationView navigationView;
    MenuItem miVictimSupport, miPPANI;
    public static UserType userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //use view pager to inflate #0 - HomePageFragment
        mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container1);
        setupViewPager(mViewPager);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        retrieveUserType(firebaseAuth.getUid());
        setupMainPage();
        checkUserSignedIn();

        miVictimSupport = findViewById(R.id.victim_support);
        miPPANI = findViewById(R.id.PPANI);

        mViewPager.beginFakeDrag();

    }

    /**
     * set up toolbar and navigation drawer
     */
    private void setupMainPage() {

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        headerName = header.findViewById(R.id.drawer_name);
        headerEmail = header.findViewById(R.id.drawer_email);

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

        Menu m = navigationView.getMenu();
        int id = menuItem.getItemId();

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                this.setViewPager(0);
                break;

      //*********** Victim Support **********//
            case R.id.victim_support:
                this.setViewPager(2);
                break;

      //********** PPANI **********//
            case R.id.PPANI:
                this.setViewPager(3);
                break;

      //********** niDirect **********//
            case R.id.niDirect:
                this.setViewPager(4);
                break;

      //********** Journey **********//
            case R.id.nav_journey:
                this.setViewPager(1);
                break;

      //********** Messages **********//
            case R.id.nav_messages:
                this.setViewPager(5);
                break;

      //********** Messages **********//
            case R.id.nav_contact:
                this.setViewPager(6);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * back button pressed within app, closes drawer or returns to HomePageFragment.xml
     */
    @Override
    public void onBackPressed() {

        startActivity(new Intent(this, MainActivity.class));

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * create all fragments for mainActivity and add to ArrayList
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomePageFragment(), "HomePageFragment");        //0
        adapter.addFragment(new MyJourneyFragment(), "MyJourneyFragment");      //1
        adapter.addFragment(new VictimSupport(), "VictimSupport");              //2
        adapter.addFragment(new PPANI(), "PPANI");                              //3
        adapter.addFragment(new NIDirect(), "NIDirectFragment");                //4
        adapter.addFragment(new MessagesFragment(), "Messages");                //5
        adapter.addFragment(new ContactUs(), "Contact Us");                     //6
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

    /**
     * method controls name and email display in the header of the navigation drawer
     *
     */
    private void checkUserSignedIn() {

        if (firebaseAuth.getCurrentUser() == null) {

            headerName.setText("Please sign in");
            headerEmail.setText("");

        } else {

            DatabaseReference databaseReference = null;

            if (userType == UserType.VICTIM) {
                databaseReference = firebaseDatabase.getReference("victims/" + firebaseAuth.getUid());

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        VictimProfile victimProfile = dataSnapshot.getValue(VictimProfile.class);

                        headerName.setText(victimProfile.getfName());
                        headerEmail.setText(victimProfile.getEmail());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            else if (userType == UserType.OFFICER) {
                databaseReference = firebaseDatabase.getReference("officers/" + firebaseAuth.getUid());

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        OfficerProfile officerProfile = dataSnapshot.getValue(OfficerProfile.class);

                        headerName.setText(officerProfile.getmFirstName());
                        headerEmail.setText(officerProfile.getmEmail());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }


    /**
     * set the UserType enum upon login (which acts as a flag for the rest of the pages
     * @param firebaseAuthUID
     */
    private void retrieveUserType(String firebaseAuthUID) {

        if(UserLoginFragment.victimIDs.contains(firebaseAuthUID)) {
            userType = UserType.VICTIM;
            Log.e("Usertype....", userType.toString());
        }
        else if(UserLoginFragment.officerIDs.contains(firebaseAuthUID)) {
            userType = UserType.OFFICER;
            Log.e("Usertype....", userType.toString());
        }
    }
}



