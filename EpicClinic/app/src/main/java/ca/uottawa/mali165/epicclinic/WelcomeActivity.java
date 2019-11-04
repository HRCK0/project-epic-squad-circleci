package ca.uottawa.mali165.epicclinic;

import ca.uottawa.mali165.epicclinic.R;
import ca.uottawa.mali165.epicclinic.Person;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


        DocumentReference dr = db.collection("users").document(getIntent().getStringExtra("CurrentUser_UID"));
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

                        if (role == "employee") {
                            user = new Employee(firstName, lastName, email, phoneNumber);
                        } else if (role == "patient") {
                            user = new Patient(firstName, lastName, email, phoneNumber);
                        } else if (role == "admin"){
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
        Intent openServicesWindow = new Intent(getApplicationContext(), ServicesActivity.class);
        startActivity(openServicesWindow);
    }
}
