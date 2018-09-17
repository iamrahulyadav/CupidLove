package com.example.cupidlove.model;

/**
 * Created by Kaushal on 09-01-2018.
 */


        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

        import java.util.List;

public class InstagramImages {

    @SerializedName("pagination")
    @Expose
    public Pagination pagination;
    @SerializedName("data")
    @Expose
    public List<Datum> data = null;
    @SerializedName("meta")
    @Expose
    public Meta meta;

    public InstagramImages withPagination(Pagination pagination) {
        this.pagination = pagination;
        return this;
    }

    public InstagramImages withData(List<Datum> data) {
        this.data = data;
        return this;
    }

    public InstagramImages withMeta(Meta meta) {
        this.meta = meta;
        return this;
    }

    public class Pagination {

        @SerializedName("next_max_id")
        @Expose
        public String nextMaxId;
        @SerializedName("next_url")
        @Expose
        public String nextUrl;

        public Pagination withNextMaxId(String nextMaxId) {
            this.nextMaxId = nextMaxId;
            return this;
        }

        public Pagination withNextUrl(String nextUrl) {
            this.nextUrl = nextUrl;
            return this;
        }

    }

    public class Datum {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("user")
        @Expose
        public User user;
        @SerializedName("images")
        @Expose
        public Images images;
        @SerializedName("created_time")
        @Expose
        public String createdTime;
        @SerializedName("caption")
        @Expose
        public Object caption;
        @SerializedName("user_has_liked")
        @Expose
        public boolean userHasLiked;
        @SerializedName("likes")
        @Expose
        public Likes likes;
        @SerializedName("tags")
        @Expose
        public List<Object> tags = null;
        @SerializedName("filter")
        @Expose
        public String filter;
        @SerializedName("comments")
        @Expose
        public Comments comments;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("link")
        @Expose
        public String link;
        @SerializedName("location")
        @Expose
        public Object location;
        @SerializedName("attribution")
        @Expose
        public Object attribution;
        @SerializedName("users_in_photo")
        @Expose
        public List<Object> usersInPhoto = null;
        @SerializedName("videos")
        @Expose
        public Videos videos;

        public Datum withId(String id) {
            this.id = id;
            return this;
        }

        public Datum withUser(User user) {
            this.user = user;
            return this;
        }

        public Datum withImages(Images images) {
            this.images = images;
            return this;
        }

