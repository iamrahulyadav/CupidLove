package com.example.cupidlove.model;

/**
 * Created by Bhumi Shah on 1/22/2018.
 */


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MutualFriend {

    @SerializedName("error")
    @Expose
    public boolean error;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("mutualFriendList")
    @Expose
    public List<MutualFriendList> mutualFriendList = null;

    public MutualFriend withError(boolean error) {
        this.error = error;
        return this;
    }

    public MutualFriend withMessage(String message) {
        this.message = message;
        return this;
    }

    public MutualFriend withMutualFriendList(List<MutualFriendList> mutualFriendList) {
        this.mutualFriendList = mutualFriendList;
        return this;
    }


    public class MutualFriendList {

        @SerializedName("about")
        @Expose
        public String about;
        @SerializedName("access_location")
        @Expose
        public int accessLocation;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("created_date")
        @Expose
        public String createdDate;
        @SerializedName("date_pref")
        @Expose
        public String datePref;
        @SerializedName("dob")
        @Expose
        public String dob;
        @SerializedName("education")
        @Expose
        public String education;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("enableAdd")
        @Expose
        public int enableAdd;
        @SerializedName("ethnicity")
        @Expose
        public String ethnicity;
        @SerializedName("fb_id")
        @Expose
        public String fbId;
        @SerializedName("fname")
        @Expose
        public String fname;
        @SerializedName("gender")
        @Expose
        public String gender;
        @SerializedName("gender_pref")
        @Expose
        public String genderPref;
        @SerializedName("height")
        @Expose
        public String height;
        @SerializedName("id")
        @Expose
        public int id;
        @SerializedName("is_admin")
        @Expose
        public int isAdmin;
        @SerializedName("is_confirmed")
        @Expose
        public int isConfirmed;
        @SerializedName("is_deleted")
        @Expose
        public int isDeleted;
        @SerializedName("kids")
        @Expose
        public String kids;
        @SerializedName("lname")
        @Expose
        public String lname;
        @SerializedName("location_lat")
        @Expose
        public String locationLat;
        @SerializedName("location_long")
        @Expose
        public String locationLong;
        @SerializedName("max_age_pref")
        @Expose
        public int maxAgePref;
        @SerializedName("max_dist_pref")
        @Expose
        public int maxDistPref;
        @SerializedName("min_age_pref")
        @Expose
        public int minAgePref;
        @SerializedName("min_dist_pref")
        @Expose
        public int minDistPref;
        @SerializedName("modified_date")
        @Expose
        public String modifiedDate;
        @SerializedName("notificationcounter")
        @Expose
        public int notificationcounter;
        @SerializedName("pass_token")
        @Expose
        public String passToken;
        @SerializedName("password")
        @Expose
        public String password;
        @SerializedName("profession")
        @Expose
        public String profession;
        @SerializedName("profile_image")
        @Expose
        public String profileImage;
        @SerializedName("que_ans")
        @Expose
        public String queAns;
        @SerializedName("que_id")
        @Expose
        public int queId;
        @SerializedName("religion")
        @Expose
        public String religion;
        @SerializedName("status")
        @Expose
        public int status;
        @SerializedName("username")
        @Expose
        public String username;

        public MutualFriendList withAbout(String about) {
            this.about = about;
            return this;
        }

        public MutualFriendList withAccessLocation(int accessLocation) {
            this.accessLocation = accessLocation;
            return this;
        }

        public MutualFriendList withAddress(String address) {
            this.address = address;
            return this;
        }

        public MutualFriendList withCreatedDate(String createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public MutualFriendList withDatePref(String datePref) {
            this.datePref = datePref;
            return this;
        }

        public MutualFriendList withDob(String dob) {
            this.dob = dob;
            return this;
        }

        public MutualFriendList withEducation(String education) {
            this.education = education;
            return this;
        }

        public MutualFriendList withEmail(String email) {
            this.email = email;
            return this;
        }

        public MutualFriendList withEnableAdd(int enableAdd) {
            this.enableAdd = enableAdd;
            return this;
        }

        public MutualFriendList withEthnicity(String ethnicity) {
            this.ethnicity = ethnicity;
            return this;
        }

        public MutualFriendList withFbId(String fbId) {
            this.fbId = fbId;
            return this;
        }

        public MutualFriendList withFname(String fname) {
            this.fname = fname;
            return this;
        }

        public MutualFriendList withGender(String gender) {
            this.gender = gender;
            return this;
        }

        public MutualFriendList withGenderPref(String genderPref) {
            this.genderPref = genderPref;
            return this;
        }

        public MutualFriendList withHeight(String height) {
            this.height = height;
            return this;
        }

        public MutualFriendList withId(int id) {
            this.id = id;
            return this;
        }

        public MutualFriendList withIsAdmin(int isAdmin) {
            this.isAdmin = isAdmin;
            return this;
        }

        public MutualFriendList withIsConfirmed(int isConfirmed) {
            this.isConfirmed = isConfirmed;
            return this;
        }

        public MutualFriendList withIsDeleted(int isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public MutualFriendList withKids(String kids) {
            this.kids = kids;
            return this;
        }

        public MutualFriendList withLname(String lname) {
            this.lname = lname;
            return this;
        }

        public MutualFriendList withLocationLat(String locationLat) {
            this.locationLat = locationLat;
            return this;
        }

        public MutualFriendList withLocationLong(String locationLong) {
            this.locationLong = locationLong;
            return this;
        }

        public MutualFriendList withMaxAgePref(int maxAgePref) {
            this.maxAgePref = maxAgePref;
            return this;
        }

        public MutualFriendList withMaxDistPref(int maxDistPref) {
            this.maxDistPref = maxDistPref;
            return this;
        }

        public MutualFriendList withMinAgePref(int minAgePref) {
            this.minAgePref = minAgePref;
            return this;
        }

        public MutualFriendList withMinDistPref(int minDistPref) {
            this.minDistPref = minDistPref;
            return this;
        }

        public MutualFriendList withModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this;
        }

        public MutualFriendList withNotificationcounter(int notificationcounter) {
            this.notificationcounter = notificationcounter;
            return this;
        }

        public MutualFriendList withPassToken(String passToken) {
            this.passToken = passToken;
            return this;
        }

        public MutualFriendList withPassword(String password) {
            this.password = password;
            return this;
        }

        public MutualFriendList withProfession(String profession) {
            this.profession = profession;
            return this;
        }

        public MutualFriendList withProfileImage(String profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public MutualFriendList withQueAns(String queAns) {
            this.queAns = queAns;
            return this;
        }

        public MutualFriendList withQueId(int queId) {
            this.queId = queId;
            return this;
        }

        public MutualFriendList withReligion(String religion) {
            this.religion = religion;
            return this;
        }

        public MutualFriendList withStatus(int status) {
            this.status = status;
            return this;
        }

        public MutualFriendList withUsername(String username) {
            this.username = username;
            return this;
        }

    }
}
