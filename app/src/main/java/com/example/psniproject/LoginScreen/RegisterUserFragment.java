package com.example.psniproject.LoginScreen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUserFragment extends Fragment {

    private TextInputLayout tiFirstName, tiSurname, tiEmail, tiPassword, tiPasswordCon, tiPhoneNum,
            tiAddress, tiCity, tiCounty, tiPostcode, tiDOB, dateSubmitted, mMsg, courtDate, courtHouse;
    private TextView tvPPS, tvTrail;
    private RadioGroup rgStatement, rgPPS, rgTrail;
    private FirebaseAuth firebaseAuth;
    private View view;
    private Button mBackButton, mRegButton, mBrowse, mEditButton;
    private String fName, sName, eMail, pWord1, pWord2, pNum, address, city, county, postcode, DOB;


    public RegisterUserFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_register_user, container, false);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

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

                    String user_email = tiEmail.getEditText().getText().toString().trim();
                    String user_password = tiPassword.getEditText().getText().toString().trim();

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

        return view;
    }

    private void displayEditAlert() {

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        final EditText userToEdit = new EditText(getActivity());
        alert.setMessage("Enter the email address of the user:");
        alert.setTitle("Edit a User's Profile");

        alert.setView(userToEdit);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String userID = userToEdit.getText().toString();
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

        rgStatement = view.findViewById(R.id.rgStatement);
        dateSubmitted = view.findViewById(R.id.etDateSubmitted);
        mBrowse = view.findViewById(R.id.browseBtn);
        mMsg = view.findViewById(R.id.etMsgField);
        tvPPS = view.findViewById(R.id.tvPPSConfirm);
        rgPPS = view.findViewById(R.id.rgPPS);
        tvTrail = view.findViewById(R.id.tvCourtConfirm);
        rgTrail = view.findViewById(R.id.rgTrail);
        courtDate = view.findViewById(R.id.etCourtDate);
        courtHouse = view.findViewById(R.id.etCourtHouse);

        mRegButton = view.findViewById(R.id.regBtn);

    }

    private Boolean validateRegistration() {
        Boolean result = false;

        fName = tiFirstName.getEditText().getText().toString();
        sName = tiSurname.getEditText().getText().toString();
        eMail = tiEmail.getEditText().getText().toString();
        pWord1 = tiPassword.getEditText().getText().toString();
        pWord2 = tiPasswordCon.getEditText().getText().toString();
        eMail = tiEmail.getEditText().getText().toString();
        pNum = tiPhoneNum.getEditText().getText().toString();
        address = tiAddress.getEditText().getText().toString();
        city = tiCity.getEditText().getText().toString();
        county = tiCounty.getEditText().getText().toString();
        postcode = tiPostcode.getEditText().getText().toString();
        DOB = tiDOB.getEditText().getText().toString();

        //checkPasswordMatch(pWord1, pWord2);

        if(fName.isEmpty() || sName.isEmpty() || eMail.isEmpty() || pWord1.isEmpty()) {
            Toast.makeText(getActivity(), "Please complete all fields.", Toast.LENGTH_SHORT).show();
        }
        else {
            result = true;
        }

        return result;
    }


    private void sendUserData() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile userProfile = new UserProfile(fName, sName, eMail, pNum, address, city, county, postcode, DOB);
        myRef.setValue(userProfile);

    }

    public void resetEditTexts() {

        tiFirstName.getEditText().getText().clear();
        tiSurname.getEditText().getText().clear();
        tiEmail.getEditText().getText().clear();
        tiPassword.getEditText().getText().clear();
        tiPasswordCon.getEditText().getText().clear();
        tiPhoneNum.getEditText().getText().clear();
        tiAddress.getEditText().getText().clear();
        tiCity.getEditText().getText().clear();
        tiPostcode.getEditText().getText().clear();
        tiCounty.getEditText().getText().clear();
        tiDOB.getEditText().getText().clear();

    }

    private void disableFields() {

        dateSubmitted.setAlpha(0.4f);
        mBrowse.setAlpha(0.4f);
        mMsg.setAlpha(0.4f);
        tvPPS.setAlpha(0.4f);
        rgPPS.setAlpha(0.4f);
        tvTrail.setAlpha(0.4f);
        rgTrail.setAlpha(0.4f);
        courtDate.setAlpha(0.4f);
        courtHouse.setAlpha(0.4f);

        dateSubmitted.setEnabled(false);
        dateSubmitted.setEnabled(false);
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

        dateSubmitted.setAlpha(1f);
        mBrowse.setAlpha(1f);
        mMsg.setAlpha(1f);
        tvPPS.setAlpha(1f);
        rgPPS.setAlpha(1f);
        tvTrail.setAlpha(1f);
        rgTrail.setAlpha(1f);
        courtDate.setAlpha(1f);
        courtHouse.setAlpha(1f);

        dateSubmitted.setEnabled(true);
        dateSubmitted.setEnabled(true);
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
