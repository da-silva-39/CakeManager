package com.bolosdaaxcila.cakemanager.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.data.model.User;
import com.bolosdaaxcila.cakemanager.data.repository.UserRepository;
import com.bolosdaaxcila.cakemanager.ui.dashboard.DashboardActivity;
import com.bolosdaaxcila.cakemanager.utils.PasswordUtils;
import com.bolosdaaxcila.cakemanager.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import android.util.Patterns;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editEmail;
    private TextInputEditText editPassword;
    private UserRepository userRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnGoRegister = findViewById(R.id.btnGoRegister);

        userRepository = new UserRepository(this);
        sessionManager = new SessionManager(this);

        btnLogin.setOnClickListener(v -> attemptLogin());
        btnGoRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void attemptLogin() {
        String email = editEmail.getText() != null ? editEmail.getText().toString().trim() : "";
        String password = editPassword.getText() != null ? editPassword.getText().toString() : "";

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError(getString(R.string.error_invalid_email));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editPassword.setError(getString(R.string.error_password_required));
            return;
        }

        userRepository.findByEmail(email, user -> {
            if (user != null && PasswordUtils.matches(password, user.getPasswordHash())) {
                sessionManager.saveSession(user.getId(), user.getName());
                startActivity(new Intent(this, DashboardActivity.class));
                finishAffinity();
            } else {
                Toast.makeText(this, R.string.error_invalid_credentials, Toast.LENGTH_LONG).show();
            }
        });
    }
}
