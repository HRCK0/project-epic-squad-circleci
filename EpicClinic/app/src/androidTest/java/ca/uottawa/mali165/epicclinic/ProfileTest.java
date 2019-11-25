package ca.uottawa.mali165.epicclinic;

import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.FirestoreGrpc;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;
import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringContains.containsString;


@RunWith(AndroidJUnit4.class)
public class ProfileTest {

    @Rule
    public ActivityTestRule<LoginActivity> myActivtiyTestRule = new ActivityTestRule<>(LoginActivity.class);

   /* @Test
    public void checkUserProfile() throws InterruptedException
    {
        //runs both espresso tests, please call this method to run the code!
        loginAndCheckFirebase();
        Thread.sleep(2000);
        editAndCheckFirebase();
    }*/


    @Test
    public void loginAndCheckFirebase() throws InterruptedException {

        onView(withId(R.id.emailEditText)).perform(clearText()).perform(typeText("gabriel@ecare.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(clearText()).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.profileBtn)).perform(click());
        Thread.sleep(3000);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        db.collection("users").document(auth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Map data = (Map) documentSnapshot.getData();

                        onView(withId(R.id.phoneNumberEditText)).check(matches(withText(containsString((String) data.get("phoneNumber")))));
                        onView(withId(R.id.addressEditText)).check(matches(withText(containsString((String) data.get("Address")))));
                        onView(withId(R.id.descriptionEditText)).check(matches(withText(containsString((String) data.get("Description")))));
                        onView(withId(R.id.nameOfCompanyEditText)).check(matches(withText(containsString((String) data.get("Name Of Company")))));

                        db.terminate();
                    }
                });
    }

    @Test
    public void editAndCheckFirebase() throws InterruptedException {


       onView(withId(R.id.emailEditText)).perform(clearText()).perform(typeText("gabriel@ecare.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(clearText()).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click()); //changes activity*/
        Thread.sleep(2000);
        onView(withId(R.id.profileBtn)).perform(click()); //changes activity again


        Thread.sleep(3000);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        Random random = new Random();

        final String phoneNumber = "647555555" + random.nextInt(10);
        final String address = "TESTING ADDRESS" + random.nextInt(10);
        final String description = "TESTING DESCRIPTION" + random.nextInt(10);
        final String company = "TESTING COMPANY" + random.nextInt(10);

        Thread.sleep(2000);



        onView(withId(R.id.addressEditText)).perform(clearText()).perform(typeText(address), closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());
        onView(withId(R.id.descriptionEditText)).perform(clearText()).perform(typeText(description), closeSoftKeyboard());
        onView(withId(R.id.nameOfCompanyEditText)).perform(clearText()).perform(typeText(company), closeSoftKeyboard());
        Thread.sleep(2000);
        onView(withId(R.id.phoneNumberEditText)).perform(clearText()).perform(typeText(phoneNumber), closeSoftKeyboard());

        Thread.sleep(3000);
        onView(withId(R.id.saveButton)).perform(click());
        Thread.sleep(2000);


        db.collection("users").document(auth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Map data = (Map) documentSnapshot.getData();

                        onView(withId(R.id.phoneNumberEditText)).check(matches(withText(containsString((String) data.get("phoneNumber")))));
                        onView(withId(R.id.addressEditText)).check(matches(withText(containsString((String) data.get("Address")))));
                        onView(withId(R.id.descriptionEditText)).check(matches(withText(containsString((String) data.get("Description")))));
                        onView(withId(R.id.nameOfCompanyEditText)).check(matches(withText(containsString((String) data.get("Name Of Company")))));
                        return;

                    }
                });

    }

}
