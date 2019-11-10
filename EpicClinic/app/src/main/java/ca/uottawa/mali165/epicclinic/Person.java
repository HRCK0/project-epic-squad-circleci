package ca.uottawa.mali165.epicclinic;

public abstract class Person {

    private String phoneNumber;
    private String firstName;
    private String lastName;
    private AccountType accountType;
    private String email;
    public enum AccountType
    {
        ADMIN,PATIENT,EMPLOYEE
    }

    public Person()
    {
        this.phoneNumber=null;
        this.firstName=null;
        this.lastName=null;
        this.accountType=null;
        this.email=null;
    }
    public Person(String firstName, String lastName, String email, String phoneNumber, AccountType accountType )
    {
        this.phoneNumber=phoneNumber;
        this.firstName=firstName;
        this.lastName=lastName;
        this.accountType=accountType;
        this.email=email;
    }

    public String getEmail() {
        return email;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmail(String email)
    {
        this.email=email;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}



