package tiy.Timeline;

/**
 * Created by fenji on 10/14/2016.
 */

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface ContactRepository extends CrudRepository<Contact, Integer> {
    Contact findByReceiver(User receiver);
    Contact findBySender(User sender);
    Contact findBySenderAndReceiver(User sender, User receiver);
    ArrayList<Contact> findBySenderOrderByReceiver(User sender);
    ArrayList<Contact> findByReceiverOrderBySender(User receiver);
    ArrayList<Contact> findBySenderAndAcceptedTrueOrderByReceiverAsc(User sender);
    ArrayList<Contact> findBySenderAndAcceptedFalseOrderByReceiverAsc(User sender);
    ArrayList<Contact> findByReceiverAndAcceptedTrueOrderBySenderAsc(User receiver);
    ArrayList<Contact> findByReceiverAndAcceptedFalseOrderBySenderAsc(User receiver);
}
