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
                System.out.println("HELLOOOOOOOOOOOO");
                final Map availabilityForEmployee = new HashMap();
                availabilityForEmployee.put("MondayFrom", mondayFromTime.getText().toString());
                availabilityForEmployee.put("MondayTo", mondayToTime.getText().toString());
                availabilityForEmployee.put("TuesdayFrom", tuesdayFromTime.getText().toString());
                availabilityForEmployee.put("TuesdayTo", tuesdayToTime.getText().toString());
                availabilityForEmployee.put("WednesdayFrom", wednesdayFromTime.getText().toString());
                availabilityForEmployee.put("WednesdayTo", wednesdayToTime.getText().toString());
                availabilityForEmployee.put("ThursdayFrom", thursdayFromTime.getText().toString());
                availabilityForEmployee.put("ThursdayTo", thursdayToTime.getText().toString());
                availabilityForEmployee.put("FridayFrom", fridayFromTime.getText().toString());
                availabilityForEmployee.put("FridayTo", fridayToTime.getText().toString());
                availabilityForEmployee.put("SaturdayFrom", saturdayFromTime.getText().toString());
                availabilityForEmployee.put("SaturdayTo", saturdayToTime.getText().toString());
                availabilityForEmployee.put("SundayFrom", sundayFromTime.getText().toString());
                availabilityForEmployee.put("SundayTo", sundayToTime.getText().toString());

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
