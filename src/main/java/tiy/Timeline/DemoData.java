package tiy.Timeline;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by fenji on 10/24/2016.
 */
public class DemoData {
    @Autowired
    static UserRepository users;

    @Autowired
    static EventRepository events;

    @Autowired
    static ContactRepository contacts;

    public static void createDemo() {
        events.deleteAll();
        contacts.deleteAll();
        users.deleteAll();

        User John, Joan, Jude;

        John = new User("John", "password", "John@email.com");
        Joan = new User("Joan", "password", "Joan@email.com");
        Jude = new User("Jude", "password", "Jude@email.com");

        John = users.save(John);
        Joan = users.save(Joan);
        Jude = users.save(Jude);

        ZonedDateTime today = ZonedDateTime.now(ZoneOffset.UTC);
        GregorianCalendar todayDate = GregorianCalendar.from(today);
        while( todayDate.get( Calendar.DAY_OF_WEEK ) != Calendar.MONDAY ) {
            todayDate.add(Calendar.DATE, 1);
        }
        ZonedDateTime monday = todayDate.toZonedDateTime();
        monday = monday.withHour(0);
        monday = monday.withMinute(0);
        monday = monday.withSecond(0);
        monday = monday.withNano(0);

        while( todayDate.get( Calendar.DAY_OF_WEEK ) != Calendar.TUESDAY ) {
            todayDate.add(Calendar.DATE, 1);
        }
        ZonedDateTime tuesday = todayDate.toZonedDateTime();
        tuesday = tuesday.withHour(0);
        tuesday = tuesday.withMinute(0);
        tuesday = tuesday.withSecond(0);
        tuesday = tuesday.withNano(0);

        while( todayDate.get( Calendar.DAY_OF_WEEK ) != Calendar.WEDNESDAY ) {
            todayDate.add(Calendar.DATE, 1);
        }
        ZonedDateTime wednesday = todayDate.toZonedDateTime();
        wednesday = wednesday.withHour(0);
        wednesday = wednesday.withMinute(0);
        wednesday = wednesday.withSecond(0);
        wednesday = wednesday.withNano(0);

        while( todayDate.get( Calendar.DAY_OF_WEEK ) != Calendar.THURSDAY ) {
            todayDate.add(Calendar.DATE, 1);
        }
        ZonedDateTime thursday = todayDate.toZonedDateTime();
        thursday = thursday.withHour(0);
        thursday = thursday.withMinute(0);
        thursday = thursday.withSecond(0);
        thursday = thursday.withNano(0);

        while( todayDate.get( Calendar.DAY_OF_WEEK ) != Calendar.FRIDAY ) {
            todayDate.add(Calendar.DATE, 1);
        }
        ZonedDateTime friday = todayDate.toZonedDateTime();
        friday = friday.withHour(0);
        friday = friday.withMinute(0);
        friday = friday.withSecond(0);
        friday = friday.withNano(0);

        while( todayDate.get( Calendar.DAY_OF_WEEK ) != Calendar.SATURDAY ) {
            todayDate.add(Calendar.DATE, 1);
        }
        ZonedDateTime saturday = todayDate.toZonedDateTime();
        saturday = saturday.withHour(0);
        saturday = saturday.withMinute(0);
        saturday = saturday.withSecond(0);
        saturday = saturday.withNano(0);

        while( todayDate.get( Calendar.DAY_OF_WEEK ) != Calendar.SUNDAY ) {
            todayDate.add(Calendar.DATE, 1);
        }
        ZonedDateTime sunday = todayDate.toZonedDateTime();
        sunday = sunday.withHour(0);
        sunday = sunday.withMinute(0);
        sunday = sunday.withSecond(0);
        sunday = sunday.withNano(0);

        //MONDAY
        //JOHN
        Event event = new Event("work", monday.plusHours(9), monday.plusHours(8), ZoneId.systemDefault().toString(), "Casa de Waffle", false, John);
        System.out.println(event.startDate.toString() + "   +   " + event.getEndDate().toString());
        //TUESDAY
        //WEDNESDAY
        //THURSDAY
        //FRIDAY
        //SATURDAY
        //SUNDAY



//        System.out.println(monday.toString());
//        System.out.println(tuesday.toString());
//        System.out.println(wednesday.toString());
//        System.out.println(thursday.toString());
//        System.out.println(friday.toString());
//        System.out.println(saturday.toString());
//        System.out.println(sunday.toString());
        //Event JohnsWork = new Event("Work", Date)

    }
}