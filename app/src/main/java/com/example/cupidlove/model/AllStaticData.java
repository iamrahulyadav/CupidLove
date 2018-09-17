package com.example.cupidlove.model;

/**
 * Created by Kaushal on 08-01-2018.
 */

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AllStaticData {

    @SerializedName("error")
    @Expose
    public boolean error;
    @SerializedName("religion")
    @Expose
    public List<Religion> religion = null;
    @SerializedName("ethnicity")
    @Expose
    public List<Ethnicity> ethnicity = null;
    @SerializedName("question")
    @Expose
    public List<Question> question = null;

    public AllStaticData withError(boolean error) {
        this.error = error;
        return this;
    }

    public AllStaticData withReligion(List<Religion> religion) {
        this.religion = religion;
        return this;
    }

    public AllStaticData withEthnicity(List<Ethnicity> ethnicity) {
        this.ethnicity = ethnicity;
        return this;
    }

    public AllStaticData withQuestion(List<Question> question) {
        this.question = question;
        return this;
    }

    public class Ethnicity {

        @SerializedName("id")
        @Expose
        public String id;

        @SerializedName("english")
        @Expose
        public String english;

        public Ethnicity withId(String id) {
            this.id = id;
            return this;
        }

        public Ethnicity withEnglish(String english) {
            this.english = english;
            return this;
        }

    }

    public class Question {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("english")
        @Expose
        public String english;

        public Question withId(String id) {
            this.id = id;
            return this;
        }

        public Question withEnglish(String english) {
            this.english = english;
            return this;
        }

    }

    public class Religion {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("english")
        @Expose
        public String english;

        public Religion withId(String id) {
            this.id = id;
            return this;
        }

        public Religion withEnglish(String english) {
            this.english = english;
            return this;
        }

    }
}