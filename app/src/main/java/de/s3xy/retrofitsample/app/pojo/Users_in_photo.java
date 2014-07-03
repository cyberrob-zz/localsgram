
package de.s3xy.retrofitsample.app.pojo;


import com.google.gson.annotations.Expose;


public class Users_in_photo {

    @Expose
    private Position position;
    @Expose
    private User user;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Users_in_photo withPosition(Position position) {
        this.position = position;
        return this;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Users_in_photo withUser(User user) {
        this.user = user;
        return this;
    }

}
