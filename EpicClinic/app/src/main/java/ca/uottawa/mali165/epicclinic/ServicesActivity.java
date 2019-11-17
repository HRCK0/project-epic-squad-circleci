package ca.uottawa.mali165.epicclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
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
  Admin user;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_services);
    listView = (ListView) findViewById(R.id.listView);

    mAuth = FirebaseAuth.getInstance();
    db = FirebaseFirestore.getInstance();

    user = getIntent().getExtras().getParcelable("admin");
    updateUI();
  }

  public void showToast(String textToShow){
    Toast.makeText(ServicesActivity.this, textToShow, Toast.LENGTH_SHORT).show();
  }

  public void updateDBAndUIToAddService(String servicename, String priceName, String cat, AlertDialog alertDialog){
    final String category = cat;
    final String serviceName = servicename;
    final String price = priceName;
    final AlertDialog dialog = alertDialog;

    db = FirebaseFirestore.getInstance();

    final HashMap service = new HashMap();
    final Activity t = this;
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
          if(dialog!=null){dialog.dismiss();}
          updateUI(); //TEST
          serviceList.add(0,new Service(serviceName, price, category)); //add the new element

          ServicesListViewAdapter servicesListViewAdapter = new ServicesListViewAdapter(t,serviceList);
          listView.setAdapter(servicesListViewAdapter);
        }
      });
  }

  public void addNewService(View newServiceBtn, final String defServiceName, final String defPrice, final String defCat, final boolean edit){

    final String serviceNameDefault = (defServiceName == null)? "": defServiceName;
    final String categoryDefault = (defPrice == null)? "": defPrice;
    final String priceDefault = (defCat == null)? "": defCat;


    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    String title = edit ? "Editing Service" : "Adding a new Service";
    builder.setTitle(title);

    LinearLayout layout = new LinearLayout(this);
    layout.setOrientation(LinearLayout.VERTICAL);

    final EditText serviceNameInput = new EditText(this);
    serviceNameInput.setHint("Name");
    serviceNameInput.setText(serviceNameDefault);
    final EditText servicePriceInput = new EditText(this);
    servicePriceInput.setHint("Price");
    servicePriceInput.setText(priceDefault);
    final EditText categoryInput = new EditText(this);
    categoryInput.setHint("Category of Service");
    categoryInput.setText(categoryDefault);


    serviceNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
    servicePriceInput.setInputType(InputType.TYPE_CLASS_NUMBER);
    categoryInput.setInputType(InputType.TYPE_CLASS_TEXT);

    layout.addView(serviceNameInput);
    layout.addView(servicePriceInput);
    layout.addView(categoryInput);

    builder.setView(layout);

    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if(edit){
          updateDBAndUIToAddService(serviceNameDefault, priceDefault, categoryDefault, null);
        }
        finish();
      }
    });

    builder.setPositiveButton("Save",
      new DialogInterface.OnClickListener()
      {
        @Override
        public void onClick(DialogInterface dialog, int which){}
      });

    final AlertDialog dialog = builder.create();
    dialog.setCancelable(false);
    dialog.setCanceledOnTouchOutside(false);
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

          updateDBAndUIToAddService(serviceName, price, category, dialog);
          if(edit)
            user.editService(new Service(defServiceName, defPrice, defCat), new Service(serviceName,price,category));
          else
            user.addService(new Service(serviceName,price,category));
        }
      }
    });
  }

  public void onClickAddNewService(View newServiceBtn) {
    addNewService(newServiceBtn, null, null, null, false);
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
                      user.addService(new Service(serviceName, price, (String) category)); //retain the old services

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

  public void onEditServiceClick(View editBtn){
    LinearLayout serviceLayout = (LinearLayout) editBtn.getParent().getParent().getParent().getParent();
    TextView serviceNameView = serviceLayout.findViewById(R.id.serviceName);
    TextView categoryNameView = serviceLayout.findViewById(R.id.category);
    TextView priceNameView = serviceLayout.findViewById(R.id.price);
    final String serviceNameInitial = serviceNameView.getText().toString();
    final String categoryNameInitial = categoryNameView.getText().toString();
    final String priceNameInitial = priceNameView.getText().toString();


    db.collection("services")
            .document("services").get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map servicesData = documentSnapshot.getData();

                //the code to verify the service being edited is there is not really necessary since by editing you assume its already in the db
                boolean found = false;
                if (servicesData.containsKey(categoryNameInitial)) {

                  Map servicesWithinCategoryMap = (Map) servicesData.remove(categoryNameInitial);
                  for (Object service : servicesWithinCategoryMap.keySet()) {
                    Map serviceMap = (Map) servicesWithinCategoryMap.get(service);
                    if (serviceMap.get("name").equals(serviceNameInitial)) {
                      found = true;
                    }
                  }
                  if(found){
                    servicesWithinCategoryMap.remove(serviceNameInitial);
                  }
                  if(!servicesWithinCategoryMap.isEmpty()){
                    servicesData.put(categoryNameInitial, servicesWithinCategoryMap);
                  }
                }

                db.collection("services").document("services").set(servicesData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Old Service Removed to Edit New One");
                            addNewService(findViewById(R.id.addNewServiceBtn), serviceNameInitial, priceNameInitial, categoryNameInitial, true);

                          }
                        });
              }
            });
  }

  public void onDeleteService(View deleteBtn) {

    LinearLayout serviceLayout = (LinearLayout) deleteBtn.getParent().getParent().getParent().getParent();

    RelativeLayout relativeLayout = (RelativeLayout) serviceLayout.getParent();

    int colorFrom = Color.BLACK;
    int colorTo = Color.RED;
    int duration = 1000;
    ObjectAnimator.ofObject(serviceLayout, "backgroundColor", new ArgbEvaluator(), colorFrom, colorTo)
            .setDuration(duration)
            .start();

    TextView serviceNameView = serviceLayout.findViewById(R.id.serviceName);
    TextView categoryNameView = serviceLayout.findViewById(R.id.category);
    TextView priceNameView = serviceLayout.findViewById(R.id.price);

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
    user.deleteService(new Service(serviceName,priceNameView.getText().toString(),categoryName));
  }
}
