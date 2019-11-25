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
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Collections;
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

  /**
   * Will create an alert dialog where a user can input details about the new service being added
   * The editBtnClicked param is false because this method is for adding not editing
   *
   * @param newServiceBtn : the add button that was clicked
   */
  public void onClickAddNewService(View newServiceBtn) {

    createAlertDialogToAddOrEditService("", "", "", "",false);

  }

  /**
   * Will create an alert dialog where a user can change the details about the service they want to edit
   * The editBtnClicked param is true as this method is editing a service
   *
   * @param editServiceBtn : the edit button that was clicked
   */
  public void onClickEditService(View editServiceBtn) {

    //Using the edit button that was clicked to get the other textfields relative to it
    //specifcially text in service name field, category field and price field

    LinearLayout serviceLayout = (LinearLayout) editServiceBtn.getParent().getParent().getParent().getParent();
    TextView serviceNameView = serviceLayout.findViewById(R.id.serviceName);
    TextView categoryNameView = serviceLayout.findViewById(R.id.category);
    TextView priceNameView = serviceLayout.findViewById(R.id.price);
    final String serviceNameInitial = serviceNameView.getText().toString();
    final String categoryNameInitial = categoryNameView.getText().toString();
    final String priceNameInitial = priceNameView.getText().toString();

    db.collection("services").document("services").get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map servicesData = documentSnapshot.getData();
                Map categoryData = (Map) servicesData.get(categoryNameInitial);
                Map serviceWithinCategoryData = (Map) categoryData.get(serviceNameInitial);
                createAlertDialogToAddOrEditService(serviceNameInitial, priceNameInitial, categoryNameInitial, serviceWithinCategoryData.get("role").toString(), true);
              }
            });


  }

  /**
   * Method called when del buttn clicked and animates the deletion and delete the service from db and UI
   * @param delServiceBtn : the button clicked to delete a given service
   */
  public void onClickDeleteService(View delServiceBtn){

    //Using the delete button that was clicked to get the other textfields relative to it
    //specifcially text in service name field, category field and price field
    LinearLayout serviceLayout = (LinearLayout) delServiceBtn.getParent().getParent().getParent().getParent();
    TextView serviceNameView = serviceLayout.findViewById(R.id.serviceName);
    TextView categoryNameView = serviceLayout.findViewById(R.id.category);
    TextView priceView = serviceLayout.findViewById(R.id.price);
    final String serviceName = serviceNameView.getText().toString();
    final String categoryName = categoryNameView.getText().toString();
    final String price = priceView.getText().toString();

    //Animating the service from black to red
    int colorFrom = Color.BLACK;
    int colorTo = Color.RED;
    int duration = 1000;
    ObjectAnimator.ofObject(serviceLayout, "backgroundColor", new ArgbEvaluator(), colorFrom, colorTo)
            .setDuration(duration)
            .start();

    deleteService(serviceName, price, categoryName, null, null, null, null, null);

  }

  /**
   * Creates a popup at the bottom of the screen for a quick message to the user
   *
   * @param textToShow : message to show to user
   */
  private void showToast(String textToShow) {
    Toast.makeText(ServicesActivity.this, textToShow, Toast.LENGTH_SHORT).show();
  }

  /**
   * Will validate that the text input fields within the dialog box are not empty and if they are, will display on screen
   *
   * @param serviceName : the service name field
   * @param price       : the price field
   * @param category    : the category field
   * @return : a boolean thats true if the fields in the alert dialog are not empty
   */
  private boolean validateDialogFieldsAndDisplayIfEmpty(EditText serviceName, EditText price, EditText category, EditText role) {

    boolean serviceNameEmpty = serviceName.getText().toString().isEmpty();
    boolean priceEmpty = price.getText().toString().isEmpty();
    boolean categoryEmpty = category.getText().toString().isEmpty();
    boolean roleEmpty = role.getText().toString().isEmpty();

    if (!serviceNameEmpty && !priceEmpty && !categoryEmpty && !roleEmpty) {
      return true;
    }

    if (serviceNameEmpty && !priceEmpty && !categoryEmpty && !roleEmpty) {
      serviceName.setError("Please fill service name");
      serviceName.requestFocus();
    } else if (priceEmpty && !serviceNameEmpty && !categoryEmpty && !roleEmpty) {
      price.setError("Please fill price field");
      price.requestFocus();
    } else if (categoryEmpty && !serviceNameEmpty && !priceEmpty && !roleEmpty) {
      category.setError("Please fill category field");
      category.requestFocus();
    } else if(!categoryEmpty && !serviceNameEmpty && !priceEmpty && roleEmpty){
      role.setError("Please fill role field");
      role.requestFocus();
    }else {
      serviceName.setError("Please fill service name");
      serviceName.requestFocus();
      price.setError("Please fill price field");
      price.requestFocus();
      category.setError("Please fill category field");
      category.requestFocus();
      role.setError("Please fill role field");
      role.requestFocus();
    }

    return false;

  }

  /**
   * Will create a alert dialog that allows entry of service name, price name and category
   * Will call edit/add methods depending on of editBtnClicked passed is null or not
   *
   * @param serviceNameInitial : the initial name to be in the service name field
   * @param priceNameInitial   : the initial name ot be in the price name field
   * @param categoryInitial    : the initial name to be in the category field
   * @param roleInitial        : the inital value to be put into the role field
   * @param editBtnClicked     : if a service was being edited, this would true otherwise false
   */
  public void createAlertDialogToAddOrEditService(final String serviceNameInitial, final String priceNameInitial, final String categoryInitial, final String roleInitial, final boolean editBtnClicked) {

    //building dialog below
    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    String title = editBtnClicked ? "Editing Service" : "Adding a new Service";
    builder.setTitle(title);

    //linear layout for dialog
    LinearLayout layout = new LinearLayout(this);
    layout.setOrientation(LinearLayout.VERTICAL);

    //creating text fields inside dialog and setting initial values
    final EditText serviceNameInput = new EditText(this);
    serviceNameInput.setHint("Name");
    serviceNameInput.setText(serviceNameInitial);
    serviceNameInput.setText(serviceNameInitial);
    final EditText servicePriceInput = new EditText(this);
    servicePriceInput.setHint("Price");
    servicePriceInput.setText(priceNameInitial);
    servicePriceInput.setText(priceNameInitial);
    final EditText categoryInput = new EditText(this);
    categoryInput.setHint("Category of Service");
    categoryInput.setText(categoryInitial);
    categoryInput.setText(categoryInitial);
    final EditText roleInput = new EditText(this);
    roleInput.setHint("Role (Doctor, Nurse, etc)");
    roleInput.setText(roleInitial);
    roleInput.setText(roleInitial);


    //setting input types of text fields
    serviceNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
    servicePriceInput.setInputType(InputType.TYPE_CLASS_NUMBER);
    categoryInput.setInputType(InputType.TYPE_CLASS_TEXT);
    roleInput.setInputType(InputType.TYPE_CLASS_TEXT);

    //adding text fields to linear layout and then to builder
    layout.addView(serviceNameInput);
    layout.addView(servicePriceInput);
    layout.addView(categoryInput);
    layout.addView(roleInput);

    builder.setView(layout);

    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        //being overwritten below to precisely control when to close dialog
      }
    });

    //cancel button
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        showToast("Cancelled dialog");
        dialog.dismiss();
      }
    });

    final AlertDialog dialog = builder.create();
    dialog.show();

    //This is to overwrte the previos positive button onclick and precisely control when to accept the okay click
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        final String newServiceName = serviceNameInput.getText().toString();
        final String newPrice = servicePriceInput.getText().toString();
        final String newCategoryName = categoryInput.getText().toString();
        final String newRole = roleInput.getText().toString();

        if (validateDialogFieldsAndDisplayIfEmpty(serviceNameInput, servicePriceInput, categoryInput, roleInput)) { //validate the fields
          if (editBtnClicked) { //being edited
            editService(serviceNameInitial, priceNameInitial, categoryInitial, roleInitial, newServiceName, newPrice, newCategoryName, newRole, dialog); // will close dialog by itself if successfuly added
          } else { //new service being added
            addService(newServiceName, newPrice, newCategoryName, newRole, dialog); //will close dialog if successfuly edited
          }
        }
      }
    });

  }

  /**
   * Will add a service to the database (if it does not already exist) AND updates the UI accordingly
   * Needs to be passed a dialog in which the data was entered
   *
   * @param serviceName  : name of service to be added
   * @param servicePrice : price of service to be added
   * @param categoryName : category name of service
   * @param dialog : the alert dialog from which the service data was entered
   */
  public void addService(final String serviceName, final String servicePrice, final String categoryName, final String roleName, final AlertDialog dialog) {
    final HashMap serviceToAdd = new HashMap();
    serviceToAdd.put("name", serviceName);
    serviceToAdd.put("price", servicePrice);
    serviceToAdd.put("role", roleName);

    db.collection("services").document("services").get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map servicesData = documentSnapshot.getData();

                //if category already exists
                if (servicesData.containsKey(categoryName)) {
                  Map servicesWithinCategoryMap = (Map) servicesData.remove(categoryName);
                  Map serviceMap = (Map) servicesWithinCategoryMap.get(serviceName);

                  //verifying that the service name within same category does not already exist
                  if (serviceMap != null) {
                    showToast("Service Name Already Exists for Category");
                    return;
                  }

                  servicesWithinCategoryMap.put(serviceName, serviceToAdd);
                  servicesData.put(categoryName, servicesWithinCategoryMap);

                  //reinserting the whole service data back into the services document with updated category info
                  db.collection("services").document("services").set(servicesData)
                          .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                              Log.d(TAG, "New Service with old category succesfully Created");
                              dialog.dismiss();
                              updateUI();
                            }
                          });

                } else { //category does not already exist

                  Map categoryData = new HashMap();
                  categoryData.put(serviceName, serviceToAdd);
                  servicesData.put(categoryName, categoryData);

                  //inserting a whole new category with the new service into db
                  db.collection("services").document("services").set(servicesData)
                          .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                              Log.d(TAG, "New service with new category successfuly created");
                              dialog.dismiss();
                              updateUI();
                            }
                          });

                }
              }
            });
    user.addService(new Service(serviceName,servicePrice,categoryName));
  }

  /**
   * Method will update a service in the db and the UI with new details as specified by the params
   * Needs to be passed dialog in which the service data was edited
   *
   * @param serviceNameInitial : the initial vale of the service name being being edited
   * @param priceNameInitial : the initial value of the price before editing
   * @param categoryNameInitial : the inital value of the category being editing
   * @param newServiceName  : the edited name of the service
   * @param newCategoryName : the edited category name
   * @param newPrice        : edited price name
   * @param dialog          : the dialog in which the service data was edited
   */
  public void editService(final String serviceNameInitial, final String priceNameInitial, final String categoryNameInitial, final String roleNameInitial, final String newServiceName, final String newPrice, final String newCategoryName, final String newRoleName, final AlertDialog dialog) {

    db.collection("services")
            .document("services").get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map servicesData = documentSnapshot.getData();
                boolean alreadyExists = false;

                if(!serviceNameInitial.equals(newServiceName) || !categoryNameInitial.equals(newCategoryName)){
                  //below code verifes that service does not already exist
                  if (servicesData.containsKey(newCategoryName)) {
                    Map servicesWithinNewCategoryMap = (Map) servicesData.get(newCategoryName);

                    //to verify that service name within a preexisting category does not already exist
                    if (servicesWithinNewCategoryMap.get(newServiceName) != null) {
                      showToast("Service already exists");
                      alreadyExists = true;
                    }
                  }
                }else{
                  alreadyExists=false;
                }



                //add new service with the edited data and remove old one
                //the below delete service is special method as it will also call the add method within it to add the updated service
                if (!alreadyExists) {
                  deleteService(serviceNameInitial, priceNameInitial, categoryNameInitial, newServiceName, newPrice, newCategoryName, newRoleName, dialog);
                }
                //no need to update ui as above methods already do so

              }
            });

  }

  /**
   * Method that deletes a service from the db and updates UI
   * @param serviceName : The name of the service that is being deleted
   * @param price : the price of the service being deleted
   * @param categoryName : the name of the category for the service being deleted
   * All the new params and the dialog are if the service being deleted is to be replaced by another one (meanig this method called from edit), null if its not
   */
  public void deleteService(final String serviceName, final String price, final String categoryName, final String newServiceName, final String newPrice, final String newCategory, final String newRoleName, final AlertDialog dialog) {

    db.collection("services")
            .document("services").get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {

                Map servicesData = (Map) documentSnapshot.getData();
                Map categoryData = (Map) servicesData.remove(categoryName);

                categoryData.remove(serviceName);

                //this is in case no more services within a category
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
                            if(newCategory!=null && newPrice!=null && newServiceName!=null){
                              addService(newServiceName, newPrice, newCategory, newRoleName, dialog);
                            }
                          }
                        });

              }
            });
    user.deleteService(new Service(serviceName, price, categoryName));

  }

  /**
   * Method that will query the database for all of the services and display them in the UI
   */
  public void updateUI() {

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

                Collections.sort(serviceList);

                ServicesListViewAdapter servicesListViewAdapter = new ServicesListViewAdapter(t, serviceList);
                listView.setAdapter(servicesListViewAdapter);

              }
            });

  }
}
