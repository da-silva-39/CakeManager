package com.bolosdaaxcila.cakemanager.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bolosdaaxcila.cakemanager.R;
import com.bolosdaaxcila.cakemanager.data.repository.UserRepository;
import com.bolosdaaxcila.cakemanager.ui.auth.LoginActivity;
import com.bolosdaaxcila.cakemanager.utils.PasswordUtils;
import com.bolosdaaxcila.cakemanager.utils.SessionManager;

public class ProfileActivity extends AppCompatActivity {

    private SessionManager session;
    private UserRepository userRepository;
    private TextView textName;
    private TextView textEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        session = new SessionManager(this);
        userRepository = new UserRepository(this);

        textName = findViewById(R.id.textProfileName);
        textEmail = findViewById(R.id.textProfileEmail);

        userRepository.findById(session.getUserId()).observe(this, user -> {
            if (user != null) {
                textName.setText(user.getName());
                textEmail.setText(user.getEmail());
            }
        });

        findViewById(R.id.btnEditName).setOnClickListener(v -> editName());
        findViewById(R.id.btnChangePassword).setOnClickListener(v -> changePassword());

        Button btnLogout = findViewById(R.id.btnProfileLogout);
        btnLogout.setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        });
    }

    private void editName() {
        final EditText input = new EditText(this);
        input.setText(textName.getText());
        new AlertDialog.Builder(this)
                .setTitle(R.string.edit_name)
                .setView(input)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.save, (d, w) -> {
                    String name = input.getText().toString().trim();
                    if (name.isEmpty()) {
                        Toast.makeText(this, R.string.error_name_required, Toast.LENGTH_LONG).show();
                        return;
                    }
                    userRepository.findById(session.getUserId()).observe(this, user -> {
                        if (user != null) {
                            user.setName(name);
                            userRepository.update(user);
                            session.saveSession(user.getId(), name);
                            textName.setText(name);
                        }
                    });
                })
                .show();
    }

    private void changePassword() {
        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        new AlertDialog.Builder(this)
                .setTitle(R.string.change_password)
                .setView(input)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.save, (d, w) -> {
                    String password = input.getText().toString();
                    if (password.length() < 6) {
                        Toast.makeText(this, R.string.error_password_short, Toast.LENGTH_LONG).show();
                        return;
                    }
                    userRepository.findById(session.getUserId()).observe(this, user -> {
                        if (user != null) {
                            user.setPasswordHash(PasswordUtils.hash(password));
                            userRepository.update(user);
                            Toast.makeText(this, R.string.password_updated, Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .show();
    }
}
