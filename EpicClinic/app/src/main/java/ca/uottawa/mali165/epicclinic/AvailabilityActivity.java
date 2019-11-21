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

                final Map availabilityForEmployee = new HashMap();

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

                String timeRegex = "^([01][0-9]|2[0-3]):[0-5][0-9]$";

                // check all time inputs
                if (!mondayFrom.matches(timeRegex)){
                    mondayFromTime.setError("Invalid Time Format. Please enter as HH:MM.");
                    mondayFromTime.requestFocus();
                } else if (!mondayTo.matches(timeRegex)) {
                    mondayToTime.setError("Invalid Time Format. Please enter as HH:MM.");
                    mondayToTime.requestFocus();
                } else if (!tuesdayFrom.matches(timeRegex)) {
                    tuesdayFromTime.setError("Invalid Time Format. Please enter as HH:MM.");
                    tuesdayFromTime.requestFocus();
                } else if (!tuesdayTo.matches(timeRegex)) {
                    tuesdayToTime.setError("Invalid Time Format. Please enter as HH:MM.");
                    tuesdayToTime.requestFocus();
                } else if (!wednesdayFrom.matches(timeRegex)) {
                    wednesdayFromTime.setError("Invalid Time Format. Please enter as HH:MM.");
                    wednesdayFromTime.requestFocus();
                } else if (!wednesdayTo.matches(timeRegex)) {
                    wednesdayToTime.setError("Invalid Time Format. Please enter as HH:MM.");
                    wednesdayToTime.requestFocus();
                } else if (!thursdayFrom.matches(timeRegex)) {
                    thursdayFromTime.setError("Invalid Time Format. Please enter as HH:MM.");
                    thursdayFromTime.requestFocus();
                } else if (!thursdayTo.matches(timeRegex)) {
                    thursdayToTime.setError("Invalid Time Format. Please enter as HH:MM.");
                    thursdayToTime.requestFocus();
                } else if (!fridayFrom.matches(timeRegex)) {
                    fridayFromTime.setError("Invalid Time Format. Please enter as HH:MM.");
                    fridayFromTime.requestFocus();
                } else if (!fridayTo.matches(timeRegex)) {
                    fridayToTime.setError("Invalid Time Format. Please enter as HH:MM.");
                    fridayToTime.requestFocus();
                } else if (!saturdayFrom.matches(timeRegex)) {
                    saturdayFromTime.setError("Invalid Time Format. Please enter as HH:MM.");
                    saturdayFromTime.requestFocus();
                } else if (!saturdayTo.matches(timeRegex)) {
                    saturdayToTime.setError("Invalid Time Format. Please enter as HH:MM.");
                    saturdayToTime.requestFocus();
                } else if (!sundayFrom.matches(timeRegex)) {
                    sundayFromTime.setError("Invalid Time Format. Please enter as HH:MM.");
                    sundayFromTime.requestFocus();
                } else if (!sundayTo.matches(timeRegex)) {
                    sundayToTime.setError("Invalid Time Format. Please enter as HH:MM.");
                    sundayToTime.requestFocus();
                } else if (Integer.parseInt(mondayFrom.substring(0, 2)) > Integer.parseInt(mondayTo.substring(0, 2))){
                    mondayFromTime.setError("Starting time cannot be before ending time");
                    mondayFromTime.requestFocus();
                } else if ((Integer.parseInt(mondayFrom.substring(0, 2)) == Integer.parseInt(mondayTo.substring(0, 2))) && (Integer.parseInt(mondayFrom.substring(3, 5)) > Integer.parseInt(mondayTo.substring(3, 5)))){
                    mondayFromTime.setError("Starting time cannot be before ending time");
                    mondayFromTime.requestFocus();
                } else if (Integer.parseInt(tuesdayFrom.substring(0, 2)) > Integer.parseInt(tuesdayTo.substring(0, 2))){
                    tuesdayFromTime.setError("Starting time cannot be before ending time");
                    tuesdayFromTime.requestFocus();
                } else if ((Integer.parseInt(tuesdayFrom.substring(0, 2)) == Integer.parseInt(tuesdayTo.substring(0, 2))) && (Integer.parseInt(tuesdayFrom.substring(3, 5)) > Integer.parseInt(tuesdayTo.substring(3, 5)))){
                    tuesdayFromTime.setError("Starting time cannot be before ending time");
                    tuesdayFromTime.requestFocus();
                } else if (Integer.parseInt(wednesdayFrom.substring(0, 2)) > Integer.parseInt(wednesdayTo.substring(0, 2))){
                    wednesdayFromTime.setError("Starting time cannot be before ending time");
                    wednesdayFromTime.requestFocus();
                } else if ((Integer.parseInt(wednesdayFrom.substring(0, 2)) == Integer.parseInt(wednesdayTo.substring(0, 2))) && (Integer.parseInt(wednesdayFrom.substring(3, 5)) > Integer.parseInt(wednesdayTo.substring(3, 5)))){
                    wednesdayFromTime.setError("Starting time cannot be before ending time");
                    wednesdayFromTime.requestFocus();
                } else if (Integer.parseInt(thursdayFrom.substring(0, 2)) > Integer.parseInt(thursdayTo.substring(0, 2))){
                    thursdayFromTime.setError("Starting time cannot be before ending time");
                    thursdayFromTime.requestFocus();
                } else if ((Integer.parseInt(thursdayFrom.substring(0, 2)) == Integer.parseInt(thursdayTo.substring(0, 2))) && (Integer.parseInt(thursdayFrom.substring(3, 5)) > Integer.parseInt(thursdayTo.substring(3, 5)))){
                    thursdayFromTime.setError("Starting time cannot be before ending time");
                    thursdayFromTime.requestFocus();
                } else if (Integer.parseInt(fridayFrom.substring(0, 2)) > Integer.parseInt(fridayTo.substring(0, 2))){
                    fridayFromTime.setError("Starting time cannot be before ending time");
                    fridayFromTime.requestFocus();
                } else if ((Integer.parseInt(fridayFrom.substring(0, 2)) == Integer.parseInt(fridayTo.substring(0, 2))) && (Integer.parseInt(fridayFrom.substring(3, 5)) > Integer.parseInt(fridayTo.substring(3, 5)))){
                    fridayFromTime.setError("Starting time cannot be before ending time");
                    fridayFromTime.requestFocus();
                } else if (Integer.parseInt(saturdayFrom.substring(0, 2)) > Integer.parseInt(saturdayTo.substring(0, 2))){
                    saturdayFromTime.setError("Starting time cannot be before ending time");
                    saturdayFromTime.requestFocus();
                } else if ((Integer.parseInt(saturdayFrom.substring(0, 2)) == Integer.parseInt(saturdayFrom.substring(0, 2))) && (Integer.parseInt(saturdayFrom.substring(3, 5)) > Integer.parseInt(saturdayTo.substring(3, 5)))){
                    saturdayFromTime.setError("Starting time cannot be before ending time");
                    saturdayFromTime.requestFocus();
                } else if (Integer.parseInt(sundayFrom.substring(0, 2)) > Integer.parseInt(sundayTo.substring(0, 2))){
                    sundayFromTime.setError("Starting time cannot be before ending time");
                    sundayFromTime.requestFocus();
                } else if ((Integer.parseInt(sundayFrom.substring(0, 2)) == Integer.parseInt(sundayTo.substring(0, 2))) && (Integer.parseInt(sundayFrom.substring(3, 5)) > Integer.parseInt(sundayTo.substring(3, 5)))){
                    sundayFromTime.setError("Starting time cannot be before ending time");
                    sundayFromTime.requestFocus();
                } else {

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
