package com.example.kevinfarel.myapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kevin Farel on 14-Apr-17.
 */

public class Profile {

    @SerializedName("Nama_Printer")
    @Expose
    private String Nama_Printer;

    @SerializedName("Email_Printer")
    @Expose
    private String Email_Printer;

    @SerializedName("Password_Printer")
    @Expose
    private String Password_Printer;

    @SerializedName("Position_Latitude")
    @Expose
    private String Position_Latitude;

    @SerializedName("Position_Longitude")
    @Expose
    private String Position_Longitude;

    @SerializedName("Status")
    @Expose
    private String Status;

    public String getName() {
        return Nama_Printer;
    }

    public void setName(String Nama_Printer) {
        this.Nama_Printer = Nama_Printer;
    }
    public String getEmail() {
        return Email_Printer;
    }

    public void setEmail(String Email_Printer) {
        this.Email_Printer = Email_Printer;
    }
    public String getPassword() {
        return Password_Printer;
    }

    public void setPassword(String Password_Printer) {
        this.Password_Printer = Password_Printer;
    }
    public String getLatitude() {
        return Position_Latitude;
    }

    public void setLatitude(String Position_Latitude) {
        this.Position_Latitude = Position_Latitude;
    }
    public String getLongitude() {
        return Position_Longitude;
    }

    public void setLongitude(String Position_Longitude) {
        this.Position_Longitude = Position_Longitude;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }


}
