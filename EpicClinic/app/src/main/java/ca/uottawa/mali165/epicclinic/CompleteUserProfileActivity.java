package ca.uottawa.mali165.epicclinic;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class CompleteUserProfileActivity extends AppCompatActivity {

    EditText addressEditText, descriptionEditText, companyEditText;
    Button savedButton;
    RadioGroup licensedRadioGroup;
    FirebaseFirestore db;
    String employeeId;
    Employee currentEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_user_profile);
        addressEditText = findViewById(R.id.addressEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        companyEditText = findViewById(R.id.nameOfCompanyEditText);

        currentEmployee= getIntent().getExtras().getParcelable("employee");
        licensedRadioGroup = findViewById(R.id.radioGroupLicensed);
        savedButton = findViewById(R.id.saveButton);
        db = FirebaseFirestore.getInstance();

        employeeId = getIntent().getStringExtra("CurrentUser_UID");

        db.collection("users")
                .document(employeeId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String nameOfCompany=null;
                        String address=null;
                        String description=null;
                        boolean licensed=true;
                        Map employeeInfo = documentSnapshot.getData();
                        if (currentEmployee.getProfileCompleted() == true) {
                            //means all the information should be there
                            if (employeeInfo.containsKey("Name of Company")) {
                                nameOfCompany= (String) employeeInfo.get("Name of Company");
                            }

                            if (employeeInfo.containsKey("Address")) {
                                address= (String) employeeInfo.get("Address");
                            }


                            if (employeeInfo.containsKey("Description")) {
                                description= (String) employeeInfo.get("Description");
                            }

                            if (employeeInfo.containsKey("Licensed")) {
                                licensed = (boolean) employeeInfo.get("Licensed");
                            }
                            updateUserProfileUI(nameOfCompany,address,description,licensed);
                        }
                    }

                });
    }

    public void savedButtonClicked(View saveButton) {
        final String address = addressEditText.getText().toString();
        if (address.isEmpty()) {
            addressEditText.setError("Please enter an address"); //address is a mandatory field--> check
            addressEditText.requestFocus();


            final String description = descriptionEditText.getText().toString();
            final String nameOfCompany = companyEditText.getText().toString();
            final boolean licensed;

            int selectedId = licensedRadioGroup.getCheckedRadioButtonId();
            RadioButton btn = findViewById(selectedId);
            String valueofbutton = btn.getText().toString().toLowerCase();
            if (valueofbutton == "yes")
                licensed = true;
            else
                licensed = false;
            //TODO:
            // figure out how to get the info to the database
            db = FirebaseFirestore.getInstance();

            employeeId = getIntent().getStringExtra("CurrentUser_UID");

            db.collection("users")
                    .document(employeeId).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Map employeeData = documentSnapshot.getData();
                            if (currentEmployee.getProfileCompleted() == true) {

                                if (employeeData.containsKey("Name of Company")) {
                                    employeeData.remove("Name of Company");
                                }
                                if (employeeData.containsKey("Address")) {
                                    employeeData.remove("Address");
                                }
                                if (employeeData.containsKey("Description")) {
                                    employeeData.remove("Description");
                                }
                                if (employeeData.containsKey("Licensed")) {
                                    employeeData.remove("Licensed");
                                }


                            }

                            employeeData.put("Name of Company", nameOfCompany);
                            employeeData.put("Description", description);
                            employeeData.put("Address", address);
                            employeeData.put("Licensed", licensed);
                            currentEmployee.setProfileCompleted(true);
                            employeeData.put("profileCompleted", true);

                        }

                    });
        }
    }

    public void updateUserProfileUI(String nameOfCompany, String address, String description, boolean licensed)
    {
        addressEditText.setText(address);
        descriptionEditText.setText(description);
        companyEditText.setText(nameOfCompany);
        if(licensed==true)
        {
            licensedRadioGroup.check(R.id.radioButton);
        }

        if(licensed==false)
        {
            licensedRadioGroup.check(R.id.radioButton2);
        }


    }

    }

