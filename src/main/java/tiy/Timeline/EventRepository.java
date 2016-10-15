package tiy.Timeline;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by fenji on 10/10/2016.
 */

public interface EventRepository extends CrudRepository<Event, Integer> {
    ArrayList<Event> findByOwner(User user);
    ArrayList<Event> findByOwnerOrderByStartDateAsc(User user);
    ArrayList<Event> findByOwnerInOrderByStartDateAsc(Collection User);
}
