package com.example.cupidlove.model;

/**
 * Created by Kaushal on 28-12-2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SideMenu {

    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("image")
    @Expose
    public int image;

    public SideMenu withTitle(String title) {
        this.title = title;
        return this;
    }
    public SideMenu withImage(int image) {
        this.image = image;
        return this;
    }

}