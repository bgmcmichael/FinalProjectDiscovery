package tiy.Timeline;

import jdk.nashorn.internal.parser.DateParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    public Failable register(@RequestBody UserPlaceholder newUser) throws Exception {
        if (users.findByEmail(newUser.email) != null) {
            return new Error("Email is already in use");
        }
        if (users.findByUsername(newUser.username) != null) {
            return new Error("Username is already in use");
        }
        if (newUser.username == null && newUser.password == null) {
            return new Error("Please enter a username and password");
        }
        if (newUser.username == null) {
            return new Error("Please enter a username");
        }
        if (newUser.password == null) {
            return new Error("Please enter a password");
        }
        if (newUser.username != null && newUser.password != null) {
            User userToBeAdded = new User(newUser.username, newUser.password);
            userToBeAdded = users.save(userToBeAdded);

            return userToBeAdded;
        }

        return new Error("Critical error");
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public Failable login(@RequestBody UserPlaceholder newUser) throws Exception {
        if (newUser.username == null && newUser.password == null) {
            return new Error("Please enter a username and password");
        }
        if (newUser.username == null) {
            return new Error("Please enter a username");
        }
        if (newUser.password == null) {
            return new Error("Please enter a password");
        }
        User loginUser;
        try {
            loginUser = users.findByUsernameAndPassword(newUser.username, newUser.password);
            if (loginUser != null) {
                return loginUser;
            }
        } catch (Exception ex) {

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
        newEvent.privacyStatus = newEventPlaceholder.privacyStatus;
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

    @RequestMapping(path = "/editEvent", method = RequestMethod.POST)
    public Failable editEvent(@RequestBody EventPlaceholder newEventPlaceholder) throws Exception {
        Event event = null;
        event = events.findOne(newEventPlaceholder.id);
        if (event == null) {
            return new Error("Event does not exist to be edited");
        }
        System.out.println(newEventPlaceholder.timezoneCreatedIn);
        newEventPlaceholder.startDate = newEventPlaceholder.startDate + "[GMT]";
        newEventPlaceholder.endDate = newEventPlaceholder.endDate + "[GMT]";
        ZonedDateTime startDateZoned = ZonedDateTime.parse(newEventPlaceholder.startDate, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        ZonedDateTime endDateZoned = ZonedDateTime.parse(newEventPlaceholder.endDate, DateTimeFormatter.ISO_ZONED_DATE_TIME);

        User tempUser = users.findByUsername(newEventPlaceholder.owner.username);

        event.startDate = startDateZoned;
        event.endDate = endDateZoned;
        event.details = newEventPlaceholder.details;
        event.name = newEventPlaceholder.name;
        event.owner = tempUser;
        event.timezoneCreatedIn = newEventPlaceholder.timezoneCreatedIn;
        event.privacyStatus = newEventPlaceholder.privacyStatus;
        event = events.save(event);

        newEventPlaceholder = new EventPlaceholder(event);

        return newEventPlaceholder;
    }

    @RequestMapping(path = "/deleteEvent", method = RequestMethod.POST)
    public Failable deleteEvent(@RequestBody EventPlaceholder newEventPlaceholder) throws Exception {
        events.delete(newEventPlaceholder.id);

        return new Error("event removed");
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
    public ArrayList<Failable> getContacts(@RequestBody UserPlaceholder senderBox) {
        User sender = users.findByUsername(senderBox.username);
        Iterable<Contact> iterator = contacts.findBySenderAndAcceptedTrueOrderByReceiverAsc(sender);
        ArrayList<Failable> contactList = new ArrayList<>();
        for (Contact contact : iterator) {
            contactList.add(new ContactPlaceholder(contact));
        }
        return contactList;
    }

    @RequestMapping(path = "/pendingContacts", method = RequestMethod.POST)
    public ArrayList<Failable> getPendingContacts(@RequestBody UserPlaceholder senderBox) {
        User sender = users.findByUsername(senderBox.username);
        Iterable<Contact> iterator = contacts.findBySenderAndAcceptedFalseOrderByReceiverAsc(sender);
        ArrayList<Failable> contactList = new ArrayList<>();
        for (Contact contact : iterator) {
            contactList.add(new ContactPlaceholder(contact));
        }
        return contactList;
    }

    @RequestMapping(path = "/contactRequests", method = RequestMethod.POST)
    public ArrayList<Failable> getContactRequests(@RequestBody UserPlaceholder receiverBox) {
        User receiver = users.findByUsername(receiverBox.username);
        Iterable<Contact> iterator = contacts.findByReceiverAndAcceptedFalseOrderBySenderAsc(receiver);
        ArrayList<Failable> contactList = new ArrayList<>();
        for (Contact contact : iterator) {
            contactList.add(new ContactPlaceholder(contact));
        }
        return contactList;
    }

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public ContactPlaceholder doTheThing() {
        UserPlaceholder user1 = new UserPlaceholder("john", "doe", "john@doe.email");
        UserPlaceholder user2 = new UserPlaceholder("jane", "doe", "jane@doe.email");

        ContactPlaceholder testContact1 = new ContactPlaceholder(30, user1, user2, false);

        return testContact1;
    }

    @RequestMapping(path = "/requestContact", method = RequestMethod.POST)
    public Failable requestContact(@RequestBody ContactPlaceholder contactBox) {
        String senderName, recieverName;
        senderName = contactBox.sender.username;
        recieverName = contactBox.receiver.username;
        User sender = users.findByUsername(senderName);
        User receiver = users.findByUsername(recieverName);
        if (sender == null || receiver == null) {
            return new Error("One or more Users could not be found");
        } else if (contacts.findBySenderAndReceiver(sender, receiver) == null) {
            Contact newContactRequest = new Contact(sender, receiver, false);
            contacts.save(newContactRequest);
            return new Error("request sent");
        }
        return new Error("Request has already been sent");
    }

    @RequestMapping(path = "/confirmContact", method = RequestMethod.POST)
    public Failable confirmContact(@RequestBody ContactPlaceholder contactBox) {
        Contact contact = contacts.findOne(contactBox.id);
        User sender = contact.sender;
        User receiver = contact.receiver;
        Contact doesContactExist = null;
        try {
            doesContactExist = contacts.findBySenderAndReceiver(receiver, sender);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (contactBox.accepted) {
            Contact original = contacts.findOne(contactBox.id);
            original.accepted = true;
            if (doesContactExist != null) {
                doesContactExist.accepted = true;
                contacts.save(original);
                contacts.save(doesContactExist);
            } else {
                Contact newContact = new Contact(original.receiver, original.sender, true);

                contacts.save(original);
                contacts.save(newContact);
            }

            return new Error("Contact accepted");
        } else {
            contacts.delete(contactBox.id);
        }
        return new Error("Contact declined");
    }

    @RequestMapping(path = "/mergeTimelines", method = RequestMethod.POST)
    public ArrayList<Failable> mergeTimelines(@RequestBody ArrayList<UserPlaceholder> userBoxList) {
//        ArrayList<UserPlaceholder> userBoxList = container.UserPlaceholderList;
        ArrayList<String> usernameList = new ArrayList<>();
        for (UserPlaceholder userPlaceholder : userBoxList) {
            usernameList.add(userPlaceholder.username);
        }
        ArrayList<User> userList = users.findByUsernameIn(usernameList);
        Collection<Event> tempEventList = events.findByOwnerInOrderByStartDateAsc(userList);
        ArrayList<Failable> eventList = new ArrayList<Failable>(tempEventList);
        TimeBlocker timeBlocker = new TimeBlocker();

        return timeBlocker.addTimeblocks(eventList);
    }

    @RequestMapping(path = "/demo", method = RequestMethod.POST)
    public void createDemo() {
        events.deleteAll();
        contacts.deleteAll();
        users.deleteAll();

        User Sampson, Danny, Ben;

        Sampson = new User("sampson", "password", "sampson@email.com");
        Danny = new User("danny", "password", "danny@email.com");
        Ben = new User("ben", "password", "ben@email.com");

        users.save(Sampson);
        users.save(Danny);
        users.save(Ben);

        ZonedDateTime today = ZonedDateTime.now(ZoneOffset.UTC);
        GregorianCalendar todayDate = GregorianCalendar.from(today);
        while (todayDate.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            todayDate.add(Calendar.DATE, 1);
        }
        ZonedDateTime monday = todayDate.toZonedDateTime();
        monday = monday.withHour(0);
        monday = monday.withMinute(0);
        monday = monday.withSecond(0);
        monday = monday.withNano(0);

        while (todayDate.get(Calendar.DAY_OF_WEEK) != Calendar.TUESDAY) {
            todayDate.add(Calendar.DATE, 1);
        }
        ZonedDateTime tuesday = todayDate.toZonedDateTime();
        tuesday = tuesday.withHour(0);
        tuesday = tuesday.withMinute(0);
        tuesday = tuesday.withSecond(0);
        tuesday = tuesday.withNano(0);

        while (todayDate.get(Calendar.DAY_OF_WEEK) != Calendar.WEDNESDAY) {
            todayDate.add(Calendar.DATE, 1);
        }
        ZonedDateTime wednesday = todayDate.toZonedDateTime();
        wednesday = wednesday.withHour(0);
        wednesday = wednesday.withMinute(0);
        wednesday = wednesday.withSecond(0);
        wednesday = wednesday.withNano(0);

        while (todayDate.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY) {
            todayDate.add(Calendar.DATE, 1);
        }
        ZonedDateTime thursday = todayDate.toZonedDateTime();
        thursday = thursday.withHour(0);
        thursday = thursday.withMinute(0);
        thursday = thursday.withSecond(0);
        thursday = thursday.withNano(0);

        while (todayDate.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
            todayDate.add(Calendar.DATE, 1);
        }
        ZonedDateTime friday = todayDate.toZonedDateTime();
        friday = friday.withHour(0);
        friday = friday.withMinute(0);
        friday = friday.withSecond(0);
        friday = friday.withNano(0);

        while (todayDate.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            todayDate.add(Calendar.DATE, 1);
        }
        ZonedDateTime saturday = todayDate.toZonedDateTime();
        saturday = saturday.withHour(0);
        saturday = saturday.withMinute(0);
        saturday = saturday.withSecond(0);
        saturday = saturday.withNano(0);

        while (todayDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            todayDate.add(Calendar.DATE, 1);
        }
        ZonedDateTime sunday = todayDate.toZonedDateTime();
        sunday = sunday.withHour(0);
        sunday = sunday.withMinute(0);
        sunday = sunday.withSecond(0);
        sunday = sunday.withNano(0);
        ZonedDateTime tempdate;
        //MONDAY
        //John
        Event event = new Event("work", monday.plusHours(9), monday.plusHours(17), ZoneId.systemDefault().toString(), "Casa de Waffle", false, Sampson);
        events.save(event);
        tempdate = monday.plusHours(17);
        tempdate = tempdate.plusMinutes(30);
        event = new Event("Traveling to Vet", monday.plusHours(17), tempdate, ZoneId.systemDefault().toString(), "spike's surgery is today, can't miss this guys", false, Sampson);
        events.save(event);
        event = new Event("Vet visit", tempdate, monday.plusHours(19), ZoneId.systemDefault().toString(), "spike's surgery is today, can't miss this guys", false, Sampson);
        events.save(event);
        //Joan
        event = new Event("work", monday.plusHours(23), monday.plusHours(31), ZoneId.systemDefault().toString(), "On Call at tech support", false, Danny);
        events.save(event);
        //Jude
        event = new Event("Consulting: Fiserv", monday.plusHours(14), monday.plusHours(22), ZoneId.systemDefault().toString(), "Due diligence meeting today", false, Ben);
        events.save(event);

        //TUESDAY
        //John
        event = new Event("work", tuesday.plusHours(9), tuesday.plusHours(25), ZoneId.systemDefault().toString(), "got scheduled for a double shift", false, Sampson);
        events.save(event);
        //Joan
        event = new Event("work", tuesday.plusHours(23), tuesday.plusHours(31), ZoneId.systemDefault().toString(), "On Call at tech support", false, Danny);
        events.save(event);
        //Jude
        event = new Event("Consulting: Fiserv", tuesday.plusHours(14), tuesday.plusHours(22), ZoneId.systemDefault().toString(), "", false, Ben);
        events.save(event);

        //WEDNESDAY
        //John
        event = new Event("grocery shopping", wednesday.plusHours(7), wednesday.plusHours(9), ZoneId.systemDefault().toString(), "eggs, 1 gallon milk, 2 creamers, 2 ribeye's, bottle of wine (I think she said she liked 13 hands chard?).", false, Sampson);
        events.save(event);
        event = new Event("work", wednesday.plusHours(9), wednesday.plusHours(17), ZoneId.systemDefault().toString(), "Casa de Waffle", false, Sampson);
        events.save(event);
        event = new Event("choir practice", wednesday.plusHours(18), wednesday.plusHours(21), ZoneId.systemDefault().toString(), "I added an hour for the drive home guys", false, Sampson);
        events.save(event);
        //Joan
        event = new Event("work", wednesday.plusHours(23), wednesday.plusHours(31), ZoneId.systemDefault().toString(), "On Call at tech support", false, Danny);
        events.save(event);
        //Jude

        //THURSDAY
        //John
        event = new Event("work", thursday.plusHours(9), thursday.plusHours(17), ZoneId.systemDefault().toString(), "Casa de Waffle", false, Sampson);
        events.save(event);
        event = new Event("datenight", thursday.plusHours(17), thursday.plusHours(21), ZoneId.systemDefault().toString(), "don't forget the steak recipe!", true, Sampson);
        events.save(event);
        //Joan
        event = new Event("work", thursday.plusHours(23), thursday.plusHours(31), ZoneId.systemDefault().toString(), "On Call at tech support", false, Danny);
        events.save(event);
        //Jude

        //FRIDAY
        //John
        event = new Event("work", friday.plusHours(9), friday.plusHours(17), ZoneId.systemDefault().toString(), "Casa de Waffle", false, Sampson);
        events.save(event);
        //Joan
        event = new Event("work", friday.plusHours(23), friday.plusHours(31), ZoneId.systemDefault().toString(), "On Call at tech support", false, Danny);
        events.save(event);
        //Jude

        //SATURDAY
        //John
        //Joan
        //Jude

        //SUNDAY
        //John
        event = new Event("church", sunday.plusHours(8), sunday.plusHours(11), ZoneId.systemDefault().toString(), "Singing in the choir if you guys want to come see", false, Sampson);
        events.save(event);
        //Joan
        //Jude

        Contact joanAsksJohn = new Contact(Danny, Sampson, false);
        Contact joanAsksJude = new Contact(Danny, Ben, true);
        Contact judeAsksJoan = new Contact(Ben, Danny, true);
        contacts.save(joanAsksJude);
        contacts.save(judeAsksJoan);
        contacts.save(joanAsksJohn);

//        System.out.println(monday.toString());
//        System.out.println(tuesday.toString());
//        System.out.println(wednesday.toString());
//        System.out.println(thursday.toString());
//        System.out.println(friday.toString());
//        System.out.println(saturday.toString());
//        System.out.println(sunday.toString());
        //Event JohnsWork = new Event("Work", Date)


//    @RequestMapping(path = "/contacts", method = RequestMethod.POST)

    }
}
