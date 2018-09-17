package com.example.cupidlove.model;

/**
 * Created by Kaushal on 04-01-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {

    @SerializedName("error")
    @Expose
    public boolean error;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("loginUserDetails")
    @Expose
    public LoginUserDetails loginUserDetails;
    @SerializedName("user_gallary")
    @Expose
    public UserGallary userGallary;

    public Login withError(boolean error) {
        this.error = error;
        return this;
    }

    public Login withMessage(String message) {
        this.message = message;
        return this;
    }

    public Login withLoginUserDetails(LoginUserDetails loginUserDetails) {
        this.loginUserDetails = loginUserDetails;
        return this;
    }

    public Login withUserGallary(UserGallary userGallary) {
        this.userGallary = userGallary;
        return this;
    }

    public class LoginUserDetails {

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
        public Object fbId;
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

        @SerializedName("ejuser")
        @Expose
        public String ejUser;

        public LoginUserDetails withId(String id) {
            this.id = id;
            return this;
        }

        public LoginUserDetails withFname(String fname) {
            this.fname = fname;
            return this;
        }

        public LoginUserDetails withLname(String lname) {
            this.lname = lname;
            return this;
        }

        public LoginUserDetails withUsername(String username) {
            this.username = username;
            return this;
        }

        public LoginUserDetails withEmail(String email) {
            this.email = email;
            return this;
        }

        public LoginUserDetails withPassword(String password) {
            this.password = password;
            return this;
        }

        public LoginUserDetails withProfileImage(String profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public LoginUserDetails withFbId(Object fbId) {
            this.fbId = fbId;
            return this;
        }

        public LoginUserDetails withDob(String dob) {
            this.dob = dob;
            return this;
        }

        public LoginUserDetails withAbout(String about) {
            this.about = about;
            return this;
        }

        public LoginUserDetails withGender(String gender) {
            this.gender = gender;
            return this;
        }

        public LoginUserDetails withEducation(String education) {
            this.education = education;
            return this;
        }

        public LoginUserDetails withProfession(String profession) {
            this.profession = profession;
            return this;
        }

        public LoginUserDetails withReligion(String religion) {
            this.religion = religion;
            return this;
        }

        public LoginUserDetails withHeight(String height) {
            this.height = height;
            return this;
        }

        public LoginUserDetails withKids(String kids) {
            this.kids = kids;
            return this;
        }

        public LoginUserDetails withAddress(String address) {
            this.address = address;
            return this;
        }

        public LoginUserDetails withLocationLat(String locationLat) {
            this.locationLat = locationLat;
            return this;
        }

        public LoginUserDetails withLocationLong(String locationLong) {
            this.locationLong = locationLong;
            return this;
        }

        public LoginUserDetails withDatePref(String datePref) {
            this.datePref = datePref;
            return this;
        }

        public LoginUserDetails withGenderPref(String genderPref) {
            this.genderPref = genderPref;
            return this;
        }

        public LoginUserDetails withMaxAgePref(String maxAgePref) {
            this.maxAgePref = maxAgePref;
            return this;
        }

        public LoginUserDetails withMinAgePref(String minAgePref) {
            this.minAgePref = minAgePref;
            return this;
        }

        public LoginUserDetails withMaxDistPref(String maxDistPref) {
            this.maxDistPref = maxDistPref;
            return this;
        }

        public LoginUserDetails withMinDistPref(String minDistPref) {
            this.minDistPref = minDistPref;
            return this;
        }

        public LoginUserDetails withEthnicity(String ethnicity) {
            this.ethnicity = ethnicity;
            return this;
        }

        public LoginUserDetails withQueId(String queId) {
            this.queId = queId;
            return this;
        }

        public LoginUserDetails withQueAns(String queAns) {
            this.queAns = queAns;
            return this;
        }

        public LoginUserDetails withAccessLocation(String accessLocation) {
            this.accessLocation = accessLocation;
            return this;
        }

        public LoginUserDetails withStatus(String status) {
            this.status = status;
            return this;
        }

        public LoginUserDetails withIsAdmin(String isAdmin) {
            this.isAdmin = isAdmin;
            return this;
        }

        public LoginUserDetails withIsDeleted(String isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public LoginUserDetails withIsConfirmed(String isConfirmed) {
            this.isConfirmed = isConfirmed;
            return this;
        }

        public LoginUserDetails withCreatedDate(String createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public LoginUserDetails withModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this;
        }

        public LoginUserDetails withNotificationcounter(String notificationcounter) {
            this.notificationcounter = notificationcounter;
            return this;
        }

        public LoginUserDetails withEnableAdd(String enableAdd) {
            this.enableAdd = enableAdd;
            return this;
        }

        public LoginUserDetails withPassToken(String passToken) {
            this.passToken = passToken;
            return this;
        }

        public LoginUserDetails withAge(int age) {
            this.age = age;
            return this;
        }

        public LoginUserDetails withAuthToken(String authToken) {
            this.authToken = authToken;
            return this;
        }

    }

    public class UserGallary {

        @SerializedName("img1")
        @Expose
        public String img1;
        @SerializedName("img3")
        @Expose
        public String img3;
        @SerializedName("img6")
        @Expose
        public String img6;
        @SerializedName("img4")
        @Expose
        public String img4;
        @SerializedName("img2")
        @Expose
        public String img2;
        @SerializedName("img5")
        @Expose
        public String img5;

        public UserGallary withImg1(String img1) {
            this.img1 = img1;
            return this;
        }

        public UserGallary withImg3(String img3) {
            this.img3 = img3;
            return this;
        }

        public UserGallary withImg6(String img6) {
            this.img6 = img6;
            return this;
        }

        public UserGallary withImg4(String img4) {
            this.img4 = img4;
            return this;
        }

        public UserGallary withImg2(String img2) {
            this.img2 = img2;
            return this;
        }

        public UserGallary withImg5(String img5) {
            this.img5 = img5;
            return this;
        }

    }

}

