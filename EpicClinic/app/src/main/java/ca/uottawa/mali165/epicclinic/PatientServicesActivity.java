package ca.uottawa.mali165.epicclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;
import java.util.List;

public class PatientServicesActivity extends AppCompatActivity {

    private final Context t = this;

    private static final String TAG = "PatientServicesActivity";

    List<Service> serviceList = new LinkedList<>();

    ListView listView;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Admin user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_services);
    }
}
