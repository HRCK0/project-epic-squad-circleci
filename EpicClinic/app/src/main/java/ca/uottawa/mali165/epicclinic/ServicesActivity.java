package ca.uottawa.mali165.epicclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ServicesActivity extends AppCompatActivity {

    private static final String TAG = "ServicesActivity";

    EditText serviceNameEditText, priceEditText;
    Button addServiceButton;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    Map<String, Object> service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        serviceNameEditText = findViewById(R.id.newServiceNameEditText);
        priceEditText = findViewById(R.id.newServicePriceEditText);
        addServiceButton = findViewById(R.id.addServiceButton);

        //TODO:
        // populate services field with previously added services from the database
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
                            //TODO:
                            // add new service to app UI
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
}
