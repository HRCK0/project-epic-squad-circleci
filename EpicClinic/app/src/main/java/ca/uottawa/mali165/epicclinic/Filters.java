package ca.uottawa.mali165.epicclinic;

import java.util.HashMap;
import java.util.Map;


public class Filters {
    private boolean filterByAvailability;
    private Map<String, String> availabilityData = new HashMap<>();
    private Float rating;
    private String searchQuery = "";

    public String getDate(){
        return availabilityData.get("date");
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
    public boolean getFilterByAavailability(){return filterByAvailability;}
    public String getSearchQuery(){return searchQuery;}

    public void setDate(String date){ //in mm/dd/yy
        availabilityData.put("date", date);
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

    public void setSearchQuery(String newQuery){
        searchQuery=newQuery;
    }

    @Override
    public String toString(){
        return ("FilterByAvailability: " + filterByAvailability + ", Date: " + availabilityData.get("date")+ ", FromTime: " + availabilityData.get("from")+
                ", ToTime: " + availabilityData.get("to") + ", MinRating: " + rating + ", SearchQuery: " + searchQuery);
    }

}
