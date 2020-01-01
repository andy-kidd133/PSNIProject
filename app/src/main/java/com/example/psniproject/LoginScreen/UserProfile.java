package com.example.psniproject.LoginScreen;

public class UserProfile {

    private String age, fName, sName, userType, email;

    public UserProfile(String age, String fName, String sName, String userType, String email) {
        this.age = age;
        this.fName = fName;
        this.sName = sName;
        this.userType = userType;
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
