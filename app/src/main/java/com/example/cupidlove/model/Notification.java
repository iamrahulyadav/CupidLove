package com.example.cupidlove.model;

/**
 * Created by Kaushal on 08-01-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Notification {

    @SerializedName("error")
    @Expose
    public boolean error;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("body")
    @Expose
    public List<Body> body = null;

    public Notification withError(boolean error) {
        this.error = error;
        return this;
    }

    public Notification withMessage(String message) {
        this.message = message;
        return this;
    }

    public Notification withBody(List<Body> body) {
        this.body = body;
        return this;
    }

    public class Body {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("send_user_id")
        @Expose
        public String sendUserId;
        @SerializedName("receive_user_id")
        @Expose
        public String receiveUserId;
        @SerializedName("approved")
        @Expose
        public String approved;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("created_date")
        @Expose
        public String createdDate;
        @SerializedName("modified_date")
        @Expose
        public String modifiedDate;
        @SerializedName("fname")
        @Expose
        public String fname;
        @SerializedName("lname")
        @Expose
        public String lname;
        @SerializedName("profile_image")
        @Expose
        public String profileImage;
        @SerializedName("location_lat")
        @Expose
        public String locationLat;
        @SerializedName("location_long")
        @Expose
        public String locationLong;
        @SerializedName("distance")
        @Expose
        public float distance;
        @SerializedName("ejuser")
        @Expose
        public String ejUser;


        public Body withId(String id) {
            this.id = id;
            return this;
        }

        public Body withSendUserId(String sendUserId) {
            this.sendUserId = sendUserId;
            return this;
        }

        public Body withReceiveUserId(String receiveUserId) {
            this.receiveUserId = receiveUserId;
            return this;
        }

        public Body withApproved(String approved) {
            this.approved = approved;
            return this;
        }

        public Body withStatus(String status) {
            this.status = status;
            return this;
        }

        public Body withCreatedDate(String createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Body withModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this;
        }

        public Body withFname(String fname) {
            this.fname = fname;
            return this;
        }

        public Body withLname(String lname) {
            this.lname = lname;
            return this;
        }

        public Body withProfileImage(String profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public Body withLocationLat(String locationLat) {
            this.locationLat = locationLat;
            return this;
        }

        public Body withLocationLong(String locationLong) {
            this.locationLong = locationLong;
            return this;
        }


    }

}