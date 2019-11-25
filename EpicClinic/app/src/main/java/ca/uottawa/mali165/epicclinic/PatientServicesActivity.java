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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PatientServicesActivity extends AppCompatActivity {

    private final Context t = this;

    private static final String TAG = "PatientServicesActivity";

    List<Service> serviceList = new LinkedList<>();

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

        db.collection("services")
                .document("services").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Service> list = new ArrayList<>();

                            Map categories = task.getResult().getData();

                            for (Object category : categories.keySet()) {

                                Map categoryData = (Map) categories.get(category);

                                for (Object service : categoryData.keySet()) {
                                    Map serviceData = (Map) categoryData.get(service);

                                    String serviceName = (String) serviceData.get("name");
                                    String price = (String) serviceData.get("price");
                                    String role = (String) serviceData.get("role");
                                    list.add(new Service(serviceName, price, category.toString(), role));


                                }
                            }
                            serviceList = list;
                        }
                        BookingServicesListViewAdapter adapter = new BookingServicesListViewAdapter(t, serviceList);
                        listView.setAdapter(adapter);
                    }

                });


    }
}
