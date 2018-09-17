package com.example.cupidlove.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kaushal on 12-01-2018.
 */

public class FacebookImages {

    @SerializedName("data")
    @Expose
    public List<Datum> data = null;
    @SerializedName("paging")
    @Expose
    public Paging paging;

    public FacebookImages withData(List<Datum> data) {
        this.data = data;
        return this;
    }

    public FacebookImages withPaging(Paging paging) {
        this.paging = paging;
        return this;
    }

    public class Datum {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("picture")
        @Expose
        public String picture;
        @SerializedName("name")
        @Expose
        public String name;

        public Datum withId(String id) {
            this.id = id;
            return this;
        }

        public Datum withPicture(String picture) {
            this.picture = picture;
            return this;
        }

        public Datum withName(String name) {
            this.name = name;
            return this;
        }

    }

    public class Paging {

        @SerializedName("cursors")
        @Expose
        public Cursors cursors;
        @SerializedName("next")
        @Expose
        public String next;

        public Paging withCursors(Cursors cursors) {
            this.cursors = cursors;
            return this;
        }

        public Paging withNext(String next) {
            this.next = next;
            return this;
        }

        public class Cursors {

            @SerializedName("before")
            @Expose
            public String before;
            @SerializedName("after")
            @Expose
            public String after;

            public Cursors withBefore(String before) {
                this.before = before;
                return this;
            }

            public Cursors withAfter(String after) {
                this.after = after;
                return this;
            }

        }

    }
}