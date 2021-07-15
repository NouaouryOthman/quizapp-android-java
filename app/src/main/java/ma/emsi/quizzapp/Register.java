package ma.emsi.quizzapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private EditText etUser, etEmail, etPassword, etPasswordConfirm;
    private Button bRegister;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Users");
        etUser = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etMail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfirm);
        bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String username = etUser.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String passwordConfirm = etPasswordConfirm.getText().toString().trim();

                if (username.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Username can not be an empty field !", Toast.LENGTH_SHORT).show();
                    etUser.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Email can not be an empty field !", Toast.LENGTH_SHORT).show();
                    etEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getApplicationContext(), "Incorrect Email !", Toast.LENGTH_SHORT).show();
                    etEmail.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Password can not be an empty field !", Toast.LENGTH_SHORT).show();
                    etPassword.requestFocus();
                    return;
                }
                if (passwordConfirm.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please confirm password !", Toast.LENGTH_SHORT).show();
                    etPasswordConfirm.requestFocus();
                    return;
                }
                if (!password.equals(passwordConfirm)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password should have minimum 6 characters !", Toast.LENGTH_SHORT).show();
                    etPassword.requestFocus();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("username", username);
                            userMap.put("email", email);
                            reference.push().setValue(userMap);
                            Toast.makeText(getApplicationContext(), "Registration successful !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Register.this, Login.class));
                        } else
                            Toast.makeText(getApplicationContext(), "Something went wrong try later !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}