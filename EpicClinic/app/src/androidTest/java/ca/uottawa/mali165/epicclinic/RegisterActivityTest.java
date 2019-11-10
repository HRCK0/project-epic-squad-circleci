package ca.uottawa.mali165.epicclinic;


import android.app.Activity;

import androidx.test.espresso.intent.Intents;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringContains.containsString;


import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


@RunWith(AndroidJUnit4.class)

public class RegisterActivityTest {

    @Rule
    public ActivityTestRule<RegisterActivity> myActivityTestRule = new ActivityTestRule<>(RegisterActivity.class);

    @Test
    public void patientCreation() throws InterruptedException {
        onView(withId(R.id.firstNameEditText)).perform(typeText("Test"), closeSoftKeyboard());
        onView(withId(R.id.lastNameEditText)).perform(typeText("Patient"), closeSoftKeyboard());
        onView(withId(R.id.phoneNumberEditText)).perform(typeText("6131234567"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(typeText("12345"), closeSoftKeyboard());
        onView(withId(R.id.patientButton)).perform(click());
        Thread.sleep(4000);
        onView(withId(R.id.welcomeTextView)).check(matches(withText(containsString("You are logged in as PATIENT")))); //welcome message is displayed
        Thread.sleep(4000);
        //to ensure the backend doesn't get clogged --> the account is deleted from the database
       /* String uid = mAuth.getCurrentUser().getUid());
        DatabaseReference dR = FireBaseDatabase.getInstance().getReference("users")*/

    }
}
