
package de.s3xy.retrofitsample.app.pojo;


import com.google.gson.annotations.Expose;


public class From_ {

    @Expose
    private String username;
    @Expose
    private String profile_picture;
    @Expose
    private String id;
    @Expose
    private String full_name;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public From_ withUsername(String username) {
        this.username = username;
        return this;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public From_ withProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public From_ withId(String id) {
        this.id = id;
        return this;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public From_ withFull_name(String full_name) {
        this.full_name = full_name;
        return this;
    }

}
