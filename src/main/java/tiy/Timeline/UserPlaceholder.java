package tiy.Timeline;

import javax.persistence.*;

/**
 * Created by fenji on 10/10/2016.
 */
public class UserPlaceholder implements Failable{
    int id;

    String username;

    String password;

    String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserPlaceholder() {
    }

    public UserPlaceholder(String username) {
        this.username = username;
    }

    public UserPlaceholder(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserPlaceholder(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserPlaceholder(User user){
        this.username = user.username;
        this.password = user.password;
        this.email = user.email;
        this.id = user.id;
    }
}
