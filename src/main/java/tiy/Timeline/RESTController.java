package tiy.Timeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Created by fenji on 10/10/2016.
 */
@RestController
public class RESTController {
    @Autowired
    UserRepository users;

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public Failable register(@RequestBody  User newUser) throws Exception{
        if(users.findByEmail(newUser.email) != null){
            return new Error("Email is already in use");
        }
        if(users.findByUsername(newUser.username) != null){
            return new Error("Username is already in use");
        }
        if(newUser.username == null && newUser.password == null){
            return new Error("Please enter a username and password");
        }
        if(newUser.username == null){
            return new Error("Please enter a username");
        }
        if(newUser.password == null){
            return new Error("Please enter a password");
        }
        if(newUser.username != null && newUser.password != null) {
            User userToBeAdded = new User(newUser.username, newUser.password);
            userToBeAdded = users.save(userToBeAdded);

            return userToBeAdded;
        }

        return new Error("Critical error");
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public Failable login(@RequestBody User newUser) throws Exception{
        if(newUser.username == null && newUser.password == null){
            return new Error("Please enter a username and password");
        }
        if(newUser.username == null){
            return new Error("Please enter a username");
        }
        if(newUser.password == null){
            return new Error("Please enter a password");
        }
        User loginUser;
        try {
            loginUser = users.findByUsernameAndPassword(newUser.username, newUser.password);
            if(loginUser != null){
                return loginUser;
            }
        } catch (Exception ex){

        }
        return new Error("Could not find user");
    }

}
