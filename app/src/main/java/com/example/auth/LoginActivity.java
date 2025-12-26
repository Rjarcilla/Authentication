package com.example.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginButton, registerButton;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(v -> {
            String inputEmail = email.getText().toString().trim();
            String inputPassword = password.getText().toString().trim();

            if (TextUtils.isEmpty(inputEmail) || TextUtils.isEmpty(inputPassword)) {
                Toast.makeText(this, "Please enter both email and password 🙂", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(inputEmail, inputPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Welcome back! 🎉", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, HomeActivity.class));
                            finish();
                        } else {
                            String message = "Login failed. Please try again.";

                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                message = "We couldn’t find an account with that email.";
                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Incorrect password. Please try again.";
                            }

                            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                        }
                    });
        });

        registerButton.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }
}
