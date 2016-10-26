package tiy.Timeline;

import javax.persistence.*;

/**
 * Created by fenji on 10/14/2016.
 */
@Entity
@Table(name = "eventusers")
public class EventUser implements Failable {
    @Id
    @GeneratedValue
    int id;

    @ManyToOne
    User attendee;

    @ManyToOne
    Event event;

    @Column
    boolean accepted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getAttendee() {
        return attendee;
    }

    public void setAttendee(User attendee) {
        this.attendee = attendee;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
