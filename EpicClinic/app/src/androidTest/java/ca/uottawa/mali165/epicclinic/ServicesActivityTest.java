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
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.Espresso.onData;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.StringEndsWith.endsWith;
//import static androidx.test.espresso.matcher.RootMatchers.isDialog;
//import static androidx.test.espresso.matcher.RootMatchers.inRoot;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static org.hamcrest.Matchers.inRoot;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)

public class ServicesActivityTest
{
    @Rule
    public ActivityTestRule<ServicesActivity> myActivityTestRule = new ActivityTestRule<>(ServicesActivity.class);

    @Test
    public void testAddingService() throws InterruptedException
    {
        onView(withId(R.id.addNewServiceBtn)).perform(click()); //opens dialog button
        Thread.sleep(1000);
        onView((withHint("Name"))).perform(typeText("Test"));
        onView((withHint("Price"))).perform(typeText("42"));
        onView((withHint("Category of Service"))).perform(typeText("2"));
        onView(withText("Save")).perform(click());
        Thread.sleep(1000);
        // the UI only displays the serivce if the value was sucessfully added to the database --> so it is sufficient to just check the UI to validate both claims
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).onChildView(withId(R.id.serviceName)).check(matches(withText("Test")));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).onChildView(withId(R.id.category)).check(matches(withText("2")));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).onChildView(withId(R.id.price)).check(matches(withText("42")));

        //to maintain the backend of the database

    }

    public void testEditingService() throws InterruptedException
    {

    }
}