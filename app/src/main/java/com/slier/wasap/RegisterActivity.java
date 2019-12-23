package com.slier.wasap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.auth = FirebaseAuth.getInstance();
        this.setupButtonEvent();
    }

    private void setupButtonEvent() {
        Button signupButton = findViewById(R.id.signup_button);

        signupButton.setOnClickListener(v -> {
            if (RegisterActivity.this.validateSignupForm()) {
                RegisterActivity.this.signupFirebaseUser();
            }
        });
    }

    private void signupFirebaseUser() {
        Util.showProgressDialog(RegisterActivity.this, "Signing up user");
        String username = ((EditText) findViewById(R.id.input_username)).getText().toString();
        String email = ((EditText) findViewById(R.id.input_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.input_password)).getText().toString();

        this.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build();

                user.updateProfile(userProfileChangeRequest).addOnCompleteListener(profileTask -> {
                    if (profileTask.isSuccessful()) {
                        Log.d("wasap", "Successfully set username");
                    }
                });

                Util.gotoActivity(RegisterActivity.this, LoginActivity.class);
            } else {
                Util.showErrorDialog(RegisterActivity.this, "Error while registering user.Sorry");
            }
        });
    }

    private boolean validateSignupForm() {
        EditText usernameInput = findViewById(R.id.input_username);
        EditText emailInput = findViewById(R.id.input_email);
        EditText passwordInput = findViewById(R.id.input_password);
        EditText passwordAgainInput = findViewById(R.id.input_password_again);
        boolean isFormValid = true;

        String username = usernameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String password_again = passwordAgainInput.getText().toString();

        if (TextUtils.isEmpty(username) || username.length() < 3) {
            isFormValid = false;
            usernameInput.setError("Username is empty or invalid");
            usernameInput.requestFocus();
        }

        if (TextUtils.isEmpty(email) || !email.contains("@")) {
            emailInput.setError("Email is empty or invalid");

            if (isFormValid) {
                emailInput.requestFocus();
            }

            isFormValid = false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 5) {
            passwordInput.setError("Password is empty or invalid");

            if (isFormValid) {
                passwordInput.requestFocus();
            }

            isFormValid = false;
        }

        if (TextUtils.isEmpty(password_again) || !password_again.equals(password)) {
            passwordAgainInput.setError("Confirm password is invalid or does not match password");

            if (isFormValid) {
                passwordAgainInput.requestFocus();
            }

            isFormValid = false;
        }

        return isFormValid;
    }
}
