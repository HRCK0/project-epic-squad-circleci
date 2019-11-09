package ca.uottawa.mali165.epicclinic;

import java.util.Comparator;

public class Service implements Comparable{

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

    public void setCategory(String category)
    {
        this.category=category;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int compareTo(Object o)
    {
        Service item;
        if(o instanceof Service)
        {
            item= (Service) o;
            if(this.category.compareTo(item.getCategory())>1)
                return 1; //this category is larger
            if(this.category.compareTo(item.getCategory())<1)
                return -1;
            else
            {
                //same category
                if(this.price.compareTo(item.getPrice())>1)
                    return 1;
                if(this.price.compareTo(item.getPrice())<1)
                    return -1;
                else
                {
                    //same price--> order by alphabetical name
                    if(this.serviceName.compareTo(item.getName())>1)
                        return 1;
                    if(this.serviceName.compareTo(item.getName())<1)
                        return -1;
                    //no need for an else statement because no completely equal services exist
                }


            }
        }
        return 0;


    }

}
