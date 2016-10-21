package tiy.Timeline;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * Created by fenji on 10/11/2016.
 */
@Entity
@Table(name = "events")
public class Event implements Failable{
    @Id
    @GeneratedValue
    int id;

    @Column
    String name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column
    ZonedDateTime startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column
    ZonedDateTime endDate;

    @Column
    String timezoneCreatedIn;

    @Column
    String details;

    @Column
    boolean privacyStatus;

    @ManyToOne
    User owner;

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

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
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

    public boolean isPrivacyStatus() {
        return privacyStatus;
    }

    public void setPrivacyStatus(boolean privacyStatus) {
        this.privacyStatus = privacyStatus;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Event() {
    }

    public Event(String name, ZonedDateTime startDate, ZonedDateTime endDate, String timezoneCreatedIn, String details, boolean privacyStatus, User owner) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timezoneCreatedIn = timezoneCreatedIn;
        this.details = details;
        this.privacyStatus = privacyStatus;
        this.owner = owner;
    }
}
