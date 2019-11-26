package ca.uottawa.mali165.epicclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
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

    ListView listView;

    FirebaseAuth auth;
    FirebaseFirestore db;
    Patient user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_services);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        listView = findViewById(R.id.listViewBookingServices);

        updateUI();

    }

    public Map getAvailability(){
        Map availabilityMap = new HashMap<String, List<String>>();
        Spinner daySpinner = findViewById(R.id.daySpinner);
        Spinner fromSpinner = findViewById(R.id.fromSpinner);
        Spinner toSpinner = findViewById(R.id.toSpinner);
        List times = new LinkedList<String>();
        times.add(fromSpinner.getSelectedItem().toString());
        times.add(toSpinner.getSelectedItem().toString());
        availabilityMap.put(daySpinner.getSelectedItem().toString(), times);
        return availabilityMap;
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
