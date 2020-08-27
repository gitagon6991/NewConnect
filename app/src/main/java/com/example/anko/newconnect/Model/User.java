package com.example.anko.newconnect.Model;

/**
 * Created by ANKO on 22/07/2020.
 */

public class User {
    private String id;
    private String username;
    private String imageurl;
    private String bio;

    //new additions
    private String age;
    private String country;
    private String gender;
    private String phonenumber;


    public User(String id, String username, String imageurl, String bio, String age, String country, String gender, String phonenumber) {
        this.id = id;
        this.username = username;
        this.imageurl = imageurl;
        this.bio = bio;
        this.age = age;
        this.country = country;
        this.gender = gender;
        this.phonenumber = phonenumber;
    }

    public User() {
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
