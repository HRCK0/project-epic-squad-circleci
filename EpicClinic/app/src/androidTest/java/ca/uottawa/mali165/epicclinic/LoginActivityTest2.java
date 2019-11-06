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


@RunWith(AndroidJUnit4.class)

public class LoginActivityTest2 {
    @Rule
    public ActivityTestRule<LoginActivity> myActivityTestRule = new ActivityTestRule<>(LoginActivity.class);
    public ActivityTestRule<WelcomeActivity> myActivityTestRule2 = new ActivityTestRule<>(WelcomeActivity.class);
    @Test
    public void adminIsInvalid(){
        onView(withId(R.id.emailEditText)).perform(typeText("admin@ecare.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(typeText("5T5ptQ"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        Intents.init();
        intended(hasComponent(WelcomeActivity.class.getName()));
        //onView(withText("Invalid Email")).check(matches(isDisplayed()));
    }
}
