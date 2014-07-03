
package de.s3xy.retrofitsample.app.pojo;


import com.google.gson.annotations.Expose;


public class Datum_ {

    @Expose
    private String created_time;
    @Expose
    private String text;
    @Expose
    private From from;
    @Expose
    private String id;

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public Datum_ withCreated_time(String created_time) {
        this.created_time = created_time;
        return this;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Datum_ withText(String text) {
        this.text = text;
        return this;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public Datum_ withFrom(From from) {
        this.from = from;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Datum_ withId(String id) {
        this.id = id;
        return this;
    }

}
