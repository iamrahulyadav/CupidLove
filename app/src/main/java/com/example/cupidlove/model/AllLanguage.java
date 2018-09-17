package com.example.cupidlove.model;

/**
 * Created by Kaushal on 04-01-2018.
 */

        import java.util.List;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class AllLanguage {


    @SerializedName("error")
    @Expose
    public boolean error;
    @SerializedName("language")
    @Expose
    public List<Language> language = null;

    public AllLanguage withError(boolean error) {
        this.error = error;
        return this;
    }

    public AllLanguage withLanguage(List<Language> language) {
        this.language = language;
        return this;
    }

    public class Language {

        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("rtl")
        @Expose
        public String rtl;

        public Language withName(String name) {
            this.name = name;
            return this;
        }

        public Language withRtl(String rtl) {
            this.rtl = rtl;
            return this;
        }

    }

}