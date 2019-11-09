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

  public void showToast(String textToShow){
    Toast.makeText(ServicesActivity.this, textToShow, Toast.LENGTH_SHORT).show();
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

    builder.setPositiveButton("Save",
            new DialogInterface.OnClickListener()
            {
              @Override
              public void onClick(DialogInterface dialog, int which)
              {}
            });

    final AlertDialog dialog = builder.create();
    dialog.show();

    //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
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

                        Map servicesWithinCategoryMap = (Map) servicesData.remove(category);
                        for(Object service : servicesWithinCategoryMap.keySet()){
                          Map serviceMap = (Map) servicesWithinCategoryMap.get(service);
                          if(serviceMap.get("name").equals(serviceName)){
                            showToast("Service Name Already Exists for Category");
                            return;
                          }
                        }

                        servicesWithinCategoryMap.put(serviceName, service);
                        servicesData.put(category, servicesWithinCategoryMap);
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
                      dialog.dismiss();
                      updateUI();
                    }
                  });

        }
      }
    });

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
}
