
package de.s3xy.retrofitsample.app.pojo;


import com.google.gson.annotations.Expose;


public class Low_resolution {

    @Expose
    private String url;
    @Expose
    private Integer width;
    @Expose
    private Integer height;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Low_resolution withUrl(String url) {
        this.url = url;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Low_resolution withWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Low_resolution withHeight(Integer height) {
        this.height = height;
        return this;
    }

}
