package ca.uottawa.mali165.epicclinic;

import android.app.Activity;
import android.content.Context;
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

    public void showToast(String textToShow){
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

    public void onAdd(View buttonPickService){

        Log.d(TAG, "onAdd: CALLED!!");
        db = FirebaseFirestore.getInstance();

        RelativeLayout serviceLayout = (RelativeLayout) buttonPickService.getParent().getParent().getParent().getParent();
        final TextView serviceName = serviceLayout.findViewById(R.id.serviceName2);
        final TextView price = serviceLayout.findViewById(R.id.price2);
        final TextView category = serviceLayout.findViewById(R.id.category2);

        final HashMap service = new HashMap();
        final Activity t = this;
        service.put("name", serviceName);
        service.put("price", price);

        db.collection("users")
                .document(getIntent().getStringExtra("uid")).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Map empData = documentSnapshot.getData();



                        Map services = (Map) documentSnapshot.get("Services");


                        if (services.containsKey(category)){
                            Map servicesWithinCategoryMap = (Map) services.remove(category);
                            Map serviceMap = (Map) servicesWithinCategoryMap.get(serviceName);

                            if (serviceMap != null) {
                                showToast("Service Already Added");
                                return;
                            }

                            servicesWithinCategoryMap.put(serviceName, service);
                            services.put(category, servicesWithinCategoryMap);


                            if (empData.containsKey("Services")) {
                                empData.remove("Services");
                            }

                            empData.put("Services", services);


                            db.collection("users").document(getIntent().getStringExtra("uid")).set(empData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "New Service Succesfully Created");
                                        }
                                    });

                        }else {
                            Map categoryData = new HashMap();
                            categoryData.put(serviceName, service);
                            services.put(category, categoryData);
                            if (empData.containsKey("Services")) {
                                empData.remove("Services");
                            }
                            empData.put("Services", services);
                            db.collection("users").document(getIntent().getStringExtra("uid")).set(empData);

                        }


                    }
                });

    }

}
