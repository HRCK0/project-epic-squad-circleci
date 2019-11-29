package ca.uottawa.mali165.epicclinic;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class MakeRatingActivity  extends AppCompatActivity {

    private final Context t = this;

    private static final String TAG = "MakeRatingActivity";

    EditText raterNameEditText, commentEditText;

    RatingBar ratingBar;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Patient user;

    String companyName;

    Map<String,Object> ratingPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_rating);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        raterNameEditText = findViewById(R.id.raterNameEditText);
        commentEditText = findViewById(R.id.commentEditText);
        ratingBar = findViewById(R.id.ratingBar3);

        companyName = getIntent().getExtras().getString("companyName");
    }

    public void onClickPostButton(View postBtn){

        String raterName = raterNameEditText.getText().toString();
        String comment = commentEditText.getText().toString();
        float rating = ratingBar.getRating();

        ratingPost = new HashMap<>();
        ratingPost.put("Name", raterName);
        ratingPost.put("Rating", rating);
        ratingPost.put("Comment",comment);

        db.collection("users")
                .whereEqualTo("Name of Company", companyName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot QuerySnapshot) {
                        if (QuerySnapshot.isEmpty()) {
                            Log.d("QUERYSNAPSHOT","no documents found");
                        } else {
                            for (DocumentSnapshot document : QuerySnapshot) {
                                Log.d("SEARCHING FOR DOUCMENT", document.getId() + " => " + document.getData());
                                String id = document.getId();
                                final Map clinicData = document.getData();

                                if (!clinicData.containsKey("Ratings")){
                                    ArrayList<Map> ratings = new ArrayList<Map>();
                                    ratings.add(ratingPost);
                                    clinicData.put("Ratings",ratings);
                                }else {
                                    ArrayList<Map> ratings = (ArrayList<Map>) clinicData.remove("Ratings");
                                    ratings.add(ratingPost);
                                    clinicData.put("Ratings",ratings);
                                }
                                db.collection("users").document(id).set(clinicData);
                            }

                        }
                    }

                });
        finish();
    }

}
