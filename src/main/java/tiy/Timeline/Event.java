package tiy.Timeline;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * Created by fenji on 10/11/2016.
 */
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue
    int id;

    @Column
    @Type(type="java.time.ZonedDateTime")
    ZonedDateTime startDate;

    @Column
    @Type(type="java.time.ZonedDateTime")
    ZonedDateTime endDate;


}
