package com.example.psniproject.LoginScreen;

public class UserProfile {

    private String Age, First, Surname, Usertype, Email;

    public UserProfile(String Age, String First, String Surname, String Usertype, String Email) {
        this.Age = Age;
        this.First = First;
        this.Surname = Surname;
        this.Usertype = Usertype;
        this.Email = Email;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        this.Age = age;
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

    public String getUserType() {
        return Usertype;
    }

    public void setUserType(String userType) {
        this.Usertype = userType;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }
}
