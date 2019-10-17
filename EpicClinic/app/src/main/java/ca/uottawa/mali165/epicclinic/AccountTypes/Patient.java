import java.util.EnumMap;

public class Patient extends Person{

    public Patient(
        String password, String firstname, String lastname, 
        String emailAdress, String phoneNumber)
    {
        super(password, firstname, lastname, emailAdress, "PATIENT", phoneNumber);
    }
}
