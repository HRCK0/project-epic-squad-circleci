package ca.uottawa.mali165.epicclinic;

public class Service{

    private String serviceName;
    private String price;
    private String category;
    private String id;

    Service(String serviceName, String price, String category, String id){
        this.serviceName=serviceName;
        this.category = category;
        this.price=price;
    }

    public String getName(){
        return serviceName;
    }
    public String getCategory(){
        return category;
    }
    public String getPrice(){
        return price;
    }

}
