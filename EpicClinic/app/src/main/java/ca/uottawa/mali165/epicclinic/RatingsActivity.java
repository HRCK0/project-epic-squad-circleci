package ca.uottawa.mali165.epicclinic;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class RatingsActivity  extends AppCompatActivity {

    ListView listView;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Employee clinic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);
        listView = (ListView) findViewById(R.id.listViewRating);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        clinic = getIntent().getExtras().getParcelable("clinic");
        updateUI();
    }

    public void updateUI() {


        final Activity t = this;

        // currently looks for the name of the company. will switch to email once i figure out how to find that object first
        // testing with hardcoded email
        db.collection("users").whereEqualTo("email", "employee@ecare.com").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot QuerySnapshot) {
                        if (QuerySnapshot.isEmpty()) {
                            Log.d("QUERYSNAPSHOT","no documents found");
                        } else {
                            for (DocumentSnapshot document : QuerySnapshot) {
                                Log.d("SEARCHING FOR DOUCMENT", document.getId() + " => " + document.getData());
                                final Map clinicData = document.getData();

                                if (!clinicData.containsKey("Ratings")){
                                    return;
                                }

                                ArrayList<Map> ratings = (ArrayList<Map>) clinicData.get("Ratings");
                                List ratingsList = new LinkedList();
                                Map ratingsMap = new HashMap();

                                for (int i = 0; i < ratings.size(); i++){
                                    ratingsMap = ratings.get(i);
                                    Rating ratingToAdd = new Rating(ratingsMap.get("Name").toString(),Float.parseFloat(ratingsMap.get("Rating").toString()), ratingsMap.get("Comment").toString());
                                    ratingsList.add(ratingToAdd);
                                }

                                //clinicData.remove("Rating");
                                RatingListViewAdapter ratingListViewAdapter = new RatingListViewAdapter(t, ratingsList);
                                listView.setAdapter(ratingListViewAdapter);
                            }
                        }
                    }

                });

    }
}



