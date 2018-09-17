package com.example.cupidlove.model;

/**
 * Created by Kaushal on 10-01-2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Register {

    @SerializedName("error")
    @Expose
    public boolean error;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("body")
    @Expose
    public Body body;

    public Register withError(boolean error) {
        this.error = error;
        return this;
    }

    public Register withMessage(String message) {
        this.message = message;
        return this;
    }

    public Register withBody(Body body) {
        this.body = body;
        return this;
    }

    public class Body {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("fname")
        @Expose
        public String fname;
        @SerializedName("lname")
        @Expose
        public String lname;
        @SerializedName("username")
        @Expose
        public String username;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("password")
        @Expose
        public String password;
        @SerializedName("profile_image")
        @Expose
        public String profileImage;
        @SerializedName("fb_id")
        @Expose
        public String fbId;
        @SerializedName("dob")
        @Expose
        public String dob;
        @SerializedName("about")
        @Expose
        public String about;
        @SerializedName("gender")
        @Expose
        public String gender;
        @SerializedName("education")
        @Expose
        public String education;
        @SerializedName("profession")
        @Expose
        public String profession;
        @SerializedName("religion")
        @Expose
        public String religion;
        @SerializedName("height")
        @Expose
        public String height;
        @SerializedName("kids")
        @Expose
        public String kids;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("location_lat")
        @Expose
        public String locationLat;
        @SerializedName("location_long")
        @Expose
        public String locationLong;
        @SerializedName("date_pref")
        @Expose
        public String datePref;
        @SerializedName("gender_pref")
        @Expose
        public String genderPref;
        @SerializedName("max_age_pref")
        @Expose
        public String maxAgePref;
        @SerializedName("min_age_pref")
        @Expose
        public String minAgePref;
        @SerializedName("max_dist_pref")
        @Expose
        public String maxDistPref;
        @SerializedName("min_dist_pref")
        @Expose
        public String minDistPref;
        @SerializedName("ethnicity")
        @Expose
        public String ethnicity;
        @SerializedName("que_id")
        @Expose
        public String queId;
        @SerializedName("que_ans")
        @Expose
        public String queAns;
        @SerializedName("access_location")
        @Expose
        public String accessLocation;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("is_admin")
        @Expose
        public String isAdmin;
        @SerializedName("is_deleted")
        @Expose
        public String isDeleted;
        @SerializedName("is_confirmed")
        @Expose
        public String isConfirmed;
        @SerializedName("created_date")
        @Expose
        public String createdDate;
        @SerializedName("modified_date")
        @Expose
        public String modifiedDate;
        @SerializedName("notificationcounter")
        @Expose
        public String notificationcounter;
        @SerializedName("enableAdd")
        @Expose
        public String enableAdd;
        @SerializedName("pass_token")
        @Expose
        public String passToken;
        @SerializedName("age")
        @Expose
        public int age;
        @SerializedName("AuthToken")
        @Expose
        public String authToken;

        @SerializedName("new_user")
        @Expose
        public String newUser;

        @SerializedName("ejuser")
        @Expose
        public String ejUser;


        public Body withId(String id) {
            this.id = id;
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

        public Body withUsername(String username) {
            this.username = username;
            return this;
        }

        public Body withEmail(String email) {
            this.email = email;
            return this;
        }

        public Body withPassword(String password) {
            this.password = password;
            return this;
        }

        public Body withProfileImage(String profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public Body withFbId(String fbId) {
            this.fbId = fbId;
            return this;
        }

        public Body withDob(String dob) {
            this.dob = dob;
            return this;
        }

        public Body withAbout(String about) {
            this.about = about;
            return this;
        }

        public Body withGender(String gender) {
            this.gender = gender;
            return this;
        }

        public Body withEducation(String education) {
            this.education = education;
            return this;
        }

        public Body withProfession(String profession) {
            this.profession = profession;
            return this;
        }

        public Body withReligion(String religion) {
            this.religion = religion;
            return this;
        }

        public Body withHeight(String height) {
            this.height = height;
            return this;
        }

        public Body withKids(String kids) {
            this.kids = kids;
            return this;
        }

        public Body withAddress(String address) {
            this.address = address;
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

        public Body withDatePref(String datePref) {
            this.datePref = datePref;
            return this;
        }

        public Body withGenderPref(String genderPref) {
            this.genderPref = genderPref;
            return this;
        }

        public Body withMaxAgePref(String maxAgePref) {
            this.maxAgePref = maxAgePref;
            return this;
        }

        public Body withMinAgePref(String minAgePref) {
            this.minAgePref = minAgePref;
            return this;
        }

        public Body withMaxDistPref(String maxDistPref) {
            this.maxDistPref = maxDistPref;
            return this;
        }

        public Body withMinDistPref(String minDistPref) {
            this.minDistPref = minDistPref;
            return this;
        }

        public Body withEthnicity(String ethnicity) {
            this.ethnicity = ethnicity;
            return this;
        }

        public Body withQueId(String queId) {
            this.queId = queId;
            return this;
        }

        public Body withQueAns(String queAns) {
            this.queAns = queAns;
            return this;
        }

        public Body withAccessLocation(String accessLocation) {
            this.accessLocation = accessLocation;
            return this;
        }

        public Body withStatus(String status) {
            this.status = status;
            return this;
        }

        public Body withIsAdmin(String isAdmin) {
            this.isAdmin = isAdmin;
            return this;
        }

        public Body withIsDeleted(String isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public Body withIsConfirmed(String isConfirmed) {
            this.isConfirmed = isConfirmed;
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

        public Body withNotificationcounter(String notificationcounter) {
            this.notificationcounter = notificationcounter;
            return this;
        }

        public Body withEnableAdd(String enableAdd) {
            this.enableAdd = enableAdd;
            return this;
        }

        public Body withPassToken(String passToken) {
            this.passToken = passToken;
            return this;
        }

        public Body withAge(int age) {
            this.age = age;
            return this;
        }

        public Body withAuthToken(String authToken) {
            this.authToken = authToken;
            return this;
        }

        public Body withNewUser(String newUser) {
            this.newUser = newUser;
            return this;
        }

    }

}