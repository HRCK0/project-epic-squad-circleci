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
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringContains.containsString;

//DELETE THIS
import org.junit.runner.*;
import android.view.*;
import android.widget.*;


@RunWith(AndroidJUnit4.class)



public class LoginActivityTest2 {

    @Rule
    public ActivityTestRule<LoginActivity> myActivityTestRule = new ActivityTestRule<>(LoginActivity.class);
    public ActivityTestRule<WelcomeActivity> myActivityTestRule2 = new ActivityTestRule<>(WelcomeActivity.class);
    @Test
    public void adminIsInvalid() throws InterruptedException {
        onView(withId(R.id.emailEditText)).perform(typeText("admin@ecare.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(typeText("5T5ptQ"), closeSoftKeyboard());
        Intents.init();
        onView(withId(R.id.loginButton)).perform(click());
        Thread.sleep(1000);
        //intended(hasComponent(WelcomeActivity.class.getName()));
        //onView(withText("Invalid Email")).check(matches(isDisplayed()));
        onView(withId(R.id.welcomeTextView));
        onView(withId(R.id.welcomeTextView)).check(matches(isDisplayed()));
    }
}
