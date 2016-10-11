package tiy.Timeline;

import javax.persistence.*;

/**
 * Created by fenji on 10/11/2016.
 */
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue
    int id;
}