        public Datum withCreatedTime(String createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public Datum withCaption(Object caption) {
            this.caption = caption;
            return this;
        }

        public Datum withUserHasLiked(boolean userHasLiked) {
            this.userHasLiked = userHasLiked;
            return this;
        }

        public Datum withLikes(Likes likes) {
            this.likes = likes;
            return this;
        }

        public Datum withTags(List<Object> tags) {
            this.tags = tags;
            return this;
        }

        public Datum withFilter(String filter) {
            this.filter = filter;
            return this;
        }

        public Datum withComments(Comments comments) {
            this.comments = comments;
            return this;
        }

        public Datum withType(String type) {
            this.type = type;
            return this;
        }

        public Datum withLink(String link) {
            this.link = link;
            return this;
        }

        public Datum withLocation(Object location) {
            this.location = location;
            return this;
        }

        public Datum withAttribution(Object attribution) {
            this.attribution = attribution;
            return this;
        }

        public Datum withUsersInPhoto(List<Object> usersInPhoto) {
            this.usersInPhoto = usersInPhoto;
            return this;
        }

        public Datum withVideos(Videos videos) {
            this.videos = videos;
            return this;
        }

        public class Images {

            @SerializedName("thumbnail")
            @Expose
            public Thumbnail thumbnail;
            @SerializedName("low_resolution")
            @Expose
            public LowResolution lowResolution;
            @SerializedName("standard_resolution")
            @Expose
            public StandardResolution standardResolution;

            public Images withThumbnail(Thumbnail thumbnail) {
                this.thumbnail = thumbnail;
                return this;
            }

            public Images withLowResolution(LowResolution lowResolution) {
                this.lowResolution = lowResolution;
                return this;
            }

            public Images withStandardResolution(StandardResolution standardResolution) {
                this.standardResolution = standardResolution;
                return this;
            }

            public class Thumbnail {

                @SerializedName("width")
                @Expose
                public int width;
                @SerializedName("height")
                @Expose
                public int height;
                @SerializedName("url")
                @Expose
                public String url;

                public Thumbnail withWidth(int width) {
                    this.width = width;
                    return this;
                }

                public Thumbnail withHeight(int height) {
                    this.height = height;
                    return this;
                }

                public Thumbnail withUrl(String url) {
                    this.url = url;
                    return this;
                }

            }

            public class LowResolution {

                @SerializedName("width")
                @Expose
                public int width;
                @SerializedName("height")
                @Expose
                public int height;
                @SerializedName("url")
                @Expose
                public String url;

                public LowResolution withWidth(int width) {
                    this.width = width;
                    return this;
                }

                public LowResolution withHeight(int height) {
                    this.height = height;
                    return this;
                }

                public LowResolution withUrl(String url) {
                    this.url = url;
                    return this;
                }

            }

            public class StandardResolution {

                @SerializedName("width")
                @Expose
                public int width;
                @SerializedName("height")
                @Expose
                public int height;
                @SerializedName("url")
                @Expose
                public String url;

                public StandardResolution withWidth(int width) {
                    this.width = width;
                    return this;
                }

                public StandardResolution withHeight(int height) {
                    this.height = height;
                    return this;
                }

                public StandardResolution withUrl(String url) {
                    this.url = url;
                    return this;
                }

            }

        }

        public class Likes {

            @SerializedName("count")
            @Expose
            public int count;

            public Likes withCount(int count) {
                this.count = count;
                return this;
            }

        }

        public class Comments {

            @SerializedName("count")
            @Expose
            public int count;

            public Comments withCount(int count) {
                this.count = count;
                return this;
            }


        }

        public class User {

            @SerializedName("id")
            @Expose
            public String id;
            @SerializedName("full_name")
            @Expose
            public String fullName;
            @SerializedName("profile_picture")
            @Expose
            public String profilePicture;
            @SerializedName("username")
            @Expose
            public String username;

            public User withId(String id) {
                this.id = id;
                return this;
            }

            public User withFullName(String fullName) {
                this.fullName = fullName;
                return this;
            }

            public User withProfilePicture(String profilePicture) {
                this.profilePicture = profilePicture;
                return this;
            }

            public User withUsername(String username) {
                this.username = username;
                return this;
            }

        }

        public class Videos {

            @SerializedName("standard_resolution")
            @Expose
            public StandardResolution_ standardResolution;
            @SerializedName("low_bandwidth")
            @Expose
            public LowBandwidth lowBandwidth;
            @SerializedName("low_resolution")
            @Expose
            public LowResolution_ lowResolution;

            public Videos withStandardResolution(StandardResolution_ standardResolution) {
                this.standardResolution = standardResolution;
                return this;
            }

            public Videos withLowBandwidth(LowBandwidth lowBandwidth) {
                this.lowBandwidth = lowBandwidth;
                return this;
            }

            public Videos withLowResolution(LowResolution_ lowResolution) {
                this.lowResolution = lowResolution;
                return this;
            }

            public class StandardResolution_ {

                @SerializedName("width")
                @Expose
                public int width;
                @SerializedName("height")
                @Expose
                public int height;
                @SerializedName("url")
                @Expose
                public String url;
                @SerializedName("id")
                @Expose
                public String id;

                public StandardResolution_ withWidth(int width) {
                    this.width = width;
                    return this;
                }

                public StandardResolution_ withHeight(int height) {
                    this.height = height;
                    return this;
                }

                public StandardResolution_ withUrl(String url) {
                    this.url = url;
                    return this;
                }

                public StandardResolution_ withId(String id) {
                    this.id = id;
                    return this;
                }

            }

            public class LowBandwidth {

                @SerializedName("width")
                @Expose
                public int width;
                @SerializedName("height")
                @Expose
                public int height;
                @SerializedName("url")
                @Expose
                public String url;
                @SerializedName("id")
                @Expose
                public String id;

                public LowBandwidth withWidth(int width) {
                    this.width = width;
                    return this;
                }

                public LowBandwidth withHeight(int height) {
                    this.height = height;
                    return this;
                }

                public LowBandwidth withUrl(String url) {
                    this.url = url;
                    return this;
                }

                public LowBandwidth withId(String id) {
                    this.id = id;
                    return this;
                }

            }

            public class LowResolution_ {

                @SerializedName("width")
                @Expose
                public int width;
                @SerializedName("height")
                @Expose
                public int height;
                @SerializedName("url")
                @Expose
                public String url;
                @SerializedName("id")
                @Expose
                public String id;

                public LowResolution_ withWidth(int width) {
                    this.width = width;
                    return this;
                }

                public LowResolution_ withHeight(int height) {
                    this.height = height;
                    return this;
                }

                public LowResolution_ withUrl(String url) {
                    this.url = url;
                    return this;
                }

                public LowResolution_ withId(String id) {
                    this.id = id;
                    return this;
                }

            }

        }

    }

    public class Meta {

        @SerializedName("code")
        @Expose
        public int code;

        public Meta withCode(int code) {
            this.code = code;
            return this;
        }

    }

}

