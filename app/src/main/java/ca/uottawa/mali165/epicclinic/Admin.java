package ca.uottawa.mali165.epicclinic;

import java.util.ArrayList;
import android.os.Parcelable;
import android.os.Parcel;

public class Admin  extends Person implements Parcelable{

    //Parcelable allows the object to be passed through each activity
    ArrayList<Service> servicesList= new ArrayList<Service> (100);
    public Admin(String firstName, String lastName, String email, String phoneNumber) {
        super(firstName, lastName, email, phoneNumber, Person.AccountType.ADMIN);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Admin> CREATOR
            = new Parcelable.Creator<Admin>() {
        public Admin createFromParcel(Parcel in) {
            return new Admin(in);
        }

        public Admin[] newArray(int size) {
            return new Admin[size];
        }
    };

    public Admin(Parcel in) {
        super(in.readString(),in.readString(),in.readString(),in.readString(),AccountType.valueOf(in.readString()));

        /*this.setFirstName(in.readString());
        this.setLastName(in.readString());
        this.setEmail(in.readString());
        this.setPhoneNumber(in.readString());
        this.setAccountType(AccountType.valueOf(in.readString()));*/

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getFirstName());
        dest.writeString(getLastName());
        dest.writeString(getEmail());
        dest.writeString(getPhoneNumber());
        dest.writeString(this.getAccountType().name());


    }


    public void addService(Service service)
    {
        if(servicesList.indexOf(service)==-1) //when the program is terminated, the obejct is killed so when we have to reload pre-existing services, makes sure only one of each service exists
            servicesList.add(service);

    }

    public void editService(Service oldService, Service newService)
    {

        int x = servicesList.indexOf(oldService);
        servicesList.remove(oldService);
        servicesList.add(x,newService);


    }
    public void deleteService(Service service)
    {

        servicesList.remove(service);

    }


}
