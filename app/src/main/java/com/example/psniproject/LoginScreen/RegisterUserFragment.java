package com.example.psniproject.LoginScreen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.psniproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class RegisterUserFragment extends Fragment {
    
    private TextInputEditText tiFirstName, tiSurname, tiEmail, tiPassword, tiPasswordCon, tiPhoneNum,
            tiAddress, tiCity, tiCounty, tiPostcode, tiDOB;

    private TextInputLayout tiDateSubmitted, mMsg, courtDate, courtHouse;
    private TextView tvPPS, tvTrail, tvFileName;
    private RadioGroup rgStatement, rgPPS, rgTrail;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private View view;
    private Button mBackButton, mRegButton, mSaveButton, mBrowse, mEditButton;
    private String fName, sName, eMail, pWord1, pWord2, pNum, address, city, county, postcode, DOB, dateSubmitted;
    public String uidEntered, fileName;
    private Context mCtx;
    private static int PICK_DOC = 123;
    Uri docPath;
    private StorageReference storageReference;

    ArrayList<UserProfile> profileList = new ArrayList<>();

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

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference();
        //StorageReference myRef1 = storageReference.child(firebaseAuth.getUid());

        mBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("application/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select document"), PICK_DOC);
            }
        });


        //method to listen for radio button Yes/No for statement
        //if no, call collapseForm method & display message "PSNI will be in contact"
        //if yes, activate rest of form

        rgStatement.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId) {
                    case R.id.statementYes:
                        //expandForm
                        enableFields();
                        break;
                    case R.id.statementNo:
                        //collapseForm
                        disableFields();
                        break;
                }
            }
        });

        //registering user
        mRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateRegistration() && checkPasswordMatch(pWord1, pWord2)) {

                    String user_email = tiEmail.getText().toString().trim();
                    String user_password = tiPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                //firebaseAuth.signOut();
                                sendUserData();
                                Toast.makeText(getActivity(), "Registration Successful & upload complete.", Toast.LENGTH_SHORT).show();
                                resetEditTexts();
                                ((LoginActivity)getActivity()).setViewPager(0);
                            }else {
                                Toast.makeText(getActivity(), "Registration Failed.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

                //possibly send an email to registered user with login details?
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

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference databaseReference = firebaseDatabase.getReference(uidEntered);

                fName = tiFirstName.getText().toString();
                sName = tiSurname.getText().toString();
                eMail = tiEmail.getText().toString();
                pWord1 = tiPassword.getText().toString();
                pWord2 = tiPasswordCon.getText().toString();
                eMail = tiEmail.getText().toString();
                pNum = tiPhoneNum.getText().toString();
                address = tiAddress.getText().toString();
                city = tiCity.getText().toString();
                county = tiCounty.getText().toString();
                postcode = tiPostcode.getText().toString();
                DOB = tiDOB.getText().toString();
                dateSubmitted = tiDateSubmitted.getEditText().getText().toString();

                UserProfile userProfile = new UserProfile(fName, sName, eMail, pNum, address, city, county, postcode, DOB, dateSubmitted);
                databaseReference.setValue(userProfile);

                //if current firebaseAuth users.getUID -- uid entered &&
                //if the court date field for example has been changed then send this certain notification

                resetEditTexts();
                showRegButton();
                Toast.makeText(getActivity(), "Details updated for " + userProfile.getfName() + " " + userProfile.getsName(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void displayEditAlert() {

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        final EditText userToEdit = new EditText(getActivity());
        alert.setMessage("Enter the email address of the user:");
        alert.setTitle("Edit a User's Profile");

        alert.setView(userToEdit);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                uidEntered = userToEdit.getText().toString();

                final DatabaseReference databaseReference = firebaseDatabase.getReference(uidEntered);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        showSaveButton();

                        UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                        tiFirstName.setText(userProfile.getfName());
                        tiSurname.setText(userProfile.getsName());
                        tiEmail.setText(userProfile.getEmail());
                        tiPhoneNum.setText(userProfile.getPhoneNum());
                        tiAddress.setText(userProfile.getAddress());
                        tiCity.setText(userProfile.getCity());
                        tiCounty.setText(userProfile.getCounty());
                        tiPostcode.setText(userProfile.getPostcode());
                        tiDOB.setText(userProfile.getDOB());

                        if (userProfile.getDateSubmitted().isEmpty()) {
                            //do nothing
                            tiDateSubmitted.getEditText().setText("");
                        }else {
                            enableFields();
                            tiDateSubmitted.getEditText().setText(userProfile.getDateSubmitted());
                        }

                        //create method to check fields from 2. onwards to see if they have data
                        //.setText if they areNotEmpty

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
                    }
                });


                /*UserProfile profileReturned = null;


                for (UserProfile userProfile : profileList) {
                    if (userProfile.getEmail().equalsIgnoreCase(emailEntered)) {
                        profileReturned = userProfile;
                        break;
                    }
                }

                tietFirstName.setText(profileReturned.getfName());

                //check the email entered matches one from my list of UserProfiles
                //return the UserProfile with the associated email address*/
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
        tiFirstName = view.findViewById(R.id.etFirstName);
        tiSurname = view.findViewById(R.id.etSurname);
        tiEmail = view.findViewById(R.id.etEmail);
        tiPassword = view.findViewById(R.id.etPassword);
        tiPasswordCon = view.findViewById(R.id.etPasswordConfirm);
        tiPhoneNum = view.findViewById(R.id.etPhoneNum);
        tiAddress = view.findViewById(R.id.etAddress);
        tiCity = view.findViewById(R.id.etCity);
        tiCounty = view.findViewById(R.id.etCounty);
        tiPostcode = view.findViewById(R.id.etPostcode);
        tiDOB = view.findViewById(R.id.etDOB);
        mBackButton = view.findViewById(R.id.backBtn);
        mEditButton = view.findViewById(R.id.editBtn);
        mSaveButton = view.findViewById(R.id.saveBtn);

        rgStatement = view.findViewById(R.id.rgStatement);
        tiDateSubmitted = view.findViewById(R.id.tiDateSubmitted);
        mBrowse = view.findViewById(R.id.browseBtn);
        tvFileName = view.findViewById(R.id.fileName);
        mMsg = view.findViewById(R.id.tiMsgField);
        tvPPS = view.findViewById(R.id.tvPPSConfirm);
        rgPPS = view.findViewById(R.id.rgPPS);
        tvTrail = view.findViewById(R.id.tvCourtConfirm);
        rgTrail = view.findViewById(R.id.rgTrail);
        courtDate = view.findViewById(R.id.tiCourtDate);
        courtHouse = view.findViewById(R.id.tiCourtHouse);

        mRegButton = view.findViewById(R.id.regBtn);

    }

    private Boolean validateRegistration() {
        Boolean result = false;

        fName = tiFirstName.getText().toString();
        sName = tiSurname.getText().toString();
        eMail = tiEmail.getText().toString();
        pWord1 = tiPassword.getText().toString();
        pWord2 = tiPasswordCon.getText().toString();
        eMail = tiEmail.getText().toString();
        pNum = tiPhoneNum.getText().toString();
        address = tiAddress.getText().toString();
        city = tiCity.getText().toString();
        county = tiCounty.getText().toString();
        postcode = tiPostcode.getText().toString();
        DOB = tiDOB.getText().toString();

        dateSubmitted = tiDateSubmitted.getEditText().getText().toString();


        if(fName.isEmpty() || sName.isEmpty() || eMail.isEmpty() || pWord1.isEmpty()) {
            Toast.makeText(getActivity(), "Please complete all fields.", Toast.LENGTH_SHORT).show();
        }
        else {
            result = true;
        }

        return result;
    }

    private String generateFileName () {
        UserProfile userProfile = new UserProfile(fName, sName, eMail, pNum, address, city, county, postcode, DOB, dateSubmitted);
        StorageReference docReference = storageReference.child(firebaseAuth.getUid()).child("statement"+ tiFirstName.getText()+ tiSurname.getText());      //can create more children for additional file types
        fileName = docReference.getName();
        return fileName;
    }


    private void sendUserData() {

        //FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile userProfile = new UserProfile(fName, sName, eMail, pNum, address, city, county, postcode, DOB, dateSubmitted);
        StorageReference docReference = storageReference.child(firebaseAuth.getUid()).child("statement"+ userProfile.getsName() + userProfile.getfName());      //can create more children for additional file types
        UploadTask uploadTask = docReference.putFile(docPath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Upload Fail", Toast.LENGTH_SHORT).show();
            }
        });
        myRef.setValue(userProfile);
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

        tiFirstName.getText().clear();
        tiSurname.getText().clear();
        tiEmail.getText().clear();
        tiPassword.getText().clear();
        tiPasswordCon.getText().clear();
        tiPhoneNum.getText().clear();
        tiAddress.getText().clear();
        tiCity.getText().clear();
        tiPostcode.getText().clear();
        tiCounty.getText().clear();
        tiDOB.getText().clear();

    }

    private void disableFields() {

        tiDateSubmitted.setAlpha(0.4f);
        mBrowse.setAlpha(0.4f);
        mMsg.setAlpha(0.4f);
        tvPPS.setAlpha(0.4f);
        rgPPS.setAlpha(0.4f);
        tvTrail.setAlpha(0.4f);
        rgTrail.setAlpha(0.4f);
        courtDate.setAlpha(0.4f);
        courtHouse.setAlpha(0.4f);

        tiDateSubmitted.setEnabled(false);
        mBrowse.setEnabled(false);
        mMsg.setEnabled(false);
        tvPPS.setEnabled(false);
        rgPPS.setEnabled(false);
        tvTrail.setEnabled(false);
        rgTrail.setEnabled(false);
        courtDate.setEnabled(false);
        courtHouse.setEnabled(false);
    }

    private void enableFields() {

        tiDateSubmitted.setAlpha(1f);
        mBrowse.setAlpha(1f);
        mMsg.setAlpha(1f);
        tvPPS.setAlpha(1f);
        rgPPS.setAlpha(1f);
        tvTrail.setAlpha(1f);
        rgTrail.setAlpha(1f);
        courtDate.setAlpha(1f);
        courtHouse.setAlpha(1f);

        tiDateSubmitted.setEnabled(true);
        mBrowse.setEnabled(true);
        mMsg.setEnabled(true);
        tvPPS.setEnabled(true);
        rgPPS.setEnabled(true);
        tvTrail.setEnabled(true);
        rgTrail.setEnabled(true);
        courtDate.setEnabled(true);
        courtHouse.setEnabled(true);
    }

    public boolean checkPasswordMatch(String password1, String password2) {

        boolean result = false;

        if (!password1.equals(password2)) {
            Toast.makeText(getActivity(), "Passwords do not match.", Toast.LENGTH_LONG).show();
        } else {
            result = true;
        }
        return result;
    }
}
