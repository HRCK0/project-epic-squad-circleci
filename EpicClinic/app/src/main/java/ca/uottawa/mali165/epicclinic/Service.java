package ca.uottawa.mali165.epicclinic;

public class Service{

    private String serviceName;
    private String price;
    private String category;

    Service(String serviceName, String price, String category){
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
