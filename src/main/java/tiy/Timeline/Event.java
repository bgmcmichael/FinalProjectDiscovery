package tiy.Timeline;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.OffsetDateTime;
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
    OffsetDateTime startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column
    OffsetDateTime endDate;

    @Column
    String timezoneCreatedIn;

    @Column
    String details;

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

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(OffsetDateTime startDate) {
        this.startDate = startDate;
    }

    public OffsetDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(OffsetDateTime endDate) {
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Event() {
    }

    public Event(String name, OffsetDateTime startDate, OffsetDateTime endDate, String timezoneCreatedIn, String details, User owner) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timezoneCreatedIn = timezoneCreatedIn;
        this.details = details;
        this.owner = owner;
    }
}
