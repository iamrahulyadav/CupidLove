package com.example.cupidlove.model;

/**
 * Created by Kaushal on 16-01-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GalleryImages {

    @SerializedName("gallary")
    @Expose
    public Gallary gallary;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("error")
    @Expose
    public boolean error;

    public GalleryImages withGallary(Gallary gallary) {
        this.gallary = gallary;
        return this;
    }

    public GalleryImages withMessage(String message) {
        this.message = message;
        return this;
    }

    public GalleryImages withError(boolean error) {
        this.error = error;
        return this;
    }

    public class Gallary {

        @SerializedName("img1")
        @Expose
        public String img1;
        @SerializedName("img2")
        @Expose
        public String img2;
        @SerializedName("img3")
        @Expose
        public String img3;
        @SerializedName("img4")
        @Expose
        public String img4;
        @SerializedName("img5")
        @Expose
        public String img5;
        @SerializedName("img6")
        @Expose
        public String img6;

        public Gallary withImg1(String img1) {
            this.img1 = img1;
            return this;
        }

        public Gallary withImg2(String img2) {
            this.img2 = img2;
            return this;
        }

        public Gallary withImg3(String img3) {
            this.img3 = img3;
            return this;
        }

        public Gallary withImg4(String img4) {
            this.img4 = img4;
            return this;
        }

        public Gallary withImg5(String img5) {
            this.img5 = img5;
            return this;
        }

        public Gallary withImg6(String img6) {
            this.img6 = img6;
            return this;
        }

    }

}
