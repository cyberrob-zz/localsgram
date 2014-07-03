
package de.s3xy.retrofitsample.app.pojo;


import com.google.gson.annotations.Expose;


public class Meta {

    @Expose
    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Meta withCode(Integer code) {
        this.code = code;
        return this;
    }

}
