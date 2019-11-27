package ca.uottawa.mali165.epicclinic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.dom.DOMLocator;

public class Filters {
    private boolean filterByAvailability;
    private Map<String, String> availabilityData = new HashMap<>();
    private Float rating;
    private List days = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

    public String getDay(){
        return availabilityData.get("day");
    }
    public String getFromTime(){
        return availabilityData.get("from");
    }
    public String getToTime(){
        return availabilityData.get("to");
    }
        public Float getMinRating(){
        return rating;
    }

    public void setDay(String day) throws IllegalArgumentException{
        if(day==null || !days.contains(day)){throw new IllegalArgumentException();}
        availabilityData.put("day", day);
    }

    public void setFromTime(String fromTime){
        availabilityData.put("from", fromTime);
    }

    public void setToTime(String toTime){
        availabilityData.put("to", toTime);
    }

    public void setRating(Float rating){
        this.rating=rating;
    }

    public void setApplyAvailabilityFilter(boolean apply){
        filterByAvailability=apply;
    }

}
