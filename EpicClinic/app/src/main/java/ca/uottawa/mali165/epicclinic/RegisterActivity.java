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

import ca.uottawa.mali165.epicclinic.R;

public class RegisterActivity extends AppCompatActivity {

    EditText firstNameEditText, lastNameEditText, emailEditText, phoneNumberEditText, passwordEditText;
    Button registerButton;
    RadioGroup radioGroup;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    Map<String, Object> user;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        radioGroup = findViewById(R.id.radioGroup);
        registerButton = findViewById(R.id.registerBtn);
    }

    public void RegisterBtnClicked(View loginBtn){

        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton btn = findViewById(selectedId);
        String role = btn.getText().toString().toLowerCase();

        user = new HashMap<>();
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("email", email);
        user.put("phoneNumber", phoneNumber);
        user.put("role", role);

        if (email.isEmpty()) {
            emailEditText.setError("Please enter an email");
            emailEditText.requestFocus();
        } else if (password.isEmpty()) {
            passwordEditText.setError("Please enter a password");
            passwordEditText.requestFocus();
        } else if (password.isEmpty() && email.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Required Fields are Empty!", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // task is successful, do whatever you want with it

                                db.collection("users").document(mAuth.getUid())
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Document has successfully been written");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Document error", e);
                                            }
                                        });
                                Log.d(TAG, "createUserWithEmail:success");
                                Intent openWelcomeWindow = new Intent(getApplicationContext(), WelcomeActivity.class);
                                startActivity(openWelcomeWindow);
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                // sign in/up fails, display message to user
                                Toast.makeText(RegisterActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
