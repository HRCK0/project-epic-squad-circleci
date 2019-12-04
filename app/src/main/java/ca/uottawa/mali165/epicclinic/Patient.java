package ca.uottawa.mali165.epicclinic;

import ca.uottawa.mali165.epicclinic.Person;

public class Patient extends Person{

    public Patient(
            String firstName, String lastName,
            String emailAddress, String phoneNumber)
    {
        super(firstName, lastName, emailAddress, phoneNumber, Person.AccountType.PATIENT );
    }
}
