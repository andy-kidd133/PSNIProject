package com.example.psniproject.LoginScreen;

public class UserProfile {

    private String First, Surname, Email, phoneNum, Address, City,
                    County, Postcode, DOB;

    public UserProfile() {

    }

    public UserProfile(String First, String Surname, String Email, String phoneNum,
                       String Address, String City, String County, String Postcode, String DOB) {
        this.First = First;
        this.Surname = Surname;
        this.Email = Email;
        this.phoneNum = phoneNum;
        this.Address = Address;
        this.City = City;
        this.County = County;
        this.Postcode = Postcode;
        this.DOB = DOB;
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String county) {
        County = county;
    }

    public String getPostcode() {
        return Postcode;
    }

    public void setPostcode(String postcode) {
        Postcode = postcode;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

}

