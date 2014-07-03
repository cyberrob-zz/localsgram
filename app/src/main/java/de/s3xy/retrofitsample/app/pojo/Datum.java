
package de.s3xy.retrofitsample.app.pojo;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;


public class Datum {

    @Expose
    private Object attribution;
    @Expose
    private List<Object> tags = new ArrayList<Object>();
    @Expose
    private String type;
    @Expose
    private Object location;
    @Expose
    private Comments comments;
    @Expose
    private String filter;
    @Expose
    private String created_time;
    @Expose
    private String link;
    @Expose
    private Likes likes;
    @Expose
    private Images images;
    @Expose
    private List<Users_in_photo> users_in_photo = new ArrayList<Users_in_photo>();
    @Expose
    private Caption caption;
    @Expose
    private Boolean user_has_liked;
    @Expose
    private String id;
    @Expose
    private User_ user;

    public Object getAttribution() {
        return attribution;
    }

    public void setAttribution(Object attribution) {
        this.attribution = attribution;
    }

    public Datum withAttribution(Object attribution) {
        this.attribution = attribution;
        return this;
    }

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    public Datum withTags(List<Object> tags) {
        this.tags = tags;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Datum withType(String type) {
        this.type = type;
        return this;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    public Datum withLocation(Object location) {
        this.location = location;
        return this;
    }

    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }

    public Datum withComments(Comments comments) {
        this.comments = comments;
        return this;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Datum withFilter(String filter) {
        this.filter = filter;
        return this;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public Datum withCreated_time(String created_time) {
        this.created_time = created_time;
        return this;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Datum withLink(String link) {
        this.link = link;
        return this;
    }

    public Likes getLikes() {
        return likes;
    }

    public void setLikes(Likes likes) {
        this.likes = likes;
    }

    public Datum withLikes(Likes likes) {
        this.likes = likes;
        return this;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public Datum withImages(Images images) {
        this.images = images;
        return this;
    }

    public List<Users_in_photo> getUsers_in_photo() {
        return users_in_photo;
    }

    public void setUsers_in_photo(List<Users_in_photo> users_in_photo) {
        this.users_in_photo = users_in_photo;
    }

    public Datum withUsers_in_photo(List<Users_in_photo> users_in_photo) {
        this.users_in_photo = users_in_photo;
        return this;
    }

    public Caption getCaption() {
        return caption;
    }

    public void setCaption(Caption caption) {
        this.caption = caption;
    }

    public Datum withCaption(Caption caption) {
        this.caption = caption;
        return this;
    }

    public Boolean getUser_has_liked() {
        return user_has_liked;
    }

    public void setUser_has_liked(Boolean user_has_liked) {
        this.user_has_liked = user_has_liked;
    }

    public Datum withUser_has_liked(Boolean user_has_liked) {
        this.user_has_liked = user_has_liked;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Datum withId(String id) {
        this.id = id;
        return this;
    }

    public User_ getUser() {
        return user;
    }

    public void setUser(User_ user) {
        this.user = user;
    }

    public Datum withUser(User_ user) {
        this.user = user;
        return this;
    }

}
