
package de.s3xy.retrofitsample.app.pojo;


import com.google.gson.annotations.Expose;


public class Caption {

    @Expose
    private String created_time;
    @Expose
    private String text;
    @Expose
    private From_ from;
    @Expose
    private String id;

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public Caption withCreated_time(String created_time) {
        this.created_time = created_time;
        return this;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Caption withText(String text) {
        this.text = text;
        return this;
    }

    public From_ getFrom() {
        return from;
    }

    public void setFrom(From_ from) {
        this.from = from;
    }

    public Caption withFrom(From_ from) {
        this.from = from;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Caption withId(String id) {
        this.id = id;
        return this;
    }

}
