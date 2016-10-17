package tiy.Timeline;

import jdk.nashorn.internal.parser.DateParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by fenji on 10/10/2016.
 */
@RestController
public class RESTController {
    @Autowired
    UserRepository users;

    @Autowired
    EventRepository events;

    @Autowired
    ContactRepository contacts;

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public Failable register(@RequestBody UserPlaceholder newUser) throws Exception{
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
    public Failable login(@RequestBody UserPlaceholder newUser) throws Exception{
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
        return new Error("Invalid credentials, please try again");
    }

    @RequestMapping(path = "/addEvent", method = RequestMethod.POST)
    public Failable addEvent(@RequestBody EventPlaceholder newEventPlaceholder) throws Exception {
        System.out.println(newEventPlaceholder.timezoneCreatedIn);
        Event newEvent = new Event();
        newEventPlaceholder.startDate = newEventPlaceholder.startDate + "[GMT]";
        System.out.println("startDate = " + newEventPlaceholder.startDate);
        newEventPlaceholder.endDate = newEventPlaceholder.endDate + "[GMT]";
        System.out.println("endDate = " + newEventPlaceholder.endDate);
        ZonedDateTime startDateZoned = ZonedDateTime.parse(newEventPlaceholder.startDate, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        System.out.println("startDateZoned = " + startDateZoned.toString());
        ZonedDateTime endDateZoned = ZonedDateTime.parse(newEventPlaceholder.endDate, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        System.out.println("endDateZoned = " + endDateZoned.toString());

        User tempUser = users.findByUsername(newEventPlaceholder.owner.username);
        System.out.println("tempUser username = " + tempUser.username);

        newEvent.startDate = startDateZoned;
        newEvent.endDate = endDateZoned;
        newEvent.details = newEventPlaceholder.details;
        newEvent.name = newEventPlaceholder.name;
        newEvent.owner = tempUser;
        newEvent.timezoneCreatedIn = newEventPlaceholder.timezoneCreatedIn;
        System.out.println("timezonecreatedin = " + newEvent.getTimezoneCreatedIn());
        System.out.println("saving new event");
        newEvent = events.save(newEvent);
        System.out.println("newEvent exists in database with name = " + newEvent.name);
        System.out.println("timezonecreatedin = " + newEvent.timezoneCreatedIn);

        newEventPlaceholder = new EventPlaceholder(newEvent);
        System.out.println("newEventPlaceholder name now = " + newEventPlaceholder.name);
        System.out.println("newEventPlaceholder timezoneCreatedIn now = " + newEventPlaceholder.timezoneCreatedIn);
        System.out.println(newEventPlaceholder.startDate);
        return newEventPlaceholder;
    }

    @RequestMapping(path = "/events", method = RequestMethod.POST)
    public ArrayList<Failable> getEvents(@RequestBody UserPlaceholder userBox) throws Exception {
        User eventOwner = users.findByUsername(userBox.username);
        Collection<Event> tempList = events.findByOwnerOrderByStartDateAsc(eventOwner);
        ArrayList<Failable> eventList = new ArrayList<Failable>(tempList);

        TimeBlocker timeBlocker = new TimeBlocker();
        return timeBlocker.addTimeblocks(eventList);
    }

    @RequestMapping(path = "/deleteContact", method = RequestMethod.POST)
    public Failable deleteContact(@RequestBody UserPlaceholder receiverBox) {
        User receiver = users.findByUsername(receiverBox.username);
        Contact contact1 = contacts.findByReceiver(receiver);
        Contact contact2 = contacts.findByReceiver(contact1.sender);
        contacts.delete(contact1);
        contacts.delete(contact2);

        return new Error("Contact deleted");
    }

    @RequestMapping(path = "/addContact", method = RequestMethod.POST)
    public Failable addContact(@RequestBody UserPlaceholder userBox) {
        User receiver = users.findByUsername(userBox.username);
        Contact contact1 = contacts.findByReceiver(receiver);
        User sender = contact1.sender;
        Contact contact2 = new Contact(receiver, sender, true);

        contact1.accepted = true;
        contacts.save(contact1);
        contacts.save(contact2);

        return new Error("Contact added");
    }

    @RequestMapping(path = "/mergeTimelines", method = RequestMethod.POST)
    public ArrayList<Failable> mergeTimelines(@RequestBody UserPlaceholderContainer container){
        ArrayList<UserPlaceholder> userBoxList = container.UserPlaceholderList;
        ArrayList<String> usernameList = new ArrayList<>();
        for(UserPlaceholder userPlaceholder : userBoxList){
            usernameList.add(userPlaceholder.username);
        }
        ArrayList<User> userList = users.findByUsernameIn(usernameList);
        Collection<Event> tempEventList = events.findByOwnerInOrderByStartDateAsc(userList);
        ArrayList<Failable> eventList = new ArrayList<Failable>(tempEventList);
        TimeBlocker timeBlocker = new TimeBlocker();

        return timeBlocker.addTimeblocks(eventList);
    }

//    @RequestMapping(path = "/contacts", method = RequestMethod.POST)

}
