package com.example.cupidlove.model;

/**
 * Created by Kaushal on 09-01-2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InstagramUserImage {

    @SerializedName("error")
    @Expose
    public boolean error;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("InstaImages")
    @Expose
    public List<InstaImage> instaImages = null;

    public InstagramUserImage withError(boolean error) {
        this.error = error;
        return this;
    }

    public InstagramUserImage withMessage(String message) {
        this.message = message;
        return this;
    }

    public InstagramUserImage withInstaImages(List<InstaImage> instaImages) {
        this.instaImages = instaImages;
        return this;
    }

    public class InstaImage {

        @SerializedName("url")
        @Expose
        public String url;

        public InstaImage withUrl(String url) {
            this.url = url;
            return this;
        }

    }

}
