package tiy.Timeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by fenji on 10/10/2016.
 */
@RestController
public class RESTController {
    @Autowired
    UserRepository users;

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public User register(@RequestBody  User newUser) throws Exception{
        User userToBeAdded = new User(newUser.username, newUser.password);
        userToBeAdded = users.save(userToBeAdded);

        return userToBeAdded;
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public User login(@RequestBody User newUser) throws Exception{
        User loginUser = users.findByUsernameAndPassword(newUser.username, newUser.password);


        return loginUser;
    }
}
