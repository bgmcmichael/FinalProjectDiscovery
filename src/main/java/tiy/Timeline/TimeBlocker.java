package tiy.Timeline;

import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Created by fenji on 10/16/2016.
 */
public class TimeBlocker {
    public ArrayList<Failable> addTimeblocks(ArrayList<Failable> eventList) {
        ArrayList<Failable> eventPlaceholderList = new ArrayList<>();
        for (int count = 0; count < eventList.size(); count++){
            if (count == eventList.size() - 1){
                EventPlaceholder thisEventPlaceholder = new EventPlaceholder((Event)eventList.get(count));
                eventPlaceholderList.add(thisEventPlaceholder);
            } else {
                EventPlaceholder thisEventPlaceholder = new EventPlaceholder((Event)eventList.get(count));
                eventPlaceholderList.add(thisEventPlaceholder);

                ZonedDateTime event1EndDate = ((Event)eventList.get(count)).getEndDate();
                ZonedDateTime event2StartDate = ((Event)eventList.get(count + 1)).getStartDate();
                if (event2StartDate.isAfter(event1EndDate)){
                    Event timeblock = new Event();
                    timeblock.name = "timeblock";
                    timeblock.startDate = event1EndDate;
                    timeblock.endDate = event2StartDate;
                    EventPlaceholder timeblockBox = new EventPlaceholder(timeblock);
                    eventPlaceholderList.add(timeblockBox);
                }
            }
        }

        return  eventPlaceholderList;
    }
}
