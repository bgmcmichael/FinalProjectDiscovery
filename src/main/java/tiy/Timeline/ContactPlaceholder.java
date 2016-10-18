package tiy.Timeline;

import javax.persistence.*;

/**
 * Created by fenji on 10/14/2016.
 */
public class ContactPlaceholder implements Failable {
    int id;

    UserPlaceholder sender;

    UserPlaceholder receiver;

    boolean accepted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserPlaceholder getSender() {
        return sender;
    }

    public void setSender(UserPlaceholder sender) {
        this.sender = sender;
    }

    public UserPlaceholder getReceiver() {
        return receiver;
    }

    public void setReceiver(UserPlaceholder receiver) {
        this.receiver = receiver;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public ContactPlaceholder() {
    }

    public ContactPlaceholder(int id, UserPlaceholder sender, UserPlaceholder receiver, boolean accepted) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.accepted = accepted;
    }

    public ContactPlaceholder(Contact contact){
        this.sender = new UserPlaceholder(contact.sender);
        this.receiver = new UserPlaceholder(contact.receiver);
        this.accepted = contact.accepted;
        this.id = contact.id;

    }
}
