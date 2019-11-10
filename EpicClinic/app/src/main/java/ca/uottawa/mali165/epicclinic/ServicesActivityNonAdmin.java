package ca.uottawa.mali165.epicclinic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ServicesActivityNonAdmin extends AppCompatActivity {

    private final Context t = this;

    private static final String TAG = "ServicesActivityNonAdmin";

    List<Service> serviceList = new LinkedList<>();

    ListView listView;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services2);
        listView = (ListView) findViewById(R.id.listView2);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        updateUI();
    }

    public void updateUI() {
        // implement code to update list with firebase services

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

                                    String serviceName = serviceData.get("name").toString();
                                    String price = serviceData.get("price").toString();
                                    list.add(new Service(serviceName, price, (String) category));

                                }

                            }
                            serviceList = list;
                        }
                        ServicesListViewAdapterNonAdmin servicesListViewAdapter = new ServicesListViewAdapterNonAdmin(t, serviceList);
                        listView.setAdapter(servicesListViewAdapter);

                    }
                });
        // this code doesnt work

    }

}
