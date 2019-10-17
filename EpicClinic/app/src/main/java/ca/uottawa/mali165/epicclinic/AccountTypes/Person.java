package ca.uottawa.mali165.epicclinic.AccountTypes;

public abstract class Person {

    private String phoneNum;
    private String password;
    private String firstName;
    private String lastName;
    private AccountType accountType;
    private String email;
    enum AccountType
    {
        ADMIN,PATIENT,EMPLOYEE
    }
    public Person(String phoneNum,String password, String firstName, String lastName, String email, AccountType accountType )
    {
        this.phoneNum=phoneNum;
        this.password=password;
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

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNum;
    }

    public void setEmail(String newEmail)
    {
        this.email=newEmail;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String userName) {
        this.phoneNum = userName;
    }
}



