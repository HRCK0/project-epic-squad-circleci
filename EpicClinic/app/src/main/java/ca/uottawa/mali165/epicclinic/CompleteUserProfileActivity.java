package ca.uottawa.mali165.epicclinic;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class CompleteUserProfileActivity extends AppCompatActivity {

    EditText addressEditText, descriptionEditText, companyEditText, phoneNumberEditText;
    Button savedButton;
    RadioGroup licensedRadioGroup;
    FirebaseFirestore db;
    String employeeId;
    Employee currentEmployee;
    private static final String TAG = "UserProfileActivity";

    public void showToast(String textToShow){
        Toast.makeText(CompleteUserProfileActivity.this, textToShow, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_user_profile);
        addressEditText = findViewById(R.id.addressEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        companyEditText = findViewById(R.id.nameOfCompanyEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);

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
                        String phoneNumber=null;
                        boolean licensed=true;
                        boolean completed=false;
                        Map employeeInfo = documentSnapshot.getData();

                        if (employeeInfo.containsKey("phoneNumber")) {
                            phoneNumber = (String)employeeInfo.get("phoneNumber");
                            updateUserProfileUI(nameOfCompany,address,description,licensed,phoneNumber);
                        }

                        if(employeeInfo.containsKey("profileCompleted")) {
                            completed = (boolean) employeeInfo.get("profileCompleted");

                        }
                            //means all the information should be there
                            if(completed==true)
                            {

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

                            updateUserProfileUI(nameOfCompany,address,description,licensed,phoneNumber);

                        }
                    }

                });
    }

    public void savedButtonClicked(View saveButton) {
        final String address = addressEditText.getText().toString();
        final String phoneNumber = phoneNumberEditText.getText().toString();
        final String nameOfCompany = companyEditText.getText().toString();
        if (address.isEmpty()) {
            addressEditText.setError("Please enter an address"); //address is a mandatory field--> check
            addressEditText.requestFocus();
        }
        //phone number is also mandatory

        else if (phoneNumber.isEmpty()) {
            phoneNumberEditText.setError("Please enter a phone number"); //phone number is a mandatory field--> check
            phoneNumberEditText.requestFocus();

        }
        else if(nameOfCompany.isEmpty())
        {
            companyEditText.setError("Please enter the name of the company"); //phone number is a mandatory field--> check
            companyEditText.requestFocus();
        }
        else {

            final String description = descriptionEditText.getText().toString();
            final boolean licensed;

            int selectedId = licensedRadioGroup.getCheckedRadioButtonId();
            RadioButton btn = findViewById(selectedId);
            String valueofbutton = btn.getText().toString().toLowerCase();
            if (valueofbutton == "Yes")
                licensed = true;
            else
                licensed = false;
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
                                if (employeeData.containsKey("phoneNumber")) {
                                    employeeData.remove("phoneNumber");
                                }
                                if (employeeData.containsKey("profileCompleted")) {
                                    employeeData.remove("profileCompleted");
                                }


                            }
                            Log.d(TAG, "hellooooooo"); //delete
                            employeeData.put("Name of Company", nameOfCompany);
                            employeeData.put("Description", description);
                            employeeData.put("Address", address);
                            employeeData.put("Licensed", licensed);
                            employeeData.put("phoneNumber", phoneNumber);
                            employeeData.put("profileCompleted", true);
                            currentEmployee.setProfileCompleted(true);
                            db.collection("users").document(employeeId).set(employeeData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "User Profile Updated");
                                        }
                                    });
                            showToast("User Profile Updated");


                        }

                    });
        }
    }



    public void updateUserProfileUI(String nameOfCompany, String address, String description, boolean licensed, String phoneNumber)
    {
        if(nameOfCompany==null)
        {
            nameOfCompany="";
        }
        if(address==null)
        {
            address="";
        }
        if(description==null)
        {
            description="";
        }

        if(phoneNumber==null)
        {
            phoneNumber="";
        }
        addressEditText.setText(address);
        descriptionEditText.setText(description);
        companyEditText.setText(nameOfCompany);
        phoneNumberEditText.setText(phoneNumber);
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

