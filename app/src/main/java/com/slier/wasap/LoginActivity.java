package com.slier.wasap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    static final String PREF_FILE = "WASAP";
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.auth = FirebaseAuth.getInstance();
        this.setupButtonEvent();
    }

    private void setupButtonEvent() {
        Button registerButton = findViewById(R.id.register_button);
        Button signinButton = findViewById(R.id.signin_button);

        registerButton.setOnClickListener(v -> {
            Util.gotoActivity(LoginActivity.this, RegisterActivity.class);
        });

        signinButton.setOnClickListener(v -> {
            String email = ((EditText) findViewById(R.id.login_email)).getText().toString();
            String password = ((EditText) findViewById(R.id.login_password)).getText().toString();

            if (this.validateSigninForm()) {
                ProgressDialog pd = Util.showProgressDialog(LoginActivity.this, "Signing in user");
                this.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        SharedPreferences prefs = LoginActivity.this.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("username", user.getDisplayName()).apply();

                        Util.gotoActivity(LoginActivity.this, ChatActivity.class, true);
                    } else {
                        pd.dismiss();
                        StringBuilder sb = new StringBuilder("Error while logging in user");
                        sb.append("\n\n");
                        sb.append(task.getException().getMessage());
                        Util.showErrorDialog(LoginActivity.this, sb.toString());
                    }
                });
            }
        });
    }

    private boolean validateSigninForm() {
        EditText emailInput = findViewById(R.id.login_email);
        EditText passwordInput = findViewById(R.id.login_password);
        boolean isFormValid = true;

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (TextUtils.isEmpty(email) || !email.contains("@")) {
            emailInput.setError("Email is empty or invalid");
            emailInput.requestFocus();
            isFormValid = false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 3) {
            passwordInput.setError("Password is empty or invalid");

            if (isFormValid) {
                passwordInput.requestFocus();
            }

            isFormValid = false;
        }

        return isFormValid;
    }
}
