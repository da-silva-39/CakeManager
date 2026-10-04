package com.bolosdaaxcila.cakemanager.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.data.model.User;
import com.bolosdaaxcila.cakemanager.data.repository.UserRepository;
import com.bolosdaaxcila.cakemanager.ui.dashboard.DashboardActivity;
import com.bolosdaaxcila.cakemanager.utils.PasswordUtils;
import com.bolosdaaxcila.cakemanager.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText editName;
    private TextInputEditText editEmail;
    private TextInputEditText editPassword;
    private UserRepository userRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        Button btnRegister = findViewById(R.id.btnRegister);

        userRepository = new UserRepository(this);
        sessionManager = new SessionManager(this);

        btnRegister.setOnClickListener(v -> attemptRegister());
    }

    private void attemptRegister() {
        String name = editName.getText() != null ? editName.getText().toString().trim() : "";
        String email = editEmail.getText() != null ? editEmail.getText().toString().trim() : "";
        String password = editPassword.getText() != null ? editPassword.getText().toString() : "";

        if (TextUtils.isEmpty(name)) {
            editName.setError(getString(R.string.error_name_required));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError(getString(R.string.error_invalid_email));
            return;
        }
        if (password.length() < 6) {
            editPassword.setError(getString(R.string.error_password_short));
            return;
        }

        userRepository.findByEmail(email, existing -> {
            if (existing != null) {
                editEmail.setError(getString(R.string.error_email_in_use));
                return;
            }
            User user = new User(name, email, PasswordUtils.hash(password));
            userRepository.insert(user, id -> {
                if (id != null && id > 0) {
                    sessionManager.saveSession(id, name);
                    startActivity(new Intent(this, DashboardActivity.class));
                    finishAffinity();
                } else {
                    Toast.makeText(this, R.string.error_not_possible_save, Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
