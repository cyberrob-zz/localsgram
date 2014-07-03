
package de.s3xy.retrofitsample.app.pojo;


import com.google.gson.annotations.Expose;


public class User_ {

    @Expose
    private String username;
    @Expose
    private String website;
    @Expose
    private String profile_picture;
    @Expose
    private String full_name;
    @Expose
    private String bio;
    @Expose
    private String id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User_ withUsername(String username) {
        this.username = username;
        return this;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public User_ withWebsite(String website) {
        this.website = website;
        return this;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public User_ withProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
        return this;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public User_ withFull_name(String full_name) {
        this.full_name = full_name;
        return this;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public User_ withBio(String bio) {
        this.bio = bio;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User_ withId(String id) {
        this.id = id;
        return this;
    }

}
