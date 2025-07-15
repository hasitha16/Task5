package com.example.task2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignupActivity extends AppCompatActivity {

    EditText usernameField, passwordField, confirmPasswordField;
    Button submitButton;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.create_p);
        confirmPasswordField = findViewById(R.id.confirm_p);
        submitButton = findViewById(R.id.sub_btn);

        auth = FirebaseAuth.getInstance();

        submitButton.setOnClickListener(view -> {
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String confirmPassword = confirmPasswordField.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                auth.createUserWithEmailAndPassword(username, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String uid = auth.getCurrentUser().getUid();

                                // Prepare user object to save
                                User user = new User(username, username);

                                // Save to DB
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(uid)
                                        .setValue(user)
                                        .addOnCompleteListener(dbTask -> {
                                            if (dbTask.isSuccessful()) {
                                                Toast.makeText(SignupActivity.this, "Signup & Save successful!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                                finish();
                                            } else {
                                                Toast.makeText(SignupActivity.this, "Signup OK, but DB save failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            } else {
                                String message = "Signup failed";
                                Exception e = task.getException();
                                if (e instanceof FirebaseAuthUserCollisionException) {
                                    message = "User already exists!";
                                } else if (e != null) {
                                    message = e.getMessage();
                                }
                                Toast.makeText(SignupActivity.this, message, Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}
