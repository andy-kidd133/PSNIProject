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
    private TextView tvName, tvCrimeDate, tvReportDate, tvStatementHeading, tvDateSubmitted, tvJFileName,
            tvJPPSHeading, tvJPPSUpdate, tvJCourtHeading, tvJCourtDate, tvJCourtName, tvJCourtAddress, tvJVerdictHeading, tvJVerdict, tvJVerdictResult;
    private ImageView ivGreenTick, ivGreenTick1, ivGreenTick2, ivGreenTick3;
    private Button btnDownload;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    CustomMapView mapView;
    GoogleMap mapToBeLoaded;
    static GoogleMap antrimMap, armaghMap, ballymenaMap, belfastLaganMap, belfastRoyalMap, colerianeMap,
                    craigavonMap, downpatrickMap, dungannonMap, enniskillenMap, lisburnMap, limavadyMap, londonderryMap,
                    magherafeltMap, newryMap, newtownardsMap, omaghMap, strbaneMap;
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
        tvJPPSUpdate = view.findViewById(R.id.tvJPPSUpdate);
        tvJCourtHeading = view.findViewById(R.id.tvJCourtHeading);
        ivGreenTick2 = view.findViewById(R.id.ivGreenTick2);
        tvJCourtName = view.findViewById(R.id.tvJCourtName);
        tvJCourtDate = view.findViewById(R.id.tvJCourtDate);
        tvJCourtAddress = view.findViewById(R.id.tvJCourtAddress);
        tvJVerdictHeading = view.findViewById(R.id.tvJVerdictHeading);
        tvJVerdict = view.findViewById(R.id.tvJVerdict);
        tvJVerdictResult = view.findViewById(R.id.tvJVerdictResult);
        ivGreenTick3 = view.findViewById(R.id.ivGreenTick3);


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
            tvJCourtHeading.setVisibility(View.GONE);
            ivGreenTick2.setVisibility(View.GONE);
            tvJVerdictHeading.setVisibility(View.GONE);
            tvJVerdict.setVisibility(View.GONE);
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
                        ivGreenTick1.setVisibility(View.GONE);
                        ivGreenTick2.setVisibility(View.GONE);
                        ivGreenTick3.setVisibility(View.GONE);
                        btnDownload.setVisibility(View.GONE);
                        tvJFileName.setVisibility(View.GONE);
                        tvDateSubmitted.setText("The PSNI will be in contact to arrange a date and time for giving your statement");
                    }
                    else {
                        ivGreenTick.setVisibility(View.VISIBLE);
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

                    if (userProfile.isPps()) {
                        tvJPPSUpdate.setText("Your statement has been submitted to the Public Prosecution Service NI.");
                        ivGreenTick1.setVisibility(View.VISIBLE);
                    }else {
                        tvJPPSUpdate.setText("Enquiries are still ongoing within your case.");
                        ivGreenTick1.setVisibility(View.GONE);
                        ivGreenTick2.setVisibility(View.GONE);
                    }

                    //********** COURT INFO **********//

                    if (userProfile.getCourtDate().isEmpty()) {
                        tvJCourtDate.setVisibility(View.GONE);
                        tvJCourtDate.setText("Any court arrangements will appear here and you will be notified.");

                        //also set map to invisible
                        mapView.setVisibility(View.GONE);
                    }
                    else {
                        tvJCourtDate.setVisibility(View.VISIBLE);
                        tvJCourtDate.setText("Your court date has been set for " + userProfile.getCourtDate() + ".");
                        ivGreenTick2.setVisibility(View.VISIBLE);
                        tvJVerdictHeading.setVisibility(View.VISIBLE);

                        //******** DISPLAY COURTHOUSE INFO *********

                        tvJCourtName.setText(userProfile.getCourtHouse().getName());
                        tvJCourtAddress.setText(userProfile.getCourtHouse().getAddress());
                        mapToBeLoaded = userProfile.getCourtHouse().getGoogleMap();
                        courthouseIndex = userProfile.getCourtHouse().getId();
                    }


                    //********** CONVICTION INFO **********//

                    if (userProfile.getConvicted() == 1) {
                        ivGreenTick3.setVisibility(View.VISIBLE);
                        tvJVerdict.setVisibility(View.VISIBLE);
                        tvJVerdictResult.setText("CONVICTED");
                    }
                    else if(userProfile.getConvicted() == 0){
                        ivGreenTick3.setVisibility(View.VISIBLE);
                        tvJVerdict.setVisibility(View.VISIBLE);
                        tvJVerdictResult.setText("NOT CONVICTED");
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

        courthouses.add(new Courthouse(0, "Antrim Courthouse", "The Courthouse\n30 Castle Way\nAntrim\nVT41 4AQ\n\nPhone: 0300 200 7812", antrimMap, 54.715206, -6.214654));
        courthouses.add(new Courthouse(1, "Armagh Courthouse", "The Courthouse\nThe Mall\nArmagh\nBT61 9DJ\nPhone: 0300 200 7812", armaghMap, 54.350656, -6.652744));
        courthouses.add(new Courthouse(2, "Ballymena Courthouse", "The Courthouse\nAlbert Place\nBallymena\nBT43 5BS\nPhone: 0300 200 7812", ballymenaMap, 54.866335, -6.279012));
        courthouses.add(new Courthouse(3, "Belfast Laganside Courthouse", "Oxford Street\nBelfast\nBT1 3LL\nPhone: 0300 200 7812", belfastLaganMap, 54.598187, -5.922387));
        courthouses.add(new Courthouse(4, "Belfast Royal Court of Justice", "Chichester Street\nBelfast\nBT1 3JF\nPhone: 0300 200 7812", belfastRoyalMap, 54.597600, -5.922891));
        courthouses.add(new Courthouse(5, "Coleraine Courthouse", "The Courthouse\n46a Mountsandel Road\nColeraine\nBT52 1NY\nPhone: 0300 200 7812", colerianeMap, 55.122000, -6.662956));
        courthouses.add(new Courthouse(6, "Craigavon Courthouse", "The Courthouse\nCentral Way\nCraigavon\nBT64 1AP\nPhone: 0300 200 7812", craigavonMap, 54.449825, -6.395116));
        courthouses.add(new Courthouse(7, "Downpatrick Courthouse", "The Courthouse\nEnglish Street\nDownpatrick\nBT30 6AB\nPhone: 0300 200 7812", downpatrickMap, 54.328971, -5.718954));
        courthouses.add(new Courthouse(8, "Dungannon Courthouse", "The Courthouse\n46 Killyman Road\nDungannon\nBT71 6DE\nPhone: 0300 200 7812", dungannonMap, 54.504938, -6.758475));
        courthouses.add(new Courthouse(9, "Enniskillen Courthouse", "The Courthouse\nEast Bridge Street\nEnniskillen\nBT74 7BW\nPhone: 0300 200 7812", enniskillenMap, 54.343821, -7.636276));
        courthouses.add(new Courthouse(10, "Lisburn Courthouse", "The Courthouse\nRailway Street\nLisburn\nBT28 1XR\nPhone: 0300 200 7812", lisburnMap, 54.513818, -6.044280));
        courthouses.add(new Courthouse(11, "Limavady Courthouse", "The Courthouse\nMain Street\nLimavady\nBT49 0EY\nPhone: 0300 200 7812", limavadyMap, 55.051362, -6953526));
        courthouses.add(new Courthouse(12, "Londonderry Courthouse", "The Courthouse\nBishop Street\nLondonderry\nBT48 6PQ\nPhone: 0300 200 7812", londonderryMap, 54.994015, -7.323963));
        courthouses.add(new Courthouse(13, "Magherafelt Courthouse", "The Courthouse\nHospital Road\nMagherafelt\nBT45 5DG\nPhone: 0300 200 7812", magherafeltMap, 54.758746, -6.610652));
        courthouses.add(new Courthouse(14, "Newry Courthouse", "The Courthouse\n23 New Street\nNewry\nBT35 6AD\nPhone: 0300 200 7812", newryMap, 54.179267, -6.334976));
        courthouses.add(new Courthouse(15, "Newtownards Courthouse", "The Courthouse\nRegent Street\nNewtownards\nBT23 4LP\nPhone: 0300 200 7812", newtownardsMap, 54.594337, -5.701970));
        courthouses.add(new Courthouse(16, "Omagh Courthouse", "The Courthouse\nHigh Street\nOmagh\nBT78 1DU\nPhone: 0300 200 7812", omaghMap, 54.600089, -7.302879));
        courthouses.add(new Courthouse(17, "Strabane Courthouse", "The Courthouse\nDerry Road\nStrabane\nBT82 8DT\nPhone: 0300 200 7812", strbaneMap, 54.829635, -7.463193));

    }
    public void hideMap() {
        mapView.setVisibility(View.GONE);
    }

}
