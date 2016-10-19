package tiy.Timeline;

import java.util.ArrayList;

/**
 * Created by fenji on 10/18/2016.
 */
public class ContactListStore {
    ArrayList<Contact> acceptedList;

    ArrayList<Contact> sentList;

    ArrayList<Contact> pendingList;

    public ArrayList<Contact> getAcceptedList() {
        return acceptedList;
    }

    public void setAcceptedList(ArrayList<Contact> acceptedList) {
        this.acceptedList = acceptedList;
    }

    public ArrayList<Contact> getSentList() {
        return sentList;
    }

    public void setSentList(ArrayList<Contact> sentList) {
        this.sentList = sentList;
    }

    public ArrayList<Contact> getPendingList() {
        return pendingList;
    }

    public void setPendingList(ArrayList<Contact> pendingList) {
        this.pendingList = pendingList;
    }

    public void addAccepted(Contact contact){
        acceptedList.add(contact);
    }

    public void addSent(Contact contact){
        sentList.add(contact);
    }

    public void addPending(Contact contact){
        pendingList.add(contact);
    }

    public ContactListStore() {
        acceptedList = new ArrayList<Contact>();
        sentList = new ArrayList<Contact>();
        pendingList = new ArrayList<>()
    }
}
