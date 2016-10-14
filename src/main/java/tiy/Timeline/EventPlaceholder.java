package tiy.Timeline;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * Created by fenji on 10/11/2016.
 */

public class EventPlaceholder implements Failable{
    int id;

    String name;

    String startDate;

    String endDate;

    String timezoneCreatedIn;

    String details;

    UserPlaceholder owner;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTimezoneCreatedIn() {
        return timezoneCreatedIn;
    }

    public void setTimezoneCreatedIn(String timezoneCreatedIn) {
        this.timezoneCreatedIn = timezoneCreatedIn;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public UserPlaceholder getOwner() {
        return owner;
    }

    public void setOwner(UserPlaceholder owner) {
        this.owner = owner;
    }

    public EventPlaceholder() {
    }

    public EventPlaceholder(String name, String startDate, String endDate, String timezoneCreatedIn, String details, UserPlaceholder owner) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timezoneCreatedIn = timezoneCreatedIn;
        this.details = details;
        this.owner = owner;
    }
}
