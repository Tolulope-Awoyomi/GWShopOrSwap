package com.example.gwswaporshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends Activity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final EditText usernameField = findViewById(R.id.username);
        final EditText emailField = findViewById(R.id.email);
        final EditText passwordField = findViewById(R.id.password);
        final EditText confirmPasswordField = findViewById(R.id.confirm_password);
        final EditText gwIdField = findViewById(R.id.id_number);

        findViewById(R.id.button_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString().trim();
                String email = emailField.getText().toString().trim();
                String gwId = gwIdField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();
                String confirmPassword = confirmPasswordField.getText().toString().trim();

                if (username.isEmpty() || email.isEmpty() || gwId.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "All fields must be filled out.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!email.endsWith("@gwu.edu")) {
                    Toast.makeText(SignupActivity.this, "Email must end with @gwu.edu", Toast.LENGTH_LONG).show();
                    return;
                }
                if (gwId.length() != 9) {
                    Toast.makeText(SignupActivity.this, "GW ID must be 9 characters", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.length() < 7) {
                    Toast.makeText(SignupActivity.this, "Password must be at least 7 characters", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                if (firebaseUser != null) {
                                    writeNewUser(firebaseUser.getUid(), username, email);
                                }
                            } else {
                                // Provide more specific error handling here based on task.getException()
                                Toast.makeText(SignupActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        // Add OnClickListener for the "Already have an account? Log in here" text
        TextView textLogin = findViewById(R.id.text_login);
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the login activity
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void writeNewUser(String userId, String username, String email) {
        User user = new User(username, email);
        mDatabase.child("users").child(userId).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignupActivity.this, "Sign up successful. Please log in.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, "Failed to create user.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
