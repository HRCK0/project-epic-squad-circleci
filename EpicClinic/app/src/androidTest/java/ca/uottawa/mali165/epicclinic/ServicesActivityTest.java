package ca.uottawa.mali165.epicclinic;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.test.espresso.intent.Intents;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import android.widget.ListView;
import android.widget.TextView;

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
import android.view.View;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.not;

import android.content.Intent;
import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;



import java.util.ArrayList;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder (MethodSorters.NAME_ASCENDING)

public class ServicesActivityTest
{
    @Rule
    public ActivityTestRule<ServicesActivity> myActivityTestRule = new ActivityTestRule<ServicesActivity>(ServicesActivity.class)
    {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent result = new Intent(targetContext, ServicesActivity.class); //intent of the service class
            result.putExtra("admin", new Admin("mock","mock","mock","mock")); //mock intent created so that service activity can be tested
            return result;
        }
    };


    //To avoid clogging the backend, please just run testAllServiceFunctions which calls all the other 3 espressoTests
    @Test
    public void testAllServiceFunctions()throws InterruptedException
    {
       testAddingService();
       testEditingService(); //allows the same value that was added to be modified
        testDeletingService();
    }


    @Test
    public void testAddingService() throws InterruptedException
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
        ListView serviceList = (ListView)myActivityTestRule.getActivity().findViewById(R.id.listView);

        View service;
        TextView nameAdded;
        TextView priceAdded;
        TextView categoryAdded;
        int positionStoredAt=-1;
        int rows =serviceList.getAdapter().getCount();
        for(int i=0;i<rows;i++)
        {
           service=serviceList.getAdapter().getView(i, null, null);
           nameAdded=(TextView)service.findViewById(R.id.serviceName);
           priceAdded=(TextView)service.findViewById(R.id.price);
           categoryAdded=(TextView)service.findViewById(R.id.category);
           String priceVal=priceAdded.getText().toString();
           String nameVal=nameAdded.getText().toString();
           String categoryVal=categoryAdded.getText().toString();
           if(priceVal.equals(price) && nameVal.equals(name) && categoryVal.equals(category) )
           {
               positionStoredAt=i;
               break;
           }



        }
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(positionStoredAt).onChildView(withId(R.id.serviceName)).check(matches(withText(name)));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(positionStoredAt).onChildView(withId(R.id.category)).check(matches(withText(category)));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(positionStoredAt).onChildView(withId(R.id.price)).check(matches(withText(price)));

        //Calling the other two tests within the first test




    }


    public void testEditingService() throws InterruptedException
    {

        ListView serviceList = (ListView)myActivityTestRule.getActivity().findViewById(R.id.listView);

        View service;
        TextView nameAdded;
        TextView priceAdded;
        TextView categoryAdded;
        int positionStoredAt=-1;
        int rows =serviceList.getAdapter().getCount();
        for(int i=0;i<rows;i++)
        {
            service=serviceList.getAdapter().getView(i, null, null);
            nameAdded=(TextView)service.findViewById(R.id.serviceName);
            priceAdded=(TextView)service.findViewById(R.id.price);
            categoryAdded=(TextView)service.findViewById(R.id.category);
            String priceVal=priceAdded.getText().toString();
            String nameVal=nameAdded.getText().toString();
            String categoryVal=categoryAdded.getText().toString();
            if(priceVal.equals("42") && nameVal.equals("Test") && categoryVal.equals("2") ) //edits the same test that was created
            {
                positionStoredAt=i;
                break;
            }



        }
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(positionStoredAt).onChildView(withId(R.id.editBtn)).perform(click()); //edit the service at the top
        String name="Test-Edited";
        String price="50";
        String category="3";
        // onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(0).onChildView(withId(R.id.editBtn)).perform(click());
        Thread.sleep(2000);
        onView((withHint("Name"))).perform(clearText()).perform(typeText(name));
        onView((withHint("Price"))).perform(clearText()).perform(typeText(price));
        onView((withHint("Category of Service"))).perform(clearText()).perform(typeText(category));
        onView(withText("Save")).perform(click());
        Thread.sleep(2000);
        // the UI only displays the service if the value was sucessfully edited within the database --> so it is sufficient to just check the UI to validate both claims

         rows =serviceList.getAdapter().getCount();
        for(int i=0;i<rows;i++)
        {
            service=serviceList.getAdapter().getView(i, null, null);
            nameAdded=(TextView)service.findViewById(R.id.serviceName);
            priceAdded=(TextView)service.findViewById(R.id.price);
            categoryAdded=(TextView)service.findViewById(R.id.category);
            String priceVal=priceAdded.getText().toString();
            String nameVal=nameAdded.getText().toString();
            String categoryVal=categoryAdded.getText().toString();
            if(priceVal.equals(price) && nameVal.equals(name) && categoryVal.equals(category) )
            {
                positionStoredAt=i;
                break;
            }



        }
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(positionStoredAt).onChildView(withId(R.id.serviceName)).check(matches(withText(name)));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(positionStoredAt).onChildView(withId(R.id.category)).check(matches(withText(category)));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(positionStoredAt).onChildView(withId(R.id.price)).check(matches(withText(price)));
        Thread.sleep(2000); //delay on the editing



    }
    @Test
    public void testDeletingService() throws InterruptedException

    {
        ListView serviceList = (ListView)myActivityTestRule.getActivity().findViewById(R.id.listView);

        View service;
        TextView nameAdded;
        TextView priceAdded;
        TextView categoryAdded;
        int positionStoredAt=-1;
        int rows =serviceList.getAdapter().getCount();
        for(int i=0;i<rows;i++)
        {
            service=serviceList.getAdapter().getView(i, null, null);
            nameAdded=(TextView)service.findViewById(R.id.serviceName);
            priceAdded=(TextView)service.findViewById(R.id.price);
            categoryAdded=(TextView)service.findViewById(R.id.category);
            String priceVal=priceAdded.getText().toString();
            String nameVal=nameAdded.getText().toString();
            String categoryVal=categoryAdded.getText().toString();
            if(priceVal.equals("50") && nameVal.equals("Test-Edited") && categoryVal.equals("3") ) //edits the same test that was created
            {
                positionStoredAt=i;
                break;
            }



        }

        Thread.sleep(4000);
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(positionStoredAt).onChildView(withId(R.id.delBtn)).perform(click()); //delete the service at the top
        Thread.sleep(2000);
        //value should not exist at that index anymore
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(positionStoredAt).onChildView(withId(R.id.serviceName)).check(matches(not(withText("Test-Edited"))));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(positionStoredAt).onChildView(withId(R.id.category)).check(matches(not(withText("3"))));
        onData(anything()).inAdapterView(withId(R.id.listView)).atPosition(positionStoredAt).onChildView(withId(R.id.price)).check(matches(not(withText("50"))));
        Thread.sleep(2000);

    }


}