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

    @RequestMapping(path = "/contacts", method = RequestMethod.POST)
    public ArrayList<Failable> getContacts(@RequestBody UserPlaceholder senderBox){
        User sender = users.findByUsername(senderBox.username);
        Iterable<Contact> iterator = contacts.findBySenderOrderByReceiver(sender);
        ArrayList<Failable> contactList = new ArrayList<>();
        for (Contact contact: iterator){
            contactList.add(new ContactPlaceholder(contact));
        }
        if (contactList.size() == 0){
            ArrayList<Failable> error = new ArrayList<>();
            error.add(new Error("No contacts"));
            return error;
        }
        return contactList;
    }

    @RequestMapping(path = "/requestContact", method = RequestMethod.POST)
    public Failable requestContact(@RequestBody ContactPlaceholder contactBox) {
        String senderName, recieverName;
        senderName = contactBox.sender.username;
        recieverName = contactBox.sender.username;
        User sender = users.findByUsername(senderName);
        User receiver = users.findByUsername(recieverName);
        if (sender == null || receiver == null){
            return new Error("One or more Users could not be found");
        } else {
            Contact newContactRequest = new Contact(sender, receiver, false);
            contacts.save(newContactRequest);
        }
        return new Error("request sent");
    }

    @RequestMapping(path = "/confirmContact", method = RequestMethod.POST)
    public Failable confirmContact(@RequestBody ContactPlaceholder contactBox) {
        if (contactBox.accepted){
            Contact original = contacts.findOne(contactBox.id);
            original.accepted = true;
            Contact newContact = new Contact(original.receiver, original.sender, true);

            contacts.save(original);
            contacts.save(newContact);

            return new Error("Contact accepted");
        } else{
            contacts.delete(contactBox.id);
        }
        return new Error("Contact declined");
    }

    @RequestMapping(path = "/mergeTimelines", method = RequestMethod.POST)
    public ArrayList<Failable> mergeTimelines(@RequestBody ArrayList<UserPlaceholder> userBoxList){
//        ArrayList<UserPlaceholder> userBoxList = container.UserPlaceholderList;
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
