package com.example.barterx.model;

public class Profile {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone ;
    private String imageUrl;
    private double latitude;
    private  double logitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLogitude() {
        return logitude;
    }

    public Profile(String id, String firstname, String lastname, String email, String phone, String imageUrl, double latitude, double logitude) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.logitude = logitude;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id='" + id + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", latitude=" + latitude +
                ", logitude=" + logitude +
                '}';
    }

    public Profile() {
    }

    public Profile(String id, String firstname, String lastname, String email, String phone, String imageUrl) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
