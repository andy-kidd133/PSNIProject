package com.example.psniproject.LoginScreen.Models;

public class VictimProfile {

    private String uID, First, Surname, Email, phoneNum, Address, City,
                    County, Postcode, DOB, crimeDate, reportDate, dateSubmitted, message, courtDate;
    private boolean pps;
    private int convicted;
    private Courthouse courtHouse;
    private OfficerProfile officerProfile;
    private UserType userType;

    public VictimProfile() {

    }

    public VictimProfile(String uID,
                         String First, String Surname, String Email, String phoneNum,
                         String Address, String City, String County, String Postcode,
                         String DOB, String crimeDate, String reportDate, OfficerProfile officerProfile,
                         String dateSubmitted, String message, boolean pps, String courtDate,
                         Courthouse courtHouse, int convicted, UserType userType) {
        this.uID = uID;
        this.First = First;
        this.Surname = Surname;
        this.Email = Email;
        this.phoneNum = phoneNum;
        this.Address = Address;
        this.City = City;
        this.County = County;
        this.Postcode = Postcode;
        this.DOB = DOB;
        this.crimeDate = crimeDate;
        this.reportDate = reportDate;
        this.officerProfile = officerProfile;
        this.dateSubmitted = dateSubmitted;
        this.message = message;
        this.pps = pps;
        this.courtDate = courtDate;
        this.courtHouse = courtHouse;
        this.convicted = convicted;
        this.userType = userType;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
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

    public String getCrimeDate() {
        return crimeDate;
    }

    public void setCrimeDate(String crimeDate) {
        this.crimeDate = crimeDate;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public OfficerProfile getOfficerProfile() {
        return officerProfile;
    }

    public void setOfficerProfile(OfficerProfile officerProfile) {
        this.officerProfile = officerProfile;
    }

    public boolean isPps() {
        return pps;
    }

    public void setPps(boolean pps) {
        this.pps = pps;
    }

    public String getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(String dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCourtDate() {
        return courtDate;
    }

    public void setCourtDate(String courtDate) {
        this.courtDate = courtDate;
    }

    public Courthouse getCourtHouse() {
        return courtHouse;
    }

    public void setCourtHouse(Courthouse courtHouse) {
        this.courtHouse = courtHouse;
    }

    public int getConvicted() {
        return convicted;
    }

    public void setConvicted(int convicted) {
        this.convicted = convicted;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}

