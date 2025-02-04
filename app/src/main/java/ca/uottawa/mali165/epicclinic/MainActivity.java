package ca.uottawa.mali165.epicclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import ca.uottawa.mali165.epicclinic.R;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void loginBtnClicked(View loginBtn){
        Intent openLoginWindow = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(openLoginWindow);
    }

    public void registerBtnClicked(View registerBtn){
        Intent openRegisterBtn = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(openRegisterBtn);
    }


}
