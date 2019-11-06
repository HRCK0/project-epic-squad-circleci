package ca.uottawa.mali165.epicclinic;

public class Service{

    private int id;
    private String serviceName;
    private String price;
    private String category;

    Service(int id, String serviceName, String price, String category){
        this.serviceName=serviceName;
        this.category=category;
        this.id=id;
        this.price=price;
    }

    public int getId(){
        return id;
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
