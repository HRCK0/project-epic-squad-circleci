package ca.uottawa.mali165.epicclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class ClinicDescriptionActivity extends AppCompatActivity {

    private static final String TAG = "BookAppointmentActivity";

    public String rating;
    public String companyName;
    public String address;
    public String description;

    TextView companyNameView;
    TextView addressView;
    TextView descriptionView;

    RatingBar ratingBar;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_description);

        companyName = getIntent().getExtras().getString("companyName");

        db = FirebaseFirestore.getInstance();

        companyNameView = findViewById(R.id.clinicDescriptionName);
        addressView = findViewById(R.id.clinicDescriptionAddress);
        descriptionView = findViewById(R.id.clinicDescriptionInfo);

        ratingBar = findViewById(R.id.descriptionRatingBar);

        db.collection("users")
                .whereEqualTo("Name of Company", companyName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        QuerySnapshot snapshot = task.getResult();
                        List<DocumentSnapshot> data = snapshot.getDocuments();
                        Map company = (Map) data.get(0).getData();

                        rating = (String) company.get("rating");
                        address = (String) company.get("Address");
                        description = (String) company.get("Description");

                        companyNameView.setText(companyName);
                        addressView.setText(address);
                        descriptionView.setText(description);

                        //ratingBar.setRating(Float.parseFloat(rating));
                    }
                });

    }

    public void onClickRating(View ratingBtn){
        companyName = getIntent().getExtras().getString("companyName");
        Intent openServicesWindow = new Intent(getApplicationContext(), RatingsActivity.class);
        openServicesWindow.putExtra("companyName", companyName);
        openServicesWindow.putExtra("CurrentUser_UID", getIntent().getStringExtra("CurrentUser_UID"));
        startActivity(openServicesWindow);

    }

    public void onClickBooking(View bookingBtn){
        companyName = getIntent().getExtras().getString("companyName");
        Intent openServicesWindow = new Intent(getApplicationContext(), BookAppointmentActivity.class);
        openServicesWindow.putExtra("companyName", companyName);
        openServicesWindow.putExtra("CurrentUser_UID", getIntent().getStringExtra("CurrentUser_UID"));
        startActivity(openServicesWindow);

    }
}
