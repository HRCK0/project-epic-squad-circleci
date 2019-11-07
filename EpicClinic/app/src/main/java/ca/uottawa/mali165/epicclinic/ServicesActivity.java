package ca.uottawa.mali165.epicclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ServicesActivity extends AppCompatActivity {

  private final Context t = this;

  private static final String TAG = "ServicesActivity";

  String[] serviceNames = {"Service1", "Service2", "Service3"};
  String[] categoryNames = {"category1", "caegory2", "category3"};
  String[] priceNames = {"price1", "price2", "price3"};
  Integer[]  serviceIds = {1, 2, 3};

  List<Service> serviceList = new LinkedList<>();

  EditText serviceNameEditText, priceEditText;
  Button addServiceButton;
  ListView listView;

  FirebaseAuth mAuth;
  FirebaseFirestore db;

  Map<String, Object> service;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_services);
    listView = (ListView) findViewById(R.id.listView);

    mAuth = FirebaseAuth.getInstance();
    db = FirebaseFirestore.getInstance();

    updateUI();

//        serviceNameEditText = findViewById(R.id.newServiceNameEditText);
//        priceEditText = findViewById(R.id.newServicePriceEditText);
//        addServiceButton = findViewById(R.id.addButton);

    //TODO: RETRIEVE SERVICES FROM DATABASE AND DISPLAY ON VIEW

//        for(int i =0; i<3; i++){
//            serviceList.add(new Service(serviceIds[i], serviceNames[i], priceNames[i], categoryNames[i]));
//        }
//
//        ServicesListViewAdapter servicesListViewAdapter = new ServicesListViewAdapter(this, serviceList);
//        listView.setAdapter(servicesListViewAdapter);
  }

  public void onCreateNewService(View serviceBtn) {

    String serviceName = serviceNameEditText.getText().toString();
    String servicePrice = priceEditText.getText().toString();

    service = new HashMap<>();
    service.put("serviceName", serviceName);
    service.put("servicePrice", servicePrice);

    if (serviceName.isEmpty()) {
      serviceNameEditText.setError("Please enter a service name");
      serviceNameEditText.requestFocus();
    } else if (servicePrice.isEmpty()) {
      priceEditText.setError("Please enter a price");
      priceEditText.requestFocus();
      //TODO;
      // validate service price is a number somehow
    } else if (serviceName.isEmpty() && servicePrice.isEmpty()) {
      Toast.makeText(this, "Required Fields are Empty!", Toast.LENGTH_SHORT).show();
    } else {
      db.collection("services").document(serviceName).set(service)
              .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                  Log.d(TAG, "Service Successfully Created");
                  serviceNameEditText.setText("");
                  priceEditText.setText("");

                }
              })
              .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                  Log.w(TAG, "Service Not Created - Document Error", e);
                }
              });
    }

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
//    final EditText categoryInput = new EditText(this);
//    categoryInput.setHint("Category");

    serviceNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
    servicePriceInput.setInputType(InputType.TYPE_CLASS_NUMBER);

    layout.addView(serviceNameInput);
    layout.addView(servicePriceInput);
//    layout.addView(categoryInput);

    builder.setView(layout);

    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        String serviceName = serviceNameInput.getText().toString();
        String price = servicePriceInput.getText().toString();
//        String category = categoryInput.getText().toString();

        if (serviceName.isEmpty()){
          serviceNameEditText.setError("Please enter a service name");
          serviceNameEditText.requestFocus();
        } else if (price.isEmpty()) {
          priceEditText.setError("Please enter a price");
          priceEditText.requestFocus();
        } else {
          // successful
          db = FirebaseFirestore.getInstance();

          HashMap service = new HashMap();

          service.put("name", serviceName);
          service.put("price", price);

          db.collection("services")
                  .document(serviceName).set(service)
                  .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                      Log.d(TAG, "Service Successfully Created");
                    }
                  })
                  .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                      Log.w(TAG, "Service Not Created - Document Error", e);
                    }
                  });
        }
      }
    });

    builder.show();

  }

  public void updateUI() {
    // implement code to update list with firebase services

    db.collection("services").get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
              @Override
              public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                  ArrayList<Service> list = new ArrayList<>();
                  for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> data = document.getData();

                    String serviceName = data.get("serviceName").toString();
                    String price = data.get("price").toString();

                    list.add(new Service(0, serviceName, price));


                  }
                  serviceList = list;
                }
              }
            });
    // this code doesnt work

    ServicesListViewAdapter servicesListViewAdapter = new ServicesListViewAdapter(this, serviceList);
    listView.setAdapter(servicesListViewAdapter);
  }
}
