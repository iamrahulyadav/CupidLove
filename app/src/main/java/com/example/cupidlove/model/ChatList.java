package com.example.cupidlove.model;

/**
 * Created by Kaushal on 08-01-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatList {

    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("body")
    @Expose
    public List<Body> body = null;

    public ChatList.Body bodyObject;

    public ChatList withMessage(String message) {
        this.message = message;
        return this;
    }

    public ChatList withError(String error) {
        this.error = error;
        return this;
    }

    public ChatList withBody(List<Body> body) {
        this.body = body;
        return this;
    }

    public Body getBodyObject() {
        this.bodyObject=new Body();
        return bodyObject;
    }

    public class Body {

        @SerializedName("fid")
        @Expose
        public String fid;
        @SerializedName("fname")
        @Expose
        public String fname;
        @SerializedName("lname")
        @Expose
        public String lname;
        @SerializedName("profile_image")
        @Expose
        public String profileImage;
        @SerializedName("ejuser")
        @Expose
        public String ejUser;

        public Body withFid(String fid) {
            this.fid = fid;
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

    }


}
