package psni.example.psniproject.LoginScreen.Models;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class OfficerProfile {

    private String uID, mFirstName, mSurname, mEmail;
    private UserType userType;
    private ArrayList<String> victimIds;
    private int messageCountO;

    public OfficerProfile() {

    }

    public OfficerProfile(String uID, String mFirstName, String mSurname, String mEmail,
                          UserType userType, ArrayList<String> victimIds, int messageCountO) {

        this.uID = uID;
        this.mFirstName = mFirstName;
        this.mSurname = mSurname;
        this.mEmail = mEmail;
        this.userType = userType;
        this.victimIds = victimIds;
        this.messageCountO = messageCountO;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmSurname() {
        return mSurname;
    }

    public void setmSurname(String mSurname) {
        this.mSurname = mSurname;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public ArrayList<String> getVictimIds() {
        return victimIds;
    }

    public void setVictimIds(ArrayList<String> victimIds) {
        this.victimIds = victimIds;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
