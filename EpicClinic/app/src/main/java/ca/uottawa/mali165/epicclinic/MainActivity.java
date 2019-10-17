package ca.uottawa.mali165.epicclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

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
