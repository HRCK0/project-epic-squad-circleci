package ca.uottawa.mali165.epicclinic;

import ca.uottawa.mali165.epicclinic.Person;

public class Employee extends Person {

    public Employee(
            String firstName, String lastName,
            String emailAddress, String phoneNumber)
    {
        super(firstName, lastName, emailAddress, phoneNumber, Person.AccountType.EMPLOYEE );
    }
}
