package psni.example.psniproject.LoginScreen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import psni.example.psniproject.LoginScreen.Emails.GMailSender;
import psni.example.psniproject.LoginScreen.Models.Courthouse;
import psni.example.psniproject.LoginScreen.Models.OfficerProfile;
import psni.example.psniproject.LoginScreen.Models.VictimProfile;
import psni.example.psniproject.LoginScreen.Models.UserType;
import psni.example.psniproject.MainApp.MyJourneyFragment;
import psni.example.psniproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class RegisterUserFragment extends Fragment {
    
    private TextInputEditText tiFirstName, tiSurname, tiEmail, tiPhoneNum,
            tiAddress, tiCity, tiCounty, tiPostcode, tiDOB,
            tiDateOfCrime, tiDateReported, tiDateSubmitted, tiCourtDate;
    private TextInputEditText tiFirstNameOfficer, tiSurnameOfficer, tiEmailOfficer;
    private TextInputLayout etDateSubmitted, etCourtDate;
    private TextView tvTitle, tvPPS, tvTrail, tvFileName, tvSelectCourthouse;
    private CardView userCard, officerCard;
    private RadioGroup rgStatement, rgPPS, rgTrail,rgConviction;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private View view;
    private Button mBackButton, mRegButton, mSaveButton, mBrowse, mEditButton;
    private String uIDVictim, fName, sName, eMail, pWord1, pNum, address, city, county, postcode, DOB, dateOfCrime, dateReported, dateSubmitted, courtDate;
    private String uIDOfficer, fNameOfficer, surnameOfficer, emailOfficer;
    private ArrayList<String> victimIds;
    private Courthouse courtHouse;
    private OfficerProfile officerProfile;
    private boolean pps;
    private int convicted = 2;
    private String uidEntered, fileName;
    private ScrollView scrollView;
    private static int PICK_DOC = 123;
    Uri docPath;
    private StorageReference storageReference;
    private boolean clicked = false;
    private UserType formState;
    private UserType userType;
    private Spinner spinCourthouse, spinOfficer;
    private int messageCountO = 0;
    private int messageCountV = 0;


    public RegisterUserFragment() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_DOC && resultCode == RESULT_OK && data.getData() != null) {
            docPath = data.getData();
            tvFileName.setText(generateFileName());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_register_user, container, false);
        setupUIViews();
        userType = UserType.VICTIM;
        formState = UserType.VICTIM;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        victimIds = new ArrayList<>();

        rgConviction.setEnabled(false);

        //fill courthouse spinner
        ArrayAdapter<Courthouse> courthouseAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, MyJourneyFragment.courthouses);
        spinCourthouse.setAdapter(courthouseAdapter);

        //fill officer spinner
        final ArrayAdapter<String> officerProfileArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, UserLoginFragment.officerNames);
        spinOfficer.setAdapter(officerProfileArrayAdapter);

        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(formState == UserType.VICTIM) {
                    userCard.setVisibility(View.GONE);
                    officerCard.setVisibility(View.VISIBLE);
                    formState = UserType.OFFICER;
                }else if(formState == UserType.OFFICER) {
                    officerCard.setVisibility(View.GONE);
                    userCard.setVisibility(View.VISIBLE);
                    formState = UserType.VICTIM;
                }
            }
        });


        mBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select document"), PICK_DOC);
                clicked = true;
            }
        });

        setRadioGroupListeners();

        //registering user
        mRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateRegistration()) {

                    if(formState == UserType.VICTIM) {

                        eMail = tiEmail.getText().toString().trim();
                        pWord1 = generatePassword(10);
                        userType = UserType.VICTIM;

                        firebaseAuth.createUserWithEmailAndPassword(eMail, pWord1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    sendVictimData();
                                    clicked = false;
                                    firebaseAuth.signOut();
                                    sendLoginDetailsEmail(eMail);
                                    Toast.makeText(getActivity(), "Registration Successful & upload complete.", Toast.LENGTH_SHORT).show();
                                    resetEditTexts();
                                    ((LoginActivity) getActivity()).setViewPager(0);
                                } else {
                                    Toast.makeText(getActivity(), "Registration Failed. Email already exists.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else if (formState == UserType.OFFICER) {

                        eMail = tiEmailOfficer.getText().toString().trim();
                        pWord1 = generatePassword(10);
                        userType = UserType.OFFICER;

                        firebaseAuth.createUserWithEmailAndPassword(eMail, pWord1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    sendOfficerData();
                                    firebaseAuth.signOut();
                                    sendLoginDetailsEmail(eMail);
                                    Toast.makeText(getActivity(), "Registration Successful & upload complete.", Toast.LENGTH_SHORT).show();
                                    resetEditTexts();
                                    ((LoginActivity) getActivity()).setViewPager(0);
                                }else {
                                    Toast.makeText(getActivity(), "Registration Failed. EMail already exists.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

                firebaseAuth.signOut();
            }
        });

        spinOfficer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String officer = (String) parent.getSelectedItem();
                System.out.println("Officer's name selected......" + officer);
                officerProfile = matchOfficerWithName(officer, UserLoginFragment.officerProfiles);

                //also add this victim being registered to an ArrayList of Victims for the officer
                //officer can have more than one victim at the same time

                if(officerProfile != null) {
                    System.out.println("Officer's profile matched......" + officerProfile.getmFirstName());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinCourthouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Courthouse courthouse = (Courthouse) parent.getSelectedItem();
                courtHouse = courthouse;
                //Toast.makeText(getContext(), "ID: " + courthouse.getId() + ", CourtHouse: " + courthouse.getName(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //back to admin login screen
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity)getActivity()).setViewPager(1);
            }
        });

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayEditAlert();
            }
        });

        final TextRemover textRemover = new TextRemover();


        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference databaseReference = firebaseDatabase.getReference("victims/" + uidEntered);

                returnFieldValues();

                VictimProfile victimProfile = new VictimProfile(uIDVictim, fName, sName, eMail, pNum, address, city, county, postcode, DOB, dateOfCrime, dateReported, officerProfile, dateSubmitted, pps, courtDate, courtHouse, convicted, userType, messageCountV);

                //if there is a new file to replace then do this
                if (clicked){
                    StorageReference docReference = storageReference.child(uidEntered).child("statement" + victimProfile.getsName() + victimProfile.getfName());      //can create more children for additional file types
                    UploadTask uploadTask = docReference.putFile(docPath);
                    clicked = false;
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Upload Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                databaseReference.setValue(victimProfile);
                showRegButton();
                textRemover.run();
                Toast.makeText(getActivity(), "Details updated for " + victimProfile.getfName() + " " + victimProfile.getsName(), Toast.LENGTH_SHORT).show();
                resetEditTexts();
            }
        });

        disableFieldsNoStatement();

        return view;
    }

    private String generatePassword(int length) {

        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789£/@#%".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for(int i =0; i< length; i++) {
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    private void sendLoginDetailsEmail(String recipient) {

        try {
            GMailSender sender = new GMailSender("andrew-kidd@outlook.com", "Newpc180");
            sender.sendMail("VictimSupport Login Details",
                    "Hi,\n\nHere are your details:\n\nUsername: " + eMail + "\nPassword: " + pWord1 + "\n\nPlease log in to the Victim Support App now to activate your account.\n\nMany thanks.\n\nVictim Support Team",
                    "andrewkidd21234@gmail.com",
                    recipient);
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
    }

    private void setRadioGroupListeners() {

        rgStatement.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId) {
                    case R.id.statementYes:
                        enableFieldsStatementGiven();
                        break;
                    case R.id.statementNo:
                        disableFieldsNoStatement();
                        break;
                }
            }
        });


        rgPPS.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId) {
                    case R.id.ppsYes:
                        pps = true;
                        break;
                    case R.id.ppsNo:
                        convicted = 2;
                        pps = false;
                        break;

                }
            }
        });

        rgTrail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId) {
                    case R.id.trailNo:
                        enableTrailFields();
                        rgConviction.setVisibility(View.GONE);
                        break;
                    case R.id.trailYes:
                        disableTrailFields();
                        rgConviction.setVisibility(View.VISIBLE);
                        rgConviction.setEnabled(true);
                        checkForConviction();
                        break;
                }
            }
        });

    }

    private void checkForConviction() {

        rgConviction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.convicted:
                        convicted = 1;
                        break;
                    case R.id.notConvicted:
                        convicted = 0;
                        break;
                }
            }
        });
    }

    public void displayEditAlert() {

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        final EditText userToEdit = new EditText(getActivity());
        alert.setMessage("Enter the User's ID:");
        alert.setTitle("Edit a User's Profile");

        alert.setView(userToEdit);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                uidEntered = userToEdit.getText().toString();

                final DatabaseReference databaseReference = firebaseDatabase.getReference("victims/" + uidEntered);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        showSaveButton();
                        VictimProfile victimProfile = dataSnapshot.getValue(VictimProfile.class);

                        tiFirstName.setText(victimProfile.getfName());
                        tiSurname.setText(victimProfile.getsName());
                        tiEmail.setText(victimProfile.getEmail());
                        tiPhoneNum.setText(victimProfile.getPhoneNum());
                        tiAddress.setText(victimProfile.getAddress());
                        tiCity.setText(victimProfile.getCity());
                        tiCounty.setText(victimProfile.getCounty());
                        tiPostcode.setText(victimProfile.getPostcode());
                        tiDOB.setText(victimProfile.getDOB());
                        tiDateOfCrime.setText(victimProfile.getCrimeDate());
                        tiDateReported.setText(victimProfile.getReportDate());

                        if (victimProfile.getDateSubmitted().isEmpty()) {
                            //do nothing
                            tiDateSubmitted.setText("");
                        }else {
                            enableFieldsStatementGiven();
                            tiDateSubmitted.setText(victimProfile.getDateSubmitted());
                            tvFileName.setText(generateFileName());
                        }

                        if(victimProfile.isPps()) {
                            rgPPS.check(R.id.ppsYes);
                        }else {
                            rgPPS.check(R.id.ppsNo);
                        }

                        if (victimProfile.getConvicted() == 1) {
                            rgConviction.check(R.id.convicted);
                        }else {
                            rgConviction.check(R.id.notConvicted);
                        }

                        if (victimProfile.getCourtDate().isEmpty()) {
                            //do nothing
                            tiCourtDate.setText("");
                        }else {
                            enableFieldsStatementGiven();
                            tiCourtDate.setText(victimProfile.getCourtDate());
                            spinCourthouse.setSelection(victimProfile.getCourtHouse().getId());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.

                //close AlertDialog
            }
        });

        alert.show();
    }

    //String fName, sName, eMail, pWord,pNum, address, city, county, postcode, DOB;

    private void setupUIViews() {

        //TextInputLayouts for
        tvTitle = view.findViewById(R.id.tvTitle);

        userCard = view.findViewById(R.id.userCard);
        tiFirstName = view.findViewById(R.id.etFirstName);
        tiSurname = view.findViewById(R.id.etSurname);
        tiEmail = view.findViewById(R.id.etEmail);
        tiPhoneNum = view.findViewById(R.id.etPhoneNum);
        tiAddress = view.findViewById(R.id.etAddress);
        tiCity = view.findViewById(R.id.etCity);
        tiCounty = view.findViewById(R.id.etCounty);
        tiPostcode = view.findViewById(R.id.etPostcode);
        tiDOB = view.findViewById(R.id.etDOB);

        tiDateOfCrime = view.findViewById(R.id.etDateOfCrime);
        tiDateReported = view.findViewById(R.id.etDateReported);
        spinOfficer = view.findViewById(R.id.spinOfficer);

        rgStatement = view.findViewById(R.id.rgStatement);
        tiDateSubmitted = view.findViewById(R.id.etDateSubmitted);
        etDateSubmitted = view.findViewById(R.id.tiDateSubmitted);
        mBrowse = view.findViewById(R.id.browseBtn);
        tvFileName = view.findViewById(R.id.fileName);
        tvPPS = view.findViewById(R.id.tvPPSConfirm);
        rgPPS = view.findViewById(R.id.rgPPS);

        tvTrail = view.findViewById(R.id.tvCourtConfirm);
        rgTrail = view.findViewById(R.id.rgTrail);
        tiCourtDate = view.findViewById(R.id.etCourtDate);
        etCourtDate = view.findViewById(R.id.tiCourtDate);
        tvSelectCourthouse = view.findViewById(R.id.tvSelectCourthouse);
        spinCourthouse = view.findViewById(R.id.spinCourtHouse);
        rgConviction = view.findViewById(R.id.rgConviction);

        officerCard = view.findViewById(R.id.officerCard);
        tiFirstNameOfficer = view.findViewById(R.id.etFirstNameOfficer);
        tiSurnameOfficer = view.findViewById(R.id.etSurnameOfficer);
        tiEmailOfficer = view.findViewById(R.id.etEmailOfficer);

        mRegButton = view.findViewById(R.id.regBtn);
        mBackButton = view.findViewById(R.id.backBtn);
        mEditButton = view.findViewById(R.id.editBtn);
        mSaveButton = view.findViewById(R.id.saveBtn);


    }

    private Boolean validateRegistration() {
        Boolean result = false;

        returnFieldValues();

        if(formState == UserType.VICTIM) {

            if (fName.isEmpty() || sName.isEmpty() || eMail.isEmpty()) {
                Toast.makeText(getActivity(), "Please complete all fields.", Toast.LENGTH_SHORT).show();
            } else {
                result = true;
            }
        }else if(formState == UserType.OFFICER) {
            if (fNameOfficer.isEmpty() || surnameOfficer.isEmpty() || emailOfficer.isEmpty()) {
                Toast.makeText(getActivity(), "Please complete all fields.", Toast.LENGTH_SHORT).show();
            }else {
                result = true;
            }
        }

        return result;
    }

    private String generateFileName () {
        VictimProfile victimProfile = new VictimProfile(uIDVictim, fName, sName, eMail, pNum, address, city, county, postcode, DOB, dateOfCrime, dateReported, officerProfile, dateSubmitted, pps, courtDate, courtHouse, convicted, userType, messageCountV);
        StorageReference docReference = storageReference.child("statement"+ tiFirstName.getText()+ tiSurname.getText());      //can create more children for additional file types
        fileName = docReference.getName();
        return fileName;
    }


    private void sendVictimData() {

        DatabaseReference myRef = firebaseDatabase.getReference("victims/" + firebaseAuth.getUid());
        uIDVictim = firebaseAuth.getUid();
        final VictimProfile victimProfile = new VictimProfile(uIDVictim, fName, sName, eMail, pNum, address, city, county, postcode, DOB, dateOfCrime, dateReported, officerProfile, dateSubmitted, pps, courtDate, courtHouse, convicted, userType, messageCountV);

        //addVictimToOfficerList(officerProfile, victimProfile);

        if (clicked){
            StorageReference docReference = storageReference.child(firebaseAuth.getUid()).child("statement" + victimProfile.getsName() + victimProfile.getfName());      //can create more children for additional file types
            UploadTask uploadTask = docReference.putFile(docPath);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Upload Fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
        myRef.setValue(victimProfile);

        //memory leak doing it this way

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("officers/" + officerProfile.getuID());
        officerProfile.getVictimIds().add(victimProfile.getuID());
        ref.setValue(officerProfile);

        /*ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OfficerProfile officerProfile1 = dataSnapshot.getValue(OfficerProfile.class);

                System.out.println("OFFICER TO HAVE VICTIM ADDED....." + officerProfile1.getmFirstName() + officerProfile1.getmSurname());

                System.out.println("VICTIM TO BE ADDED....." + victimProfile.getfName() + victimProfile.getsName());

                officerProfile1.getVictimProfiles().add(victimProfile);

                //infinite loop of victimprofiles being added
                //ref.setValue(officerProfile1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    private void sendOfficerData() {
        DatabaseReference myRef = firebaseDatabase.getReference("officers/" + firebaseAuth.getUid());
        uIDOfficer = firebaseAuth.getUid();
        //add empty entry to create the arraylist for first victim to be added
        victimIds.add("a");
        OfficerProfile officerProfile = new OfficerProfile(uIDOfficer, fNameOfficer, surnameOfficer, emailOfficer, userType, victimIds, messageCountO);
        myRef.setValue(officerProfile);
    }

    private void addVictimToOfficerList(OfficerProfile officerProfile, final VictimProfile victimProfile) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("officers/" + officerProfile.getuID());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OfficerProfile officerProfile1 = dataSnapshot.getValue(OfficerProfile.class);
                officerProfile1.getVictimIds().add(victimProfile.getuID());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void returnFieldValues() {

        fName = tiFirstName.getText().toString();
        sName = tiSurname.getText().toString();
        eMail = tiEmail.getText().toString();
        eMail = tiEmail.getText().toString();
        pNum = tiPhoneNum.getText().toString();
        address = tiAddress.getText().toString();
        city = tiCity.getText().toString();
        county = tiCounty.getText().toString();
        postcode = tiPostcode.getText().toString();
        DOB = tiDOB.getText().toString();
        dateOfCrime = tiDateOfCrime.getText().toString();
        dateReported = tiDateReported.getText().toString();
        dateSubmitted = tiDateSubmitted.getText().toString();
        courtDate = tiCourtDate.getText().toString();

        fNameOfficer = tiFirstNameOfficer.getText().toString();
        surnameOfficer = tiSurnameOfficer.getText().toString();
        emailOfficer = tiEmailOfficer.getText().toString();
    }

    private boolean isNotEmpty(TextInputEditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return true;
        return false;
    }

    private void showSaveButton() {
        mSaveButton.setVisibility(View.VISIBLE);
        mRegButton.setVisibility(View.GONE);
    }

    private void showRegButton() {
        mRegButton.setVisibility(View.VISIBLE);
        mSaveButton.setVisibility(View.GONE);
    }

    public void resetEditTexts() {

        tiFirstName.setText("");
        tiSurname.setText("");
        tiEmail.setText("");
        tiPhoneNum.setText("");
        tiAddress.setText("");
        tiCity.setText("");
        tiPostcode.setText("");
        tiCounty.setText("");
        tiDOB.setText("");
        tiDateOfCrime.setText("");
        tiDateReported.setText("");
        tvFileName.setText("");
        tiDateSubmitted.setText("");
        tiCourtDate.setText("");

        tiFirstNameOfficer.setText("");
        tiSurnameOfficer.setText("");
        tiEmailOfficer.setText("");

    }

    private void disableFieldsNoStatement() {

        etDateSubmitted.setAlpha(0.4f);
        mBrowse.setAlpha(0.4f);
        tvPPS.setAlpha(0.4f);
        rgPPS.setAlpha(0.4f);
        tvTrail.setAlpha(0.4f);
        rgTrail.setAlpha(0.4f);
        etCourtDate.setAlpha(0.4f);
        tvSelectCourthouse.setAlpha(0.4f);
        spinCourthouse.setAlpha(0.4f);

        tiDateSubmitted.setEnabled(false);
        mBrowse.setEnabled(false);
        tvPPS.setEnabled(false);
        rgPPS.setEnabled(false);
        tvTrail.setEnabled(false);
        rgTrail.setEnabled(false);
        tiCourtDate.setEnabled(false);
        tvSelectCourthouse.setEnabled(false);
        spinCourthouse.setEnabled(false);
    }

    private void enableFieldsStatementGiven() {

        etDateSubmitted.setAlpha(1f);
        mBrowse.setAlpha(1f);
        tvPPS.setAlpha(1f);
        rgPPS.setAlpha(1f);
        tvTrail.setAlpha(1f);
        rgTrail.setAlpha(1f);
        etCourtDate.setAlpha(1f);
        tvSelectCourthouse.setAlpha(1f);
        spinCourthouse.setAlpha(1f);

        tiDateSubmitted.setEnabled(true);
        mBrowse.setEnabled(true);
        tvPPS.setEnabled(true);
        rgPPS.setEnabled(true);
        tvTrail.setEnabled(true);
        rgTrail.setEnabled(true);
        tiCourtDate.setEnabled(true);
        tvSelectCourthouse.setEnabled(true);
        spinCourthouse.setEnabled(true);
    }

    public void disableTrailFields() {

        etCourtDate.setAlpha(0.4f);
        tvSelectCourthouse.setAlpha(0.4f);
        spinCourthouse.setAlpha(0.4f);

        tiCourtDate.setEnabled(false);
        tvSelectCourthouse.setEnabled(false);
        spinCourthouse.setEnabled(false);
    }

    public void enableTrailFields() {

        etCourtDate.setAlpha(1f);
        tvSelectCourthouse.setAlpha(1f);
        spinCourthouse.setAlpha(1f);

        tiCourtDate.setEnabled(true);
        tvSelectCourthouse.setEnabled(true);
        spinCourthouse.setEnabled(true);
    }

    private boolean checkPasswordMatch(String password1, String password2) {

        boolean result = false;

        if (!password1.equals(password2)) {
            Toast.makeText(getActivity(), "Passwords do not match.", Toast.LENGTH_LONG).show();
        } else {
            result = true;
        }
        return result;
    }

    public static OfficerProfile matchOfficerWithName(String officer, ArrayList<OfficerProfile> arrayList) {

        OfficerProfile officerProfile = new OfficerProfile();

        for(int i = 0; i<arrayList.size(); i++) {

            String nameToTest = arrayList.get(i).getmFirstName() + " " + arrayList.get(i).getmSurname();
            System.out.println("Name pulled from OfficerProfile ArrayList....." + nameToTest);

            if(officer.equals(nameToTest)) {

                officerProfile = arrayList.get(i);
            }
        }
        return officerProfile;
    }

    private class TextRemover extends Thread {

        public void run() {
            resetEditTexts();
        }
    }


}
