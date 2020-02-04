package com.example.psniproject.LoginScreen;

public class UserProfile {

    private String First, Surname, Email;

    public UserProfile() {

    }

    public UserProfile(String First, String Surname, String Email) {
        this.First = First;
        this.Surname = Surname;
        this.Email = Email;
    }



    public String getfName() {
        return First;
    }

    public void setfName(String fName) {
        this.First = fName;
    }

    public String getsName() {
        return Surname;
    }

    public void setsName(String sName) {
        this.Surname = sName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

}

