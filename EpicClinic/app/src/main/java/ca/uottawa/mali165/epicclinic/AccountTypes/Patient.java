package ca.uottawa.mali165.epicclinic.AccountTypes;

public class Patient extends Person{

    public Patient(
        String password, String firstname, String lastname, 
        String emailAdress, String phoneNumber)
    {
        super(phoneNumber,password, firstname, lastname, emailAdress, AccountType.PATIENT );
    }
}
