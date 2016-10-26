package tiy.Timeline;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by fenji on 10/10/2016.
 */

public interface EventUserRepository extends CrudRepository<EventUser, Integer> {
    ArrayList<EventUser> findByEventOrderByAttendee(Event event);

}
