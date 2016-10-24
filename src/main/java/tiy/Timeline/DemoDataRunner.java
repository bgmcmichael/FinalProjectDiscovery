package tiy.Timeline;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by fenji on 10/24/2016.
 */
public class DemoDataRunner {
    public static void main(String[] args) {
        DemoData.createDemo();
    }
}
