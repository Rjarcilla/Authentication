package com.example.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText registerEmail, registerPassword, registerConfirmPassword;
    Button registerBtn, backButton, loginLinkButton;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfirmPassword = findViewById(R.id.registerConfirmPassword);
        registerBtn = findViewById(R.id.registerBtn);

        // Find the back button and login link button
        backButton = findViewById(R.id.backButton);
        loginLinkButton = findViewById(R.id.loginLinkButton);

        mAuth = FirebaseAuth.getInstance();

        // Register Button Click Listener
        registerBtn.setOnClickListener(v -> {
            String email = registerEmail.getText().toString().trim();
            String password = registerPassword.getText().toString().trim();
            String confirmPassword = registerConfirmPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                registerEmail.setError("Email required");
                registerEmail.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                registerPassword.setError("Password required");
                registerPassword.requestFocus();
                return;
            }

            if (password.length() < 6) {
                registerPassword.setError("Password must be at least 6 characters");
                registerPassword.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(confirmPassword)) {
                registerConfirmPassword.setError("Confirm password required");
                registerConfirmPassword.requestFocus();
                return;
            }

            if (!password.equals(confirmPassword)) {
                registerConfirmPassword.setError("Passwords do not match");
                registerConfirmPassword.requestFocus();
                return;
            }

            // Show loading/progress if you have one
            // progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        // progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this,
                                    "Registration successful! Please login.",
                                    Toast.LENGTH_SHORT).show();

                            // Navigate to Login Activity
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                        } else {
                            String errorMessage = "Registration failed";
                            if (task.getException() != null) {
                                errorMessage = task.getException().getMessage();
                                // Handle specific Firebase errors
                                if (errorMessage.contains("email address is already in use")) {
                                    registerEmail.setError("Email already registered");
                                    registerEmail.requestFocus();
                                } else if (errorMessage.contains("badly formatted")) {
                                    registerEmail.setError("Invalid email format");
                                    registerEmail.requestFocus();
                                }
                            }
                            Toast.makeText(RegisterActivity.this,
                                    errorMessage,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Back Button Click Listener
        backButton.setOnClickListener(v -> {
            // Go back to Login Activity
            navigateToLogin();
        });

        // Login Link Button Click Listener
        loginLinkButton.setOnClickListener(v -> {
            // Navigate to Login Activity
            navigateToLogin();
        });

        // Modern way to handle back button/gestures (AndroidX)
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button/gesture event
                navigateToLogin();
            }
        });
    }

    // Helper method to navigate to login
    private void navigateToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}