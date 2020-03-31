package com.example.psniproject.MainApp;


import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psniproject.LoginScreen.Courthouse;
import com.example.psniproject.LoginScreen.RegisterUserFragment;
import com.example.psniproject.LoginScreen.UserProfile;
import com.example.psniproject.R;
import com.example.psniproject.SplashScreen.SplashScreenActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyJourneyFragment extends Fragment implements OnMapReadyCallback{

    private View view;
    private TextView tvName, tvCrimeDate, tvReportDate, tvStatementHeading, tvDateSubmitted, tvJFileName, tvJPPSHeading, tvJCourtDate;
    private ImageView ivGreenTick, ivGreenTick1;
    private Button btnDownload;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private Animation fadeIn;
    StorageReference storageReference;
    CustomMapView mapView;
    GoogleMap mapToBeLoaded;
    static GoogleMap antrimMap, armaghMap, ballymenaMap;
    boolean signedIn;

    public static int courthouseIndex;

    public static ArrayList<Courthouse> courthouses = new ArrayList<>();

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        if (firebaseAuth.getCurrentUser() == null) {
            signedIn = false;
            hideMap();
        }else {

            DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                    mapToBeLoaded = googleMap;
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(
                            courthouses.get(userProfile.getCourtHouse().getId()).getLatitude(),
                            courthouses.get(userProfile.getCourtHouse().getId()).getLongitude()))
                            .title(courthouses.get(userProfile.getCourtHouse().getId()).getName()));

                    CameraPosition Liberty = CameraPosition.builder().target(new LatLng(courthouses.get(userProfile.getCourtHouse().getId()).getLatitude(), courthouses.get(userProfile.getCourtHouse().getId()).getLongitude())).zoom(16).bearing(0).build();
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public MyJourneyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_journey, container, false);

        mapView = view.findViewById(R.id.mapViewHolder);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        tvName = view.findViewById(R.id.tvName);
        tvCrimeDate = view.findViewById(R.id.tvCrimeDate);
        tvReportDate = view.findViewById(R.id.tvReportDate);
        tvStatementHeading = view.findViewById(R.id.tvJStatementHeading);
        ivGreenTick = view.findViewById(R.id.ivGreenTick);
        tvDateSubmitted = view.findViewById(R.id.tvJDateSubmitted);
        tvJFileName = view.findViewById(R.id.tvJFileName);
        btnDownload = view.findViewById(R.id.btnDownload);
        tvJPPSHeading = view.findViewById(R.id.tvJPPSHeading);
        ivGreenTick1 = view.findViewById(R.id.ivGreenTick1);

        tvJCourtDate = view.findViewById(R.id.tvJCourtDate);

        fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference();

        if (firebaseAuth.getCurrentUser() == null) {
            tvName.setText("Please log in to use this feature");
            tvName.setTextSize(20);
            tvStatementHeading.setVisibility(View.GONE);
            ivGreenTick.setVisibility(View.GONE);
            btnDownload.setVisibility(View.GONE);
            tvJPPSHeading.setVisibility(View.GONE);
            ivGreenTick1.setVisibility(View.GONE);

        }
        else {

            DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                    //********** DETAILS **********//

                    tvName.setText("Hi " + userProfile.getfName() + ",");
                    tvCrimeDate.setText("We have record of the crime which took place on " +
                                                    userProfile.getCrimeDate() + ". Below you will find immediate updates on your case as they happen.");
                    tvReportDate.setText("You reported the crime to the PSNI on " + userProfile.getReportDate() + ".");

                    //********** STATEMENT INFO **********//

                    if (userProfile.getDateSubmitted().isEmpty()) {

                        //remove these statements into sep method taking away other subsequent
                        //details from Journey
                        tvStatementHeading.setVisibility(View.VISIBLE);
                        ivGreenTick.setVisibility(View.GONE);
                        btnDownload.setVisibility(View.GONE);
                        tvJFileName.setVisibility(View.GONE);
                        tvDateSubmitted.setText("The PSNI will be in contact to arrange a date and time for giving your statement");
                    }
                    else {
                        ivGreenTick.setVisibility(View.VISIBLE);
                        ivGreenTick.setAnimation(fadeIn);
                        tvDateSubmitted.setText("Your statement was given to the PSNI on " + userProfile.getDateSubmitted() + ". You can view it here:");
                        btnDownload.setVisibility(View.VISIBLE);

                        final String statementFileName = storageReference.child(firebaseAuth.getUid()).child("statement" + userProfile.getsName() + userProfile.getfName()).toString();

                        storageReference.child(firebaseAuth.getUid()).child("statement" + userProfile.getsName() + userProfile.getfName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                tvJFileName.setText("statement" + userProfile.getsName() + userProfile.getfName());
                            }
                        });

                        btnDownload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                StorageReference docReference = storageReference.child(firebaseAuth.getUid()).child("statement" + userProfile.getsName() + userProfile.getfName());      //can create more children for additional file types
                                docReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();
                                        downloadFile(getActivity(), statementFileName, ".pdf,", DIRECTORY_DOWNLOADS, url);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                        });
                    }


                    //********** PPS **********//





                    //********** COURT INFO **********//

                    if (userProfile.getCourtDate().isEmpty()) {
                        tvJCourtDate.setVisibility(View.GONE);

                        //also set map to invisible
                        mapView.setVisibility(View.GONE);
                    }
                    else {
                        tvJCourtDate.setVisibility(View.VISIBLE);
                        tvJCourtDate.setText("Your court date has been set for " + userProfile.getCourtDate() + ".");

                        //******** DISPLAY COURTHOUSE INFO *********

                        mapToBeLoaded = userProfile.getCourtHouse().getGoogleMap();
                        courthouseIndex = userProfile.getCourtHouse().getId();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }

    public void downloadFile(Context context, String fileName, String fileExtension,
                             String destinationDirectory, String url) {

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);
        downloadManager.enqueue(request);

    }

    public static void setCourthouseData() {

        courthouses.add(new Courthouse(0, "Antrim Courthouse", antrimMap, 54.715206, -6.214654));
        courthouses.add(new Courthouse(1, "Armagh Courthouse", armaghMap, 54.350656, -6.652744));
        courthouses.add(new Courthouse(2, "Ballymena Courthouse", ballymenaMap, 54.866335, -6.279012));
        /*courthouses.add(new Courthouse("04", "Belfast Laganside"));
        courthouses.add(new Courthouse("05", "Belfast Royal COJ"));
        courthouses.add(new Courthouse("06", "Coleraine"));
        courthouses.add(new Courthouse("07", "Craigavon"));
        courthouses.add(new Courthouse("08", "Downpatrick"));
        courthouses.add(new Courthouse("09", "Dungannon"));
        courthouses.add(new Courthouse("10", "Enniskillen"));
        courthouses.add(new Courthouse("11", "Lisburn"));
        courthouses.add(new Courthouse("12", "Limavady"));
        courthouses.add(new Courthouse("13", "Londonderry"));
        courthouses.add(new Courthouse("14", "Magherafelt"));
        courthouses.add(new Courthouse("15", "Newry"));
        courthouses.add(new Courthouse("16", "Newtownards"));
        courthouses.add(new Courthouse("17", "Omagh"));
        courthouses.add(new Courthouse("18", "Strabane"));*/

    }
    public void hideMap() {
        mapView.setVisibility(View.GONE);
    }

}
