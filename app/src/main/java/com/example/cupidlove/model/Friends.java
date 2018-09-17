package com.example.cupidlove.model;

/**
 * Created by Ankita on 5/4/2018.
 */

public class Friends {

    String id, fname, lname, ejaberedid, profileImage, friend_id;


    public Friends() {

    }

    public Friends(String fname, String lname, String ejaberedid, String profileImage, String friend_id) {
        this.fname = fname;
        this.lname = lname;
        this.ejaberedid = ejaberedid;
        this.profileImage = profileImage;
        this.friend_id = friend_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEjaberedid() {
        return ejaberedid;
    }

    public void setEjaberedid(String ejaberedid) {
        this.ejaberedid = ejaberedid;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }
}
