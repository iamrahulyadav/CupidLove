package com.example.cupidlove.model;

/**
 * Created by Jitendra on 1/19/2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GooglePlaces {

    @SerializedName("html_attributions")
    @Expose
    public List<Object> htmlAttributions = null;
    @SerializedName("next_page_token")
    @Expose
    public String nextPageToken;
    @SerializedName("results")
    @Expose
    public List<Result> results = null;
    @SerializedName("status")
    @Expose
    public String status;

    public GooglePlaces withHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
        return this;
    }

    public GooglePlaces withNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
        return this;
    }

    public GooglePlaces withResults(List<Result> results) {
        this.results = results;
        return this;
    }

    public GooglePlaces withStatus(String status) {
        this.status = status;
        return this;
    }

    public class Result {

        @SerializedName("geometry")
        @Expose
        public Geometry geometry;
        @SerializedName("icon")
        @Expose
        public String icon;
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("opening_hours")
        @Expose
        public OpeningHours openingHours;
        @SerializedName("photos")
        @Expose
        public List<Photo> photos = null;
        @SerializedName("place_id")
        @Expose
        public String placeId;
        @SerializedName("rating")
        @Expose
        public float rating;
        @SerializedName("reference")
        @Expose
        public String reference;
        @SerializedName("scope")
        @Expose
        public String scope;
        @SerializedName("types")
        @Expose
        public List<String> types = null;
        @SerializedName("vicinity")
        @Expose
        public String vicinity;

        public Result withGeometry(Geometry geometry) {
            this.geometry = geometry;
            return this;
        }

        public Result withIcon(String icon) {
            this.icon = icon;
            return this;
        }

        public Result withId(String id) {
            this.id = id;
            return this;
        }

        public Result withName(String name) {
            this.name = name;
            return this;
        }

        public Result withOpeningHours(OpeningHours openingHours) {
            this.openingHours = openingHours;
            return this;
        }

        public Result withPhotos(List<Photo> photos) {
            this.photos = photos;
            return this;
        }

        public Result withPlaceId(String placeId) {
            this.placeId = placeId;
            return this;
        }

        public Result withRating(float rating) {
            this.rating = rating;
            return this;
        }

        public Result withReference(String reference) {
            this.reference = reference;
            return this;
        }

        public Result withScope(String scope) {
            this.scope = scope;
            return this;
        }

        public Result withTypes(List<String> types) {
            this.types = types;
            return this;
        }

        public Result withVicinity(String vicinity) {
            this.vicinity = vicinity;
            return this;
        }

        public class Geometry {

            @SerializedName("location")
            @Expose
            public Location location;
            @SerializedName("viewport")
            @Expose
            public Viewport viewport;

            public Geometry withLocation(Location location) {
                this.location = location;
                return this;
            }

            public Geometry withViewport(Viewport viewport) {
                this.viewport = viewport;
                return this;
            }

            public class Location {

                @SerializedName("lat")
                @Expose
                public float lat;
                @SerializedName("lng")
                @Expose
                public float lng;

                public Location withLat(float lat) {
                    this.lat = lat;
                    return this;
                }

                public Location withLng(float lng) {
                    this.lng = lng;
                    return this;
                }

            }

            public class Viewport {

                @SerializedName("northeast")
                @Expose
                public Northeast northeast;
                @SerializedName("southwest")
                @Expose
                public Southwest southwest;

                public Viewport withNortheast(Northeast northeast) {
                    this.northeast = northeast;
                    return this;
                }

                public Viewport withSouthwest(Southwest southwest) {
                    this.southwest = southwest;
                    return this;
                }

                public class Northeast {

                    @SerializedName("lat")
                    @Expose
                    public float lat;
                    @SerializedName("lng")
                    @Expose
                    public float lng;

                    public Northeast withLat(float lat) {
                        this.lat = lat;
                        return this;
                    }

                    public Northeast withLng(float lng) {
                        this.lng = lng;
                        return this;
                    }

                }

                public class Southwest {

                    @SerializedName("lat")
                    @Expose
                    public float lat;
                    @SerializedName("lng")
                    @Expose
                    public float lng;

                    public Southwest withLat(float lat) {
                        this.lat = lat;
                        return this;
                    }

                    public Southwest withLng(float lng) {
                        this.lng = lng;
                        return this;
                    }

                }

            }

        }

        public class OpeningHours {

            @SerializedName("open_now")
            @Expose
            public boolean openNow;
            @SerializedName("weekday_text")
            @Expose
            public List<Object> weekdayText = null;

            public OpeningHours withOpenNow(boolean openNow) {
                this.openNow = openNow;
                return this;
            }

            public OpeningHours withWeekdayText(List<Object> weekdayText) {
                this.weekdayText = weekdayText;
                return this;
            }

        }

        public class Photo {

            @SerializedName("height")
            @Expose
            public int height;
            @SerializedName("html_attributions")
            @Expose
            public List<String> htmlAttributions = null;
            @SerializedName("photo_reference")
            @Expose
            public String photoReference;
            @SerializedName("width")
            @Expose
            public int width;

            public Photo withHeight(int height) {
                this.height = height;
                return this;
            }

            public Photo withHtmlAttributions(List<String> htmlAttributions) {
                this.htmlAttributions = htmlAttributions;
                return this;
            }

            public Photo withPhotoReference(String photoReference) {
                this.photoReference = photoReference;
                return this;
            }

            public Photo withWidth(int width) {
                this.width = width;
                return this;
            }

        }

    }

}


