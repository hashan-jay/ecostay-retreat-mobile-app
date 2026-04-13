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
import com.ecostayretreat.app.models.User;
import com.ecostayretreat.app.session.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputEditText emailEdit;
    private TextInputEditText passwordEdit;
    private Button loginBtn;
    private TextView goRegister;

    private EcoStayRepository repo;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        repo = new EcoStayRepository(this);
        session = new SessionManager(this);

        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        emailEdit = findViewById(R.id.emailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        loginBtn = findViewById(R.id.loginBtn);
        goRegister = findViewById(R.id.goRegister);

        loginBtn.setOnClickListener(v -> attemptLogin());
        goRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void attemptLogin() {
        emailLayout.setError(null);
        passwordLayout.setError(null);

        String email = safeText(emailEdit);
        String password = safeText(passwordEdit);

        boolean ok = true;
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Enter a valid email");
            ok = false;
        }
        if (!isValidPassword(password)) {
            passwordLayout.setError("Password must be at least 8 characters and include a number");
            ok = false;
        }
        if (!ok) return;

        User user = repo.login(email, password);
        if (user == null) {
            passwordLayout.setError("Incorrect email or password");
            return;
        }

        session.setUserId(user.id);
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

