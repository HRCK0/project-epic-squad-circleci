package ca.uottawa.mali165.epicclinic;

import android.os.Parcel;
import android.os.Parcelable;

import ca.uottawa.mali165.epicclinic.Person;

public class Employee extends Person implements Parcelable {

    boolean profileCompleted;

    public Employee(String firstName, String lastName, String emailAddress, String phoneNumber, boolean profileCompleted)
    {
        super(firstName, lastName, emailAddress, phoneNumber, Person.AccountType.EMPLOYEE );
        this.profileCompleted = profileCompleted;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Employee> CREATOR
            = new Parcelable.Creator<Employee>() {
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };


    public Employee(Parcel in) {
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

}
