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
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.Espresso.onData;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)

//To ensure the backend is not clogged, please run all of ServiceActivityTest so the same service, is added, edited and then removed from the database
public class ServicesActivityTest
{
    @Rule
    public ActivityTestRule<ServicesActivity> myActivityTestRule = new ActivityTestRule<>(ServicesActivity.class);

    @Test
    public void A_testAddingService() throws InterruptedException
    {
        String name="Test";
        String price="42";
        String category="2";
        onView(withId(R.id.addNewServiceBtn)).perform(click()); //opens dialog button
        Thread.sleep(1000);
        onView((withHint("Name"))).perform(typeText(name));
        onView((withHint("Price"))).perform(typeText(price));
        onView((withHint("Category of Service"))).perform(typeText(category));
        onView(withText("Save")).perform(click());
        Thread.sleep(1000);
        // the UI only displays the serivce if the value was sucessfully added to the database --> so it is sufficient to just check the UI to validate both claims
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).onChildView(withId(R.id.serviceName)).check(matches(withText(name)));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).onChildView(withId(R.id.category)).check(matches(withText(category)));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).onChildView(withId(R.id.price)).check(matches(withText(price)));

        //to maintain the backend of the database

    }

    @Test
    public void B_testEditingService() throws InterruptedException
    {
        Thread.sleep(4000);
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).onChildView(withId(R.id.editBtn)).perform(click()); //edit the service at the top
        String name="Test-Edited";
        String price="50";
        String category="3";
        // onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).onChildView(withId(R.id.editBtn)).perform(click());
        Thread.sleep(2000);
        onView((withHint("Name"))).perform(clearText()).perform(typeText(name));
        onView((withHint("Price"))).perform(clearText()).perform(typeText(price));
        onView((withHint("Category of Service"))).perform(clearText()).perform(typeText(category));
        onView(withText("Save")).perform(click());
        Thread.sleep(1000);
        // the UI only displays the service if the value was sucessfully edited within the database --> so it is sufficient to just check the UI to validate both claims
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).onChildView(withId(R.id.serviceName)).check(matches(withText(name)));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).onChildView(withId(R.id.category)).check(matches(withText(category)));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).onChildView(withId(R.id.price)).check(matches(withText(price)));

    }
    @Test
    public void C_testDeletingService() throws InterruptedException
    {
        Thread.sleep(4000);
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).onChildView(withId(R.id.delBtn)).perform(click()); //delete the service at the top

        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).onChildView(withId(R.id.serviceName)).check(matches(not(withText("Test-Edited"))));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).onChildView(withId(R.id.category)).check(matches(not(withText("3"))));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).onChildView(withId(R.id.price)).check(matches(not(withText("50"))));
        Thread.sleep(2000);

    }
}