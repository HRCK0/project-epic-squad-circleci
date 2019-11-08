package ca.uottawa.mali165.epicclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.UUID;

public class ServicesActivity extends AppCompatActivity {

  private final Context t = this;

  private static final String TAG = "ServicesActivity";

  List<Service> serviceList = new LinkedList<>();

  ListView listView;

  FirebaseAuth mAuth;
  FirebaseFirestore db;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_services);
    listView = (ListView) findViewById(R.id.listView);

    mAuth = FirebaseAuth.getInstance();
    db = FirebaseFirestore.getInstance();

    updateUI();
  }


  public void onClickAddNewService(View newServiceBtn) {

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Add a new Service");

    LinearLayout layout = new LinearLayout(this);
    layout.setOrientation(LinearLayout.VERTICAL);

    final EditText serviceNameInput = new EditText(this);
    serviceNameInput.setHint("Name");
    final EditText servicePriceInput = new EditText(this);
    servicePriceInput.setHint("Price");
    final EditText categoryInput = new EditText(this);
    categoryInput.setHint("Category of Service");

    serviceNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
    servicePriceInput.setInputType(InputType.TYPE_CLASS_NUMBER);
    categoryInput.setInputType(InputType.TYPE_CLASS_TEXT);

    layout.addView(serviceNameInput);
    layout.addView(servicePriceInput);
    layout.addView(categoryInput);

    builder.setView(layout);

    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {

        final String serviceName = serviceNameInput.getText().toString();
        final String price = servicePriceInput.getText().toString();
        final String category = categoryInput.getText().toString();

        if (serviceName.isEmpty()){
          serviceNameInput.setError("Please enter a service name");
          serviceNameInput.requestFocus();
        } else if (price.isEmpty()) {
          servicePriceInput.setError("Please enter a price");
          servicePriceInput.requestFocus();
        } else if (category.isEmpty()) {
          categoryInput.setError("Please enter a category");
          categoryInput.requestFocus();
        } else {
          // successful
          db = FirebaseFirestore.getInstance();

          final HashMap service = new HashMap();

          service.put("name", serviceName);
          service.put("price", price);

          db.collection("services")
                  .document("services").get()
                  .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                      Map servicesData = documentSnapshot.getData();

                      if (servicesData.containsKey(category)) {
                        Map categoryData = (Map) servicesData.remove(category);
                        categoryData.put(serviceName, service);
                        servicesData.put(category, categoryData);
                        db.collection("services").document("services").set(servicesData)
                          .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                              Log.d(TAG, "New Service Succesfully Created");
                            }
                          });
                      } else {
                        Map categoryData = new HashMap();
                        categoryData.put(serviceName, service);
                        servicesData.put(category, categoryData);
                        db.collection("services").document("services").set(servicesData);
                      }
                      updateUI();
                    }
                  });

        }
      }
    });

    builder.show();

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
                ServicesListViewAdapter servicesListViewAdapter = new ServicesListViewAdapter(t, serviceList);
                listView.setAdapter(servicesListViewAdapter);

              }
            });
    // this code doesnt work

  }

  public void onDeleteService(View deleteBtn) {

    LinearLayout serviceLayout = (LinearLayout) deleteBtn.getParent().getParent().getParent().getParent();
    TextView serviceNameView = serviceLayout.findViewById(R.id.serviceName);
    TextView categoryNameView = serviceLayout.findViewById(R.id.category);

    final String serviceName = serviceNameView.getText().toString();
    final String categoryName = categoryNameView.getText().toString();

    db.collection("services")
            .document("services").get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {

                Map servicesData = (Map) documentSnapshot.getData();
                Map categoryData = (Map) servicesData.remove(categoryName);

                categoryData.remove(serviceName);

                if (!categoryData.isEmpty()) {
                  servicesData.put(categoryName, categoryData);
                }

                db.collection("services")
                        .document("services").set(servicesData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void aVoid) {
                            Log.d(TAG, (serviceName + " deleted from category " + categoryName));
                            updateUI();
                          }
                        });

              }
            });
  }
}
