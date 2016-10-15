package tiy.Timeline;

import org.hibernate.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by fenji on 10/14/2016.
 */
@Entity
@Table(name = "contacts")
public class Contact implements Failable {
    @Id
    @GeneratedValue
    int id;

    @ManyToOne
    User sender;

    @ManyToOne
    User receiver;

    @Column
    boolean accepted;

//    @Autowired
//    UserRepository users;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Contact() {
    }

    public Contact(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
        accepted = false;
    }

//    public Contact(UserPlaceholder senderBox, UserPlaceholder receiverBox) {
//        User sender = users.findByUsername(senderBox.username);
//        User receiver = users.findByUsername(receiverBox.username);
//        this.sender = sender;
//        this.receiver = receiver;
//        accepted = false;
//    }

    public Contact(User sender, User receiver, boolean accepted) {
        this.sender = sender;
        this.receiver = receiver;
        this.accepted = accepted;
    }
}
