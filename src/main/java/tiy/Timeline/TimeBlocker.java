package tiy.Timeline;

import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Created by fenji on 10/16/2016.
 */
public class TimeBlocker {
    public ArrayList<Failable> addTimeblocks(ArrayList<Failable> eventList) {
        ArrayList<Failable> eventPlaceholderList = new ArrayList<>();
        Event dateHolder = new Event();
        boolean lastEventAddedWasATimeblock = true;
        for (int count = 0; count < eventList.size(); count++){
            Event currentEvent = (Event)eventList.get(count);
            //add the last event
            if (count == eventList.size() - 1){
                EventPlaceholder thisEventPlaceholder = new EventPlaceholder(currentEvent);
                eventPlaceholderList.add(thisEventPlaceholder);
            } else {
                Event nextEvent = (Event)eventList.get(count + 1);
                //add the next event
                EventPlaceholder thisEventPlaceholder = new EventPlaceholder(currentEvent);
                eventPlaceholderList.add(thisEventPlaceholder);
                //if this is the first event added store its start and end date
                //if the last event was a timeblock
                if (lastEventAddedWasATimeblock) {
                    dateHolder.startDate = currentEvent.startDate;
                    dateHolder.endDate = currentEvent.endDate;
                } else if (currentEvent.endDate.isAfter(dateHolder.endDate)){
                    //store the
                    dateHolder.endDate = currentEvent.endDate;
                }
                lastEventAddedWasATimeblock = false;

                if (!((nextEvent.startDate.equals(dateHolder.startDate) || nextEvent.startDate.isAfter(dateHolder.startDate)) && (nextEvent.endDate.isBefore(dateHolder.endDate) || nextEvent.endDate.equals(dateHolder.endDate)))) {
                    if (nextEvent.startDate.isAfter(currentEvent.endDate)) {
                        Event timeblock = new Event();
                        timeblock.name = "timeblock";
                        timeblock.startDate = currentEvent.endDate;
                        timeblock.endDate = nextEvent.startDate;
                        EventPlaceholder timeblockBox = new EventPlaceholder(timeblock);
                        eventPlaceholderList.add(timeblockBox);
                        lastEventAddedWasATimeblock = true;
                    }
                }
            }
        }

        return  eventPlaceholderList;
    }
}
