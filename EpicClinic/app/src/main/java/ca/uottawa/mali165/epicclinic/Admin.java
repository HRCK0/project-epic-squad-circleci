package ca.uottawa.mali165.epicclinic;

public class Admin extends Person {

    public Admin(String firstName, String lastName, String email, String phoneNumber) {
        super(firstName, lastName, email, phoneNumber, Person.AccountType.ADMIN);
    }
}
