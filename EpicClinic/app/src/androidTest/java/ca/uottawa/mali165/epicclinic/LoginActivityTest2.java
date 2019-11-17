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
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringContains.containsString;


@RunWith(AndroidJUnit4.class)


public class LoginActivityTest2 {

    @Rule
    public ActivityTestRule<LoginActivity> myActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void adminIsInvalid() throws InterruptedException {
        onView(withId(R.id.emailEditText)).perform(clearText()).perform(typeText("admin@ecare.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(clearText()).perform(typeText("5T5ptQ"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        Thread.sleep(4000);
        onView(withId(R.id.welcomeTextView));
        onView(withId(R.id.welcomeTextView)).check(matches(withText(containsString("You are logged in as ADMIN")))); //welcome message is displayed

    }



    @Test
    public void employeeIsInvalid() throws InterruptedException {
        onView(withId(R.id.emailEditText)).perform(clearText()).perform(typeText("rdeal081@uottawa.ca"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(clearText()).perform(typeText("seg2105"), closeSoftKeyboard());

        onView(withId(R.id.loginButton)).perform(click());
        Thread.sleep(4000);
        onView(withId(R.id.welcomeTextView));
        onView(withId(R.id.welcomeTextView)).check(matches(withText(containsString("You are logged in as EMPLOYEE")))); //welcome message is displayed
    }

    @Test
    public void patientIsInvalid() throws InterruptedException {
        onView(withId(R.id.emailEditText)).perform(clearText()).perform(typeText("gabriel@ecare.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(clearText()).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        Thread.sleep(4000);
        onView(withId(R.id.welcomeTextView));
        onView(withId(R.id.welcomeTextView)).check(matches(withText(containsString("You are logged in as PATIENT")))); //welcome message is displayed
    }
}
