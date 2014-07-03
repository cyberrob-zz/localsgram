
package de.s3xy.retrofitsample.app.pojo;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;


public class Likes {

    @Expose
    private Integer count;
    @Expose
    private List<Datum__> data = new ArrayList<Datum__>();

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Likes withCount(Integer count) {
        this.count = count;
        return this;
    }

    public List<Datum__> getData() {
        return data;
    }

    public void setData(List<Datum__> data) {
        this.data = data;
    }

    public Likes withData(List<Datum__> data) {
        this.data = data;
        return this;
    }

}
