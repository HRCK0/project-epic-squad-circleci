package ca.uottawa.mali165.epicclinic;

public class Rating {

    private String raterName;
    private String rating;
    private String comment;

    Rating(String raterName, String rating, String comment){
        this.raterName = raterName;
        this.rating = rating;
        this.comment = comment;
    }

    public String getRaterName(){
        return this.raterName;
    }

    public String getRating() {
        return this.rating;
    }

    public String getComment() {
        return this.comment;
    }
}
