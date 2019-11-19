package ca.uottawa.mali165.epicclinic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ServicesActivityNonAdmin_2 extends AppCompatActivity {

    private final Context t = this;

    private static final String TAG = "ServicesNonAdmin2";

    List<Service> serviceList = new LinkedList<>();

    ListView listView;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services3);
        listView = (ListView) findViewById(R.id.listView3);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        updateUI();
    }

    public void showToast(String textToShow){
        Toast.makeText(ServicesActivityNonAdmin_2.this, textToShow, Toast.LENGTH_SHORT).show();
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
                        ServicesListViewAdapterNonAdmin_2 servicesListViewAdapter = new ServicesListViewAdapterNonAdmin_2(t, serviceList);
                        listView.setAdapter(servicesListViewAdapter);

                    }
                });

        // this code doesnt work

    }

    public void onAdd(View buttonPickService){


        db = FirebaseFirestore.getInstance();

        RelativeLayout serviceLayout = (RelativeLayout) buttonPickService.getParent().getParent().getParent().getParent();
        final TextView serviceName = serviceLayout.findViewById(R.id.serviceName3);
        final TextView price = serviceLayout.findViewById(R.id.price3);
        final TextView category = serviceLayout.findViewById(R.id.category3);

        final String serviceName2 = serviceName.getText().toString();
        final String price2 = price.getText().toString();
        final String category2 = category.getText().toString();

        final HashMap service = new HashMap();
        final Activity t = this;
        service.put("name", serviceName2);
        service.put("price", price2);

        db.collection("users")
                .document(getIntent().getStringExtra("CurrentUser_UID")).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Map empData = documentSnapshot.getData();

                        if (empData.containsKey("Services")) {
                            Map services = (Map) documentSnapshot.get("Services");

                            if (services.containsKey(category2)){
                                Map servicesWithinCategoryMap = (Map) services.remove(category2);
                                Map serviceMap = (Map) servicesWithinCategoryMap.get(serviceName2);

                                if (serviceMap != null) {
                                    showToast("Service Already Added");
                                    return;
                                }

                                servicesWithinCategoryMap.put(serviceName2, service);
                                services.put(category2, servicesWithinCategoryMap);

                                empData.remove("Services");

                                empData.put("Services", services);
                                db.collection("users").document(getIntent().getStringExtra("CurrentUser_UID")).set(empData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "New Service Succesfully Created");
                                            }
                                        });
                                updateUI();
                            }else {
                                Map categoryData = new HashMap();
                                categoryData.put(serviceName2, service);
                                services.put(category2, categoryData);
                                if (empData.containsKey("Services")) {
                                    empData.remove("Services");
                                }
                                empData.put("Services", services);
                                db.collection("users").document(getIntent().getStringExtra("CurrentUser_UID")).set(empData);
                                updateUI();

                            }
                        } else{
                            Log.d(TAG, "onSuccess: CALLED!!!");
                            Map categoryData = new HashMap();
                            Map services = new HashMap();
                            categoryData.put(serviceName2, service);
                            services.put(category2, categoryData);
                            empData.put("Services", services);
                            db.collection("users").document(getIntent().getStringExtra("CurrentUser_UID")).set(empData);
                            updateUI();
                        }
                    }

                });
    }
}
