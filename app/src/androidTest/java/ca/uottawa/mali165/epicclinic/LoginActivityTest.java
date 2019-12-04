package ca.uottawa.mali165.epicclinic;


import android.widget.TextView;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;



public class LoginActivityTest {
    @Rule

    public ActivityTestRule<LoginActivity>  myActivityTestRule = new ActivityTestRule((LoginActivity.class));
    private LoginActivity myActivity = null;
    private TextView text;
    private TextView text2;


    @Before
    public void setUp() throws Exception{
        myActivity = myActivityTestRule.getActivity();
    }

    @Test
    @UiThreadTest

    public void checkLoginAdmin() throws Exception{
        text = myActivity.findViewById(R.id.emailEditText);
        text.setText("admin@ecare.com");
        assertNotNull(myActivity.findViewById(R.id.emailEditText));
        String name = text.getText().toString();
        assertEquals("admin@ecare.com", name);

        text2 = myActivity.findViewById((R.id.passwordEditText));
        text2.setText("5T5ptQ");
        assertNotNull(myActivity.findViewById(R.id.passwordEditText));
        String pass = text2.getText().toString();
        assertEquals("5T5ptQ", pass);
    }


    @Test
    @UiThreadTest

    public void checkLoginEmployee() throws Exception{
        text = myActivity.findViewById(R.id.emailEditText);
        text.setText("rdeal081@uottawa.ca");
        assertNotNull(myActivity.findViewById(R.id.emailEditText));
        String name = text.getText().toString();
        assertEquals("rdeal081@uottawa.ca", name);

        text2 = myActivity.findViewById((R.id.passwordEditText));
        text2.setText("seg2105");
        assertNotNull(myActivity.findViewById(R.id.passwordEditText));
        String pass = text2.getText().toString();
        assertEquals("seg2105", pass);
    }


    @Test
    @UiThreadTest

    public void checkLoginPatient() throws Exception{
        text = myActivity.findViewById(R.id.emailEditText);
        text.setText("gabriel@ecare.com");
        assertNotNull(myActivity.findViewById(R.id.emailEditText));
        String name = text.getText().toString();
        assertEquals("gabriel@ecare.com", name);

        text2 = myActivity.findViewById((R.id.passwordEditText));
        text2.setText("password");
        assertNotNull(myActivity.findViewById(R.id.passwordEditText));
        String pass = text2.getText().toString();
        assertEquals("password", pass);
    }
}
