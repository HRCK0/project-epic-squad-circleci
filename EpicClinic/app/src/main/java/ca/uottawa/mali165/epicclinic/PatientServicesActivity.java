package ca.uottawa.mali165.epicclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.rpc.Help;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PatientServicesActivity extends AppCompatActivity {

    private final Context t = this;

    private static final String TAG = "PatientServicesActivity";

    List<String> companiesList = new LinkedList<>();
    List<String> addressesList = new LinkedList<>();
    List<String> ratingsList = new LinkedList<>();
    Spinner fromSpinner;
    Spinner toSpinner;
    CheckBox applyAvailabilityFilter;
    RatingBar ratingBar;
    Button dateBtn;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    ListView listView;

    FirebaseAuth auth;
    FirebaseFirestore db;
    Patient user;

    private Filters filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_services);

        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);

        applyAvailabilityFilter = findViewById(R.id.applyAvailabilityFilter);

        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating(Float.parseFloat("5.0"));

        dateBtn = findViewById(R.id.dateDialogButton);

        filters = new Filters();

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        PatientServicesActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++; //month starts at 0 need to incr
                Log.d(TAG, "onDateSet: mm/dd/yy: " + month + "/" + dayOfMonth + "/" + year);

                String date = month + "/" + dayOfMonth + "/" + year;
                dateBtn.setText(date);
            }
        };

        //Will listen to the radio button that checks to see wether to enable or disable avilability
        applyAvailabilityFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filters.setApplyAvailabilityFilter(applyAvailabilityFilter.isChecked());
                updateUI();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                filters.setRating(ratingBar.getRating());
            }
        });

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filters.setFromTime(fromSpinner.getItemAtPosition(i).toString());
                updateUI();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filters.setToTime(toSpinner.getItemAtPosition(i).toString());
                updateUI();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        listView = findViewById(R.id.listViewBookingServices);


        updateUI();

    }

    public void updateUI() {

        final Activity t = this;

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<String> companies = new ArrayList<>();
                            ArrayList<String> addresses = new ArrayList<>();
                            ArrayList<String> ratings = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Map user = document.getData();
                                if (user.get("role").equals("employee")) {
                                    if (user.get("profileCompleted").equals(true)){
                                        String companyName = (String) user.get("Name of Company");
                                        String address = (String) user.get("Address");
                                        String rating = (String) user.get("rating");

                                        companies.add(companyName);
                                        addresses.add(address);
                                        ratings.add(rating);
                                    }

                                }
                            }

                            companiesList = companies;
                            ratingsList = ratings;
                            addressesList = addresses;

                        }
                        BookingServicesListViewAdapter adapter = new BookingServicesListViewAdapter(t, companiesList, addressesList, ratingsList);
                        listView.setAdapter(adapter);
                    }
                });
    }
}
