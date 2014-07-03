
package de.s3xy.retrofitsample.app.pojo;


import com.google.gson.annotations.Expose;


public class Images {

    @Expose
    private Low_resolution low_resolution;
    @Expose
    private Thumbnail thumbnail;
    @Expose
    private Standard_resolution standard_resolution;

    public Low_resolution getLow_resolution() {
        return low_resolution;
    }

    public void setLow_resolution(Low_resolution low_resolution) {
        this.low_resolution = low_resolution;
    }

    public Images withLow_resolution(Low_resolution low_resolution) {
        this.low_resolution = low_resolution;
        return this;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Images withThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public Standard_resolution getStandard_resolution() {
        return standard_resolution;
    }

    public void setStandard_resolution(Standard_resolution standard_resolution) {
        this.standard_resolution = standard_resolution;
    }

    public Images withStandard_resolution(Standard_resolution standard_resolution) {
        this.standard_resolution = standard_resolution;
        return this;
    }

}
