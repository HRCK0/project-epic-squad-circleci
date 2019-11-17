package ca.uottawa.mali165.epicclinic;

import androidx.appcompat.app.AppCompatActivity;
package ca.uottawa.mali165.epicclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;

public class CompleteUserProfileActivity extends AppCompatActivity {

    EditText addressEditText,descriptionEditText, companyEditText;
    Button savedButton;
    RadioGroup licensedRadioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_user_profile);
        addressEditText = findViewById(R.id.addressEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        companyEditText = findViewById(R.id.nameOfCompanyEditText);


        licensedRadioGroup = findViewById(R.id.RadioGroupLicensed);
        savedButton = findViewById(R.id.saveButton);
    }

    public void savedButtonClicked(View saveButton)
    {
        String address= addressEditText.getText().toString();
        if (address.isEmpty()) {
            addressEditText.setError("Please enter an email"); //address is a mandatory field
            addressEditText.requestFocus();

        String description= descriptionEditText.getText().toString();
        String company= companyEditText.getText().toString();
        boolean licensed;

        int selectedId = licensedRadioGroup.getCheckedRadioButtonId();
        RadioButton btn = findViewById(selectedId);
        String valueofbutton = btn.getText().toString().toLowerCase();
        if(valueofbutton=="yes")
            licensed=true;
        else
            licensed=false;


    }


}
