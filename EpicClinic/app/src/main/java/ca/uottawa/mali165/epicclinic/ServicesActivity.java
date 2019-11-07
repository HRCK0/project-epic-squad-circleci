package ca.uottawa.mali165.epicclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

  List<Service> serviceList = new LinkedList<>();

  EditText serviceNameEditText, priceEditText;ListView listView;

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

    serviceNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
    servicePriceInput.setInputType(InputType.TYPE_CLASS_NUMBER);

    layout.addView(serviceNameInput);
    layout.addView(servicePriceInput);

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
