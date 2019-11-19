package ca.uottawa.mali165.epicclinic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ServicesActivityNonAdmin extends AppCompatActivity {

    private final Context t = this;

    private static final String TAG = "ServicesNonAdmin";

    List<Service> serviceList = new LinkedList<>();
    List<String> categoryList = new LinkedList<>();
    List<String> serviceList2 = new LinkedList<>();


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

    public void showToast(String textToShow) {
        Toast.makeText(ServicesActivityNonAdmin.this, textToShow, Toast.LENGTH_SHORT).show();
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
                            Map categories = task.getResult().getData();

                            for (Object category : categories.keySet()) {
                                Map categoryData = (Map) categories.get(category);
                                Log.d(TAG, "ADDING TO LIST_CATEGORY: " + category);
                                categoryList.add((String) category);

                                for (Object service : categoryData.keySet()) {
                                  Log.d(TAG, "ADDING TO LIST_SERVICE: " + service);
                                    serviceList2.add((String) service);

                                }
                            }
                        }
                    }
                });

        db.collection("users")
                .document(getIntent().getStringExtra("CurrentUser_UID")).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            ArrayList<Service> list = new ArrayList<>();
                            Map empData = task.getResult().getData();
                            if (empData.containsKey("Services")) {
                                Map services = (Map) empData.get("Services");

                                for (Object category : services.keySet()) {

                                    Map categoryData = (Map) services.get(category);

                                    if (categoryList.remove((String) category)) {

                                        for (Object service : categoryData.keySet()) {

                                            Map serviceData = (Map) categoryData.get(service);

                                            if (serviceList2.remove((String) service)) {

                                                String serviceName = serviceData.get("name").toString();
                                                String price = serviceData.get("price").toString();
                                                Log.d(TAG, "onComplete: ADDED TO MAIN LIST");
                                                list.add(new Service(serviceName, price, (String) category));
                                            } else {
                                                Log.d(TAG, "onComplete: " + category);
                                                //Log.d(TAG, "onComplete: " + service);
                                               // deleteFromDB((String) category, (String) service);
                                            }
                                        }
                                    } else {
                                       Log.d(TAG, "onComplete2: " + category);

                                        deleteFromDB((String) category, null);
                                    }
                                }
                                serviceList = list;
                            }
                            ServicesListViewAdapterNonAdmin servicesListViewAdapter = new ServicesListViewAdapterNonAdmin(t, serviceList);
                            listView.setAdapter(servicesListViewAdapter);
                        }
                    }
                });


    }

    public void allServicesBtnClicked(View buttonSeeAdminServices) {

        Intent openServicesWindow = new Intent(getApplicationContext(), ServicesActivityNonAdmin_2.class);
//        Employee employeeUser = (Employee) user;
//        openServicesWindow.putExtra("employee", employeeUser);
        openServicesWindow.putExtra("CurrentUser_UID", getIntent().getStringExtra("CurrentUser_UID"));
        startActivity(openServicesWindow);
    }

    public void deleteFromDB(final String category, final String service) {

        db.collection("users")
                .document(getIntent().getStringExtra("CurrentUser_UID")).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Map empData = documentSnapshot.getData();
                        Map services = (Map) documentSnapshot.get("Services");
                        Map servicesWithinCategoryMap = (Map) services.remove(category);

                        empData.remove("Services");

                        if (service == null){
                            services.remove(category);
                        }else {
                            servicesWithinCategoryMap.remove(service);
                            services.put(category,servicesWithinCategoryMap);
                        }
                        empData.put("Services", services);
                        db.collection("users").document(getIntent().getStringExtra("CurrentUser_UID")).set(empData);
                    }
                });
    }
}
