package ca.uottawa.mali165.epicclinic;

import ca.uottawa.mali165.epicclinic.R;
import ca.uottawa.mali165.epicclinic.Person;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";

    TextView welcomeTextView;

    Button profileBtn;
    Button servicesBtn;
    Button availabilityBtn;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    String role;
    String firstName;
    String lastName;
    String phoneNumber;
    String email;
    Person user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeTextView = findViewById(R.id.welcomeTextView);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        profileBtn = findViewById(R.id.profileBtn);
        servicesBtn = findViewById(R.id.servicesBtn);
        availabilityBtn = findViewById(R.id.availabilityBtn);

        DocumentReference dr = db.collection("users").document(getIntent().getStringExtra("uid"));
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        role = document.get("role").toString();
                        firstName = document.get("firstName").toString();
                        lastName = document.get("lastName").toString();
                        phoneNumber = document.get("phoneNumber").toString();
                        email = document.get("email").toString();


                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        String welcomeMessage = "Welcome " + firstName + " " + lastName + ". You are logged in as " + role.toUpperCase() + ".";
                        welcomeTextView.setText(welcomeMessage);

                        if (role.toLowerCase().equals("admin"))
                            System.out.println("hi");
                        if (role.toUpperCase().equals("EMPLOYEE")) {
                            boolean profileCompleted = (boolean) document.get("profileCompleted");
                            user = new Employee(firstName, lastName, email, phoneNumber, profileCompleted);

                            if (!profileCompleted) {
                                servicesBtn.setClickable(false);
                                servicesBtn.setBackgroundColor(Color.parseColor("#5F6967"));
                                availabilityBtn.setClickable(false);
                                availabilityBtn.setBackgroundColor(Color.parseColor("#5F6967"));
                            } else {

                            }
                        } else if (role.toUpperCase().equals("PATIENT") ) {
                            profileBtn.setVisibility(View.GONE);
                            availabilityBtn.setVisibility(View.GONE);
                            user = new Patient(firstName, lastName, email, phoneNumber);
                        } else if (role.toUpperCase().equals("ADMIN")){
                            profileBtn.setVisibility(View.GONE);
                            availabilityBtn.setVisibility(View.GONE);
                            user = new Admin(firstName, lastName, email, phoneNumber);
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void servicesBtnClicked(View servicesBtn) {
        if(role.toUpperCase().equals("ADMIN"))
        {
            Intent openServicesWindow = new Intent(getApplicationContext(), ServicesActivity.class);
            Admin adminUser = (Admin)user;
            openServicesWindow.putExtra("admin",adminUser); //passing admin object to services page
            startActivity(openServicesWindow);
        }
        else if (role.toUpperCase().equals("EMPLOYEE")) {
            //TODO:
            // rename this class because georges is fucking stupid
            Intent openServicesWindow = new Intent(getApplicationContext(), ServicesActivityNonAdmin.class);
            Employee employeeUser = (Employee) user;
            openServicesWindow.putExtra("employee", employeeUser);
            startActivity(openServicesWindow);
        }
        else
        {
            Intent openServicesWindow = new Intent(getApplicationContext(), ServicesActivityNonAdmin.class);
            startActivity(openServicesWindow);
        }

    }
    public void profileButtonClicked(View profileButtonClicked
    {
        Intent openCompleteProfileWindow = new Intent(getApplicationContext(), CompleteUserProfileActivity.class);
        Employee employeeUser = (Employee) user;
        openCompleteProfileWindow.putExtra("employee", employeeUser);
        startActivity(openCompleteProfileWindow);
    }

    public void availabilityBtnClicked(View availabilityBtn) {

        Intent openAvailabilityWindow = new Intent(getApplicationContext(), AvailabilityActivity.class);
        openAvailabilityWindow.putExtra("uid", getIntent().getStringArrayExtra("uid"));
        startActivity(openAvailabilityWindow);

    }
}
