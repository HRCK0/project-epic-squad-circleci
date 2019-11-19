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
        final Map[] servicesData = new Map[1];

        db.collection("services")
                .document("services").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        servicesData[0] = (Map) documentSnapshot.getData();

                        //querying users database
                        db.collection("users")
                                .document(getIntent().getStringExtra("CurrentUser_UID")).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        List serviceToDisplayList = new LinkedList();

                                        if(!documentSnapshot.getData().containsKey("Services")){return;}


                                        Map userData = (Map) documentSnapshot.getData();
                                        Map currentUserServicesData = (Map) userData.get("Services");



                                        for(Object category : currentUserServicesData.keySet()){
                                            if(servicesData[0].containsKey(category)){ //making sure that category of users service is a valid category
                                                Map servicesWithinCategoryForAdmin = (Map) servicesData[0].get(category);
                                                Map servicesWithinCategoryForUser = (Map) currentUserServicesData.get(category);
                                                for(Object service : servicesWithinCategoryForUser.keySet()){ //if the category of users service was valid, then check to see if service name is valid
                                                    if(servicesWithinCategoryForAdmin.containsKey(service)){
                                                        Map serviceMap = (Map) servicesWithinCategoryForUser.get(service);
                                                        Service serviceToAdd = new Service(serviceMap.get("name").toString(), serviceMap.get("price").toString(), category.toString());
                                                        serviceToDisplayList.add(serviceToAdd);

                                                    }else{
                                                        currentUserServicesData.remove(category);
                                                        servicesWithinCategoryForUser.remove(service);
                                                        if(!servicesWithinCategoryForUser.keySet().isEmpty()){ //in case the service that was removed was last in category
                                                            currentUserServicesData.put(category, servicesWithinCategoryForUser);
                                                        }
                                                    }
                                                }
                                            }else{
                                                currentUserServicesData.remove(category);
                                            }
                                        }

                                        userData.remove("Services");
                                        userData.put("Services", currentUserServicesData);

                                        ServicesListViewAdapterNonAdmin servicesListViewAdapter = new ServicesListViewAdapterNonAdmin(t, serviceToDisplayList);
                                        listView.setAdapter(servicesListViewAdapter);

                                        db.collection("users").document(getIntent().getStringExtra("CurrentUser_UID")).set(userData)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "Employee services updated succesfully");
                                                    }
                                                });


                                    }});

                    }});
        


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


                        empData.remove("Services");

                        if (service == null){
                            services.remove(category);
                        }else {
                            Map servicesWithinCategoryMap = (Map) services.remove(category);
                            servicesWithinCategoryMap.remove(service);
                            services.put(category,servicesWithinCategoryMap);
                        }
                        empData.put("Services", services);
                        db.collection("users").document(getIntent().getStringExtra("CurrentUser_UID")).set(empData);
                    }
                });
    }
}
