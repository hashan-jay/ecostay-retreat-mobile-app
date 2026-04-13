package com.ecostayretreat.app.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ecostayretreat.app.R;
import com.ecostayretreat.app.dashboard.DashboardActivity;
import com.ecostayretreat.app.data.EcoStayRepository;
import com.ecostayretreat.app.session.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout nameLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmPasswordLayout;
    private TextInputEditText nameEdit;
    private TextInputEditText emailEdit;
    private TextInputEditText passwordEdit;
    private TextInputEditText confirmPasswordEdit;
    private Button registerBtn;
    private TextView goLogin;

    private EcoStayRepository repo;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        repo = new EcoStayRepository(this);
        session = new SessionManager(this);

        nameLayout = findViewById(R.id.nameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);
        nameEdit = findViewById(R.id.nameEdit);
        emailEdit = findViewById(R.id.emailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        confirmPasswordEdit = findViewById(R.id.confirmPasswordEdit);
        registerBtn = findViewById(R.id.registerBtn);
        goLogin = findViewById(R.id.goLogin);

        registerBtn.setOnClickListener(v -> attemptRegister());
        goLogin.setOnClickListener(v -> finish());
    }

    private void attemptRegister() {
        nameLayout.setError(null);
        emailLayout.setError(null);
        passwordLayout.setError(null);
        confirmPasswordLayout.setError(null);

        String name = safeText(nameEdit);
        String email = safeText(emailEdit);
        String password = safeText(passwordEdit);
        String confirmPassword = safeText(confirmPasswordEdit);

        boolean ok = true;
        if (name.length() < 2) {
            nameLayout.setError("Enter your name");
            ok = false;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Enter a valid email");
            ok = false;
        } else if (repo.emailExists(email)) {
            emailLayout.setError("Email already registered");
            ok = false;
        }
        if (!isValidPassword(password)) {
            passwordLayout.setError("Password must be at least 8 characters and include a number");
            ok = false;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordLayout.setError("Passwords do not match");
            ok = false;
        }
        if (!ok) return;

        long userId = repo.registerUser(name, email, password);
        if (userId <= 0) {
            emailLayout.setError("Could not create account. Try a different email.");
            return;
        }

        session.setUserId(userId);
        Intent i = new Intent(this, DashboardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    private boolean isValidPassword(String password) {
        if (password == null) return false;
        if (password.length() < 8) return false;
        boolean hasDigit = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                hasDigit = true;
                break;
            }
        }
        return hasDigit;
    }

    private String safeText(TextInputEditText e) {
        if (e.getText() == null) return "";
        return e.getText().toString().trim();
    }
}

