package ca.uottawa.mali165.epicclinic;

import android.widget.TextView;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;

public class ServicesActivityTest {

    @Rule
    public ActivityTestRule<ServicesActivity>  myActivityTestRule = new ActivityTestRule((ServicesActivity.class));
    private ServicesActivity myActivity = null;
    private TextView text;
    private TextView text2;


    @Before
    public void setUp() throws Exception{
        myActivity = myActivityTestRule.getActivity();
    }

    @Test
    @UiThreadTest
    public void checkAddingService() throws Exception{
        text = myActivity.findViewById(R.id.newServiceNameEditText);
        text.setText("Hourly ");
        assertNotNull(myActivity.findViewById(R.id.emailEditText));
        String name = text.getText().toString();
        assertEquals("admin@ecare.com", name);

        text2 = myActivity.findViewById((R.id.passwordEditText));
        text2.setText("5T5ptQ");
        assertNotNull(myActivity.findViewById(R.id.passwordEditText));
        String pass = text2.getText().toString();
        assertEquals("5T5ptQ", pass);
    }


}
