package ca.uottawa.mali165.epicclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AvailabilityActivity extends AppCompatActivity {

    FirebaseFirestore db;
    private static final String TAG = "AvailabilityActivity";
    String empId;

    EditText mondayFromTime;
    EditText mondayToTime;

    EditText tuesdayFromTime;
    EditText tuesdayToTime;

    EditText wednesdayFromTime;
    EditText wednesdayToTime;

    EditText thursdayFromTime;
    EditText thursdayToTime;

    EditText fridayFromTime;
    EditText fridayToTime;

    EditText saturdayFromTime;
    EditText saturdayToTime;

    EditText sundayFromTime;
    EditText sundayToTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_availability);

        Button updateButton = findViewById(R.id.updateAvailabilityBtn);

        mondayFromTime = findViewById(R.id.mondayFromTime);
        mondayToTime = findViewById(R.id.mondayToTime);

        tuesdayFromTime = findViewById(R.id.tuesdayFromTime);
        tuesdayToTime = findViewById(R.id.tuesdayToTime);

        wednesdayFromTime = findViewById(R.id.wednesdayFromTime);
        wednesdayToTime = findViewById(R.id.wednesdayToTime);

        thursdayFromTime = findViewById(R.id.thursdayFromTime);
        thursdayToTime = findViewById(R.id.thursdayToTime);

        fridayFromTime = findViewById(R.id.fridayFromTime);
        fridayToTime = findViewById(R.id.fridayToTime);

        saturdayFromTime = findViewById(R.id.saturdayFromTime);
        saturdayToTime = findViewById(R.id.saturdayToTime);

        sundayFromTime = findViewById(R.id.sundayFromTime);
        sundayToTime = findViewById(R.id.sundayToTime);

        db = FirebaseFirestore.getInstance();

        empId = getIntent().getStringExtra("CurrentUser_UID");

        System.out.println("HELLOOOOOOOOOOOOOOOO" + empId);

        db.collection("users")
                .document(empId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map empData = documentSnapshot.getData();

                        if (empData.containsKey("availability")) {
                            Map availabilityForEmployee = (Map) empData.get("availability");
                            updateUI(availabilityForEmployee);
                        }
                    }
                });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mondayFrom = mondayFromTime.getText().toString();
                String mondayTo = mondayToTime.getText().toString();
                String tuesdayFrom = tuesdayFromTime.getText().toString();
                String tuesdayTo = tuesdayToTime.getText().toString();
                String wednesdayFrom = wednesdayFromTime.getText().toString();
                String wednesdayTo = wednesdayToTime.getText().toString();
                String thursdayFrom = thursdayFromTime.getText().toString();
                String thursdayTo = thursdayToTime.getText().toString();
                String fridayFrom = fridayFromTime.getText().toString();
                String fridayTo = fridayToTime.getText().toString();
                String saturdayFrom = saturdayFromTime.getText().toString();
                String saturdayTo = saturdayToTime.getText().toString();
                String sundayFrom = sundayFromTime.getText().toString();
                String sundayTo = sundayToTime.getText().toString();

                boolean valid = checkTimeValidities(mondayFrom, mondayTo, tuesdayFrom, tuesdayTo, wednesdayFrom, wednesdayTo, thursdayFrom, thursdayTo, fridayFrom, fridayTo, saturdayFrom, saturdayTo, sundayFrom, sundayTo);

                if (valid) {
                    updateAvailability(mondayFrom, mondayTo, tuesdayFrom, tuesdayTo, wednesdayFrom, wednesdayTo, thursdayFrom, thursdayTo, fridayFrom, fridayTo, saturdayFrom, saturdayTo, sundayFrom, sundayTo);
                }


            }
        });

    }

    public boolean checkTimeValidities(String mondayFrom, String mondayTo, String tuesdayFrom, String tuesdayTo, String wednesdayFrom,
                                   String wednesdayTo, String thursdayFrom, String thursdayTo, String fridayFrom, String fridayTo,
                                   String saturdayFrom, String saturdayTo, String sundayFrom, String sundayTo) {

        // check all time inputs
        // ctrl-c + ctrl-v at its finest
        // sorry to whoever has to read this :/

        String timeRegex = "^([01][0-9]|2[0-3]):[0-5][0-9]$";


        if (!mondayFrom.matches(timeRegex) && !mondayFrom.isEmpty()){
            mondayFromTime.setError("Invalid Time Format. Please enter as HH:MM (24 hour time) or leave both start and end empty for this day.");
            mondayFromTime.requestFocus();
            return false;
        } else if (!mondayTo.matches(timeRegex) && !mondayFrom.isEmpty() && !mondayTo.isEmpty()) {
            mondayToTime.setError("Invalid Time Format. Please enter as HH:MM (24 hour time) or leave both start and end empty for this day.");
            mondayToTime.requestFocus();
            return false;
        } else if (!tuesdayFrom.matches(timeRegex) && !tuesdayFrom.isEmpty()) {
            tuesdayFromTime.setError("Invalid Time Format. Please enter as HH:MM (24 hour time) or leave both start and end empty for this day.");
            tuesdayFromTime.requestFocus();
            return false;
        } else if (!tuesdayTo.matches(timeRegex) && !tuesdayFrom.isEmpty() && !tuesdayTo.isEmpty()) {
            tuesdayToTime.setError("Invalid Time Format. Please enter as HH:MM (24 hour time) or leave both start and end empty for this day.");
            tuesdayToTime.requestFocus();
            return false;
        } else if (!wednesdayFrom.matches(timeRegex) && !wednesdayFrom.isEmpty()) {
            wednesdayFromTime.setError("Invalid Time Format. Please enter as HH:MM (24 hour time) or leave both start and end empty for this day.");
            wednesdayFromTime.requestFocus();
            return false;
        } else if (!wednesdayTo.matches(timeRegex) && !wednesdayFrom.isEmpty() && !wednesdayTo.isEmpty()) {
            wednesdayToTime.setError("Invalid Time Format. Please enter as HH:MM (24 hour time) or leave both start and end empty for this day.");
            wednesdayToTime.requestFocus();
            return false;
        } else if (!thursdayFrom.matches(timeRegex) && !thursdayFrom.isEmpty()) {
            thursdayFromTime.setError("Invalid Time Format. Please enter as HH:MM (24 hour time) or leave both start and end empty for this day.");
            thursdayFromTime.requestFocus();
            return false;
        } else if (!thursdayTo.matches(timeRegex) && !thursdayFrom.isEmpty() && !thursdayTo.isEmpty()) {
            thursdayToTime.setError("Invalid Time Format. Please enter as HH:MM (24 hour time) or leave both start and end empty for this day.");
            thursdayToTime.requestFocus();
            return false;
        } else if (!fridayFrom.matches(timeRegex) && !fridayFrom.isEmpty()) {
            fridayFromTime.setError("Invalid Time Format. Please enter as HH:MM (24 hour time) or leave both start and end empty for this day.");
            fridayFromTime.requestFocus();
            return false;
        } else if (!fridayTo.matches(timeRegex) && !fridayFrom.isEmpty() && !fridayTo.isEmpty()) {
            fridayToTime.setError("Invalid Time Format. Please enter as HH:MM (24 hour time) or leave both start and end empty for this day.");
            fridayToTime.requestFocus();
            return false;
        } else if (!saturdayFrom.matches(timeRegex) && !saturdayFrom.isEmpty()) {
            saturdayFromTime.setError("Invalid Time Format. Please enter as HH:MM (24 hour time) or leave both start and end empty for this day.");
            saturdayFromTime.requestFocus();
            return false;
        } else if (!saturdayTo.matches(timeRegex) && !saturdayTo.isEmpty() && !saturdayTo.isEmpty()) {
            saturdayToTime.setError("Invalid Time Format. Please enter as HH:MM (24 hour time) or leave both start and end empty for this day.");
            saturdayToTime.requestFocus();
            return false;
        } else if (!sundayFrom.matches(timeRegex) && !sundayFrom.isEmpty()) {
            sundayFromTime.setError("Invalid Time Format. Please enter as HH:MM (24 hour time) or leave both start and end empty for this day.");
            sundayFromTime.requestFocus();
            return false;
        } else if (!sundayTo.matches(timeRegex) && !sundayFrom.isEmpty() && !sundayTo.isEmpty()) {
            sundayToTime.setError("Invalid Time Format. Please enter as HH:MM (24 hour time) or leave both start and end empty for this day.");
            sundayToTime.requestFocus();
            return false;
        }

        if (!mondayFrom.isEmpty()) {
            if (Integer.parseInt(mondayFrom.substring(0, 2)) > Integer.parseInt(mondayTo.substring(0, 2)) && !mondayFrom.isEmpty() && !mondayFrom.isEmpty()){
                mondayFromTime.setError("Starting time cannot be before ending time");
                mondayFromTime.requestFocus();
                return false;
            } else if ((Integer.parseInt(mondayFrom.substring(0, 2)) == Integer.parseInt(mondayTo.substring(0, 2))) && (Integer.parseInt(mondayFrom.substring(3, 5)) > Integer.parseInt(mondayTo.substring(3, 5))) && !mondayFrom.isEmpty() && !mondayFrom.isEmpty()){
                mondayFromTime.setError("Starting time cannot be before ending time");
                mondayFromTime.requestFocus();
                return false;
            }
        } else if (!tuesdayFrom.isEmpty()) {
            if (Integer.parseInt(tuesdayFrom.substring(0, 2)) > Integer.parseInt(tuesdayTo.substring(0, 2)) && !mondayFrom.isEmpty()){
                tuesdayFromTime.setError("Starting time cannot be before ending time");
                tuesdayFromTime.requestFocus();
                return false;
            } else if ((Integer.parseInt(tuesdayFrom.substring(0, 2)) == Integer.parseInt(tuesdayTo.substring(0, 2))) && (Integer.parseInt(tuesdayFrom.substring(3, 5)) > Integer.parseInt(tuesdayTo.substring(3, 5))) && !mondayFrom.isEmpty() && !mondayFrom.isEmpty()){
                tuesdayFromTime.setError("Starting time cannot be before ending time");
                tuesdayFromTime.requestFocus();
                return false;
            }
        } else if (!wednesdayFrom.isEmpty()) {
            if (Integer.parseInt(wednesdayFrom.substring(0, 2)) > Integer.parseInt(wednesdayTo.substring(0, 2)) && !mondayFrom.isEmpty() && !mondayFrom.isEmpty()){
                wednesdayFromTime.setError("Starting time cannot be before ending time");
                wednesdayFromTime.requestFocus();
                return false;
            } else if ((Integer.parseInt(wednesdayFrom.substring(0, 2)) == Integer.parseInt(wednesdayTo.substring(0, 2))) && (Integer.parseInt(wednesdayFrom.substring(3, 5)) > Integer.parseInt(wednesdayTo.substring(3, 5))) && !mondayFrom.isEmpty() && !mondayFrom.isEmpty()){
                wednesdayFromTime.setError("Starting time cannot be before ending time");
                wednesdayFromTime.requestFocus();
                return false;
            }
        } else if (!thursdayFrom.isEmpty()) {
            if (Integer.parseInt(thursdayFrom.substring(0, 2)) > Integer.parseInt(thursdayTo.substring(0, 2)) && !mondayFrom.isEmpty() && !mondayFrom.isEmpty()){
                thursdayFromTime.setError("Starting time cannot be before ending time");
                thursdayFromTime.requestFocus();
                return false;
            } else if ((Integer.parseInt(thursdayFrom.substring(0, 2)) == Integer.parseInt(thursdayTo.substring(0, 2))) && (Integer.parseInt(thursdayFrom.substring(3, 5)) > Integer.parseInt(thursdayTo.substring(3, 5))) && !mondayFrom.isEmpty() && !mondayFrom.isEmpty()){
                thursdayFromTime.setError("Starting time cannot be before ending time");
                thursdayFromTime.requestFocus();
                return false;
            }
        } else if (!fridayFrom.isEmpty()) {
            if (Integer.parseInt(fridayFrom.substring(0, 2)) > Integer.parseInt(fridayTo.substring(0, 2)) && !mondayFrom.isEmpty() && !mondayFrom.isEmpty()){
                fridayFromTime.setError("Starting time cannot be before ending time");
                fridayFromTime.requestFocus();
                return false;
            } else if ((Integer.parseInt(fridayFrom.substring(0, 2)) == Integer.parseInt(fridayTo.substring(0, 2))) && (Integer.parseInt(fridayFrom.substring(3, 5)) > Integer.parseInt(fridayTo.substring(3, 5))) && !mondayFrom.isEmpty() && !mondayFrom.isEmpty()){
                fridayFromTime.setError("Starting time cannot be before ending time");
                fridayFromTime.requestFocus();
                return false;
            }
        } else if (!saturdayFrom.isEmpty()) {
            if (Integer.parseInt(saturdayFrom.substring(0, 2)) > Integer.parseInt(saturdayTo.substring(0, 2)) && !mondayFrom.isEmpty() && !mondayFrom.isEmpty()){
                saturdayFromTime.setError("Starting time cannot be before ending time");
                saturdayFromTime.requestFocus();
                return false;
            } else if ((Integer.parseInt(saturdayFrom.substring(0, 2)) == Integer.parseInt(saturdayFrom.substring(0, 2))) && (Integer.parseInt(saturdayFrom.substring(3, 5)) > Integer.parseInt(saturdayTo.substring(3, 5))) && !mondayFrom.isEmpty() && !mondayFrom.isEmpty()){
                saturdayFromTime.setError("Starting time cannot be before ending time");
                saturdayFromTime.requestFocus();
                return false;
            }
        } else if (!sundayFrom.isEmpty()) {
            if (Integer.parseInt(sundayFrom.substring(0, 2)) > Integer.parseInt(sundayTo.substring(0, 2)) && !mondayFrom.isEmpty() && !mondayFrom.isEmpty()){
                sundayFromTime.setError("Starting time cannot be before ending time");
                sundayFromTime.requestFocus();
                return false;
            } else if ((Integer.parseInt(sundayFrom.substring(0, 2)) == Integer.parseInt(sundayTo.substring(0, 2))) && (Integer.parseInt(sundayFrom.substring(3, 5)) > Integer.parseInt(sundayTo.substring(3, 5))) && !mondayFrom.isEmpty() && !mondayFrom.isEmpty()){
                sundayFromTime.setError("Starting time cannot be before ending time");
                sundayFromTime.requestFocus();
                return false;
            }
        }
        return true;
    }

    public void updateAvailability(String mondayFrom, String mondayTo, String tuesdayFrom, String tuesdayTo, String wednesdayFrom,
                                   String wednesdayTo, String thursdayFrom, String thursdayTo, String fridayFrom, String fridayTo,
                                   String saturdayFrom, String saturdayTo, String sundayFrom, String sundayTo) {

        final Map availabilityForEmployee = new HashMap();

        availabilityForEmployee.put("MondayFrom", mondayFrom);
        availabilityForEmployee.put("MondayTo", mondayTo);
        availabilityForEmployee.put("TuesdayFrom", tuesdayFrom);
        availabilityForEmployee.put("TuesdayTo", tuesdayTo);
        availabilityForEmployee.put("WednesdayFrom", wednesdayFrom);
        availabilityForEmployee.put("WednesdayTo", wednesdayTo);
        availabilityForEmployee.put("ThursdayFrom", thursdayFrom);
        availabilityForEmployee.put("ThursdayTo", thursdayTo);
        availabilityForEmployee.put("FridayFrom", fridayFrom);
        availabilityForEmployee.put("FridayTo", fridayTo);
        availabilityForEmployee.put("SaturdayFrom", saturdayFrom);
        availabilityForEmployee.put("SaturdayTo", saturdayTo);
        availabilityForEmployee.put("SundayFrom", sundayFrom);
        availabilityForEmployee.put("SundayTo", sundayTo);

        db.collection("users")
                .document(empId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map empData = documentSnapshot.getData();

                        if(empData.containsKey("availability")){
                            empData.remove("availability");
                        }

                        empData.put("availability", availabilityForEmployee);

                        db.collection("users").document(empId).set(empData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Availability Updated Succesfully");
                                    }
                                });
                        updateUI(availabilityForEmployee);

                    }
                });
    }

    /**
     * Updates the UI
     * @param availabilityForEmployee a map with keys being each day (to and from) and value being the time
     */
    public void updateUI(Map availabilityForEmployee){
        mondayFromTime.setText(availabilityForEmployee.get("MondayFrom").toString());
        mondayToTime.setText(availabilityForEmployee.get("MondayTo").toString());

        tuesdayFromTime.setText(availabilityForEmployee.get("TuesdayFrom").toString());
        tuesdayToTime.setText(availabilityForEmployee.get("TuesdayTo").toString());

        wednesdayFromTime.setText(availabilityForEmployee.get("WednesdayFrom").toString());
        wednesdayToTime.setText(availabilityForEmployee.get("WednesdayTo").toString());

        thursdayFromTime.setText(availabilityForEmployee.get("ThursdayFrom").toString());
        thursdayToTime.setText(availabilityForEmployee.get("ThursdayTo").toString());

        fridayFromTime.setText(availabilityForEmployee.get("FridayFrom").toString());
        fridayToTime.setText(availabilityForEmployee.get("FridayTo").toString());

        saturdayFromTime.setText(availabilityForEmployee.get("SaturdayFrom").toString());
        saturdayToTime.setText(availabilityForEmployee.get("SaturdayTo").toString());

        sundayFromTime.setText(availabilityForEmployee.get("SundayFrom").toString());
        sundayToTime.setText(availabilityForEmployee.get("SundayTo").toString());

    }


}
