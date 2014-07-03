
package de.s3xy.retrofitsample.app.pojo;


import com.google.gson.annotations.Expose;


public class Position {

    @Expose
    private Double y;
    @Expose
    private Double x;

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Position withY(Double y) {
        this.y = y;
        return this;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Position withX(Double x) {
        this.x = x;
        return this;
    }

}
