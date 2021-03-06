package psni.example.psniproject.LoginScreen.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;

public class Courthouse {

    private int id;
    private String name, address;
    private GoogleMap googleMap;
    private double latitude;
    private double longitude;

    public Courthouse() {
    }

    public Courthouse(int id, String name, String address, GoogleMap googleMap, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.googleMap = googleMap;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Courthouse) {
            Courthouse c = (Courthouse) obj;
            if(c.getName().equals(name) && (c.getId() == id)) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
