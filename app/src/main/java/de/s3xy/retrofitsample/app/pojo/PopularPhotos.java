
package de.s3xy.retrofitsample.app.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;


public class PopularPhotos implements Serializable{

    @Expose
    private Meta meta;
    @Expose
    private List<Datum> data = new ArrayList<Datum>();

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public PopularPhotos withMeta(Meta meta) {
        this.meta = meta;
        return this;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public PopularPhotos withData(List<Datum> data) {
        this.data = data;
        return this;
    }

}
